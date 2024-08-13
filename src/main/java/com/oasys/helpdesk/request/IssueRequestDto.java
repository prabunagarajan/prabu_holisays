package com.oasys.helpdesk.request;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IssueRequestDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7556535340883131007L;

	private Long id;
	
	@NotBlank(message = "103")
	private String issueName;
	
	@NotNull(message = "103")
	private Long subCategoryId;
	
	private Long  categoryId;
	
	@NotNull(message = "103")
    private boolean isActive; 
	
	private Long createdBy;
	
	private Long modifiedBy;
	
	
	private String issuecode;
	
	@NotNull(message = "103")
    private boolean isIssuetype; 
	
}
