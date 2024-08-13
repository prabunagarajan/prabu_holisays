package com.oasys.helpdesk.dto;


import java.io.Serializable;
import java.util.Date;
import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class DistrictRequestDTO implements Serializable {
	
	
	private static final long serialVersionUID = 1L;

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
