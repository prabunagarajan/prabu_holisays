package com.oasys.helpdesk.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AssetBrandRequestDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
    private Long assetTypeId;
	
	private String assetType;
	
	@NotBlank(message = "103")
	private String brand;
	
	@NotNull(message = "103")
	private Boolean status;
	
	 private Long createdBy;
}
