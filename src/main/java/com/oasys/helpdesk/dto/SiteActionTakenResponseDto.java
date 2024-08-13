package com.oasys.helpdesk.dto;

import lombok.Data;

@Data
public class SiteActionTakenResponseDto {
	
	private Long id;
	private String actionTaken;
	private Long observationId;
	private String observation;
	private boolean isActive;
	private String createdDate;
	private String modifiedDate;
	private String createdBy;
	private String modifiedBy;
	private String createdByName;
	private String modifiedByName;

}
