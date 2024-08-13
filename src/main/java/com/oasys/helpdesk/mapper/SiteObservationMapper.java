package com.oasys.helpdesk.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.SiteObservationDTO;
import com.oasys.helpdesk.dto.SiteObservationRequestDto;
import com.oasys.helpdesk.entity.SiteIssueTypeEntity;
import com.oasys.helpdesk.entity.SiteObservationEntity;
import com.oasys.helpdesk.repository.SiteIssueTypeRepository;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;

@Component
public class SiteObservationMapper {
	
	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private CommonDataController commonDataController;
	
	@Autowired
	private SiteIssueTypeRepository siteIssueTypeRepository;
	
	public SiteObservationEntity convertRequestDTOToEntity(SiteObservationRequestDto requestDTO,
			SiteObservationEntity entity) {
		if (Objects.isNull(entity)) {
			entity = commonUtil.modalMap(requestDTO, SiteObservationEntity.class);
		}

		Optional<SiteIssueTypeEntity> siteObservation = siteIssueTypeRepository
				.findById(requestDTO.getSiteIssueTypeId());
		if (!siteObservation.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "SiteIssueType_Id" }));
		}
		entity.setIssueType(siteObservation.get());
		entity.setObservation(requestDTO.getSiteObservation());
		entity.setActive(requestDTO.isActive());
		return entity;
	}
	
	
	public SiteObservationDTO convertEntityToResponseDTO(SiteObservationEntity siteObservationEntity) {
	    SiteObservationDTO responseDTO = commonUtil.modalMap(siteObservationEntity, SiteObservationDTO.class);
	    
	    if (siteObservationEntity != null) {
	        String createdByUserName = commonDataController.getUserNameById(siteObservationEntity.getCreatedBy());
	        String modifiedByUserName = commonDataController.getUserNameById(siteObservationEntity.getModifiedBy());

	        responseDTO.setCreatedByName(createdByUserName);
	        responseDTO.setModifiedByName(modifiedByUserName);

	        if (siteObservationEntity.getCreatedDate() != null) {
	            DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
	            responseDTO.setCreatedDate(dateFormat.format(siteObservationEntity.getCreatedDate()));
	        }
	        
	        if (siteObservationEntity.getModifiedDate() != null) {
	            DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
	            responseDTO.setModifiedDate(dateFormat.format(siteObservationEntity.getModifiedDate()));
	        }

	        if (siteObservationEntity.getIssueType() != null) {
	            responseDTO.setSiteIssueTypeId(siteObservationEntity.getIssueType().getId());
	            responseDTO.setSiteIssueType(siteObservationEntity.getIssueType().getIssuetype());
	        }

	        responseDTO.setObservation(siteObservationEntity.getObservation());
	    }

	    return responseDTO;
	}

}
