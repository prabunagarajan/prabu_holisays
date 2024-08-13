package com.oasys.posasset.response;

import java.util.Date;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeviceResponseDTO {
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

	private String fpsCode;

	private String fpsName;

	private String createdBy;

	private String modifiedBy;

	private String createdDate;

	private String modifiedDate;

	private String simId2;

	private String printerName;

	private String assetType;

	private Long assetTypeId;
	private Long assetNameId;
	private Long assetBrandId;
	private Long assetSubtypeId;
	private Long supplierNameId;
	private Long warrantyPeriod;
	private Long rating;

}
