package com.oasys.helpdesk.dto;

import java.util.List;

import lombok.Data;

@Data
public class DevicehardwareNameResponseDTO {

	private Long id;

	private String deviceName;

	private Long assetTypeId;

	private boolean status;

	private String assetType;
	
	private String createdBy;
	
	private String createdDate;
	
	private String modifiedBy;
	
	private String modifiedDate;



	
}
