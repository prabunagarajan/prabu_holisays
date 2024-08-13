package com.oasys.helpdesk.dto;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SalutationResponseDTO {

	
private Long id;
	
	@NotBlank(message = "103")
	private String code;
	
	@NotBlank(message = "103")
	private String salutationname;
	
	@NotBlank(message = "103")
	private Boolean status;
	
	private String createdBy;

	private String createdDate;

	private String modifiedBy;

	private String modifiedDate;
	
	
}
