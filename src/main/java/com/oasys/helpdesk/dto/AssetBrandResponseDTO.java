package com.oasys.helpdesk.dto;

import lombok.Data;

@Data
public class AssetBrandResponseDTO {
	private Long id;

	private String brand;

	private Boolean status;

	private String createdBy;

	private String createdDate;

	private String modifiedBy;

	private String modifiedDate;
	
     private Long assetTypeId;
	
	private String assetType;
	
}
