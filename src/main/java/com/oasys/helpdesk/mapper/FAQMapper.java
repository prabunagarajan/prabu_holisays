package com.oasys.helpdesk.mapper;

import static com.oasys.helpdesk.constant.Constant.SUB_CATEGORY_ID;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.entity.Faq;
import com.oasys.helpdesk.entity.SubCategory;
import com.oasys.helpdesk.repository.SubCategoryRepository;
import com.oasys.helpdesk.request.FaqRequestDto;
import com.oasys.helpdesk.response.FaqResponseDto;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;

@Component
public class FAQMapper {
	
	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private CommonDataController commonDataController;
	

	@Autowired
	private SubCategoryRepository subCategoryRepository;
	
	public FaqResponseDto convertEntityToResponseDTO(Faq entity) {

		FaqResponseDto responseDTO = commonUtil.modalMap(entity, FaqResponseDto.class);
		
		if (Objects.nonNull(entity.getSubCategoryId())) {
			responseDTO.setSubCategoryName(entity.getSubCategoryId().getSubCategoryName());
			responseDTO.setSubCategoryid(entity.getSubCategoryId().getId());
			if (Objects.nonNull(entity.getSubCategoryId().getHelpDeskTicketCategory())) {
				responseDTO.setCategoryName(entity.getSubCategoryId().getHelpDeskTicketCategory().getCategoryName());
				responseDTO.setCategoryid(entity.getSubCategoryId().getHelpDeskTicketCategory().getId());
			}
		}
		
		if (Objects.nonNull(entity.getCreatedBy())) {
			responseDTO.setCreatedBy(commonDataController.getUserNameById(entity.getCreatedBy()));
		}
		if (Objects.nonNull(entity.getModifiedBy())) {
			responseDTO.setModifiedBy(commonDataController.getUserNameById(entity.getModifiedBy()));
		}
		responseDTO.setCreatedDate(entity.getCreatedDate());
		responseDTO.setModifiedDate(entity.getModifiedDate());
		return responseDTO;
	}

	public Faq convertRequestDTOToEntity(FaqRequestDto requestDTO, Faq entity) {
		if (Objects.isNull(entity)) {
			entity = commonUtil.modalMap(requestDTO, Faq.class);
		}
		
		SubCategory subCategory = subCategoryRepository.getById(requestDTO.getSubCategoryid());
		if (Objects.isNull(subCategory) || Objects.isNull(subCategory.getHelpDeskTicketCategory())) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { SUB_CATEGORY_ID }));
		}
	
		entity.setSubCategoryId(subCategory);
		entity.setAnswer(requestDTO.getAnswer().trim());
		entity.setQuestion(requestDTO.getQuestion().trim());
		entity.setStatus(requestDTO.getStatus());
		return entity;
	}

}
