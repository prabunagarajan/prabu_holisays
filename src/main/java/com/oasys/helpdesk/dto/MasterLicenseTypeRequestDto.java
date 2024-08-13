package com.oasys.helpdesk.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class MasterLicenseTypeRequestDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	@NotBlank(message = "103")
	private String lincensetypename;

	@NotBlank(message = "103")
	private String code;

	@NotNull(message = "103")
	private String active;

}
