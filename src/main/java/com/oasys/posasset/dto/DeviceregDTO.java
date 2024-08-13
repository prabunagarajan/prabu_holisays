package com.oasys.posasset.dto;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;

import com.oasys.helpdesk.dto.WorkLocationResponseDTO;
import com.oasys.posasset.constant.ApprovalStatus;
import com.oasys.posasset.entity.EALRequestMapEntity;

import lombok.Data;

@Data
public class DeviceregDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String androidID;

	private String androidVersion;

	private String board;

	private String bootLoader;

	private String brand;

	private String cpuSpeed;

	private String device;

	private String deviceName;

	private String deviceNumber;

	private String display;

	private String fingerprint;

	private String firstInstallTime;

	private String hardware;

	private String host;

	private String IMEINumber1;

	private String lastUpdateTime;

	private String manufacturer;

	private String memory;

	private String product;

	private String radio;

	private String screenResolution;

	private String SDKVersion;

	private String serial;

	private String serialNumber;

	private String tags;

	private String type;

	private String versionCode;

	private String versionName;

	private String WIFIMAC;

	private String appType;

	private String IMEINumber2;

	private String fpsCode;

	private String fpsName;

	private String Make;

	private String Model;

	private String SimID;

	private String MACID;

	private String printerID;

	private String lastSyncOn;

	private String village;

	private String talukCode;

	private String districtCode;

	private String remarks;

	private String SIMID2;

	private String status;

	private String assetType;

	private String entity;

	private String deviceStatus;

	private Long assetTypeId;

	private Long assetNameId;

	private Long assetBrandId;

	private Long assetSubtypeId;

	private Long supplierNameId;

	private Long warrantyPeriod;

	private Long rating;

}
