package com.oasys.helpdesk.dto;


import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CLSResponseDTO {
	
private Long id;
	
	private String grievanceId;
	
	private String userName;
	
	private String password;
	
	private String otp;
	
	private Integer phoneNumber;
	
	private String created_by;

	public String created_date;
	
	private String modified_by;
	
	public String modified_date;
}
