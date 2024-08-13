package com.oasys.helpdesk.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

import lombok.Data;

@Data
public class ZabbixServerDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long id;

	private String serviceID;

	private String status;

	private String serverName;

	private String sli;
	
	private String errorBudget;

	private String upTime;
	
	private String downTime;
	
	private String excludedownTimes;

	private Integer slo;
	
	private String todaydate;
	
	private String Uptime_In_Timestamp;
	
	private String createdDate;
	
	private String modifiedDate;
	
	
	
}
