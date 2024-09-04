package com.devar.cabs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import com.devar.cabs.common.ErrorCode;
import com.devar.cabs.common.ErrorMessages;
import com.devar.cabs.common.ResponseMessageConstant;
import com.devar.cabs.entity.UserEntity;
import com.devar.cabs.requestDTO.DriverDetailsRequestDTO;
import com.devar.cabs.requestDTO.LoginRequestDTO;
import com.devar.cabs.responseDTO.AuthResponseDTO;
import com.devar.cabs.responseDTO.ResponseData;
import com.devar.cabs.service.UserService;
import com.devar.cabs.utility.GenericResponse;
import com.devar.cabs.utility.JwtUtil;
import com.devar.cabs.utility.Library;
import com.devar.cabs.utility.ResponseHeaderUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.time.Instant;
import java.util.Optional;

@Api(value = "User", description = "This controller contain all  operation of Driver Details")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })
@RestController
@CrossOrigin("*")
@RequestMapping("/auth")
public class UserController {
	
	 @Autowired
	    private UserService userService;

	    @Autowired
	    private AuthenticationManager authenticationManager;

	    @Autowired
	    private JwtUtil jwtUtil;

//	    @PostMapping("/signup")
//	    public ResponseEntity<UserEntity> signup(@RequestBody UserEntity user) {
//	        UserEntity createdUser = userService.createUser(user);
//	        return ResponseEntity.ok(createdUser);
//	    }
	    
		@PostMapping("/signup")
		@ApiOperation(value = "This api is used to create a new User", notes = "Returns HTTP 200 if successful get the record")
		public GenericResponse createDriverDetails(@RequestBody UserEntity user) {
			return userService.createUser(user);
		}

	    @PostMapping("/login")
	    @ApiOperation(value = "This api is to login by user", notes = "Returns HTTP 200 if successful get the record")
	    public ResponseEntity<GenericResponse> login(@RequestBody LoginRequestDTO loginRequest) {
	        String username = loginRequest.getUserName();
	        String password = loginRequest.getPassWord();

	        Optional<UserEntity> userOpt = userService.findByUserName(username);
	        if (userOpt.isPresent()) {
	            UserEntity user = userOpt.get();
	            if (userService.checkPassword(password, user.getPassword())) {
	                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
	                final UserDetails userDetails = userService.loadUserByUsername(username);
	                final String jwt = jwtUtil.generateToken(userDetails, user);

	                // Creating the user data for the response
	                AuthResponseDTO userData = new AuthResponseDTO();
	                userData.setId(user.getId());
	                userData.setUserName(user.getUserName());
	                userData.setEmail(user.getEmail());
	                userData.setFirstname(user.getFirstName());
	                userData.setPhoneNumber(user.getPhoneNumber());
	                userData.setToken("Bearer " + jwt);

	                // Creating the response data
	                ResponseData responseData = new ResponseData();
	                responseData.setAuth(userData);
	                responseData.setLastLoginTime(Instant.now()); // Or fetch the last login time if stored

	                // Creating the final GenericResponse object
	                GenericResponse response = new GenericResponse();
	                response.setStatus("success");
	                response.setErrorCode(200);
	                response.setUserDisplayMesg("User logged in successfully.");
	                response.setData(responseData);
//	                response = Library.getSuccessfulResponse(responseData, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
//	        				ResponseMessageConstant.SUCCESSFULL_LOGIN.getMessage());

	                return ResponseEntity.ok(response);
	            } else {
	                GenericResponse response = Library.getFailResponseCode(
	                    ErrorCode.BAD_REQUEST.getErrorCode(),
	                    ErrorMessages.INVALID_USERNAME_PASSWORD
	                );
	                return ResponseEntity.status(HttpStatus.OK).body(response);
	            }
	        } else {
	            GenericResponse response = Library.getFailResponseCode(
	                ErrorCode.BAD_REQUEST.getErrorCode(),
	                ErrorMessages.INVALID_USERNAME_PASSWORD
	            );
	            return ResponseEntity.status(HttpStatus.OK).body(response);
	        }
	    }


//	    @GetMapping("/{id}")
//	    public ResponseEntity<UserEntity> getUserById(@PathVariable Long id) {
//	        Optional<UserEntity> userOpt = userService.findById(id);
//	        return userOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(404).body(null));
//	    }
	    
		@GetMapping("/getUserById/{id}")
		@ApiOperation(value = "This api is to get User by id", notes = "Returns HTTP 200 if successful get the record")
		public ResponseEntity<Object> getById(@PathVariable Long id) {
			return new ResponseEntity<>(userService.findById(id), ResponseHeaderUtility.HttpHeadersConfig(),
					HttpStatus.OK);
		}

}
