package com.oasys.helpdesk.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DistrictResponseDto implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 3541544291607409845L;

	private Long id;

	private String code;

	private String name;

	private String shortName;

	private StateResponseDto stateResDto;
	
	private CountryResponseDto countryResponseDto;
	
	private String stateName;
	
	private String countryName;

	private Boolean active;

}
