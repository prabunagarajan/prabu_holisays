package com.oasys.helpdesk.dto;

import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class DeviceHardwareListResponseDTO {

	
	private Long id;

	
	private String deviceId;
	
	private String deviceSerialNo;

	private String deviceHardwareName;
	
	@Min(value =0,message = "workingdays must be equals or greater then 0")
	private Integer warranty;
	

	private Date registeredDate;
	

	private Date expiredDate;
	

	private String deviceStatus;
	
	private boolean status;
	
	private String createdBy;

	private String createdDate;

	private String modifiedBy;

	private String modifiedDate;
}

