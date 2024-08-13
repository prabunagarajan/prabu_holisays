package com.oasys.helpdesk.mapper;

import static com.oasys.helpdesk.constant.Constant.ISSUE_DETAILS;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.entity.IssueDetails;
import com.oasys.helpdesk.entity.PriorityMaster;
import com.oasys.helpdesk.entity.SlaMasterEntity;
import com.oasys.helpdesk.repository.IssueDetailsRepository;
import com.oasys.helpdesk.repository.PriorityMasterRepository;
import com.oasys.helpdesk.repository.SlaMasterRepository;
import com.oasys.helpdesk.request.SlaMasterRequestDto;
import com.oasys.helpdesk.response.SlaMasterResponseDto;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;

@Component
public class SlaMasterMapper {
	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private CommonDataController commonDataController;
	
	@Autowired
	private PriorityMasterRepository priorityMasterRepository;
	
	@Autowired
	private IssueDetailsRepository issueDetailsRepository;
	
	@Autowired
	SlaMasterRepository slaMasterRepository;

	public SlaMasterResponseDto convertEntityToResponseDTO(SlaMasterEntity entity) {
		SlaMasterResponseDto responseDTO = commonUtil.modalMap(entity,
				SlaMasterResponseDto.class);
		String createdByUserName = commonDataController.getUserNameById(entity.getCreatedBy());
		String modifiedByUserName = commonDataController.getUserNameById(entity.getModifiedBy());

		responseDTO.setCreatedBy(createdByUserName);
		responseDTO.setModifiedBy(modifiedByUserName);
		responseDTO.setCode(entity.getCode());
		if(Objects.nonNull(entity.getPriorityMaster())) {
			responseDTO.setPriority(entity.getPriorityMaster().getPriority());
			responseDTO.setPriorityId(entity.getPriorityMaster().getId());
		}
		
		responseDTO.setSla(entity.getSla());
		if (Objects.nonNull(entity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			responseDTO.setCreatedDate(dateFormat.format(entity.getCreatedDate()));
		}
		if (Objects.nonNull(entity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			responseDTO.setModifiedDate(dateFormat.format(entity.getModifiedDate()));
		}
		
		
		if (Objects.nonNull(entity.getIssueDetails())) {
			responseDTO.setIssueDetails(entity.getIssueDetails().getIssueName());
			responseDTO.setIssueDetailsId(entity.getIssueDetails().getId());
			if (Objects.nonNull(entity.getIssueDetails().getSubcategoryId())) {
				responseDTO.setSubCategoryName(entity.getIssueDetails().getSubcategoryId().getSubCategoryName());
				responseDTO.setSubCategoryId(entity.getIssueDetails().getSubcategoryId().getId());
				if (Objects.nonNull(entity.getIssueDetails().getSubcategoryId().getHelpDeskTicketCategory())) {
					responseDTO.setCategoryName(
							entity.getIssueDetails().getSubcategoryId().getHelpDeskTicketCategory().getCategoryName());
					responseDTO.setCategoryId(
							entity.getIssueDetails().getSubcategoryId().getHelpDeskTicketCategory().getId());
				}
			}
		}
     	responseDTO.setActive(entity.isActive());	
     	return responseDTO;
     	
     	
	}
	
	public SlaMasterEntity convertRequestDTOToEntity(SlaMasterRequestDto requestDTO, SlaMasterEntity entity) {
		if(Objects.isNull(entity)) {
			entity = commonUtil.modalMap(requestDTO, SlaMasterEntity.class);
		}
		
		Optional<PriorityMaster> priorityMaster = priorityMasterRepository.findById(requestDTO.getPriorityId());
		if (!priorityMaster.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { Constant.PRIORITYID }));
		}
		if (Objects.nonNull(requestDTO.getIssueDetailsId())) {
			Optional<IssueDetails> issueDetailsEntity = issueDetailsRepository.findById(requestDTO.getIssueDetailsId());
			if (!issueDetailsEntity.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ISSUE_DETAILS }));
			}
			entity.setIssueDetails(issueDetailsEntity.get());
//			if (Objects.nonNull(requestDTO.getIssueDetailsId()) && Objects.nonNull(requestDTO.getPriorityId())
//					&& (issueDetailsEntity.get().getSubcategoryId().getId() != priorityMaster.get().getSubCategory()
//							.getId())) {
//				throw new InvalidDataValidation(ErrorMessages.SUBCATEGORY_MISMATCH);
//			}
		} else {
			entity.setIssueDetails(null);
		}
		entity.setSla(requestDTO.getSla());
		entity.setActive(requestDTO.isActive());
		entity.setPriorityMaster(priorityMaster.get());
		return entity;
	}
	
	
}
