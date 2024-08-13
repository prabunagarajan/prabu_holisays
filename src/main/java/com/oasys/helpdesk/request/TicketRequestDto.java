package com.oasys.helpdesk.request;

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
public class TicketRequestDto {

    private Long id;
	
    private String ticketNumber;
	
	private Long categoryId;

	private Long subcategoryId;
	
	private Long entityId;
	
	private Long entityTypeId;


	private Long issueId;
	
	private Long knowledgeBaseId;
	
    private String queryIssue;
	
	private String queryKnowledge;

	private Long groupId;
	
	private Long ticketstatus;
	
	private Long groupMemberid;

	private Long slaId;
	
	private Long priorityId;
	
	private Long closurePriorityId;

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

	private Long actualproblemId;
	
	private Long actionTakenId;
	
	private Long ticketid;

    private boolean isActive;

	private String createdBy;

	private Date createdDate;

	private String modifiedBy;

	private Date modifiedDate;
	
	 
	

}
