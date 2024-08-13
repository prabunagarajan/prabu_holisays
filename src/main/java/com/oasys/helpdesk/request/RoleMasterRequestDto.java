package com.oasys.helpdesk.request;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleMasterRequestDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7556535340883131007L;

	private Long id;

	private String roleName;
	
	private String roleCode;
	
	private Boolean status;
	
	private Long createdBy;
	
	private Long modifiedBy;
	
	private List<Long> activityIdList;
	
	private Long landingActivityId;
	
}
