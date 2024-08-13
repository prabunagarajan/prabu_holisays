package com.oasys.helpdesk.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class AssetMapResponseDTO {
	private Long id;
	private String assetGroup;
	private String district;
	private Long districtId;
	private String State;
	private Long stateId;
	private String assetLocation;
	private String userName;
	private String assetType;
	private String assetName;
	private String serialNo;
	private String dateOfInstallation;
	private boolean isActive;
	private String createdDate;
	private String modifiedDate;
	private Long createdBy;
	private Long modifiedBy;
	private String createdByName;
	private String modifiedByName;
	private Long assetTypeId;
	private Long assetNameId;
	private Long assetGroupId;
	private Long statusId;
	private String status;
	private String userType;
	private String unitName;
	private String licenseNo;
	private String Designation;	
	private String reason;
	private String unitcode;
	
}
