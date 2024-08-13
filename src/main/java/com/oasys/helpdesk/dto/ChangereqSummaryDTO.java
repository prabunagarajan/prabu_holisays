package com.oasys.helpdesk.dto;

public interface ChangereqSummaryDTO {
	
	int getTotalChangeRequest();
	int getInprogress();
	int getAccepted();
	int getApproved();
	int getAssigned();
	int getPending();
	int getRequestForClarification();
	int getRejected();
	int getCancelled();
	int getCompleted();

}
