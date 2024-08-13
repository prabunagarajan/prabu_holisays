package com.oasys.helpdesk.request;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class GrievanceWorkflowRequestDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7556535340883131007L;
	
	
	private Long id;
	
	@NotBlank(message = "103")
	private String code;
	
	@NotNull(message = "103")
  	private Long priorityId;
	
	@NotNull(message = "103")
	private Long sla;
   
	@NotNull(message = "103")
  	private Long assignGroup;
  	
	@NotNull(message = "103")
  	private Long assignToId;
    
	@NotNull(message = "103")
   	private boolean status;
	
	@NotNull(message = "103")
	private String typeofUser;
	
	private Long issuedetialsId;

}
