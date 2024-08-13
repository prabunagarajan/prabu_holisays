package com.oasys.helpdesk.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DistrictResponseDTO {
	
private Long id;
	
	private String countryname;
	
	private String districtname;
	
	private String status;
	
	private String districtcode;
	
	private String districtshortname;
	
	private String zone;
	
	private String code;
	
	
	private String created_by;

	public String created_date;

	private String modified_by;

	public String modified_date;

	private String update_by;

	public Date update_date;
	
	private String state;
	
	

}
