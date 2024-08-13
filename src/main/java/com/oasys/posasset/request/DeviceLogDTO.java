package com.oasys.posasset.request;

import java.io.Serializable;
import java.util.Date;
import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class DeviceLogDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String deviceId;
	
	private String shopCode;
	
	private String status;
	
	private String remarks;
	
	
	
	
}