package com.oasys.helpdesk.request;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SiteActionTakenRequestDto implements Serializable{
	
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

private Long id;
	
	@NotBlank(message = "103")
	private String siteActionTaken;
	
	@NotNull(message = "103")
	private Long observationId;

	@NotNull(message = "103")
    private boolean isActive;

}
