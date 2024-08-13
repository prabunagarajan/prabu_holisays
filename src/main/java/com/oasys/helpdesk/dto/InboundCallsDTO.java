package com.oasys.helpdesk.dto;

import lombok.Data;

@Data
public class InboundCallsDTO {
	
	private Long id;
	private String startTime;
	private String endTime;
	private Long totalCallsReceived;
	private Long totalCallsAttended;
	private Long totalCallsAbondoned;
	private float callsAttendedPercentage;

	private String fromDate;
	private String toDate;
}
