package com.oasys.helpdesk.response;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.oasys.helpdesk.dto.EntityMasterDTO;
import com.oasys.helpdesk.dto.EntityTypeMasterDTO;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketResponseDto {
	 private Long id;
		
	    private String ticketNumber;
		
		private String categoryName;

		private String subcategoryName;
		
		private String entityTypeName;
		
		private String entityName;

		private String issueName;
		
		private String knowledgeBaseName;

		private String groupName;
		
		private String ticketstatusName;
		
		private String groupMemberName;

		private String slaName;
		
		private Integer slaDays;
		
		private String priorityName;
		
		private String closurepriorityName;

        private String  licenceNumber;

		private String  inrNumber;
		
		private String  address1;

		private String  address2;
		
		private String  state;
		
		private String  mobileNumber;
		
		private String  emailId;
		
		private String  callerNumber;
		
		private String  description;
		
		private String  notes;


	    private boolean isActive;

		private String createdBy;

		private Date createdDate;

		private String modifiedBy;

		private Date modifiedDate;
		
		

	
	    private List<TicketHistoryResponseDto> ticketHistoryResponseDto;
	    
	    private EntityMasterDTO entityMasterDTO;
	    
	    private EntityTypeMasterDTO entityTypeMasterDTO;
	    
	    private EntityType entityType;
	    
	    private UserMasterResponseDto userMasterResponseDto;
	
	    private UserDetails userDetails;
	    
}
