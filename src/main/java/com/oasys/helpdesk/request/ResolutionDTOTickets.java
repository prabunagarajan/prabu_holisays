package com.oasys.helpdesk.request;


import java.util.ArrayList;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.criteria.Expression;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResolutionDTOTickets {
	    private Long id;
		
	    private Integer TotalRaisedTicketsINYEAR2023;
		
		private Integer Below12HoursTickets;

		private Integer Above12HoursTickets;
		
		private float ResolutionPercentage;
		
		private String Month;
		
}
