package com.oasys.helpdesk.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class ForgotPasswordDTO implements Serializable{
	 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String password;

    private String confirmPassword;

    private String otp;
    
    private String emailId;

	@Override
	public String toString() {
		return "ForgotPasswordDTO [emailId=" + emailId + "]";
	}
    
}
