package com.oasys.helpdesk.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmailRequestResponseDto {
	private Long id;

	private String applicationNo;
	
	private String bccEmailList;
	
	private String ccEmailList;
	
    private String emailBody;	
    
    private String emailType;
    
  	private String fromEmailId;
  	
	private boolean isValidEmail;

  	private String priority;
  	
  	private String subject;
  	
	private String toEmailidList;

    private boolean isActive;

	private String createdBy;

	private Date createdDate;

	private String modifiedBy;

	private Date modifiedDate;

}
