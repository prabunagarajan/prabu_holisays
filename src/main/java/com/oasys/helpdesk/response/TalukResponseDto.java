package com.oasys.helpdesk.response;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TalukResponseDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7121310139309476899L;

	private Long id;

	private String code;

	private String name;

	private String stateName;
	
	private String districtName;
	
	private String createdBy;
	
	private Date createdDate;
	
	private String modifiedBy;
	
	private Date modifiedDate;

	private Boolean active;
	
	private DistrictResponseDto districtResponseDto;
	
	
}
