package com.oasys.helpdesk.dto;

public interface SecuritymanagementDTO {

	int getTotal_Tickets_Raised();

	int getTickets_Closed_Inprogress_Within_SLA();

	int getTickets_Breached_SLA();
	
	float getResolutionPercentage();
	

}
