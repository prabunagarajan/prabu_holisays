package com.oasys.helpdesk.request;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SubCategoryRequestDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7556535340883131007L;

	private Long id;
	
	@NotNull(message = "103")
	private Long categoryid;

	@NotBlank(message = "103")
	private String subcategoryName;
	
	@NotNull(message = "103")
	private Boolean active;
	
	@NotBlank(message = "103")
	private String code;
	
}
