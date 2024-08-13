package com.oasys.helpdesk.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;



import lombok.Data;

@Data
public class AssociatedUserRequestDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
//	@NotNull(message = "103")
//	private Set<Long> handlingOfficerIds;
	
	private List<HandlingofficerDTO> handlingOfficerIds;
	
	private List<AssigntoofficerDTO> assignToOfficerIds;
	
	//private Set<Long> assignToOfficerIds;
	
	@NotNull(message = "103")
	private Boolean isUpdateRequest;
}
