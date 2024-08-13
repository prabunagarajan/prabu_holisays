package com.oasys.helpdesk.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KnowledgeResponseDto {
	private Long id;
	
	private String issueName;
	
	private Long issueid;
	
	private String answer;
	
	private Long answercount;
	
	
    private boolean isActive;
    
    private boolean isAnswerActive;
    
    private String createdBy;

	private Date createdDate;

	private String modifiedBy;

	private Date modifiedDate;
}
