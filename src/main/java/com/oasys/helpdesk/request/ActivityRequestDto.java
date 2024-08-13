package com.oasys.helpdesk.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActivityRequestDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2829053753070593981L;
	
	private Long id;

	private String code;
	
	private String name;
	
	private Long displayOrder;
	
	private Long appModuleId;
	
	private String icon;
	
	private String routeUrl;
	
	private Boolean active;
	
}
