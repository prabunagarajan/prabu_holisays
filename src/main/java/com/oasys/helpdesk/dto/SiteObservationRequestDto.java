package com.oasys.helpdesk.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SiteObservationRequestDto implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	@NotBlank(message = "103")
	private String siteObservation;
	
	@NotNull(message = "103")
	private Long siteIssueTypeId;

	@NotNull(message = "103")
    private boolean isActive;

}
