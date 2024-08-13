package com.oasys.helpdesk.dto;
import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SMSDetails implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String mobileNumber;
	
	private String message;
	
	private String event;
	
	private String subEvent;
	
	private String langCode;
	
	private String templateId;
	
	
}