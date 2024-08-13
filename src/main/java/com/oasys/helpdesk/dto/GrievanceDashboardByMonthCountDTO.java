package com.oasys.helpdesk.dto;

public interface GrievanceDashboardByMonthCountDTO {

	String getMonth();

	Integer getTotal_Ticket_Count();

	Integer getCall_Closer_Reported();

	Integer getClosed();

	Integer getResolved();

	Integer getPending();

	Integer getAssigned();

	Integer getNeed_Clarification_from_Dept();

	Double getPer_Need_Clarification_from_Dept();

	Double getPer_Call_Closer_Reported();

	Double getPer_Closed();

	Double getPer_Resolved();

	Double getPer_Pending();

	Double getPer_Assigned();

}
