package com.oasys.helpdesk.dto;

import java.util.Date;

import lombok.Data;

@Data
public class AssetReportRequestDTO {

	Date fromDate;
	Date toDate;
	String assetGroup;
	String assetType;
	String assetName;
	String assetBrand;
	String assetSubType;
	String supplierName;
	String serialNo;
	String status;
	String unitName;
	String entity;
	String unitCode;
	String district;
	

}
