package com.oasys.helpdesk.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserShiftConfigurationResponseDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String startDate;
	
	private String endDate;
	
	private ShiftConfigResponseDTO shiftConfigResponseDTO;
	
	private ShiftWorkingDaysResponseDTO shiftWorkingDaysResponseDTO;

	private String createdBy;
	private String createdDate;
	private String modifiedBy;
	private String modifiedDate;
}
