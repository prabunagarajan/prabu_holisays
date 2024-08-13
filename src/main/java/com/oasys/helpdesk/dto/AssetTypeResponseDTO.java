package com.oasys.helpdesk.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssetTypeResponseDTO {
	
	private Long id;
	
	private String code;
	
	private String type;
	
	private Boolean status;
	
	private String createdBy;

	private String createdDate;

	private String modifiedBy;

	private String modifiedDate;
}
