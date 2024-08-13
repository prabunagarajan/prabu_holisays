package com.oasys.helpdesk.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryResponseDto {
	private Long id;
	
	private String categoryName;
	
    private Boolean active;

	private String createdBy;

	private Date createdDate;

	private String modifiedBy;

	private Date modifiedDate;
	
	private String code;
}
