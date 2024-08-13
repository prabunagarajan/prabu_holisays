package com.oasys.helpdesk.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.oasys.helpdesk.conf.Trackable;

import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)

public class EntityType extends Trackable {


	private String code;

	private String name;

	private Boolean active;
	
	private List<WorkflowScreens> workFlowScreenList;
	 
    private List<EntityLicenseMapping> licenseList;
	
}
