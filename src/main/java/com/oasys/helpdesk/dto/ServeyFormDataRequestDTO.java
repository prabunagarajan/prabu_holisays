package com.oasys.helpdesk.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ServeyFormDataRequestDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
    
	@NotNull(message = "103")
	private Long questionId;
	
	@NotNull(message = "103")
	private String rating;
	
	@NotNull(message = "103")
	private String userName;
	
	private String email;
	
	private String licenceId;
	
	private String ticketNo;
	
	private String questionName;
}
