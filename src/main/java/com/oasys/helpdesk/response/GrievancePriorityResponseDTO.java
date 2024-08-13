package com.oasys.helpdesk.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GrievancePriorityResponseDTO {

	private Long id;
	
	private String code;
	
	private String priority;
	
	private Long categoryId;
	
	private String categoryName;
	
	private Boolean status;

	private String createdBy;

	private Date createdDate;

	private String modifiedBy;

	private Date modifiedDate;
	
	private String typeofUser;
	
	
	
	
	
	
	
}
