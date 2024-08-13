package com.oasys.helpdesk.dto;

import java.text.DecimalFormat;
import java.util.Date;

public interface InboundCallsSummaryDTO {

	
	 Long getTotalCallsReceived();
	 Long getTotalCallsAbandoned();
	 Long getTotalCallsAttended();
	
	 Float getCallsAttendedPercentage();
	 }
