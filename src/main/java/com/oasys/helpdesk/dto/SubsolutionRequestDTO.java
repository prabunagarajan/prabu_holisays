package com.oasys.helpdesk.dto;


import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.oasys.helpdesk.entity.Group;
import com.oasys.helpdesk.entity.KnowledgeBase;
import com.oasys.helpdesk.entity.Category;
import com.oasys.helpdesk.entity.Priority;
import com.oasys.helpdesk.entity.SubCategory;
import com.oasys.helpdesk.entity.SlaConfiguration;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubsolutionRequestDTO implements Serializable{
	
	
	private Long id;
	
	private String issueDetails;
	
	private boolean status;
	
    private Long subCategoryId;
	
	private Long  categoryId;
	
	private String priority;
	
	private int sla;
	
	private String knowledge_resolution;
	
	private String remarks;
	
	private String subCode;
	
	private String subSolution;
	
	private String created_by;

	public String created_date;

	private String modified_by;

	public String modified_date;	

}
