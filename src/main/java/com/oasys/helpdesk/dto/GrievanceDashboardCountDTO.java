package com.oasys.helpdesk.dto;

public interface GrievanceDashboardCountDTO {

	Integer getTotal_Counts();

	Integer getNeed_Clarification_from_Dept();

	Integer getCall_Closer_Reported();

	Integer getClosed();

	Integer getResolved();

	Integer getPending();

	Integer getAssigned();

	Double getPer_Need_Clarification_from_Dept();

	Double getPer_Call_Closer_Reported();

	Double getPer_Closed();

	Double getPer_Resolved();

	Double getPer_Pending();

	Double getPer_Assigned();

}
