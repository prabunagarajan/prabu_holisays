package com.oasys.helpdesk.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.dto.SiteVisitResponseDTO;
import com.oasys.helpdesk.dto.SiteVisitUserResponseDTO;
import com.oasys.helpdesk.dto.TicketstausResponseDTO;
import com.oasys.helpdesk.entity.SiteVisitEntity;
import com.oasys.helpdesk.entity.TicketStatusEntity;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;


@Component
public class SiteVisitMapper {
	
	
	@Autowired
	private CommonDataController commonDataController;
	@Autowired
	private CommonUtil commonUtil;
	
	
	
	
	
	public SiteVisitResponseDTO entityToResponseDTO(SiteVisitEntity tsEntity) {
		SiteVisitResponseDTO siteVisitDTO = commonUtil.modalMap(tsEntity, SiteVisitResponseDTO.class);
		String createdByUserName=commonDataController.getUserNameById(tsEntity.getCreatedBy());
		String modifiedByUserName=commonDataController.getUserNameById(tsEntity.getModifiedBy());
		siteVisitDTO.setId(tsEntity.getId());
		
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
		
		if (Objects.nonNull(tsEntity.getEntityType())) {
			siteVisitDTO.setEntityType(tsEntity.getEntityType().getEntityName());
			siteVisitDTO.setEntityTypeId(tsEntity.getEntityType().getId());	
		}
		if (Objects.nonNull(tsEntity.getSiteObservation())) {
			siteVisitDTO.setSiteObservation(tsEntity.getSiteObservation().getObservation());
			siteVisitDTO.setSiteobservationId(tsEntity.getSiteObservation().getId());
		}
		if (Objects.nonNull(tsEntity.getSiteActionTaken())) {
			siteVisitDTO.setSiteActionTaken(tsEntity.getSiteActionTaken().getSiteActionTaken());
			siteVisitDTO.setSiteactionTakenId(tsEntity.getSiteActionTaken().getId());
		}
		
		if (Objects.nonNull(tsEntity.getSiteVisitStatus())) {
			siteVisitDTO.setSiteVisitStatus(tsEntity.getSiteVisitStatus().getName());
			siteVisitDTO.setSiteVisitstausId(tsEntity.getSiteVisitStatus().getId());
		}
		
		if (Objects.nonNull(tsEntity.getSiteIssueType())) {
			siteVisitDTO.setSiteIssueType(tsEntity.getSiteIssueType().getIssuetype());
			siteVisitDTO.setSiteIssueTypeId(tsEntity.getSiteIssueType().getId());
		}
		siteVisitDTO.setContactNo(tsEntity.getContactNo());
		siteVisitDTO.setTicketNumber(tsEntity.getTicketNumber());
		siteVisitDTO.setFinalStatus(tsEntity.getFinalStatus());
		return siteVisitDTO;
	}
	
	public SiteVisitUserResponseDTO entityToUserResponseDTO(SiteVisitEntity tsEntity) {
		SiteVisitUserResponseDTO siteVisitDTO = commonUtil.modalMap(tsEntity, SiteVisitUserResponseDTO.class);
		String createdByUserName=commonDataController.getUserNameById(tsEntity.getCreatedBy());
		String modifiedByUserName=commonDataController.getUserNameById(tsEntity.getModifiedBy());
		siteVisitDTO.setId(tsEntity.getId());
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
		
		if (Objects.nonNull(tsEntity.getEntityType())) {
			siteVisitDTO.setEntityType(tsEntity.getEntityType().getEntityName());
			siteVisitDTO.setEntityTypeId(tsEntity.getEntityType().getId());	
		}
		
		if (Objects.nonNull(tsEntity.getSiteObservation())) {
			siteVisitDTO.setSiteObservation(tsEntity.getSiteObservation().getObservation());
			siteVisitDTO.setSiteobservationId(tsEntity.getSiteObservation().getId());
		}
		if (Objects.nonNull(tsEntity.getSiteActionTaken())) {
			siteVisitDTO.setSiteActionTaken(tsEntity.getSiteActionTaken().getSiteActionTaken());
			siteVisitDTO.setSiteactionTakenId(tsEntity.getSiteActionTaken().getId());
		}
		
		if (Objects.nonNull(tsEntity.getSiteVisitStatus())) {
			siteVisitDTO.setSiteVisitStatus(tsEntity.getSiteVisitStatus().getName());
			siteVisitDTO.setSiteVisitstausId(tsEntity.getSiteVisitStatus().getId());
		}
		
		if (Objects.nonNull(tsEntity.getSiteIssueType())) {
			siteVisitDTO.setSiteIssueType(tsEntity.getSiteIssueType().getIssuetype());
			siteVisitDTO.setSiteIssueTypeId(tsEntity.getSiteIssueType().getId());
		}

		siteVisitDTO.setContactNo(tsEntity.getContactNo());
		siteVisitDTO.setTicketNumber(tsEntity.getTicketNumber());
		siteVisitDTO.setFinalStatus(tsEntity.getFinalStatus());
	return siteVisitDTO;
}
}


