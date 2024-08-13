package com.oasys.helpdesk.request;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SlaSmsTemplateRequestDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7556535340883131007L;

	private Long id;
	
	private String mobileNumberList;

	private String message;
	
	private String templateName;

	private boolean isActive;

    private Long createdBy;
	
	private Long modifiedBy;
	
	
	
}
