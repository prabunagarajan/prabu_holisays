package com.oasys.helpdesk.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class DeviceHardwareAddResponseDTO implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	private String deviceId;
	private String hardwareName;
	private String deviceSerialNo;
	private boolean warranty;
	private Date registeredDate;
	private Date expiryDate;
	private String deviceStatus;
	private boolean status;
	private String CreatedByName;
	private String ModifiedByName;

}
