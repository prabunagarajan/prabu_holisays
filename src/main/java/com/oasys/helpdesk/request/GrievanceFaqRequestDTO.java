package com.oasys.helpdesk.request;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class GrievanceFaqRequestDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7556535340883131007L;
	
	private Long id;
	
	@NotBlank(message = "103")
	private String answer;
	
//	private Boolean deleted = Boolean.FALSE;
	
	@NotBlank(message = "103")
	private String question;
	
	@NotNull(message = "103")
	private Boolean status;
	
	@NotBlank(message = "103")
	private String code;
	
	@NotNull(message = "103")
	private Long issueDetailsId;
	
	@NotNull(message = "103")
	private String typeofUser;
	
	
	
	
	
	
	
	
	
}
