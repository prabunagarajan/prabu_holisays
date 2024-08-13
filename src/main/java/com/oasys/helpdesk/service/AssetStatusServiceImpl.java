package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.CODE;
import static com.oasys.helpdesk.constant.Constant.DEVICE_ACCESSORIES_STATUS;

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
import com.oasys.helpdesk.dto.AssetStatusRequestDTO;
import com.oasys.helpdesk.dto.AssetStatusResponseDTO;
import com.oasys.helpdesk.dto.DeviceAccessoriesStatusRequestDTO;
import com.oasys.helpdesk.entity.AssetStatusEntity;
import com.oasys.helpdesk.entity.DeviceAccessoriesStatusEntity;
import com.oasys.helpdesk.mapper.AssetMapMapper;
import com.oasys.helpdesk.mapper.AssetStatusMapper;
import com.oasys.helpdesk.mapper.DeviceAccessoriesStatusMapper;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.repository.AssetStatusRepository;
import com.oasys.helpdesk.repository.DeviceAccessoriesStatusRepository;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class AssetStatusServiceImpl implements AssetStatusService {
	@Autowired
	private AssetStatusRepository assetStatusRepository;

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private AssetStatusMapper assetstatusmapper;

	@Override
	public GenericResponse add(AssetStatusRequestDTO requestDTO) {
		Optional<AssetStatusEntity> entity = assetStatusRepository.findByNameIgnoreCase(requestDTO.getName());
		if (entity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { "NAME " }));
		}
		entity = assetStatusRepository.findByCodeIgnoreCase(requestDTO.getCode());
		if (entity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { "CODE" }));
		}
		requestDTO.setId(null);
		AssetStatusEntity assetBrandEntity = commonUtil.modalMap(requestDTO, AssetStatusEntity.class);
		assetStatusRepository.save(assetBrandEntity);
		return Library.getSuccessfulResponse(assetBrandEntity, ErrorCode.CREATED.getErrorCode(),
				ErrorMessages.RECORED_CREATED);
	}

	@Override
	public GenericResponse update(AssetStatusRequestDTO requestDTO) {
		if (Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { "ID" }));
		}
		Optional<AssetStatusEntity> entityOptional = assetStatusRepository
				.findByStatusIgnoreCaseNotInId(requestDTO.getName().toUpperCase(), requestDTO.getId());
		if (entityOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { "NAME" }));
		}
		entityOptional = assetStatusRepository.findById(requestDTO.getId());
		if (!entityOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "ID" }));
		}
		AssetStatusEntity entity = entityOptional.get();
		entity.setName(requestDTO.getName());
		entity.setCode(requestDTO.getCode());
	    assetStatusRepository.save(entity);
		return Library.getSuccessfulResponse(entity, ErrorCode.CREATED.getErrorCode(), ErrorMessages.RECORED_UPDATED);
	}

	@Override
	public GenericResponse getAll() {
		List<AssetStatusEntity> assetBrandList = assetStatusRepository.findAllByOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(assetBrandList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<AssetStatusResponseDTO> assetBrandResponseList = assetBrandList.stream()
				.map(assetstatusmapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(assetBrandResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
}
