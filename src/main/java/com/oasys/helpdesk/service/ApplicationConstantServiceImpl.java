package com.oasys.helpdesk.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.ApplicationConstantRequestDTO;
import com.oasys.helpdesk.entity.ApplicationConstantEntity;
import com.oasys.helpdesk.repository.ApplicationConstantRepository;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;

@Service
public class ApplicationConstantServiceImpl implements ApplicationConstantService {

	@Autowired
	private ApplicationConstantRepository applicationConstantRepository;

	@Override
	public GenericResponse update(ApplicationConstantRequestDTO requestDTO) {
		Optional<ApplicationConstantEntity> applicationConstantEntity = applicationConstantRepository
				.findByCodeIgnoreCase(requestDTO.getCode());
		if (applicationConstantEntity.isPresent()) {
			ApplicationConstantEntity applicationEntity = applicationConstantEntity.get();
			applicationEntity.setValue(requestDTO.getValue());
			applicationConstantRepository.save(applicationEntity);
			return Library.getSuccessfulResponse(applicationEntity,
					ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_CREATED);
		} else {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { Constant.CODE }));
		}
	}

}
