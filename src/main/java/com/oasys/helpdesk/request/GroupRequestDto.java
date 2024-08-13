package com.oasys.helpdesk.request;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupRequestDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7556535340883131007L;

	private Long id;
	
	private Long rev;
	
	private Long memberid;

	private String name;
	
	private String type;
	
    private boolean isActive;
	
	private Long createdBy;
	
	private Long modifiedBy;
	
	private List<Long> memberIdList;
	
	//private Long landingActivityId;
	
}
