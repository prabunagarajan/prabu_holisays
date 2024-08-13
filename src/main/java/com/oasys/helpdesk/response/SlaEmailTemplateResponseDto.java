package com.oasys.helpdesk.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SlaEmailTemplateResponseDto {
	private Long id;
	
    private String emailccList;
	
	private String emailSenderList;

	private String emailSubject;
	
	private String message;
	
	private String templateName;
	
    private boolean isActive;

	private String createdBy;

	private Date createdDate;

	private String modifiedBy;

	private Date modifiedDate;
	
}
