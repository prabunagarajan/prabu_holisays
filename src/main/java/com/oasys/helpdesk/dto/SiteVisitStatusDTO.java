package com.oasys.helpdesk.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class SiteVisitStatusDTO implements Serializable{

	private Long id;
	private String code;
	private String name;
	private boolean isActive;
	private Long createdBy;
	private Long modifiedBy;
	private String createdByName;
	private String modifiedByName;
	private String createdDate;
	private String modifiedDate;
}
