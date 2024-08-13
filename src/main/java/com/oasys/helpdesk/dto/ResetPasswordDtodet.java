package com.oasys.helpdesk.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ResetPasswordDtodet {

	
	private Long userId;
	
	private String password;
	
	private Boolean accountLocked;
	
	
}
