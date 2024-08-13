package com.oasys.helpdesk.mapper;

import static com.oasys.helpdesk.constant.Constant.CATEGORYID;
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
import com.oasys.helpdesk.entity.ActualProblem;
import com.oasys.helpdesk.entity.Category;
import com.oasys.helpdesk.entity.SubCategory;
import com.oasys.helpdesk.repository.CategoryRepository;
import com.oasys.helpdesk.repository.SubCategoryRepository;
import com.oasys.helpdesk.request.ActualProblemRequestDto;
import com.oasys.helpdesk.response.ActualProblemResponseDto;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;

@Component
public class ActualProblemMapper {
	
	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private CommonDataController commonDataController;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private SubCategoryRepository subCategoryRepository;

	public ActualProblemResponseDto convertEntityToResponseDTO(ActualProblem actualProblemEntity) {
		ActualProblemResponseDto responseDTO = commonUtil.modalMap(actualProblemEntity,
				ActualProblemResponseDto.class);
		String createdByUserName = commonDataController.getUserNameById(actualProblemEntity.getCreatedBy());
		String modifiedByUserName = commonDataController.getUserNameById(actualProblemEntity.getModifiedBy());

		responseDTO.setCreatedBy(createdByUserName);
		responseDTO.setModifiedBy(modifiedByUserName);
		responseDTO.setActualProblem(actualProblemEntity.getActualProblem());
		responseDTO.setActualProblemCode(actualProblemEntity.getCode());
		if (Objects.nonNull(actualProblemEntity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			responseDTO.setCreatedDate(dateFormat.format(actualProblemEntity.getCreatedDate()));
		}
		if (Objects.nonNull(actualProblemEntity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			responseDTO.setModifiedDate(dateFormat.format(actualProblemEntity.getModifiedDate()));
		}
		
		if (Objects.nonNull(actualProblemEntity.getSubCategory())) {
			responseDTO.setSubCategoryName(actualProblemEntity.getSubCategory().getSubCategoryName());
			responseDTO.setSubCategoryId(actualProblemEntity.getSubCategory().getId());
			if (Objects.nonNull(actualProblemEntity.getSubCategory().getHelpDeskTicketCategory())) {
				responseDTO.setCategoryName(
						actualProblemEntity.getSubCategory().getHelpDeskTicketCategory().getCategoryName());
				responseDTO.setCategoryId(actualProblemEntity.getSubCategory().getHelpDeskTicketCategory().getId());
			}
		}
		return responseDTO;
	}
	
	public ActualProblem convertRequestDTOToEntity(ActualProblemRequestDto requestDTO, ActualProblem entity) {
		if(Objects.isNull(entity)) {
			entity = commonUtil.modalMap(requestDTO, ActualProblem.class);
		}
		Optional<Category> categoryEntity = categoryRepository.findById(requestDTO.getCategoryId());
		if (!categoryEntity.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { CATEGORYID }));
		}

		Optional<SubCategory> subCategoryEntity = subCategoryRepository.findById(requestDTO.getSubCategoryId());
		if (!subCategoryEntity.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { SUB_CATEGORY_ID }));
		}
		entity.setSubCategory(subCategoryEntity.get());
		entity.setActive(requestDTO.isActive());
		entity.setActualProblem(requestDTO.getActualproblem());
		return entity;
	}
	

}
