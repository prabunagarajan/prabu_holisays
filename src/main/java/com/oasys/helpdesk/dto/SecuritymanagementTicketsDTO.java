package com.oasys.helpdesk.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.oasys.helpdesk.request.ResolutionDTOTickets;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SecuritymanagementTicketsDTO {

	private Integer Total_Tickets_Raised;

	private Integer Tickets_Closed_Inprogress_Within_SLA;

	private Integer Tickets_Breached_SLA;

	private float Resolution_Percentage;
	

}
