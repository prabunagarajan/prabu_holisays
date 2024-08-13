package com.oasys.helpdesk.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.oasys.helpdesk.entity.Category;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IssueDetailsResponseDto {
	private Long id;
	
	private String issueName;
	
	private String subCategoryName;
	
	private String categoryName;

	private String issuecode;
	
    private boolean isActive;

	private String createdBy;

	private Date createdDate;

	private String modifiedBy;

	private Date modifiedDate;
	
    private Long subCategoryId;
	
	private Long  categoryId;

	private String issuetypeName;
	
	private boolean isIssuetype; 
	
}
