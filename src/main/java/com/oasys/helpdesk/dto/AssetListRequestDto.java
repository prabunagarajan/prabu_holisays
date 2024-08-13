package com.oasys.helpdesk.dto;

import java.sql.Date;


import lombok.Data;

@Data
public class AssetListRequestDto {
	
	private Long id;
	private String serialNo;
	private String rating;
	private String dateOfPurchase;
	private String warrantyPeriod;
	private String supplierName;
	private String assetType;
	private String assetName;
	private String assetBrand;
	private String assetSubType;
	private boolean isActive;
	private Long supplierId;
	private Long assetTypeId;
	private Long assetNameId;
	private Long assetBrandId;
	private Long assetSubTypeId;
	

}
