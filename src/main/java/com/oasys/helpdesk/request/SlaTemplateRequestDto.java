package com.oasys.helpdesk.request;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SlaTemplateRequestDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7556535340883131007L;

	private Long id;
	
	private String templateType;
	
	private String templateName;

	private String subject;
	
	private String fromAddress;
	
	private String ccAddress;
	
	private String description;
	
	private String mobileNumber;

	private String message;


    private boolean isActive;

    private Long createdBy;
	
	private Long modifiedBy;
	
	
	
}
