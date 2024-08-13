package com.oasys.helpdesk.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EntityResponseDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7581134234275973637L;

	private Long entityId;
	
	private String entityName;
	
	private String entityCode;
	
	private String entityType;
	
	
} 
