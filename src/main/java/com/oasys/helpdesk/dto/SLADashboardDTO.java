package com.oasys.helpdesk.dto;

import java.util.List;

import lombok.Data;

@Data
public class SLADashboardDTO {

	private String serviceID;
	private String serverName;
	
	private List<String> servicsIds;
	
	private String fromDate;
	private String toDate;
}
