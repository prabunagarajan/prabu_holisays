package com.oasys.helpdesk.response;

import java.util.Date;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActualProblemResponseDto {
	private Long id;
	
	private String actualProblem;
	
    private boolean isActive;

	private String createdBy;

	private String createdDate;

	private String modifiedBy;

	private String modifiedDate;
	
	private String categoryName;

	private String subCategoryName;
	
	private Long categoryId;

	private Long subCategoryId;
	
	private String actualProblemCode;
}
