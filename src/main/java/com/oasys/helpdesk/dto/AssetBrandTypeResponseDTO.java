package com.oasys.helpdesk.dto;

import lombok.Data;

@Data
public class AssetBrandTypeResponseDTO {

	private Long id;
	private String code;
	private String assetType;
	private String assetBrand;
	private Boolean status;
	private String createdBy;
	private String createdDate;
	private String modifiedBy;
	private String modifiedDate;
	private Long assetTypeId;
	private Long assetBrandId;
}
