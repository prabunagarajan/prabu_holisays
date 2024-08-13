package com.oasys.helpdesk.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ChangePasswordDto {

	@NotBlank(message = "103")
	private String userName;
	
	@NotBlank(message = "103")
	private String password;
	
	@NotBlank(message = "103")
	private String confirmPassword;
	
	@NotBlank(message = "103")
	private String currentPassword;

	@Override
	public String toString() {
		return "ChangePasswordDto [userName=" + userName + "]";
	}
	
	
}
