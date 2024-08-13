package com.oasys.helpdesk.request;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.oasys.helpdesk.entity.KnowledgeBase;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KnowledgeRequestDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7556535340883131007L;

	private Long id;
	
	
	
	private Long issueid;
	
	private String answer;
	
	private List<KnowledgeBase> knowledgeList;

    private boolean isActive;
	
	private Long createdBy;
	
	private Long modifiedBy;
	
	
	
}
