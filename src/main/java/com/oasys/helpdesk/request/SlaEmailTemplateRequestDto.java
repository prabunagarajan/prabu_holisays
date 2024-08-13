package com.oasys.helpdesk.request;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SlaEmailTemplateRequestDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7556535340883131007L;

	private Long id;
	
	private String emailccList;
	
	private String emailSenderList;

	private String emailSubject;
	
	private String message;
	
	private String templateName;

	private boolean isActive;

    private Long createdBy;
	
	private Long modifiedBy;
	
	
	
}
