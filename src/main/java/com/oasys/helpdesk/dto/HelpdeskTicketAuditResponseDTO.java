package com.oasys.helpdesk.dto;

import lombok.Data;

@Data
public class HelpdeskTicketAuditResponseDTO {
	
	private Long id;
	
	private String actionPerformedByUser;
	
	private String actionPerformedByRole;
	
	private String action;
	
	private String ticketNumber;
	
	private String ticketStatus;
	
	private String comments;
	
	private String designationValue;
	private String designationCode;
	private String actionPerformedDateTime;
	private String assignTo;
	
}
