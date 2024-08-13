package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.CODE;
import static com.oasys.helpdesk.constant.Constant.DESC;
import static com.oasys.helpdesk.constant.Constant.ID;
import static com.oasys.helpdesk.constant.Constant.MODIFIED_DATE;
import static com.oasys.helpdesk.constant.Constant.STATUS;
import static com.oasys.helpdesk.constant.Constant.CONFIGURATION;

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

import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.ShiftConfigRequestDTO;
import com.oasys.helpdesk.dto.ShiftConfigResponseDTO;
import com.oasys.helpdesk.entity.ShiftConfigEntity;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.mapper.ShiftConfigMapper;
import com.oasys.helpdesk.repository.ShiftConfigRepository;

import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.RandomUtil;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ShiftConfigServiceImpl implements ShiftConfigService {

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	CommonDataController commonDataController;

	@Autowired
	private ShiftConfigRepository shiftConfigRepository;

	@Autowired
	private PaginationMapper paginationMapper;

	@Autowired
	private ShiftConfigMapper shiftConfigMapper;


	
	
	@Override
	public GenericResponse addShiftConfiguration(ShiftConfigRequestDTO requestDto) {

		Optional<ShiftConfigEntity> shiftConfigOptional = shiftConfigRepository
				.findByCodeIgnoreCase(requestDto.getCode().toUpperCase());
		if (shiftConfigOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { CODE }));
		}

		requestDto.setId(null);
		ShiftConfigEntity shiftConfigEntity = commonUtil.modalMap(requestDto, ShiftConfigEntity.class);
		shiftConfigRepository.save(shiftConfigEntity);
		return Library.getSuccessfulResponse(shiftConfigEntity, ErrorCode.CREATED.getErrorCode(),
				ErrorMessages.RECORED_CREATED);
	}

	@Override
	public GenericResponse getByConfiguration(String config) {
		List<ShiftConfigEntity> shiftConfig = shiftConfigRepository.findAllByConfiguration(config);
		if (shiftConfig == null) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<ShiftConfigResponseDTO> shiftConfigEntitylist = shiftConfig.stream()
				.map(shiftConfigMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(shiftConfigEntitylist, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

	@Override
	public GenericResponse updateShiftConfiguration(ShiftConfigRequestDTO requestDTO) {
		if (Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		Optional<ShiftConfigEntity> shiftConfigOptional = shiftConfigRepository
				.findByConfigurationAndId(requestDTO.getConfiguration(), requestDTO.getId());
		if (shiftConfigOptional.isPresent()) {
//			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
//					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { CONFIGURATION }));
			ShiftConfigEntity shiftConfigEntity = shiftConfigOptional.get();
			shiftConfigEntity.setConfiguration(requestDTO.getConfiguration());
			shiftConfigEntity.setStatus(requestDTO.getStatus());
			shiftConfigRepository.save(shiftConfigEntity);
			return Library.getSuccessfulResponse(shiftConfigEntity, ErrorCode.CREATED.getErrorCode(),
					ErrorMessages.RECORED_UPDATED);
		}
		shiftConfigOptional = shiftConfigRepository.findById(requestDTO.getId());
		if (!shiftConfigOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}

		ShiftConfigEntity shiftConfigEntity = shiftConfigOptional.get();
		shiftConfigEntity.setConfiguration(requestDTO.getConfiguration());
		shiftConfigEntity.setStatus(requestDTO.getStatus());
		shiftConfigRepository.save(shiftConfigEntity);
		return Library.getSuccessfulResponse(shiftConfigEntity, ErrorCode.CREATED.getErrorCode(),
				ErrorMessages.RECORED_UPDATED);
	}

	@Override
	public GenericResponse searchByConfigFilter(PaginationRequestDTO paginationDto) {
		Pageable pageable = null;
		Page<ShiftConfigEntity> list = null;
		String shiftConfig = null;
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
			if (Objects.nonNull(paginationDto.getFilters().get("shiftConfig"))
					&& !paginationDto.getFilters().get("shiftConfig").toString().trim().isEmpty()) {
				try {
					//shiftConfig = Long.valueOf(paginationDto.getFilters().get("shiftConfig").toString());
					shiftConfig = (String) paginationDto.getFilters().get("shiftConfig");
					
				}catch(Exception e) {
				log.error("error occurred while parsing shiftConfig :: {}", e);
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
//		if (Objects.nonNull("shiftConfig") && Objects.nonNull(status)) {
//			list = shiftConfigRepository.getByConfigurationAndStatus(shiftConfig, status, pageable);
//		} else if (Objects.nonNull("shiftConfig") && Objects.isNull(status)) {
//			list = shiftConfigRepository.getByConfiguration(shiftConfig, pageable);
//		} else if (Objects.isNull("shiftConfig") && Objects.nonNull(status)) {
//			list = shiftConfigRepository.getByStatus(status, pageable);
//		}
		if (Objects.isNull(list)) {
			list = shiftConfigRepository.getAllSubString(shiftConfig,pageable);
		}
		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		Page<ShiftConfigResponseDTO> dataResponse = list.map(shiftConfigMapper::convertEntityToResponseDTO);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(dataResponse),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getAll() {
		List<ShiftConfigEntity> shiftConfigData = shiftConfigRepository.findAllByOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(shiftConfigData)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<ShiftConfigResponseDTO> shiftConfigResponseData = shiftConfigData.stream()
				.map(shiftConfigMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(shiftConfigResponseData, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getById(Long Id) {
		Optional<ShiftConfigEntity> shiftConfigEntity = shiftConfigRepository.findById(Id);
		if (!shiftConfigEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(shiftConfigMapper.convertEntityToResponseDTO(shiftConfigEntity.get()),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}
	
	
	@Override
	public GenericResponse getCode() {
		MenuPrefix prefix = MenuPrefix.getType(CONFIGURATION);
		String code = prefix.toString() + RandomUtil.getRandomNumber();
		while (true) {
			Optional<ShiftConfigEntity> shiftEntity = shiftConfigRepository.findByCodeIgnoreCase(code);
			if (shiftEntity.isPresent()) {
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
		List<ShiftConfigEntity> configList = shiftConfigRepository.findAllByStatusOrderByModifiedDateDesc(Boolean.TRUE);
		if (CollectionUtils.isEmpty(configList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<ShiftConfigResponseDTO> shiftResponseList = configList.stream()
				.map(shiftConfigMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(shiftResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
}
