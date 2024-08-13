package com.oasys.helpdesk.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;



import lombok.Data;

@Data
public class AccessoriesRequestDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
    private Long assetTypeId;
    
	private String assetType;
	
	private Long assetnameId;
	    
	private String assetName;
	
	private String assetsubType;

	private boolean status;
	
	private Long createdBy;
	
}
