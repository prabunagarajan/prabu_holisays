package com.oasys.helpdesk.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class DeviceHardwareAddRequestDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int noOfDevices;
	
	private String hardwareName;
}
