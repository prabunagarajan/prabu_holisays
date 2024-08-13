package com.oasys.helpdesk.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;

import lombok.Data;

@Data
public class FeedBackEntityRequestDTO implements Serializable {

private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String name;
	
	@Email(regexp = "[A-Za-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}")
	
	private String email;

	private String subject;
	
	private String message;

}
