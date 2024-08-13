package com.oasys.helpdesk.request;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode(callSuper=false)
public class AcknowledgeAndOthersEmailDTO extends BaseTemplateMailDTO {
	
	@NotNull(message = "103")
	private String category;
	@NotNull(message = "103")
	private String subCategory;
	@NotNull(message = "103")
	private String sla;	
	@NotNull(message = "103")
	private String ticketStatus;
	@NotNull(message = "103")
    private String teamName;
	@NotNull(message = "103")
	private String contactNumber;
	@NotNull(message = "103")
	private String issue;
	@NotNull(message = "103")
	private String resolveDate;
	@NotNull(message = "103")
	private String surveyQuestion;
}

