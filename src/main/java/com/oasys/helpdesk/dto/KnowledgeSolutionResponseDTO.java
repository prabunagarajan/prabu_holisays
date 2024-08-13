package com.oasys.helpdesk.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KnowledgeSolutionResponseDTO {


	private Long id;

	private Long subcategoryId;

	private Long categoryId;
	
	private String subcategoryName;

	private String categoryName;

	private String solution;

	private String solutionId;

	private String issueDetails;

	private String notes;

	private String createdBy;

	private String createdDate;

	private String modifiedBy;

	private String modifiedDate;
}
