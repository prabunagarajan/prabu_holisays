package com.oasys.helpdesk.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GrievanceSlaResponseDTO {
	
	private Long id;
	
	private Long sla;
	
	private String code;
	
	private String priorityName;
	
	private Long priorityId;
	
	private String categoryName;
	
	private Long categoryId;
	
	private String issueDetails;
	
	private Long issueDetailsId;
	
	private Boolean status;

	private String createdBy;

	private Date createdDate;

	private String modifiedBy;

	private Date modifiedDate;
	
	private String typeofUser;

	
}
