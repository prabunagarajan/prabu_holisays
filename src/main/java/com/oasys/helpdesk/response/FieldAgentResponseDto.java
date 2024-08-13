package com.oasys.helpdesk.response;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FieldAgentResponseDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7581134234275973637L;

	private Long id;
	
	private String groupName;
	
	private String fieldAgentName;
	
	private Long fieldAgentid;
	
	private boolean isActive;

    private String createdBy;

	private Date createdDate;

	private String modifiedBy;

	private Date modifiedDate;
	
	
} 
