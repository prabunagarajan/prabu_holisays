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
public class TicketStatusRequestDto {

    private Long id;
	
    private Long ticketid;
	
	private Integer assignedBy;

	private Long assignedgroup;
	
	private String comments;

	private Long ticketstatus;


    private boolean isActive;

	private String createdBy;

	private Date createdDate;

	private String modifiedBy;

	private Date modifiedDate;
	
	

}
