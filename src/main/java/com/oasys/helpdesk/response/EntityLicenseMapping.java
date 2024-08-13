package com.oasys.helpdesk.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.oasys.helpdesk.conf.Trackable;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EntityLicenseMapping extends Trackable {
	
	private String licenseTypeCode;
	
	private String licenseTypeDesc;
	
	private String licenseDesc;
	
	private Boolean isActive;

	
	private EntityType entityType;

	
}
