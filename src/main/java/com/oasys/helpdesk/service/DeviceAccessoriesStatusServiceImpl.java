package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.CODE;
import static com.oasys.helpdesk.constant.Constant.DESC;
import static com.oasys.helpdesk.constant.Constant.DEVICE_ACCESSORIES_STATUS;
import static com.oasys.helpdesk.constant.Constant.ID;
import static com.oasys.helpdesk.constant.Constant.MODIFIED_DATE;
import static com.oasys.helpdesk.constant.Constant.STATUS;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.DeviceAccessoriesStatusRequestDTO;
import com.oasys.helpdesk.dto.DeviceAccessoriesStatusResponseDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.entity.DeviceAccessoriesStatusEntity;
import com.oasys.helpdesk.mapper.DeviceAccessoriesStatusMapper;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.repository.DeviceAccessoriesStatusRepository;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.RandomUtil;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class DeviceAccessoriesStatusServiceImpl implements DeviceAccessoriesStatusService{
	
	@Autowired
	private DeviceAccessoriesStatusRepository deviceAccessoriesStatusRepository;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private DeviceAccessoriesStatusMapper deviceAccessoriesStatusMapper;
	
	@Autowired
	private PaginationMapper paginationMapper;
	
	public static final String Device_Status= "Device Accessories Status";
	
	@Override
	public GenericResponse add(DeviceAccessoriesStatusRequestDTO requestDTO)	{
		Optional<DeviceAccessoriesStatusEntity> entity = deviceAccessoriesStatusRepository
				.findByDeviceAccesoriesStatusIgnoreCase(requestDTO.getDeviceAccesoriesStatus());
		if (entity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { DEVICE_ACCESSORIES_STATUS }));
		}
		entity = deviceAccessoriesStatusRepository
				.findByCodeIgnoreCase(requestDTO.getCode());
		if (entity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { CODE }));
		}
		requestDTO.setId(null);
		DeviceAccessoriesStatusEntity assetBrandEntity = commonUtil.modalMap(requestDTO, DeviceAccessoriesStatusEntity.class);
		deviceAccessoriesStatusRepository.save(assetBrandEntity);
		return Library.getSuccessfulResponse(assetBrandEntity, ErrorCode.CREATED.getErrorCode(),ErrorMessages.RECORED_CREATED);
	} 
	
	@Override
	public GenericResponse update(DeviceAccessoriesStatusRequestDTO requestDTO)	{
		if(Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		Optional<DeviceAccessoriesStatusEntity> entityOptional = deviceAccessoriesStatusRepository
				.findByAccessoriesStatusIgnoreCaseNotInId(requestDTO.getDeviceAccesoriesStatus().toUpperCase(), requestDTO.getId());
		if (entityOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { DEVICE_ACCESSORIES_STATUS }));
		}
		entityOptional = deviceAccessoriesStatusRepository.findById(requestDTO.getId());
		if (!entityOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		DeviceAccessoriesStatusEntity entity = entityOptional.get();
		entity.setDeviceAccesoriesStatus(requestDTO.getDeviceAccesoriesStatus());
		entity.setStatus(requestDTO.getStatus());
		deviceAccessoriesStatusRepository.save(entity);
		return Library.getSuccessfulResponse(entity, ErrorCode.CREATED.getErrorCode(),ErrorMessages.RECORED_UPDATED);
	}
	
	@Override
	public GenericResponse getById(Long id) {
		Optional<DeviceAccessoriesStatusEntity> assetBrandEntity = deviceAccessoriesStatusRepository.findById(id);
		if (!assetBrandEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(deviceAccessoriesStatusMapper.convertEntityToResponseDTO(assetBrandEntity.get()), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	@Override
	public GenericResponse getAll() {
		List<DeviceAccessoriesStatusEntity> assetBrandList = deviceAccessoriesStatusRepository.findAllByOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(assetBrandList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<DeviceAccessoriesStatusResponseDTO> assetBrandResponseList = assetBrandList.stream()
				.map(deviceAccessoriesStatusMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(assetBrandResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	@Override
	public GenericResponse searchByFilter(PaginationRequestDTO paginationDto) {
		Pageable pageable = null;
		Page<DeviceAccessoriesStatusEntity> list = null;
		Long id = null;
		Boolean status = null;
		if (StringUtils.isBlank(paginationDto.getSortField())) {
			paginationDto.setSortField(MODIFIED_DATE);
		}
		if (StringUtils.isBlank(paginationDto.getSortOrder())) {
			paginationDto.setSortOrder(DESC);
		}
		if (paginationDto.getSortOrder().equalsIgnoreCase(ASC)) {
			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
					Sort.by(Direction.ASC, paginationDto.getSortField()));
		} else {
			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
					Sort.by(Direction.DESC, paginationDto.getSortField()));
		}
		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get(ID))
					&& !paginationDto.getFilters().get(ID).toString().trim().isEmpty()) {
				try {
				id = Long.valueOf(paginationDto.getFilters().get(ID).toString());
				}catch(Exception e) {
					log.error("error occurred while parsing id :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}

			if (Objects.nonNull(paginationDto.getFilters().get(STATUS))
					&& !paginationDto.getFilters().get(STATUS).toString().trim().isEmpty()) {
				try {
					status = Boolean.valueOf(paginationDto.getFilters().get(STATUS).toString());
				} catch (Exception e) {
					log.error("error occurred while parsing status :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}
		}
		if (Objects.nonNull(id) && Objects.nonNull(status)) {
			list = deviceAccessoriesStatusRepository.getByIdAndStatus(id, status, pageable);
		} else if (Objects.nonNull(id) && Objects.isNull(status)) {
			list = deviceAccessoriesStatusRepository.getById(id, pageable);
		} else if (Objects.isNull(id) && Objects.nonNull(status)) {
			list = deviceAccessoriesStatusRepository.getByStatus(status, pageable);
		}
		if (Objects.isNull(list)) {
			list = deviceAccessoriesStatusRepository.getAll(pageable);
		}
		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		Page<DeviceAccessoriesStatusResponseDTO> finalResponse = list.map(deviceAccessoriesStatusMapper::convertEntityToResponseDTO);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	@Override
	public GenericResponse getCode() {
		MenuPrefix prefix = MenuPrefix.getType(Device_Status);
		String code = prefix.toString() + RandomUtil.getRandomNumber();
		while (true) {
			Optional<DeviceAccessoriesStatusEntity> entity = deviceAccessoriesStatusRepository.findByCodeIgnoreCase(code);
			if (entity.isPresent()) {
				code = prefix.toString() + RandomUtil.getRandomNumber();
			} else {
				break;
			}
		}
		return Library.getSuccessfulResponse(code, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	@Override
	public GenericResponse getAllActive() {
		List<DeviceAccessoriesStatusEntity> assetBrandList = deviceAccessoriesStatusRepository.findByStatusOrderByModifiedDateDesc(Boolean.TRUE);
		if (CollectionUtils.isEmpty(assetBrandList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<DeviceAccessoriesStatusResponseDTO> assetBrandResponseList = assetBrandList.stream()
				.map(deviceAccessoriesStatusMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(assetBrandResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
}
