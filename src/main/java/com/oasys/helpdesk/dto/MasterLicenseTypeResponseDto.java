package com.oasys.helpdesk.dto;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MasterLicenseTypeResponseDto {

	private long id;

	@NotBlank(message = "103")
	private String lincensetypename;

	@NotBlank(message = "103")
	private String code;

	@NotBlank(message = "103")
	private Boolean active;

	private String created_by;

	private String created_date;

	private String modified_by;

	private String modified_date;

}
