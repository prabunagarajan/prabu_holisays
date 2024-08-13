package com.oasys.helpdesk.dto;

import java.sql.Date;
import java.time.LocalDate;

import lombok.Data;

@Data

public class AssetMapRequestDto {
	private Long id;
	// private String assetGroup;
	private String district;
	private Long districtId;
	private String State;
	private Long stateId;
	private String assetLocation;
	private String userName;
	// private String assetType;
	// private String assetName;
	private String serialNo;
	private String dateOfInstallation;
	private boolean isActive;
	private Long assetTypeId;
	private Long assetGroupId;
	private Long assetNameId;
	private Long statusId;
	private String status;
	private String statusName;
	private String userType;
	private String unitName;
	private String licenseNo;
	private String Designation;
	private String reason;
	private String unitcode;

}
