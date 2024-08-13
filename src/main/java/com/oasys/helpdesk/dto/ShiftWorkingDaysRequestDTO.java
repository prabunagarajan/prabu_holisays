package com.oasys.helpdesk.dto;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ShiftWorkingDaysRequestDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long Id;
	
	@NotBlank(message = "103")
	private String code;
	
	@Min(value =0,message = "workingdays must be equals or greater then 0")
	private Long workingdays;
	
	@NotNull(message = "103")
	private Boolean status;

}