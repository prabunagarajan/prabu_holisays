package com.oasys.helpdesk.mapper;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.GrievanceEscalationWorkflowRequestDTO;
import com.oasys.helpdesk.dto.GrievanceEscalationWorkflowResponseDTO;
import com.oasys.helpdesk.entity.GrievanceEscalationWorkflowEntity;
import com.oasys.helpdesk.entity.GrievanceSlaEntity;
import com.oasys.helpdesk.entity.RoleMaster;
import com.oasys.helpdesk.entity.UserEntity;
import com.oasys.helpdesk.repository.GrievanceEscalationWorkflowRepository;
import com.oasys.helpdesk.repository.GrievanceSlaRepository;
import com.oasys.helpdesk.repository.RoleMasterRepository;
import com.oasys.helpdesk.repository.UserRepository;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;

@Component
public class GrievanceEscalationWorkflowMapper {

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private GrievanceSlaRepository grievanceSlaRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleMasterRepository roleMasterRepository;
	
	@Autowired
	private CommonDataController commonDataController;
	
	@Autowired
	private GrievanceEscalationWorkflowRepository workflowRepository;

	public GrievanceEscalationWorkflowEntity convertRequestDTOToEntity(GrievanceEscalationWorkflowRequestDTO requestDTO,
			GrievanceEscalationWorkflowEntity entity) {
		if (Objects.isNull(entity)) {
			entity = commonUtil.modalMap(requestDTO, GrievanceEscalationWorkflowEntity.class);
		}
		Optional<GrievanceSlaEntity> slaEntity = grievanceSlaRepository.findById(requestDTO.getSlaId());
		if (!slaEntity.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { Constant.SLA_ID }));
		}
		Optional<GrievanceEscalationWorkflowEntity> existingWorkflow = null;
		if (Objects.nonNull(entity.getId())) {
			existingWorkflow = workflowRepository
					.findByIssueDetailIdNotInWorkflowId(slaEntity.get().getGIssueDetails().getId(), requestDTO.getId());

		} else {
			existingWorkflow = workflowRepository.findByIssueDetailId(slaEntity.get().getGIssueDetails().getId());

		}
		if (existingWorkflow.isPresent()) {
			throw new InvalidDataValidation(ErrorMessages.WORKFLOW_ALREADY_EXIST);
		}
		Optional<RoleMaster> roleMasterOptional = roleMasterRepository.findById(requestDTO.getAssignGroupId());
		if (!roleMasterOptional.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { Constant.ASSIGN_GROUP_ID }));
		}
		if (!roleMasterOptional.get().getRoleCode().equals(Constant.HANDLING_OFFICER_ROLE)) {
			throw new InvalidDataValidation(ErrorMessages.INVALID_ASSIGN_GROUP_PASSED + Constant.HANDLING_OFFICER);
		}
		Optional<UserEntity> userEntity = userRepository.findById(requestDTO.getAssignToId());
		if (!userEntity.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { Constant.ASSIGN_TO_ID }));
		}
		if (Objects.isNull(userEntity.get().getRoles()) || !userEntity.get().getRoles().stream()
				.anyMatch(r -> roleMasterOptional.get().getRoleCode().equals(r.getRoleCode()))) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { Constant.ASSIGN_TO_ID }));
		}
		entity.setSla(slaEntity.get());
		entity.setAssignGroup(roleMasterOptional.get());
		entity.setAssignTo(userEntity.get());
		entity.setTypeofUser(requestDTO.getTypeofUser());
		return entity;
	}

	public GrievanceEscalationWorkflowResponseDTO convertEntityToResponseDTO(GrievanceEscalationWorkflowEntity entity) {
		GrievanceEscalationWorkflowResponseDTO responseDTO = commonUtil.modalMap(entity,
				GrievanceEscalationWorkflowResponseDTO.class);

		responseDTO.setCreatedBy(commonDataController.getUserNameById(entity.getCreatedBy()));
		responseDTO.setModifiedBy(commonDataController.getUserNameById(entity.getModifiedBy()));
		responseDTO.setCreatedDate(entity.getCreatedDate());
		responseDTO.setModifiedDate(entity.getModifiedDate());
		responseDTO.setTypeofUser(entity.getTypeofUser());

		if (Objects.nonNull(entity.getSla())) {
			responseDTO.setSlaValue(entity.getSla().getSla());
			responseDTO.setSlaId(entity.getSla().getId());
			if (Objects.nonNull(entity.getSla().getGIssueDetails())) {
				responseDTO.setIssueDetailsId(entity.getSla().getGIssueDetails().getId());
				responseDTO.setIssueDetails(entity.getSla().getGIssueDetails().getIssueName());

				if (Objects.nonNull(entity.getSla().getGIssueDetails().getCategory())) {
					responseDTO.setCategoryName(entity.getSla().getGIssueDetails().getCategory().getCategoryName());
					responseDTO.setCategoryId(entity.getSla().getGIssueDetails().getCategory().getId());
				}
			}
			if (Objects.nonNull(entity.getSla().getPriority())) {
				responseDTO.setPriority(entity.getSla().getPriority().getPriority());
				responseDTO.setPriorityId(entity.getSla().getPriority().getId());
			}

		}
		if (Objects.nonNull(entity.getAssignTo())) {
			String assignToName = commonDataController.getUserNameById(entity.getAssignTo().getId());
			responseDTO.setAssignToId(entity.getAssignTo().getId());
			responseDTO.setAssignToName(assignToName);
			if (Objects.nonNull(entity.getAssignTo()) && !CollectionUtils.isEmpty(entity.getAssignTo().getRoles())) {
				RoleMaster roleMaster = entity.getAssignTo().getRoles().get(0);
				responseDTO.setAssignGroupId(Objects.nonNull(roleMaster) ? roleMaster.getId() : null);
				responseDTO.setAssignGroupName(Objects.nonNull(roleMaster) ? roleMaster.getRoleName() : null);
			}
		}
		return responseDTO;
	}


	
	
	
	
	
}
