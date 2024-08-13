package com.oasys.posasset.request;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeviceRequestDTO {
	private Long id;

	private boolean active;

	private boolean associated;

	private String deviceNumber;

	private String make;

	private String model;

	private String serialNumber;

	private String simId;

	private Date lastSyncOn;

	private Long printerId;

	private String macId;

	private String projectName;

	private String shopName;

	private String shopCode;

	private String simId2;

	private String remarks;

	private String status;

	private String previousDeviceNumber;

	private String assetType;

	private Long assetTypeId;
	private Long assetNameId;
	private Long assetBrandId;
	private Long assetSubtypeId;
	private Long supplierNameId;
	private Long warrantyPeriod;
	private Long rating;
}
