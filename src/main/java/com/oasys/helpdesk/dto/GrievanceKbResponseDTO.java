package com.oasys.helpdesk.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GrievanceKbResponseDTO {

	
	 private Long id;

	    private String code;

	    private String category;
	    
	    private Long categoryid;
	    
	    private Long issueDetailsId;

	    private String issueDetails;

	    private String priority;

	    private Long sla;

	    private String remarks;

	    private String knowledgeSolution;

	    private boolean isResolved;
	    
	    private boolean status;

	    private String createdBy;

	    private String createdDate;

	    private String modifiedBy;

	    private String modifiedDate;
	    
	    private int count;
	    
	    private String typeofUser;

	   
	    
}
