package com.oasys.helpdesk.mapper;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.entity.AssociatedUserEntity;
import com.oasys.helpdesk.entity.GrievanceWorkflowEntity;
import com.oasys.helpdesk.repository.AssociatedUserRepository;
import com.oasys.helpdesk.response.GrievanceWorkflowResponseDTO;
import com.oasys.helpdesk.utility.CommonDataController;

@Component
public class GrievanceWorkflowMapper {
	

	@Autowired
	private CommonDataController commonDataController;
	
	@Autowired
	private AssociatedUserRepository associatedUserRepository;
	
	public GrievanceWorkflowResponseDTO convertEntityToResponseDTO(GrievanceWorkflowEntity workflowentity) {

		GrievanceWorkflowResponseDTO responseDTO = new GrievanceWorkflowResponseDTO();

		responseDTO.setId(workflowentity.getId());
		responseDTO.setCode(workflowentity.getCode());
		
		
		
		if (Objects.nonNull(workflowentity.getSla())) {
			responseDTO.setSla(workflowentity.getSla().getSla());
			responseDTO.setSlaId(workflowentity.getSla().getId());
			if (Objects.nonNull(workflowentity.getSla().getGIssueDetails())) {
				responseDTO.setIssueDetailsId(workflowentity.getSla().getGIssueDetails().getId());
				responseDTO.setIssueDetails(workflowentity.getSla().getGIssueDetails().getIssueName());
				if (Objects.nonNull(workflowentity.getSla().getGIssueDetails().getCategory())) {
					responseDTO.setCategoryId(workflowentity.getSla().getGIssueDetails().getCategory().getId());
					responseDTO.setCategoryName(
							workflowentity.getSla().getGIssueDetails().getCategory().getCategoryName());
				}
			}
			if (Objects.nonNull(workflowentity.getSla().getPriority())) {
				responseDTO.setPriorityId(workflowentity.getSla().getPriority().getId());
				responseDTO.setPriority(workflowentity.getSla().getPriority().getPriority());
			}
		}
		if (Objects.nonNull(workflowentity.getAssignto_Group())) {
			responseDTO.setAssignGroupId(workflowentity.getAssignto_Group());
			//responseDTO.setAssignGroupName(workflowentity.getAssignGroup().getRoleName());
			Long asigngroup=workflowentity.getAssignto_Group();
			 List<AssociatedUserEntity> assigngroupfrom = associatedUserRepository.findByUser(asigngroup);
			 if(!assigngroupfrom.isEmpty()) {
				 assigngroupfrom.stream().forEach(action ->{
			 responseDTO.setAssignGroupName(action.getUserName());	
		});		
	}
			
			
		}
		if (Objects.nonNull(workflowentity.getAssignto_id())) {
			responseDTO.setAssignToId(workflowentity.getAssignto_id());
			Long asigntoid=workflowentity.getAssignto_id();
			 List<AssociatedUserEntity> assignfrom = associatedUserRepository.findByAssociatedUserId(asigntoid);
			 if(!assignfrom.isEmpty()) {
			 assignfrom.stream().forEach(action ->{
			 responseDTO.setAssignToName(action.getAssociateduserName());	
		});		
	}	
			//responseDTO.setAssignToName(workflowentity.getAssignTo().getUsername());
		}
		responseDTO.setStatus(workflowentity.isStatus());

		if (Objects.nonNull(workflowentity.getCreatedBy())) {
			responseDTO.setCreatedBy(commonDataController.getUserNameById(workflowentity.getCreatedBy()));
		}
		if (Objects.nonNull(workflowentity.getModifiedBy())) {
			responseDTO.setModifiedBy(commonDataController.getUserNameById(workflowentity.getModifiedBy()));
		}
		responseDTO.setCreatedDate(workflowentity.getCreatedDate());
		responseDTO.setModifiedDate(workflowentity.getModifiedDate());
		responseDTO.setTypeofUser(workflowentity.getTypeofUser());
		return responseDTO;
	}

}
