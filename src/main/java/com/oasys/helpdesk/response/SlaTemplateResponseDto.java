package com.oasys.helpdesk.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SlaTemplateResponseDto {
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

	private String createdBy;

	private Date createdDate;

	private String modifiedBy;

	private Date modifiedDate;
	
}
