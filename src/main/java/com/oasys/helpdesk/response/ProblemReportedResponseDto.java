package com.oasys.helpdesk.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProblemReportedResponseDto {
	private Long id;
	
	private String problem;
	
	private String priorityName;
	
	private String ticketSubcategoryName;
	
	private String ticketCategoryName;

    private boolean isActive;

	private String createdBy;

	private Date createdDate;

	private String modifiedBy;

	private Date modifiedDate;
	
	private String prCode;
	
	private Long subCategoryId;

	private Long categoryId;
	
}
