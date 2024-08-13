package com.oasys.helpdesk.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class ServeyFormDataResponseDTO implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	
private String formId;
	
	private Long questionId;

	private Long rating;
	
	

}
