package com.oasys.helpdesk.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class KnowledgeSolutionRequestDTO {

	private Long id;

	private Long subcategoryId;

	private Long categoryId;

	private String solution;
	
	private String solutionId;

	private String issueDetails;

	private String notes;

}
