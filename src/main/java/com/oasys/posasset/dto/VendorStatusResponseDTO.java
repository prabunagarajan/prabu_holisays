package com.oasys.posasset.dto;

import lombok.Data;

@Data
public class VendorStatusResponseDTO {

	private Long id;

	private String code;

	private String name;

	private Boolean isActive;

	private String createdBy;

	private String createdDate;

	private String modifiedBy;

	private String modifiedDate;
}
