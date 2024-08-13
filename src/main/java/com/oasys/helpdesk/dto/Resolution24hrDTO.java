package com.oasys.helpdesk.dto;

public interface Resolution24hrDTO {
	 int getTotalRaisedTicketsINYEAR2023();
	 int getBelow24HoursTickets();
	 int getAbove24HoursTickets();
	 String getMonth();
}
