package com.oasys.helpdesk.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;


@Data
public class DeviceHardwareNameRequestDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	@NotBlank(message = "103")
	private String deviceName;

	private Long assetTypeId;
	
	//@NotBlank(message = "103")
	private String assetType;

	private Long createdBy;

	private boolean status;

	


}
