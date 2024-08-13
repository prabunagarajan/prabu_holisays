package com.oasys.helpdesk.request;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SlaMasterRequestDto implements Serializable{
	private static final long serialVersionUID = 7556535340883131007L;

	private Long id;
	
	@NotBlank(message = "103")
	private String code;
	
	@NotNull(message = "103")
    private boolean isActive;
	
	private Long createdBy;
	
	private Long modifiedBy;
	
	private Long issueDetailsId;
	
	private Long sla;
	
	@NotNull(message = "103")
	private Long priorityId;
}
