package com.oasys.helpdesk.dto;



import java.io.Serializable;
import java.util.Date;
import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ShopcodeResponseDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long id;
 
	private String division;
	
	private String shopCode;
	
	private Integer districtCode;
	
	private String userId;
    
	private String districtName;
	
	public String created_date;

	public String modified_date;

	public String stateCode;
	
	
	
	
}
