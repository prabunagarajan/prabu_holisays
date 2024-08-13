package com.oasys.helpdesk.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GrievanceCategoryRequestDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7556535340883131007L;

	private Long id;
	
	//@NotBlank(message = "103")
	private String categoryName;
	
	//@NotNull(message = "103")
	private boolean isActive;
	
	//@NotBlank(message = "103")
	private String code;
	
	//@NotBlank(message = "103")
	private String typeofUser;

}
