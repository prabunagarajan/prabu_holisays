package com.oasys.helpdesk.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UserShiftConfigurationRequestDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	@NotNull(message = "103")
	private String startDate;
	
	@NotNull(message = "103")
	private String endDate;
	
	@NotNull(message = "103")
	private Long shifConfigId;

	@NotNull(message = "103")
	private Long shifWorkingDaysId;
	
}
