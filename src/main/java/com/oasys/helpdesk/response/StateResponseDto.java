package com.oasys.helpdesk.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StateResponseDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 480448100765105007L;

	private Long id;

	private String stateCode;
	
	private String shortName;
	
	private String stateName;

	private CountryResponseDto country;
	
	private String countryName;
	

	
	private Boolean active;

}
