package com.oasys.helpdesk.service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.oasys.helpdesk.conf.exception.InvalidTokenException;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.entity.LoginHistory;
import com.oasys.helpdesk.entity.UserAttempts;
import com.oasys.helpdesk.entity.UserEntity;
import com.oasys.helpdesk.repository.RoleMasterRepository;
import com.oasys.helpdesk.repository.UserAttemptsRepository;
import com.oasys.helpdesk.repository.UserRepository;
import com.oasys.helpdesk.repository.UserRoleRepository;
import com.oasys.helpdesk.request.UserLoginRequestDto;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.security.JwtTokenProvider;
import com.oasys.helpdesk.security.UserPrincipal;
import com.oasys.helpdesk.utility.EmploymentStatus;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.RedisUtil;

import lombok.extern.log4j.Log4j2;


@Service
@Log4j2
public class LoginServiceImpl implements LoginService{

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleMasterRepository roleMasterRepository;
	
	@Autowired
	UserRoleRepository userRoleRepository;
	
	@Autowired
	JwtTokenProvider tokenProvider;
	
	@Autowired
    private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;
	
	@Value("${maxLoginAttempts}")
  	private Integer maxLoginAttempts;
	
	@Autowired	
	private CustomerDetailsService customerDetailsService;
	
	@Autowired
	private UserAttemptsRepository userAttemptsRepository;
	
	@Autowired
	private RedisUtil redisUtil;
	
	@Value("${sessionExpiryTimeInSeconds}")
	private Integer sessionExpiryTimeInSeconds;

	public GenericResponse login(UserLoginRequestDto userLoginRequestDto, HttpSession httpSession,
			HttpServletRequest httpServletRequest) {
		GenericResponse genericResponse = null;
		userLoginRequestDto.setUsername(userLoginRequestDto.getUsername().trim());
		userLoginRequestDto.setPassword(userLoginRequestDto.getPassword().trim());
    	String password = userLoginRequestDto.getPassword();
        Authentication authentication = null;
    	try{
    		authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginRequestDto.getUsername(),password));    		    		
    	}catch (BadCredentialsException e) {
    		
    		genericResponse = Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.BAD_CREDENTIALS.getMessage());
    		
    		Boolean isLocked = userService.updateFailAttempts(userLoginRequestDto.getUsername());
    		
			Optional<UserEntity> storeUser = userRepository.findByUsernameOrEmailIdIgnoreCase(userLoginRequestDto.getUsername(),userLoginRequestDto.getUsername());
			if(isLocked){
				UserAttempts userAttempts = userService.getUserAttempts(storeUser.get().getId());
				if(storeUser.isPresent() && !storeUser.get().getAccountLocked()){
					userAttempts.setAttempts(maxLoginAttempts);
					userAttemptsRepository.save(userAttempts);
				}				
				
				genericResponse = Library.getFailResponseCode(ErrorCode.USER_ACCOUNT_LOCKED.getErrorCode(),
						ResponseMessageConstant.USER_ACCOUNT_LOCKED.getMessage());
				return genericResponse;
			}			
			return genericResponse;
		}catch (Exception exception) {
			log.error("====authenticateUser===",exception);			
			genericResponse = Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ErrorMessages.INVALID_USERNAME_PASSWORD);
			return genericResponse;
		}
    	//when user name
