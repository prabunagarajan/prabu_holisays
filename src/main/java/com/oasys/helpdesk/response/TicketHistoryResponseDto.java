package com.oasys.helpdesk.response;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketHistoryResponseDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7581134234275973637L;

	private Long id;
	
	private String historyprocessedBy;
	
	private String historyticketstatusName;
	
	private Date historycreatedDate;

	private String historygroupName;
	
	private String actualproblem;
	
	private String actionTaken;
	
	private String closurePriority;

	
	private String historydescription;
	
	private boolean isActive;

    private String createdBy;

	private Date createdDate;

	private String modifiedBy;

	private Date modifiedDate;
	
	
} 
