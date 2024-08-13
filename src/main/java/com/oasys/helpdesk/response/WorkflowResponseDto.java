package com.oasys.helpdesk.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkflowResponseDto {
private Long id;
	
	private Long sla;
	
	private Long slaId;
	
	private String issueDetails;
	
	private Long issueDetailsId;
	
    private boolean isActive;

	private String createdBy;

	private String createdDate;

	private String modifiedBy;

	private String modifiedDate;
	
	private String categoryName;
	
	private String subcategoryName;
	
	private String code;
	
	private Long categoryId;
	
	private Long subCategoryId;
	
	private String priorityName;
	
	private Long assignToId;
	
	private String assignToName;
	
	private Long assignToGroupId;
	
	private String assignToGroupName;
	
	private Long priorityId;
	
}
