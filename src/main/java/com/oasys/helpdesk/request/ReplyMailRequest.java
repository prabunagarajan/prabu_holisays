package com.oasys.helpdesk.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ReplyMailRequest 
{
	@NotNull(message="103")
	private Long emailId;
	
	@NotBlank(message="103")
	private String emailBody;
}
