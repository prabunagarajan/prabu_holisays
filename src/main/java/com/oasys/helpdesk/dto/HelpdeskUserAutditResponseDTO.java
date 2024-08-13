package com.oasys.helpdesk.dto;

import lombok.Data;

@Data
public class HelpdeskUserAutditResponseDTO {
	private Long id;

	private String actionPerformedByUsername;

	private String actionPerformedByRole;

	private String actionPerformedOnUsername;

	private String actionPerformedOnRole;

	private String action;

	private String employmentStatus;

	private String designationValue;
	private String designationCode;
	private String actionPerformedDateTime;
	
	private String actionPerformedByEmployeeId;
	private String actionPerformedOnEmployeeId;
}
