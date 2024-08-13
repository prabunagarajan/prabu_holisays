package com.oasys.posasset.service.impl;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;

import com.oasys.posasset.entity.DeviceLostLogEntity;
import com.oasys.posasset.mapper.DeviceLostLogMapper;
import com.oasys.posasset.repository.DevicelostlogRepository;
import com.oasys.posasset.response.DeviceLostLogResponseDTO;
import com.oasys.posasset.service.DeviceLostLogService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class DeviceLostLogServiceImpl implements DeviceLostLogService {
	

	
	@Autowired
	DeviceLostLogMapper devicelostlogmapper;
	

	@Autowired
	private EntityManager entityManager;
	

	
	@Autowired
	private DevicelostlogRepository devicelostlogrepository;
	
	
	@Override
	public GenericResponse getAll() {
		List<DeviceLostLogEntity> list = devicelostlogrepository.findAll();

		if (CollectionUtils.isEmpty(list)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<DeviceLostLogResponseDTO> responseDto = list.stream().map(devicelostlogmapper::convertEntityToResponseDTO)
				.collect(Collectors.toList());
		return Library.getSuccessfulResponse(responseDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	@Override
	public GenericResponse getById(Long id) {
		Optional<DeviceLostLogEntity> devicelostlogEntity = devicelostlogrepository.findById(id);
		if (!devicelostlogEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(devicelostlogmapper.convertEntityToResponseDTO(devicelostlogEntity.get()), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	
	public GenericResponse getByApplicationNo(String applicationNo) {
		List<DeviceLostLogEntity> fetchRecords=devicelostlogrepository
				.findAllByApplicationNumberOrderByCreatedDateDesc(applicationNo);
		if(fetchRecords.isEmpty()) {
			return Library.getSuccessfulResponse(fetchRecords, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		} else {
			//fetchRecords=fetchRecords.stream().map(this::convert).collect(Collectors.toList());
			return Library.getSuccessfulResponse(fetchRecords, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}
	}
	
	
}
