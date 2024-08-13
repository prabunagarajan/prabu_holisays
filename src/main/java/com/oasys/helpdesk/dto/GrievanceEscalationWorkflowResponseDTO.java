package com.oasys.helpdesk.dto;

import java.util.Date;

import lombok.Data;

@Data
public class GrievanceEscalationWorkflowResponseDTO {
	private Long id;

	private String code;

	private Long categoryId;

	private String categoryName;

	private Long issueDetailsId;

	private String issueDetails;

	private Long priorityId;

	private String priority;

	private Long slaValue;

	private Long assignGroupId;

	private String assignGroupName;

	private Long assignToId;

	private String assignToName;

	private Boolean status;

	private String createdBy;

	private Date createdDate;

	private String modifiedBy;

	private Date modifiedDate;

	private Long slaId;
	
	private String typeofUser;
	
	
	
	
	
	
}
