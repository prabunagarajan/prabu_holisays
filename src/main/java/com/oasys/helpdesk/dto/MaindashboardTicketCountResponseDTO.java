package com.oasys.helpdesk.dto;

import lombok.Data;
import lombok.Value;

@Data
public class MaindashboardTicketCountResponseDTO {
	
	private Integer overalltickets;
   
	private Integer closedwithinsla;
	
	private Integer closedoutofsla;
	
	private Integer openwithinsla;
	
	private Integer openoutofsla;
	
}
