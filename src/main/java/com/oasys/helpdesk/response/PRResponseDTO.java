package com.oasys.helpdesk.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.oasys.helpdesk.entity.Category;
import com.oasys.helpdesk.entity.SubCategory;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PRResponseDTO {
	
private Long id;
	
	private String problem;
	
	private String priorityName;
	
	private String ticketSubcategoryName;
	
	private String ticketCategoryName;

    private boolean isActive;

	private String createdBy;

	private String createdDate;

	private String modifiedBy;

	private String modifiedDate;
	
	private String prCode;
	
	private String categoryId;
	
	private String subCategoryId;
	
	

}
