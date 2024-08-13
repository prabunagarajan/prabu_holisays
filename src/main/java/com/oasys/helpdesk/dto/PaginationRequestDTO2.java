package com.oasys.helpdesk.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class PaginationRequestDTO2 {
	
	@Min(value =0,message = "pageNo must be equals or greater then 0")
	Integer pageNo;

	@Min(value =1,message = "paginationSize must be equals or greater then 1")
	Integer paginationSize;
	
	@NotBlank(message = "103")
	String sortField;
	
	@NotBlank(message = "103")
	String sortOrder;

	Map<String, Object> filters;
	
	
	String search;

	private String fromDate;
	
	private String toDate;
	
	
	private ArrayList<String> licenseNumber;
	
	private ArrayList<String> shopCode;
	
	  private Long id;
	 
	    private String ticketNumber;
		
		private Long categoryId;

		private Long subCategoryId;
		
		private Long priorityId;
		
		private Long ticketStatusId;
		
		private Long issueDetailsId;
		
		private Long slaId;
		
		private Long knowledgeBaseId;
		
		private Long assignGroupId;
		
		private Long issueFromId;
		
		private String entityTypeId;
		
		//private String  licenseNumber;
		
		private String licenceTypeId;
		
		private String callDisconnect;
		
		private String RequiredField;
		
		private Long assignToId;

	    private boolean isActive;

		private String createdBy;

		private String createdDate;

		private String modifiedBy;

		private Date modifiedDate;
		
		private String remarks;
		
		private String unitName;
		
		private String licenseStatus;
		
		private Long actualProblemId;
		
		private Long actionTakenId;
		
		private Long problemReportedId;
		
		private String problemReported;
		
		private String email;
		
		private String mobile;
		
		private String notes;
		
		private Long solutionId;
		
		private boolean flag;
		
		private String alternativemobileNumber;
		
	//	private String fromDate;
		
	//	private String toDate;
		
		private String address;
	    
		private String ticketstatusName;
		
		private String createdbyName;

		public void put(String lic_no) {
			// TODO Auto-generated method stub
			
		}
	
		

	}



