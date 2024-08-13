package com.oasys.helpdesk.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.entity.RoleMaster;
import com.oasys.helpdesk.entity.SlaMasterEntity;
import com.oasys.helpdesk.entity.UserEntity;
import com.oasys.helpdesk.entity.WorkflowEntity;
import com.oasys.helpdesk.repository.SlaMasterRepository;
import com.oasys.helpdesk.repository.UserRepository;
import com.oasys.helpdesk.repository.WorkflowRepository;
import com.oasys.helpdesk.request.WorkflowRequestDto;
import com.oasys.helpdesk.response.WorkflowResponseDto;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class WorkflowMapper {
	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private CommonDataController commonDataController;
	
	@Autowired
	private SlaMasterRepository slaMasterRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	WorkflowRepository workflowRepository;


	public WorkflowResponseDto convertEntityToResponseDTO(WorkflowEntity entity) {
		WorkflowResponseDto responseDTO = commonUtil.modalMap(entity,
				WorkflowResponseDto.class);
		String createdByUserName = commonDataController.getUserNameById(entity.getCreatedBy());
		String modifiedByUserName = commonDataController.getUserNameById(entity.getModifiedBy());

		responseDTO.setCreatedBy(createdByUserName);
		responseDTO.setModifiedBy(modifiedByUserName);
		responseDTO.setCode(entity.getCode());
		if (Objects.nonNull(entity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			responseDTO.setCreatedDate(dateFormat.format(entity.getCreatedDate()));
		}
		if (Objects.nonNull(entity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			responseDTO.setModifiedDate(dateFormat.format(entity.getModifiedDate()));
		}

		
		if (Objects.nonNull(entity.getSlaDetails())) {
			responseDTO.setSla(entity.getSlaDetails().getSla());
			responseDTO.setSlaId(entity.getSlaDetails().getId());
			if (Objects.nonNull(entity.getSlaDetails().getIssueDetails())) {
				responseDTO.setIssueDetailsId(entity.getSlaDetails().getIssueDetails().getId());
				responseDTO.setIssueDetails(entity.getSlaDetails().getIssueDetails().getIssueName());
				if (Objects.nonNull(entity.getSlaDetails().getIssueDetails().getSubcategoryId())) {
					responseDTO.setSubcategoryName(
							entity.getSlaDetails().getIssueDetails().getSubcategoryId().getSubCategoryName());
					responseDTO.setSubCategoryId(entity.getSlaDetails().getIssueDetails().getSubcategoryId().getId());
					if (Objects.nonNull(
							entity.getSlaDetails().getIssueDetails().getSubcategoryId().getHelpDeskTicketCategory())) {
						responseDTO.setCategoryName(entity.getSlaDetails().getIssueDetails().getSubcategoryId()
								.getHelpDeskTicketCategory().getCategoryName());
						responseDTO.setCategoryId(entity.getSlaDetails().getIssueDetails().getSubcategoryId()
								.getHelpDeskTicketCategory().getId());
					}
				}
			}
			
			if (Objects.nonNull(entity.getSlaDetails().getPriorityMaster())) {
				responseDTO.setPriorityName(entity.getSlaDetails().getPriorityMaster().getPriority());
				responseDTO.setPriorityId(entity.getSlaDetails().getPriorityMaster().getId());
			}

		}
		if(Objects.nonNull(entity.getAssignTo()))
		{
			String assignToName = commonDataController.getUserNameById(entity.getAssignTo().getId());
			responseDTO.setAssignToId(entity.getAssignTo().getId());
			responseDTO.setAssignToName(assignToName);
			if (Objects.nonNull(entity.getAssignTo()) && !CollectionUtils.isEmpty(entity.getAssignTo().getRoles())) {
				RoleMaster roleMaster = entity.getAssignTo().getRoles().get(0);
				responseDTO.setAssignToGroupId(Objects.nonNull(roleMaster) ? roleMaster.getId() : null);
				responseDTO.setAssignToGroupName(Objects.nonNull(roleMaster) ? roleMaster.getRoleName() : null);
			}
		}
		
		return responseDTO;
	}
	
	public WorkflowEntity convertRequestDTOToEntity(WorkflowRequestDto requestDTO, WorkflowEntity entity) {
		if(Objects.isNull(entity)) {
			entity = commonUtil.modalMap(requestDTO, WorkflowEntity.class);
		}
		
		
		Optional<SlaMasterEntity> slaMasterEntity =slaMasterRepository.findById(requestDTO.getSla());
		if (!slaMasterEntity.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { Constant.SLAID }));
		}
		

		List<WorkflowEntity> workflowEntityList = workflowRepository
				.findByissueDetailsId(slaMasterEntity.get().getIssueDetails().getId());
		
		if (!CollectionUtils.isEmpty(workflowEntityList)) {
			throw new InvalidDataValidation(ErrorMessages.WORKFLOW_ALREADY_EXIST);
		}
		if(Objects.nonNull(requestDTO.getAssignTo())) {
			Optional<UserEntity> uOptional = userRepository.findById(requestDTO.getAssignTo());
			if (!uOptional.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { Constant.USER_ID }));
			}
			entity.setAssignTo(uOptional.get());
		}
		
		entity.setSlaDetails(slaMasterEntity.get());
		entity.setActive(requestDTO.isActive());
		entity.setPriority(requestDTO.getPriority());
		return entity;
	}
}
