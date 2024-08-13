package com.oasys.helpdesk.dto;

import lombok.Data;

@Data

public class AssetStatusResponseDTO {

	private Long id;

	private String code;

	private String name;

	private Boolean status;

	private String createdBy;

	private String createdDate;

	private String modifiedBy;

	private String modifiedDate;
}
