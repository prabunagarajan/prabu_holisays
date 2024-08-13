package com.oasys.helpdesk.dto;

import lombok.Data;

@Data
public class ChangeRequestFeaturesDTO {

	private Long id;
	
	private String code;

	private String name;

	private boolean isActive;

	private String createdDate;

	private String createdBy;

	private String modifiedBy;

	private String modifiedDate;

}
