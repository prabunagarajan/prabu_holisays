package com.oasys.helpdesk.mapper;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.entity.GrievanceSlaEntity;
import com.oasys.helpdesk.response.GrievanceSlaResponseDTO;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;

@Component
public class GrievanceSlaMapper {
	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private CommonDataController commonDataController;

	
	
	
	public GrievanceSlaResponseDTO  convertEntityToResponseDTO(GrievanceSlaEntity slaentity) {

		GrievanceSlaResponseDTO responseDTO = commonUtil.modalMap(slaentity, GrievanceSlaResponseDTO.class);

		responseDTO.setId(slaentity.getId());
		responseDTO.setSla(slaentity.getSla());
		responseDTO.setCode(slaentity.getCode());
		responseDTO.setPriorityId(slaentity.getPriority().getId());
		responseDTO.setPriorityName(slaentity.getPriority().getPriority());
		
		responseDTO.setStatus(slaentity.getStatus());
		
		if (Objects.nonNull(slaentity.getGIssueDetails())) {
			responseDTO.setIssueDetails(slaentity.getGIssueDetails().getIssueName());
			responseDTO.setIssueDetailsId(slaentity.getGIssueDetails().getId());
			if (Objects.nonNull(slaentity.getGIssueDetails().getCategory())) {
				responseDTO.setCategoryName(slaentity.getGIssueDetails().getCategory().getCategoryName());
				responseDTO.setCategoryId(slaentity.getGIssueDetails().getCategory().getId());
			}
		}

		if (Objects.nonNull(slaentity.getCreatedBy())) {
			responseDTO.setCreatedBy(commonDataController.getUserNameById(slaentity.getCreatedBy()));
		}
		if (Objects.nonNull(slaentity.getModifiedBy())) {
			responseDTO.setModifiedBy(commonDataController.getUserNameById(slaentity.getModifiedBy()));
		}
		responseDTO.setCreatedDate(slaentity.getCreatedDate());
		responseDTO.setModifiedDate(slaentity.getModifiedDate());
		responseDTO.setTypeofUser(slaentity.getTypeofUser());
		
		return responseDTO;
	}

	
	
	
	
}
