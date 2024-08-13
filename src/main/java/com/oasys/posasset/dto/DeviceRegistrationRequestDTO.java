package com.oasys.posasset.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import com.oasys.posasset.entity.DevicestatusEntity;

import lombok.Data;

@Data
public class DeviceRegistrationRequestDTO implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	private String androidId;

	private String androidVersion;

	private String BluetoothMac;

	private String board;

	private String bootLoader;

	private String brand;

	private String cpuSpeed;

	private String device;

	private String deviceName;

	@NotBlank(message = "103")
	private String deviceNumber;

	private String display;

	private String fingerPrint;

	private String firstInstallTime;

	private String hardware;

	private String host;

	private String imeiNumber;

	private String lastUpdateTime;

	private String manufacturer;

	private String memory;

	private String product;

	private String radio;

	private String screenResolution;

	private String sdkVersion;

	private String serial;

	private String serialNumber;

	private String tags;

	private String type;

	private String versionCode;

	private String versionName;

	private String wifiMac;

	private String appType;
	
	private String imeiNumber2;
	
	private String fpsCode;

	private String fpsName;
	
	private String status;
	
	private String remarks;
	
	private DevicestatusEntity deviceStatus;
	
	private String entity;
	
	private String statuscode;
	
	private String talukCode;
	
	private String districtCode;
	
	private String districtName;
	
	private String talukName;

}
