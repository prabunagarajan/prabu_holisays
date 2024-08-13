package com.oasys.helpdesk.dto;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class EntityMasterDTO {
	
	private Long id;

	private String code;

	private String name;
	

	private Boolean active;	
	 
}



