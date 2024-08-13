package com.oasys.helpdesk.response;

import java.util.Date;

import lombok.Data;

@Data
public class SubCategoryResponseDto {
	private Long id;
	
	private Long categoryid;
	
	private String categoryName;
	
	private String subcategoryName;
	
    private Boolean active;

	private String createdBy;

	private Date createdDate;

	private String modifiedBy;

	private Date modifiedDate;
	private String code;
}
