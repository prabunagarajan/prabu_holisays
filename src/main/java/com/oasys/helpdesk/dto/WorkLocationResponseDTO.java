package com.oasys.helpdesk.dto;

import java.io.Serializable;

import javax.persistence.Column;

import lombok.Data;

@Data
public class WorkLocationResponseDTO implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String departmentCode;
	private String departmentValue;
	
	private String entityTypeCode;
	private String entityTypeValue;
	private String districtCode;
	private String districtValue;
	
	private String updateReason;
	private String createdBy;
	private String createdDate;
	private String modifiedBy;
	private String modifiedDate;
	private Long id;
	private String stateCode;
	
	private String districtNames;
}
