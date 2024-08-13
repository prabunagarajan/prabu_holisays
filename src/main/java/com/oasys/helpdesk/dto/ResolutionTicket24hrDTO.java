package com.oasys.helpdesk.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.oasys.helpdesk.request.ResolutionDTOTickets;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResolutionTicket24hrDTO {

	 private Long id;
		
	    private Integer TotalRaisedTicketsINYEAR2023;
		
		private Integer Below24HoursTickets;

		private Integer Above24HoursTickets;
		
		private float ResolutionPercentage;
		
		private String Month;
		
}
