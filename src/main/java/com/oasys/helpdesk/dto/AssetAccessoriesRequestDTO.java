package com.oasys.helpdesk.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class AssetAccessoriesRequestDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String accessoriesCode;
	
	private String accessoriesName;
	
	private Boolean status;

}
