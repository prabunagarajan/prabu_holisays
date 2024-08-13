package com.oasys.posasset.dto;

import java.util.List;

import lombok.Data;

@Data
public class POSAssetResponseDTO {
	private Long id;
	private String applicationDate;
	
	private String applicationNumber;
	
	private String applicantPhoneNumber;
	
	private String applicantEmailId;
	
	private String assetType;
	
	private Long assetTypeId;
	
	private String assetBrand;
	
	private Long assetBrandId;
	
	private String deviceHardwareName;
	
	private Long deviceHardwareNameId;
	
	private String shopCode;
	
	private List<POSAssetResponseDetailDTO> assetDetailsList;	
	private Long approvedById;

	private String approvedByUsername;

	private String approvalDate;
	private Long assignById;

	private String assignByUsername;
	
	private String remarks;

	private String requestDateTime;
	
	private String status;
}
