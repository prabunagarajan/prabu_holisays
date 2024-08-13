package com.oasys.helpdesk.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;



import lombok.Data;

@Data
public class AccessoriesResponseDTO {
	private Long id;
	
	private Boolean status; 
	
	private String createdBy;
	
	private String createdDate;
	
	private String modifiedBy;
	
	private String modifiedDate;
	
    private Long assetTypeId;
    
	private String assetType;
	
	private Long assetnameId;
	    
	private String assetName;
	
	private String assetsubType;
	
	
	
	
}
