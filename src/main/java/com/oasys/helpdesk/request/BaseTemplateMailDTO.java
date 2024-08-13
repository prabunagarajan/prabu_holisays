package com.oasys.helpdesk.request;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BaseTemplateMailDTO {
	
	@NotNull(message = "103")
	protected String subject;
	@NotNull(message = "103")
	protected String toId;	
	@NotNull(message = "103")
	protected String customerName;
	@NotNull(message = "103")
	protected String ticketID;
	@NotNull(message = "103")
	protected String timeStamp;
	@NotNull(message = "103")
	protected String licenseNo;
	
}
