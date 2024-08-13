package com.oasys.helpdesk.dto;

import lombok.Data;

@Data
public class SurveyFormListResponseDTO {

	private String formId;
	
	private Long questionId;

	private Long rating;
	
	private String userName;

	private String email;
	
	private String licenceId;
	
	private String createdBy;

	private String createdDate;

	private String modifiedBy;

	private String modifiedDate;
	
}
