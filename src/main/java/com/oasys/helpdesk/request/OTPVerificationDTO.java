package com.oasys.helpdesk.request;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class OTPVerificationDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotBlank(message = "103")
	private String emailId;
	
	@NotBlank(message = "103")
	private String otp;

}
