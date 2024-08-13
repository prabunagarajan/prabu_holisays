package com.oasys.helpdesk.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oasys.helpdesk.dto.ForgotPasswordDTO;
import com.oasys.helpdesk.dto.PasswordResetDTO;
import com.oasys.helpdesk.request.OTPVerificationDTO;
import com.oasys.helpdesk.request.UserLoginRequestDto;
import com.oasys.helpdesk.service.LoginService;
import com.oasys.helpdesk.service.UserService;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "Authentication")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/authentication")
public class AuthenticationController {

	@Autowired
	private LoginService loginService;
	
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ApiOperation(value = "This api is used to login", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> login(@RequestBody UserLoginRequestDto userLoginRequestDto, HttpSession httpSession,
			 HttpServletRequest httpServletRequest) {
		GenericResponse objGenericResponse = loginService.login(userLoginRequestDto, httpSession,httpServletRequest);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/check-token", method = RequestMethod.GET)
	@ApiOperation(value = "This api is used to login", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> login(@RequestParam String token) {
		Object objResponse = loginService.authenticateCustomerToken(token);
		return new ResponseEntity<>(objResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PostMapping("/reset-password")
	@ApiOperation(value = "This api is used to update the customer password when customer is getting onboard", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> passwordReset(HttpServletRequest httpServletRequest,@RequestBody PasswordResetDTO passwordResetDTO) {
		return new ResponseEntity<>(userService.resetPassword(passwordResetDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);	
	}
	
	@PostMapping("/email/generate-otp")
	@ApiOperation(value = "This api is used to send otp", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> sendOTP(@RequestParam(value="emailId", required=true) String emailId) {
		return new ResponseEntity<>(userService.generateOTP(emailId), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);	
	}
	
	
	@PostMapping("/verify/otp")
	@ApiOperation(value = "This api is used to send otp", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> verifyOTP(@Valid @RequestBody OTPVerificationDTO otpVerificationDTO)  {
		return new ResponseEntity<>(userService.verifyOTP(otpVerificationDTO), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	@PostMapping("/forgotPassword")
	@ApiOperation(value = "This api is used to update the customer password when trying to use the forgot password feature")
	public ResponseEntity<Object> forgotPassword(@Valid @RequestBody ForgotPasswordDTO requestDTO) {
		return new ResponseEntity<>(userService.forgotPassword(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PostMapping("/forgotPassword/generate-otp")
	@ApiOperation(value = "This api is used to send otp for forgot password screen", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> generateForgotPasswordOTP(@RequestParam(value="emailId", required=true) String emailId) {
		return new ResponseEntity<>(userService.generateForgotPasswordOTP(emailId), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);	
	}
	 
}
