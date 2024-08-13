package com.oasys.helpdesk.dto;

public interface DashboardCount {

	String getmonth_name();

	Integer getTotal_Tickets();

	Integer getOpen_Total();

	Integer getPending_Total();

	Integer getClosed_Total();

	Double getOpen_Percentage();

	Double getPending_Percentage();

	Double getClosed_Percentage();

}
