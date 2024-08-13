package com.oasys.helpdesk.dto;

import java.io.Serializable;



import lombok.Data;
@Data
public class SiteObservationDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String observation;
	private boolean isActive;
	private Long siteIssueTypeId;
	private String siteIssueType;
	private String createdDate;
	private String modifiedDate;
	private Long createdBy;
	private Long modifiedBy;
	private String createdByName;
	private String modifiedByName;
	
	
}
