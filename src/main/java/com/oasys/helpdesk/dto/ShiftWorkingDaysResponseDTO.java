package com.oasys.helpdesk.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShiftWorkingDaysResponseDTO {

	
	private Long id;
	
	@NotBlank(message = "103")
	private String code;
	
	@Min(value =0,message = "workingdays must be equals or greater then 0")
	private Long workingdays;
	
	@NotBlank(message = "103")
	private Boolean status;
	
	private String createdBy;

	private String createdDate;

	private String modifiedBy;

	private String modifiedDate;
}