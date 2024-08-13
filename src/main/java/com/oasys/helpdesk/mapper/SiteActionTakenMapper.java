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
import com.oasys.helpdesk.dto.SiteActionTakenResponseDto;
import com.oasys.helpdesk.entity.SiteActionTakenEntity;
import com.oasys.helpdesk.entity.SiteObservationEntity;
import com.oasys.helpdesk.repository.SiteActionTakenRepository;
import com.oasys.helpdesk.repository.SiteObservationRepository;
import com.oasys.helpdesk.request.SiteActionTakenRequestDto;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;

@Component
public class SiteActionTakenMapper {

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private CommonDataController commonDataController;

	@Autowired
	private SiteActionTakenRepository siteActionTakenRepository;

	@Autowired
	private SiteObservationRepository siteObservationRepository;

	public SiteActionTakenEntity convertRequestDTOToEntity(SiteActionTakenRequestDto requestDTO,
			SiteActionTakenEntity entity) {
		if (Objects.isNull(entity)) {
			entity = commonUtil.modalMap(requestDTO, SiteActionTakenEntity.class);
		}

		Optional<SiteObservationEntity> siteActionTaken = siteObservationRepository
				.findById(requestDTO.getObservationId());
		if (!siteActionTaken.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Observation_Id" }));
		}
		entity.setObservation(siteActionTaken.get());
		entity.setSiteActionTaken(requestDTO.getSiteActionTaken());
		entity.setActive(requestDTO.isActive());
		return entity;
	}

	public SiteActionTakenResponseDto convertEntityToResponseDTO(SiteActionTakenEntity siteActionTakenEntity) {
		SiteActionTakenResponseDto responseDTO = commonUtil.modalMap(siteActionTakenEntity,
				SiteActionTakenResponseDto.class);
		String createdByUserName = commonDataController.getUserNameById(siteActionTakenEntity.getCreatedBy());
		String modifiedByUserName = commonDataController.getUserNameById(siteActionTakenEntity.getModifiedBy());

		responseDTO.setCreatedBy(createdByUserName);
		responseDTO.setModifiedBy(modifiedByUserName);

		if (Objects.nonNull(siteActionTakenEntity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			responseDTO.setCreatedDate(dateFormat.format(siteActionTakenEntity.getCreatedDate()));
		}
		if (Objects.nonNull(siteActionTakenEntity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			responseDTO.setModifiedDate(dateFormat.format(siteActionTakenEntity.getModifiedDate()));
		}

		if (Objects.nonNull(siteActionTakenEntity.getObservation())) {
			responseDTO.setObservationId(siteActionTakenEntity.getObservation().getId());
			responseDTO.setObservation(siteActionTakenEntity.getObservation().getObservation());
		}
		
		responseDTO.setActionTaken(siteActionTakenEntity.getSiteActionTaken());
		return responseDTO;
	}

}
