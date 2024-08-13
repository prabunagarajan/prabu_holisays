package com.oasys.posasset.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.oasys.helpdesk.utility.DeviceRegistrationStatus;

import lombok.Data;

@Data
public class DeviceRegistrationUpdateRequestDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String village;

	private String talukCode;

	private String districtCode;

	private String remarks;

	private String fpsCode;

	private String fpsName;

	@NotNull(message = "103")
	private DeviceRegistrationStatus deviceStatus;

	@NotBlank(message = "103")
	private String deviceNumber;

	private String status;

	private String shopType;

	private String entityType;

	private String districtName;

	private String talukName;

}
