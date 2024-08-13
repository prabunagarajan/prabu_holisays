package com.oasys.helpdesk.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DesignationRequestDto {
	private static final long serialVersionUID = 7556535340883131007L;
private Long id;
	
	@NotBlank(message = "103")
	private String designationName;
	
	@NotBlank(message = "103")
	private String code;

	@NotNull
    private boolean isActive; 
	
	private Long createdBy;
	
	private Long modifiedBy;
}
