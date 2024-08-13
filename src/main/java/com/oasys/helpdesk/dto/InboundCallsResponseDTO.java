package com.oasys.helpdesk.dto;

import lombok.Data;

@Data
public class InboundCallsResponseDTO {

	private Long id;
	private String startTime;
	private String endTime;
	private Long totalCallsReceived;
	private Long totalCallsAttended;
	private Long totalCallsAbondoned;
	private float callsAttendedPercentage;
	private String createdDate;
	private String modifiedDate;
	private Long createdBy;
	private Long modifiedBy;
	private String createdByName;
	private String modifiedByName;
}
