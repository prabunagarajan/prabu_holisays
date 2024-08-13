package com.oasys.helpdesk.response;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GrievanceWorkflowResponseDTO {
	
	private Long id;
	
	private String code;
	
	private Long categoryId;
	
	private String categoryName;
	
	private Long issueDetailsId;
	
	private String issueDetails;
	
	private Long priorityId;
	
  	private String priority;
	
	private Long sla;
   
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
