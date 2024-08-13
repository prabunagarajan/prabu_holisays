package com.oasys.posasset.dto;

public interface UpexServerDetailsDTO {

	String getServerName();
	String getTotalOperatingHrs();
	String getTotalUptimeHrs();
	String getTotalDowntimeHrs();
	String getErrorBudget();
	String getSlo();
}
