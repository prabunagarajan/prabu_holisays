package com.oasys.helpdesk.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SlaConfigurationResponseDto {
	private Long id;
	
    private String ruleName;
	
    private String groupAssignee;
	
	private String priority;
	
	private String status;
	
	private int thresholdTime;
	
	private String userAssignee;
	
	private String slaEmailName;
	
	private String slaSmsName;
	
	private String categoryName;
	
	private String subcategoryName;
	
	private String templateType;
	
	private boolean isActive;

	private String createdBy;

	private Date createdDate;

	private String modifiedBy;

	private Date modifiedDate;
	
}
