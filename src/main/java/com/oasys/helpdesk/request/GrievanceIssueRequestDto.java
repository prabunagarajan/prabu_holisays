package com.oasys.helpdesk.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GrievanceIssueRequestDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7556535340883131007L;

	private Long id;
	
	private String issueName;
	
	private Long  categoryId;
	
    private boolean isActive; 
	
	private String issuecode;
	
	private String typeofUser;
	

}
