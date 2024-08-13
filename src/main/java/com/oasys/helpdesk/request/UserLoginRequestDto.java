package com.oasys.helpdesk.request;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserLoginRequestDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6058478600184470778L;

	@NotBlank(message = "103")
	private String username;
	
	@NotBlank(message = "103")
	private String password;
	
}