//    	Users storeUser = userRepository.findByUsernameOrEmail(loginRequest.getUsernameOrEmail(),loginRequest.getUsernameOrEmail()).orElse(null);
    	//when login id
    	Optional<UserEntity> storeUser = userRepository.findByUsernameOrEmailIdIgnoreCase(userLoginRequestDto.getUsername(),userLoginRequestDto.getUsername());
    	if(storeUser.isPresent() && storeUser.get().getAccountLocked()){
    		genericResponse = Library.getFailResponseCode(ErrorCode.USER_ACCOUNT_LOCKED.getErrorCode(),
					ResponseMessageConstant.USER_ACCOUNT_LOCKED.getMessage());
    		return genericResponse;
    	}
    	//user is inactive
    	if(storeUser!=null && EmploymentStatus.INACTIVE.getType().equals(storeUser.get().getEmploymentStatus())){
    		genericResponse = Library.getFailResponseCode(ErrorCode.INACTIVE_USER_ACCOUNT.getErrorCode(),
					ResponseMessageConstant.INACTIVE_USER_ACCOUNT.getMessage());
			return genericResponse;
    	}
    	userService.resetFailAttempts(storeUser.get().getId());//reset failed login attempts on successful login
        //save the last login details
        userService.saveUpdateLoginHistory(storeUser.get().getId(),httpServletRequest.getRemoteAddr(),"login");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //generate token based on the following claims
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        //set the content in authentication DTO
        AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setUserId(userPrincipal.getId());
        authenticationDTO.setUserName(userPrincipal.getUsername());
        authenticationDTO.setEmail(userPrincipal.getEmail());
        authenticationDTO.setDesignationCode(storeUser.get().getDesignationCode());
        //find allocated entity unit
        authenticationDTO.setAuthorities(userPrincipal.getAuthorities());
        authenticationDTO.setRoleCodes( userPrincipal.getRoles());
        authenticationDTO.setEmployeeId(userPrincipal.getEmployeeId());
      
        //end
        //generate token
        String jwt = null;
        List<String> roles = new ArrayList<>();
        roles.add(Constant.HELPDESK_ADMIN);
        roles.add(Constant.GRIEVANCE_ADMIN);
        roles.add(Constant.FIELD_ADMIN);
//		if (authenticationDTO.getRoleCodes().stream().anyMatch(r -> roles.contains(r))) {
//			jwt = redisUtil.getValue(authenticationDTO.getEmail() + Constant.UNDERSCORE + authenticationDTO.getEmployeeId());
//		}
         
		if (org.apache.commons.lang3.StringUtils.isBlank(jwt) || !tokenProvider.validateToken(jwt)) {
			jwt = tokenProvider.generateToken(authenticationDTO);
		}
        authenticationDTO.setToken("Bearer ".concat(jwt));
        Map<String,Object> finalMap = new LinkedHashMap<>();
       
        finalMap.put("auth",authenticationDTO);
        //find the last login time from login history
  		List<LoginHistory> loginHistoryList = userService.findLastLoginTime(userPrincipal.getId());
  		if(loginHistoryList!=null && !loginHistoryList.isEmpty() && loginHistoryList.size() > 1){
  			finalMap.put("lastLoginTime",loginHistoryList.get(1).getLoginTime().toString());
  		}
//		redisUtil.set(authenticationDTO.getEmail() + Constant.UNDERSCORE + authenticationDTO.getEmployeeId(), jwt,
//				sessionExpiryTimeInSeconds);
		log.info("========token inserted======={}", LocalDateTime.now());
        //generate token finish
		genericResponse = Library.getSuccessfulResponse(finalMap, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ResponseMessageConstant.SUCCESSFULL_LOGIN.getMessage());
		return genericResponse;
	}
	
	public AuthenticationDTO authenticateCustomerToken(String customerToken) {
		AuthenticationDTO authenticationDTO = null;
		String parseToken = tokenProvider.parseJwt(customerToken);
		if (StringUtils.hasText(parseToken) && tokenProvider.validateToken(parseToken)) {
			authenticationDTO = tokenProvider.getCustomerObjectFromJWT(parseToken);
			if(authenticationDTO!=null) {
				UserDetails userDetails = customerDetailsService.loadUserById(authenticationDTO.getUserId());
				if (userDetails == null) {
		            throw new InvalidTokenException("Token not allowed");
		        }
			}else {
				 throw new InvalidTokenException("Token not valid");
			}
		}
		return authenticationDTO;
	}

	
	public Long getCurrentUserId() {
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			log.info("getCurrentUserId() - SecurityContextHolder.getContext().getAuthentication()  is null");
			return null;
		}
		Object authObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (authObject == null) {
			log.info("getCurrentUserId() - authObject  is null");
			return null;
		} else {
			log.info("getCurrentUser()-UserMaster - found");
			AuthenticationDTO authenticationDTO= (AuthenticationDTO) authObject;
			return authenticationDTO.getUserId();
		}
	}

}
