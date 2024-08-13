package com.oasys.helpdesk.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SentEmailResponseDTO {

	private Long id;

	private String emailBody;

	private String toEmailList;

	private Long emailInboxId;

	private boolean isActive;

	public Date createdDate;

	public String createdBy;

	public Date modifiedDate;

	public String modifiedBy;

}
