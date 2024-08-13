package com.oasys.helpdesk.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PriorityMasterResponseDto {
private Long id;
	
    private boolean isActive;

	private String createdBy;

	private String createdDate;

	private String modifiedBy;

	private String modifiedDate;
	
	private String categoryName;

	private String subCategoryName;
	
	private Long categoryId;

	private Long subCategoryId;
	
	private String code;
	
	private String priority;
}
