package com.oasys.helpdesk.dto;

import lombok.Data;

@Data
public class DeviceAccessoriesStatusResponseDTO {
	private Long id;

	private String code;

	private String deviceAccesoriesStatus;

	private Boolean status;

	private String createdBy;

	private String createdDate;

	private String modifiedBy;

	private String modifiedDate;
}
