package com.oasys.helpdesk.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.ChangeRequestFeaturesDTO;
import com.oasys.helpdesk.entity.ChangeRequestFeaturesEntity;
import com.oasys.helpdesk.repository.ChangeRequestFeaturesRepository;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ChangeRequestFeaturesServiceIMPL implements ChangeRequestFeaturesService {

	@Autowired
	ChangeRequestFeaturesRepository changerequestfeaturesrepository;

	@Override
	public GenericResponse getAllMasterActive() {

		List<ChangeRequestFeaturesEntity> List = changerequestfeaturesrepository
				.findAllByIsActiveOrderByModifiedDateDesc(Boolean.TRUE);
		if (CollectionUtils.isEmpty(List)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}

		return Library.getSuccessfulResponse(List, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

	@Override
	public GenericResponse addMasterChangeRequest(ChangeRequestFeaturesDTO changerequestfeaturesDTO) {

		Optional<ChangeRequestFeaturesEntity> changeRequestFeatureEntity = changerequestfeaturesrepository
				.findByCodeIgnoreCase(changerequestfeaturesDTO.getCode());

		if (changeRequestFeatureEntity.isPresent()) {

			return Library.getFailedfulResponse(changeRequestFeatureEntity, ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { "CODE" }));
		}

		ChangeRequestFeaturesEntity changereqEntity = new ChangeRequestFeaturesEntity();

		changereqEntity.setCode(changerequestfeaturesDTO.getCode());
		changereqEntity.setName(changerequestfeaturesDTO.getName());
		changereqEntity.setActive(changerequestfeaturesDTO.isActive());
		changerequestfeaturesrepository.save(changereqEntity);
		return Library.getSuccessfulResponse(changereqEntity, ErrorCode.CREATED.getErrorCode(),
				ErrorMessages.RECORED_CREATED);

	}

	@Override
	public GenericResponse updateMasterchangerequest(ChangeRequestFeaturesDTO changerequestfeaturesDTO) {

		if (Objects.isNull(changerequestfeaturesDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { "ID" }));
		}

		Optional<ChangeRequestFeaturesEntity> entityOptional = changerequestfeaturesrepository
				.findById(changerequestfeaturesDTO.getId());

		ChangeRequestFeaturesEntity changerequestFeaturesEntity = null;

		if (entityOptional.isPresent()) {
			changerequestFeaturesEntity = entityOptional.get();
			changerequestFeaturesEntity.setActive(changerequestfeaturesDTO.isActive());
			changerequestFeaturesEntity.setCode(changerequestfeaturesDTO.getCode());
			changerequestFeaturesEntity.setName(changerequestfeaturesDTO.getName());
			changerequestfeaturesrepository.save(changerequestFeaturesEntity);
		} else {
			throw new InvalidDataValidation("Invalid ID");
		}
		return Library.getSuccessfulResponse(changerequestFeaturesEntity, ErrorCode.CREATED.getErrorCode(),
				ErrorMessages.RECORED_UPDATED);
	}
}
