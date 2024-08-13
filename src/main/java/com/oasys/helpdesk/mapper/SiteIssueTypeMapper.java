package com.oasys.helpdesk.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.dto.SiteIssueTypeResponseDTO;
import com.oasys.helpdesk.dto.SiteVisitResponseDTO;
import com.oasys.helpdesk.entity.SiteIssueTypeEntity;
import com.oasys.helpdesk.entity.SiteVisitEntity;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;
@Component
public class SiteIssueTypeMapper {
	
	@Autowired
	private CommonDataController commonDataController;
	
	
	
	@Autowired
	private CommonUtil commonUtil;
	
	
	
	
	
	public SiteIssueTypeResponseDTO entityToResponseDTO(SiteIssueTypeEntity tsEntity) {
		SiteIssueTypeResponseDTO siteVisitDTO = commonUtil.modalMap(tsEntity, SiteIssueTypeResponseDTO.class);
		String createdByUserName=commonDataController.getUserNameById(tsEntity.getCreatedBy());
		String modifiedByUserName=commonDataController.getUserNameById(tsEntity.getModifiedBy());
		
		siteVisitDTO.setCreated_by(createdByUserName);
		siteVisitDTO.setModified_by(modifiedByUserName);
		if (Objects.nonNull(tsEntity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			siteVisitDTO.setCreated_date(dateFormat.format(tsEntity.getCreatedDate()));
		}
		if (Objects.nonNull(tsEntity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			siteVisitDTO.setModified_date(dateFormat.format(tsEntity.getModifiedDate()));;
		}
		return siteVisitDTO;
	}

}
