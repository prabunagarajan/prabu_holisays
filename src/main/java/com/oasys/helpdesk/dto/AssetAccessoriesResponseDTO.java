package com.oasys.helpdesk.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssetAccessoriesResponseDTO {
	
	private Long id;
	
	private String accessoriesCode;
	
	private String accessoriesName;
	
	private Boolean status;
	
	private String created_by;

	public String created_date;

	private String modified_by;
	
	public String modified_date;

}
