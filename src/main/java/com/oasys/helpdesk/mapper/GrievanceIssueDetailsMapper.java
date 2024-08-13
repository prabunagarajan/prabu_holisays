package com.oasys.helpdesk.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.entity.GrievanceIssueDetails;
import com.oasys.helpdesk.response.GrievanceIssueResponseDto;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;

@Component
public class GrievanceIssueDetailsMapper {
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private CommonDataController commonDataController;
	
	
	public GrievanceIssueResponseDto convertEntityToResponseDTO(GrievanceIssueDetails gidEntity) {
		
		GrievanceIssueResponseDto giResponseDTO = commonUtil.modalMap(gidEntity, GrievanceIssueResponseDto.class);
		String createdByUserName=commonDataController.getUserNameById(gidEntity.getCreatedBy());
		String modifiedByUserName=commonDataController.getUserNameById(gidEntity.getModifiedBy());
		giResponseDTO.setCreatedBy(createdByUserName);
		giResponseDTO.setModifiedBy(modifiedByUserName);
		
		
		giResponseDTO.setId(gidEntity.getId());
		giResponseDTO.setIssuecode(gidEntity.getIssuecode());
		giResponseDTO.setIssueName(gidEntity.getIssueName());
		giResponseDTO.setCategoryName((gidEntity.getCategory().getCategoryName()));
		giResponseDTO.setCategoryid(gidEntity.getCategory().getId());
		giResponseDTO.setActive(gidEntity.isActive());
		giResponseDTO.setTypeofUser(gidEntity.getTypeofUser());
	
		if (Objects.nonNull(gidEntity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			giResponseDTO.setCreatedDate(dateFormat.format(gidEntity.getCreatedDate()));
		}
		if (Objects.nonNull(gidEntity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			giResponseDTO.setModifiedDate(dateFormat.format(gidEntity.getModifiedDate()));
		}
		return giResponseDTO;
	}
	
	
	
public GrievanceIssueResponseDto convertEntityToResponseDTOFlow(GrievanceIssueDetails gidEntity) {
		
		GrievanceIssueResponseDto giResponseDTO = commonUtil.modalMap(gidEntity, GrievanceIssueResponseDto.class);
		String createdByUserName=commonDataController.getUserNameById(gidEntity.getCreatedBy());
		String modifiedByUserName=commonDataController.getUserNameById(gidEntity.getModifiedBy());
		giResponseDTO.setCreatedBy(createdByUserName);
		giResponseDTO.setModifiedBy(modifiedByUserName);
		
		
		giResponseDTO.setId(gidEntity.getId());
		//giResponseDTO.setIssuecode(gidEntity.getIssuecode());
		giResponseDTO.setIssueName(gidEntity.getIssueName());
		//giResponseDTO.setCategoryName(String.valueOf(gidEntity.getCategoryName().getCategoryName()));
		//giResponseDTO.setActive(gidEntity.isActive());
	
		if (Objects.nonNull(gidEntity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			giResponseDTO.setCreatedDate(dateFormat.format(gidEntity.getCreatedDate()));
		}
		if (Objects.nonNull(gidEntity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			giResponseDTO.setModifiedDate(dateFormat.format(gidEntity.getModifiedDate()));
		}
		return giResponseDTO;
	}
	
	
	
	
}	