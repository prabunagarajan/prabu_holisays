package com.oasys.helpdesk.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GrievanceFaqResponseDTO {
	
	private Long id;
	
	private String code;
	
	private String answer;
	
	private String question;
	
    private Boolean status;
    
    
	private String categoryName;
	
	private Long catId;

	private String createdBy;

	private Date createdDate;

	private String modifiedBy;

	private Date modifiedDate;
	
	private Long issueDetailsId;
	
	private String issueDetails;
	
	private String typeofUser;
	
	
	
	
	
	
	
	
	
	

}
