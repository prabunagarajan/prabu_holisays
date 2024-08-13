package com.oasys.helpdesk.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.MasterLicenseTypeResponseDto;
import com.oasys.helpdesk.entity.MasterLicenseTypeEntity;
import com.oasys.helpdesk.mapper.MasterLicenseTypeMapper;
import com.oasys.helpdesk.repository.MasterLicenseTypeRepository;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class MasterLicenseTypeServiceImpl implements MasterLicenseTypeService {

	@Autowired
	MasterLicenseTypeRepository Masterlicensetyperepository;

	@Autowired
	MasterLicenseTypeMapper masterlicsencetypemapper;

	@Override
	public GenericResponse getAll() {

		List<MasterLicenseTypeEntity> masterlicensetype = Masterlicensetyperepository
				.findAllByOrderByModifiedDateDesc();

		if (CollectionUtils.isEmpty(masterlicensetype)) {

			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<MasterLicenseTypeResponseDto> masterLicenseTyperesponseData = masterlicensetype.stream()
				.map(masterlicsencetypemapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(masterLicenseTyperesponseData, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	@Override
	public GenericResponse getAllActive() {
		List<MasterLicenseTypeEntity> List = Masterlicensetyperepository.findAllByIsActiveOrderByModifiedDateDesc(Boolean.TRUE);
		if (CollectionUtils.isEmpty(List)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}

		return Library.getSuccessfulResponse(List, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}


}
