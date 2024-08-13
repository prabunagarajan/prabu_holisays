package com.oasys.helpdesk.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.oasys.helpdesk.conf.Trackable;

import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkflowScreens extends Trackable {
	
	private String screenName;
	
	private String screenCode;
	
	private String screenDescription;
	
	private Boolean isActive;

	
	private EntityType entityType;

	
}
