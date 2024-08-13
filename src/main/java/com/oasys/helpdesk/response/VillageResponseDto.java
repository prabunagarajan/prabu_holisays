package com.oasys.helpdesk.response;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VillageResponseDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6972215401995186541L;

	private Long id;

	private String code;

	private String name;

	private String talukName;
	
	private String districtName;
	
	private String createdBy;
	
	private Date createdDate;
	
	private String modifiedBy;
	
	private Date modifiedDate;

	private Boolean active;
	
	private TalukResponseDto talukResponseDto;
	
	private DistrictResponseDto districtResponseDto;
	
	private StateResponseDto stateResponseDto;
	
	private CountryResponseDto countryResponseDto;

}
