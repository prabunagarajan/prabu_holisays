package com.oasys.helpdesk.dto;

import java.util.Date;

import javax.validation.constraints.Min;

import lombok.Data;

@Data
public class AccessoriesListDataResponseDTO {

	
private Long id;

	
	private String accessoriesNameId;
	

	private String accessoriesCode;
	
	
	private String accessoriesName;
	
	
	private String accessoriesSerialNo;

	@Min(value =0,message = "workingdays must be equals or greater then 0")
	private Integer warranty;
	
	
	private Date registeredDate;

	private Date expiredDate;

	private String accessoriesStatus;

	private boolean status; 
	
	private String createdBy;
	private String createdDate;
	private String modifiedBy;
	private String modifiedDate;
}
