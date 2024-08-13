package com.oasys.helpdesk.request;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SlaConfigurationRequestDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7556535340883131007L;

	private Long id;
	
	private String ruleName;
	
//	private String userAssignee;
//	
//	private String groupAssignee;

	private String priority;
	
	private String status;
	
	private int thresholdTime;
	
	private Long emailTemplateId;
	
	private Long smsTemplateId;
	
	private Long ticketCategoryId;
	
	private Long ticketSubCategoryId;
	
	private String templateType;

    private boolean isActive;

    private Long createdBy;
	
	private Long modifiedBy;
	
	
	
}
