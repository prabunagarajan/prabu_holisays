package com.oasys.posasset.response;

import java.io.Serializable;
import java.util.Date;
import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class DevicestatusResponseDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String code;
	
	private String name;
	
    private String createdBy;
	
	private String modifiedBy;
	
	private String createdDate;
	
	private String modifiedDate;
	
	
}