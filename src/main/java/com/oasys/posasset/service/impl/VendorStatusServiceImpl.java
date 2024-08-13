package com.oasys.posasset.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.posasset.dto.VendorStatusRequestDTO;
import com.oasys.posasset.dto.VendorStatusResponseDTO;
import com.oasys.posasset.entity.VendorStatusMasterEntity;
import com.oasys.posasset.mapper.VendorStatusMapper;
import com.oasys.posasset.repository.VendorStatusRepository;
import com.oasys.posasset.service.VendorStatusService;

@Service
public class VendorStatusServiceImpl implements VendorStatusService {

	@Autowired
	private VendorStatusRepository vendorStatusRepository;

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private VendorStatusMapper vendorstatusmapper;

	@Override
	public GenericResponse add(VendorStatusRequestDTO requestDTO) {
		Optional<VendorStatusMasterEntity> entity = vendorStatusRepository.findByNameIgnoreCase(requestDTO.getName());
		if (entity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { "NAME " }));
		}
		entity = vendorStatusRepository.findByCodeIgnoreCase(requestDTO.getCode());
		if (entity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { "CODE" }));
		}
		requestDTO.setId(null);
		VendorStatusMasterEntity vendorStatusEntity = commonUtil.modalMap(requestDTO, VendorStatusMasterEntity.class);
		vendorStatusRepository.save(vendorStatusEntity);
		return Library.getSuccessfulResponse(vendorStatusEntity, ErrorCode.CREATED.getErrorCode(),
				ErrorMessages.RECORED_CREATED);
	}

	@Override
	public GenericResponse update(VendorStatusRequestDTO requestDTO) {
		if (Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { "ID" }));
		}
		Optional<VendorStatusMasterEntity> entityOptional = vendorStatusRepository
				.findByStatusIgnoreCaseNotInId(requestDTO.getName().toUpperCase(), requestDTO.getId());
		if (entityOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { "NAME" }));
		}
		entityOptional = vendorStatusRepository.findById(requestDTO.getId());
		if (!entityOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "ID" }));
		}
		VendorStatusMasterEntity entity = entityOptional.get();
		entity.setName(requestDTO.getName());
		entity.setCode(requestDTO.getCode());
		entity.setActive(requestDTO.isActive());
		vendorStatusRepository.save(entity);
		return Library.getSuccessfulResponse(entity, ErrorCode.CREATED.getErrorCode(), ErrorMessages.RECORED_UPDATED);
	}

	@Override
	public GenericResponse getAll() {
		List<VendorStatusMasterEntity> vendorStatusList = vendorStatusRepository.findAllByOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(vendorStatusList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<VendorStatusResponseDTO> vendorStatusResponseList = vendorStatusList.stream()
				.map(vendorstatusmapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(vendorStatusResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
}
