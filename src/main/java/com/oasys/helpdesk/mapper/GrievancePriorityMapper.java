package com.oasys.helpdesk.mapper;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.entity.GrievancePriorityEntity;
import com.oasys.helpdesk.response.GrievancePriorityResponseDTO;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;

@Component
public class GrievancePriorityMapper {

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private CommonDataController commonDataController;

	
	
	
	public GrievancePriorityResponseDTO convertEntityToResponseDTO(GrievancePriorityEntity priorityentity) {

		GrievancePriorityResponseDTO responseDTO = commonUtil.modalMap(priorityentity, GrievancePriorityResponseDTO.class);

		responseDTO.setId(priorityentity.getId());
		responseDTO.setPriority(priorityentity.getPriority());
		responseDTO.setCode(priorityentity.getCode());
		responseDTO.setStatus(priorityentity.getStatus());
		responseDTO.setTypeofUser(priorityentity.getTypeofUser());
		if (Objects.nonNull(priorityentity.getCategory())) {
			responseDTO.setCategoryName(priorityentity.getCategory().getCategoryName());
			responseDTO.setCategoryId(priorityentity.getCategory().getId());
		}

		if (Objects.nonNull(priorityentity.getCreatedBy())) {
			responseDTO.setCreatedBy(commonDataController.getUserNameById(priorityentity.getCreatedBy()));
		}
		if (Objects.nonNull(priorityentity.getModifiedBy())) {
			responseDTO.setModifiedBy(commonDataController.getUserNameById(priorityentity.getModifiedBy()));
		}
		responseDTO.setCreatedDate(priorityentity.getCreatedDate());
		responseDTO.setModifiedDate(priorityentity.getModifiedDate());

		return responseDTO;
	}
	
	
	
	
	
	
	
	
}
