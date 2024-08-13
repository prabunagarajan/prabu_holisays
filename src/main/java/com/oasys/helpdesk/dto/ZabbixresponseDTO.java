package com.oasys.helpdesk.dto;

import java.text.DecimalFormat;
import java.util.Date;

public interface ZabbixresponseDTO {

	String getserviceId();
	String getstatus();
	String getserverName();
	String getsli();
	String getcreatedDate();
	String getupTime();
	String gettodayDate();
	String getdownTime();
	String geterrorBudget();
	String getexcludedDowntimes();
	String getslo();
	String getUptimeHMS();

	
}
