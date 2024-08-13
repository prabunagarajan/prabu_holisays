package com.oasys.helpdesk.mapper;


import static com.oasys.helpdesk.constant.Constant.SUB_CATEGORY_ID;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.entity.PriorityMaster;
import com.oasys.helpdesk.entity.SubCategory;
import com.oasys.helpdesk.repository.PriorityMasterRepository;
import com.oasys.helpdesk.repository.SubCategoryRepository;
import com.oasys.helpdesk.request.PriorityMasterRequestDto;
import com.oasys.helpdesk.response.PriorityMasterResponseDto;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;

@Component
public class PriorityMasterMapper {
	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private CommonDataController commonDataController;
	
	@Autowired
	private SubCategoryRepository subCategoryRepository;
	
	@Autowired
	PriorityMasterRepository priorityMasterRepository;

	public PriorityMasterResponseDto convertEntityToResponseDTO(PriorityMaster entity) {
		PriorityMasterResponseDto responseDTO = commonUtil.modalMap(entity,
				PriorityMasterResponseDto.class);
		String createdByUserName = commonDataController.getUserNameById(entity.getCreatedBy());
		String modifiedByUserName = commonDataController.getUserNameById(entity.getModifiedBy());

		responseDTO.setCreatedBy(createdByUserName);
		responseDTO.setModifiedBy(modifiedByUserName);
		responseDTO.setCode(entity.getCode());
		responseDTO.setPriority(entity.getPriority());
		responseDTO.setActive(entity.isActive());		
		if (Objects.nonNull(entity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			responseDTO.setCreatedDate(dateFormat.format(entity.getCreatedDate()));
		}
		if (Objects.nonNull(entity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			responseDTO.setModifiedDate(dateFormat.format(entity.getModifiedDate()));
		}
		
		if (Objects.nonNull(entity.getSubCategory())) {
			responseDTO.setSubCategoryName(entity.getSubCategory().getSubCategoryName());
			responseDTO.setSubCategoryId(entity.getSubCategory().getId());
			if (Objects.nonNull(entity.getSubCategory().getHelpDeskTicketCategory())) {
				responseDTO.setCategoryName(entity.getSubCategory().getHelpDeskTicketCategory().getCategoryName());
				responseDTO.setCategoryId(entity.getSubCategory().getHelpDeskTicketCategory().getId());
			}
		}
 
     	return responseDTO;
     	
     	
	}
	
	public PriorityMaster convertRequestDTOToEntity(PriorityMasterRequestDto requestDTO, PriorityMaster entity) {
		if(Objects.isNull(entity)) {
			entity = commonUtil.modalMap(requestDTO, PriorityMaster.class);
		}
		Optional<SubCategory> subCategoryEntity = subCategoryRepository.findById(requestDTO.getSubCategoryId());
		if (!subCategoryEntity.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { SUB_CATEGORY_ID }));
		}
		
		entity.setSubCategory(subCategoryEntity.get());
		entity.setActive(requestDTO.isActive());
		
		return entity;
	}
}
