package com.oasys.helpdesk.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CountryResponseDto {


	private Long id;

	private String code;
	
	private String countryCode;
	
	private String shortName;
	
	private String countryName;

	private String currency;
	private Boolean isActive;
	
	private Date createdDate;
	
	private String createdBy;
	
	private Date modifiedDate;
	
	private String modifiedBy;
	
}
