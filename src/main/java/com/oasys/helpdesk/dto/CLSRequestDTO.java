package com.oasys.helpdesk.dto;

import java.io.Serializable;

import java.util.Date;
import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CLSRequestDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String grievanceId;
	
	private String userName;
	
	private String password;
	
	private String otp;
	
	private Integer phoneNumber;
}
