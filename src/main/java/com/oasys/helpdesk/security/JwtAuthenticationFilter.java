package com.oasys.helpdesk.security;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.oasys.helpdesk.conf.exception.InvalidTokenException;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.dto.CommanMasterUnitCodeDTO;
import com.oasys.helpdesk.service.CommonMasterService;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.RedisUtil;

import lombok.Synchronized;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Autowired
	private RedisUtil redisUtil;

	@Value("${sessionExpiryTimeInSeconds}")
	private Integer sessionExpiryTimeInSeconds;

	@Autowired
	private CommonDataController commonDataController;
	
	@Autowired
	private CommonMasterService commonmasterservice;

	@Override
	// @Synchronized
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String jwt = parseJwt(request);
			String path = request.getServletPath();
			log.info("api path ==== : {} ", path);
			if (path.toLowerCase().contains("/ticket") || path.toLowerCase().contains("/ticketcategory")
					|| path.toLowerCase().contains("/priority") || path.toLowerCase().contains("/commonmaster")
					|| path.toLowerCase().contains("/helpdeskfaq") || path.toLowerCase().contains("/role-master")
					|| path.toLowerCase().contains("/sla") || path.toLowerCase().contains("/devicelost")
					|| path.toLowerCase().contains("/user") || path.toLowerCase().contains("/asset-type")
					|| path.toLowerCase().contains("/assetaccessories")
					|| path.toLowerCase().contains("/helpdeskWorkFlow") || path.toLowerCase().contains("/device")
					|| path.toLowerCase().contains("/helpdeskknowledge") || path.toLowerCase().contains("/ealrequest")
					|| path.toLowerCase().contains("/ealstock") || path.toLowerCase().contains("/issuefrom")
					|| path.toLowerCase().contains("/issuedetails") || path.toLowerCase().contains("/ticketstatus")
					|| path.toLowerCase().contains("/grievance") || path.toLowerCase().contains("/surveyform")
					|| path.toLowerCase().contains("/assetmap")||path.toLowerCase().contains("/changerequest")) {

				log.info("user service ==== : ");
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
				String authTokenHeader = request.getHeader("X-Authorization");
				AuthenticationDTO authenticationDTO = this.executeGet(headers, authTokenHeader);
				log.info("authenticationDTO ==== : " + authenticationDTO);
				if (Objects.isNull(authenticationDTO) || Objects.isNull(authenticationDTO.getUserId())) {
					this.authenticate(jwt);
				}
				authenticationDTO.setToken("Bearer ".concat(jwt));
				Authentication authentication = new UsernamePasswordAuthenticationToken(authenticationDTO,
						"authentication", null);
				SecurityContextHolder.getContext().setAuthentication(authentication);

			}

			else {
				log.info("helpdesk service user ==== : ");
				this.authenticate(jwt);
			}
		} catch (Exception e) {
			log.error("SignatureException token Expired or Invalid {}:::" + e);
		}

		filterChain.doFilter(request, response);
		
	}

	
	private String parseJwt(HttpServletRequest request) {
		String headerAuth = request.getHeader("X-Authorization");
		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7, headerAuth.length());
		}
		return null;
	}

	
	private AuthenticationDTO executeGet(HttpHeaders headers, String token) {
		AuthenticationDTO authenticationDTO = null;
		try {
			if (headers.getContentType() == null) {
				headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			}
			
			//CommanMasterUnitCodeDTO requestDTO=new CommanMasterUnitCodeDTO();
			//String responce = commonmasterservice.UserloginTokenGet(requestDTO);
			authenticationDTO = commonDataController.executeBusinessUser(token);
			//authenticationDTO = commonDataController.executeBusinessUser(responce);
			//log.info("checktoken=====response======::::" + authenticationDTO);
			//log.info(":::::??login token??:::: " + responce );
			return authenticationDTO;
		} catch (Exception ex) {
			//CommanMasterUnitCodeDTO requestDTO=new CommanMasterUnitCodeDTO();
			//String responce = commonmasterservice.UserloginTokenGet(requestDTO);
		   // authenticationDTO = commonDataController.executeBusinessUser(responce);
			//log.info(":::::??login token??:::: " + responce );
			
			//log.info("=====executeGet=====AuthenticationDTO====" + authenticationDTO);
			//System.out.println(ex.getMessage());
			log.info("executeGet====checktokenException======" + ex.getMessage());
		    
		    //return authenticationDTO;
		}
		
		return null;
	}

	
	
	
	
	private void authenticate(String jwt) {
		if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
			AuthenticationDTO authenticationDTO = tokenProvider.getCustomerObjectFromJWT(jwt);
			if (authenticationDTO != null) {
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//	    	authenticationDTO = executeGet("Bearer ".concat(jwt));
				log.info("========authenticationDTO=======" + authenticationDTO);
				if (authenticationDTO == null || authenticationDTO.getUserId() == null) {
					throw new InvalidTokenException("Token not allowed");
				}
//				if (redisUtil.hasKey(
//						authenticationDTO.getEmail() + Constant.UNDERSCORE + authenticationDTO.getEmployeeId())) {
//					redisUtil.expire(
//							authenticationDTO.getEmail() + Constant.UNDERSCORE + authenticationDTO.getEmployeeId(),
//							sessionExpiryTimeInSeconds);
//					log.info("========token updated======={}, token : {}", LocalDateTime.now(), redisUtil.getValue(
//							authenticationDTO.getEmail() + Constant.UNDERSCORE + authenticationDTO.getEmployeeId()));
//				} else {
//					log.info("========token not found ======={}" + LocalDateTime.now());
//					throw new InvalidTokenException("Token Expired");
//				}
				if (authenticationDTO != null && authenticationDTO.getUserId() != null) {
					authenticationDTO.setToken("Bearer ".concat(jwt));
					Authentication authentication = new UsernamePasswordAuthenticationToken(authenticationDTO,
							"authentication", authenticationDTO.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		}
	}

}