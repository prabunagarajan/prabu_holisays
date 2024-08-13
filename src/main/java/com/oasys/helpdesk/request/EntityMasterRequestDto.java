package com.oasys.helpdesk.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EntityMasterRequestDto implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6852770424570186317L;

	private Long id;

	private String code;
	
	private String name;
	
	private String entityType;
	
	private Long entityTypeId;
}
