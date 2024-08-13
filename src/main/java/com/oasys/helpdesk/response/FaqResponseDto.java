package com.oasys.helpdesk.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FaqResponseDto {
	private Long id;
	
	private String answer;
	
	private String question;
	
    private Boolean status;
    
    private Boolean deleted;
    
	private String categoryName;
	
	private String subCategoryName;

	private String createdBy;

	private Date createdDate;

	private String modifiedBy;

	private Date modifiedDate;
	
	private Long categoryid;
	
	private Long subCategoryid;
	private String code;
}
