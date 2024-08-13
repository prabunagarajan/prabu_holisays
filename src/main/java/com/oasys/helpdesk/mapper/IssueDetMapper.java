package com.oasys.helpdesk.mapper;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.entity.IssueDetails;
import com.oasys.helpdesk.repository.CategoryRepository;
import com.oasys.helpdesk.repository.SubCategoryRepository;
import com.oasys.helpdesk.response.IsssueDetresdto;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;

@Component
public class IssueDetMapper {
	
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private CommonDataController commonDataController;
	
	@Autowired
	CategoryRepository helpDeskTicketCategoryRepository;
	
	@Autowired
	SubCategoryRepository helpDeskTicketSubCategoryRepository;
	
	
	
	public IsssueDetresdto convertEntityToResponseDTO(IssueDetails isdetEntity) {
		IsssueDetresdto issuedetResponseDTO = commonUtil.modalMap(isdetEntity, IsssueDetresdto.class);
		String createdByUserName=commonDataController.getUserNameById(isdetEntity.getCreatedBy());
		String modifiedByUserName=commonDataController.getUserNameById(isdetEntity.getModifiedBy());
		
		issuedetResponseDTO.setCreatedBy(createdByUserName);
		issuedetResponseDTO.setModifiedBy(modifiedByUserName);
		if (Objects.nonNull(isdetEntity.getSubcategoryId())) {
			issuedetResponseDTO.setSubCategoryId(String.valueOf(isdetEntity.getSubcategoryId().getId()));
			issuedetResponseDTO.setSubCategoryName(isdetEntity.getSubcategoryId().getSubCategoryName());
			if (Objects.nonNull(isdetEntity.getSubcategoryId().getHelpDeskTicketCategory())) {
				issuedetResponseDTO.setCategoryId(
						String.valueOf(isdetEntity.getSubcategoryId().getHelpDeskTicketCategory().getId()));
				issuedetResponseDTO
						.setCategoryName(isdetEntity.getSubcategoryId().getHelpDeskTicketCategory().getCategoryName());
			}
		}
		
		issuedetResponseDTO.setIssuetype(isdetEntity.isIssuetype());
		Boolean software=issuedetResponseDTO.isIssuetype();
		Boolean ver=true;
			if(software.equals(ver)) {
				issuedetResponseDTO.setIssuetypeName("Software");	
			}
			
			else {
				issuedetResponseDTO.setIssuetypeName("Hardware");		
			}

		if (Objects.nonNull(isdetEntity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			issuedetResponseDTO.setCreatedDate(dateFormat.format(isdetEntity.getCreatedDate()));
		}
		if (Objects.nonNull(isdetEntity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			issuedetResponseDTO.setModifiedDate(dateFormat.format(isdetEntity.getModifiedDate()));
		}
		return issuedetResponseDTO;
	}
	
	
	
	

}
