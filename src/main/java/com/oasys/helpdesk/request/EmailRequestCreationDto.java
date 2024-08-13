package com.oasys.helpdesk.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmailRequestCreationDto implements Serializable {

	private String bccEmailList;
	private String applicationNo;
	private String ccEmailList;
	private String emailBody;
	private String emailType;
	private String fromEmailId;
	private boolean isValidEmail;
	private String priority;
	private String subject;
	private String toEmailidList;
	private boolean isActive;
}
