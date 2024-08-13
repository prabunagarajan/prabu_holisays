package com.oasys.helpdesk.request;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class GrievanceOTPVerificationRequestDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotBlank(message = "103")
	private String phoneNumber;
	
	@NotBlank(message = "103")
	private String otp;

}

