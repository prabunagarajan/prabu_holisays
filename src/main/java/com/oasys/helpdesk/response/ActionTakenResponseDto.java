package com.oasys.helpdesk.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActionTakenResponseDto {
	private Long id;
	
	private String actualProblem;
	
	private String actionTaken;
	
	private Long actualProblemId;
	
    private boolean isActive;

	private String createdBy;

	private String createdDate;

	private String modifiedBy;

	private String modifiedDate;
	
	private String categoryName;
	
	private String subcategoryName;
	
	private String actionTakenCode;
	
	private Long categoryId;
	
	private Long subCategoryId;
} 
