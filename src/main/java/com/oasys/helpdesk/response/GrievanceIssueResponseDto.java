package com.oasys.helpdesk.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GrievanceIssueResponseDto {
	
	private Long id;
	
	private String issueName;
	
	private Long categoryid;
	
	private String  categoryName;
	
    private boolean isActive; 
	
	private String issuecode;
	
	private String createdBy;

	private String createdDate;

	private String modifiedBy;

	private String modifiedDate;
	
	private String typeofUser;
	
	
}
