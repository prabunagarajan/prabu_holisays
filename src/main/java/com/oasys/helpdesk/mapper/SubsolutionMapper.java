package com.oasys.helpdesk.mapper;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.dto.SubsolutionResponseDTO;
import com.oasys.helpdesk.entity.Category;
import com.oasys.helpdesk.entity.ProblemReported;
import com.oasys.helpdesk.entity.SubCategory;
import com.oasys.helpdesk.entity.SubsolutionEntity;
import com.oasys.helpdesk.repository.CategoryRepository;
import com.oasys.helpdesk.repository.SubCategoryRepository;
import com.oasys.helpdesk.response.PRResponseDTO;

import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;

@Component
@Service
public class SubsolutionMapper {
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private CommonDataController commonDataController;
	
	@Autowired
	CategoryRepository helpDeskTicketCategoryRepository;
	
	@Autowired
	SubCategoryRepository helpDeskTicketSubCategoryRepository;
	
	public SubsolutionResponseDTO convertEntityToResponseDTO(SubsolutionEntity prEntity) {
		SubsolutionResponseDTO prResponseDTO = commonUtil.modalMap(prEntity, SubsolutionResponseDTO.class);
		String createdByUserName=commonDataController.getUserNameById(prEntity.getCreatedBy());
		String modifiedByUserName=commonDataController.getUserNameById(prEntity.getModifiedBy());
		prResponseDTO.setCategoryId(String.valueOf(prEntity.getCategoryId().getId()));
		prResponseDTO.setSubCategoryId(String.valueOf(prEntity.getSubcategoryId().getId()));
		
		SubCategory helpDeskTicketSubCategory= helpDeskTicketSubCategoryRepository.getById(prEntity.getSubcategoryId().getId());
		Category helpDeskTicketCategory= helpDeskTicketCategoryRepository.getById(helpDeskTicketSubCategory.getHelpDeskTicketCategory().getId());
		
		prResponseDTO.setTicketSubcategoryName(helpDeskTicketSubCategory.getSubCategoryName());
		prResponseDTO.setTicketCategoryName(helpDeskTicketCategory.getCategoryName());
		
		prResponseDTO.setCreated_by(createdByUserName);
		prResponseDTO.setModified_by(modifiedByUserName);
		if (Objects.nonNull(prEntity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			prResponseDTO.setCreated_date(dateFormat.format(prEntity.getCreatedDate()));
		}
		if (Objects.nonNull(prEntity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			prResponseDTO.setModified_date(dateFormat.format(prEntity.getModifiedDate()));
		}
		return prResponseDTO;
	}
	
	
	

}
