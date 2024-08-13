package com.oasys.helpdesk.dto;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;

import lombok.Data;

@Data
public class AssociatedUserResponseDTO {
	
	
	private Long handlingOfficerUserId;
	
//	private Set<Long> assignToOfficerList;
	
	private Long assignToOfficerId;
	
	//private List<HandlingofficerDTO> handlingOfficerIds;
	
	//private List<AssigntoofficerDTO> assignToOfficerIds;
	
	private String handlingfirstName;
	
	private String assigntoName;
	
    private String associateduserName;

	private String userName;
	
	
}
