package com.oasys.helpdesk.response;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IsssueDetresdto {
	
	
private Long id;
	
	private String issueName;
	
	private String subCategoryName;
	
	private String categoryName;

	private String issuecode;
	
    private boolean isActive;

	private String createdBy;

	private String createdDate;

	private String modifiedBy;

	private String modifiedDate;
	
    private String subCategoryId;
	
	private String  categoryId;
	
    private String issuetypeName;
	
	private boolean isIssuetype; 
	

}
