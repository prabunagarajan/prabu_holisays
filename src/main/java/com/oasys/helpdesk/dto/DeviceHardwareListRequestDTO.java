package com.oasys.helpdesk.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class DeviceHardwareListRequestDTO  implements Serializable{


	private static final long  serialVersionUID = 1L;

	private String deviceCode;

	private String deviceId;

	private String deviceSerialNo;
	
	private String deviceHardwareName;

	private Integer warranty;

	private Date registeredDate;

	private Date expiredDate;

	private String deviceStatus;

	private boolean status;
	
}
