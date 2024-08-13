package com.oasys.helpdesk.dto;

import java.io.Serializable;

import lombok.Data;

@Data

public class SiteIssueTypeDTO implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String issuetype;
	private boolean isActive;
	private String createdDate;
	private String modifiedDate;
	private Long createdBy;
	private Long modifiedBy;
	private String createdByName;
	private String modifiedByName;
	

}
