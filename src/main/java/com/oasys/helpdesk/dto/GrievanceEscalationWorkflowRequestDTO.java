package com.oasys.helpdesk.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class GrievanceEscalationWorkflowRequestDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7556535340883131007L;
	
	
	private Long id;
	
	@NotBlank(message = "103")
	private String code;
	
	@NotNull(message = "103")
	private Long slaId;
   
	@NotNull(message = "103")
  	private Long assignGroupId;
  	
	@NotNull(message = "103")
  	private Long assignToId;
    
	@NotNull(message = "103")
   	private Boolean status;
	
	@NotNull(message = "103")
	private String typeofUser;

	
	
	
	
	
	
	
	
	
	
	
}
