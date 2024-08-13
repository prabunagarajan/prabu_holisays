package com.oasys.helpdesk.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AssetBrandTypeRequestDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	@NotBlank(message = "103")
	private String code;

	@NotNull(message = "103")
	private Long assetTypeId;

	@NotNull(message = "103")
	private Long assetBrandId;

	@NotNull(message = "103")
	private Boolean status;
}
