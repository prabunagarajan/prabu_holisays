package com.oasys.posasset.response;

import java.io.Serializable;
import java.util.Date;
import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class DeviceLogResponseDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String deviceId;
	
	private String shopCode;
	
	private String status;
	
	private String remarks;
	
	private String createdBy;
	
	private String createdDate;
	
	
}