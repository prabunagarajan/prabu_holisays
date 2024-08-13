package com.oasys.helpdesk.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.oasys.helpdesk.conf.Trackable;

import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)

public class UserAddress extends Trackable {

	private String country;
	
	private Long pinCode; 
	
	private String stateCode;
	
	private String stateDesc;
	
	private String districtCode;
	
	private String districtDesc;
	
	private String tahsilCode;
	
	private String tahsilDesc;
	
	private String localityType;

	private String municipalAreaCode;
	
	private String municipalAreaDesc;
	
	private String ward;
	
	private String block;
	
	private String village;
	
	private String policeStationCode;
	
	private String policeStationDesc;
	
	private String locality;
	
	private String street;
	
	
	private UserResponseDto user;
	
}