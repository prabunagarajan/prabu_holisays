package com.oasys.helpdesk.dto;
import java.io.Serializable;

import lombok.Data;

@Data
public class PasswordResetDTO implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String password;

    private String confirmPassword;

    private String token;
    
    private String emailId;

}