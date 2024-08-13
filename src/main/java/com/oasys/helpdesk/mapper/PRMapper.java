package com.oasys.helpdesk.mapper;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.entity.ProblemReported;
import com.oasys.helpdesk.repository.CategoryRepository;
import com.oasys.helpdesk.repository.SubCategoryRepository;
import com.oasys.helpdesk.response.PRResponseDTO;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;

@Component
@Service
public class PRMapper {
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private CommonDataController commonDataController;
	
	@Autowired
	CategoryRepository helpDeskTicketCategoryRepository;
	
	@Autowired
	SubCategoryRepository helpDeskTicketSubCategoryRepository;
	
	public PRResponseDTO convertEntityToResponseDTO(ProblemReported prEntity) {
		PRResponseDTO prResponseDTO = commonUtil.modalMap(prEntity, PRResponseDTO.class);
		String createdByUserName=commonDataController.getUserNameById(prEntity.getCreatedBy());
		String modifiedByUserName=commonDataController.getUserNameById(prEntity.getModifiedBy());
		if (Objects.nonNull(prEntity.getSubCategoryId())) {
			prResponseDTO.setSubCategoryId(String.valueOf(prEntity.getSubCategoryId().getId()));
			prResponseDTO.setTicketSubcategoryName(prEntity.getSubCategoryId().getSubCategoryName());

			if (Objects.nonNull(prEntity.getSubCategoryId().getHelpDeskTicketCategory())) {
				prResponseDTO
						.setCategoryId(String.valueOf(prEntity.getSubCategoryId().getHelpDeskTicketCategory().getId()));
				prResponseDTO.setTicketCategoryName(
						prEntity.getSubCategoryId().getHelpDeskTicketCategory().getCategoryName());

			}
		}
		
		prResponseDTO.setCreatedBy(createdByUserName);
		prResponseDTO.setModifiedBy(modifiedByUserName);
		if (Objects.nonNull(prEntity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			prResponseDTO.setCreatedDate(dateFormat.format(prEntity.getCreatedDate()));
		}
		if (Objects.nonNull(prEntity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			prResponseDTO.setModifiedDate(dateFormat.format(prEntity.getModifiedDate()));
		}
		return prResponseDTO;
	}
	
	
	

}
