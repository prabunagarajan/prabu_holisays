package com.oasys.helpdesk.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class YearMasterDTO implements Serializable {
	
	private Long id;
	private String yearCode;
	private boolean isActive;
	private Long createdBy;
	private Long modifiedBy;
	private String createdByName;
	private String modifiedByName;
	private String createdDate;
	private String modifiedDate;
	

}
