package com.oasys.helpdesk.request;

import java.io.Serializable;

import lombok.Data;

@Data
public class GrievancePriorityRequestDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7556535340883131007L;
	
	private Long id;
	
	private String code;
	
	private String priority;
	
	private Long categoryId;
	
	private Boolean status;
	
	private String typeofUser;
	
	
	
	
	
	
	
	

}
