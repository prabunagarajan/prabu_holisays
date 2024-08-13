package com.oasys.helpdesk.dto;

import java.io.Serializable;

import lombok.Data;
@Data
public class SiteIssueTypeResponseDTO {
	/**
	 * 
	 */

	private Long id;
	private String issuetype;
	private boolean isActive;
	private String createdDate;
	private String modifiedDate;
	private Long createdBy;
	private Long modifiedBy;
	//private String createdByName;
	//private String modifiedByName;
	private String created_by;
    private String modified_by;
    public String created_date;
    public String modified_date;


}
