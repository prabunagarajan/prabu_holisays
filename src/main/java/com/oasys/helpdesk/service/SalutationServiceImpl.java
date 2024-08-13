package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.CODE;
import static com.oasys.helpdesk.constant.Constant.DESC;
import static com.oasys.helpdesk.constant.Constant.SALUTATION;
import static com.oasys.helpdesk.constant.Constant.STATUS;
import static com.oasys.helpdesk.constant.Constant.ID;
import static com.oasys.helpdesk.constant.Constant.MODIFIED_DATE;

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
import com.oasys.helpdesk.dto.SalutationRequestDTO;
import com.oasys.helpdesk.dto.SalutationResponseDTO;
import com.oasys.helpdesk.entity.SalutationEntity;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.mapper.SalutationMapper;
import com.oasys.helpdesk.repository.SalutationRepository;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.RandomUtil;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class SalutationServiceImpl implements SalutationService {

	@Autowired
	private SalutationRepository salutationRepo;

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private SalutationMapper salutationMapper;

	@Autowired
	private PaginationMapper paginationMapper;

	@Override
	public GenericResponse addSalutation(SalutationRequestDTO requestDTO) {

		Optional<SalutationEntity> salutationOptional = salutationRepo
				.findByCodeIgnoreCase(requestDTO.getCode().toUpperCase());
		if (salutationOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { CODE }));
		}

		requestDTO.setId(null);
		SalutationEntity salutationEntity = commonUtil.modalMap(requestDTO, SalutationEntity.class);
		salutationRepo.save(salutationEntity);
		return Library.getSuccessfulResponse(salutationEntity, ErrorCode.CREATED.getErrorCode(),
				ErrorMessages.RECORED_CREATED);
	}

	@Override
	public GenericResponse updateSalutation(SalutationRequestDTO requestDTO) {
		Optional<SalutationEntity> salutationOptional = salutationRepo.findById(requestDTO.getId());
		if (Objects.isNull(requestDTO.getId()) || !salutationOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		salutationOptional = salutationRepo.findBySalutationnameAndId(requestDTO.getSalutationname(),
				requestDTO.getId());
		if (salutationOptional.isPresent()) {
//			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
//					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { SALUTATION }));
			SalutationEntity salutationEntity = salutationOptional.get();
			salutationEntity.setSalutationname(requestDTO.getSalutationname());
			salutationEntity.setStatus(requestDTO.getStatus());
			salutationRepo.save(salutationEntity);
			return Library.getSuccessfulResponse(salutationEntity, ErrorCode.CREATED.getErrorCode(),
					ErrorMessages.RECORED_UPDATED);
		}
		salutationOptional = salutationRepo.findById(requestDTO.getId());
		if (!salutationOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}

		SalutationEntity salutationEntity = salutationOptional.get();
		salutationEntity.setSalutationname(requestDTO.getSalutationname());
		salutationEntity.setStatus(requestDTO.getStatus());
		salutationRepo.save(salutationEntity);
		return Library.getSuccessfulResponse(salutationEntity, ErrorCode.CREATED.getErrorCode(),
				ErrorMessages.RECORED_UPDATED);
	}

	@Override
	public GenericResponse getAll() {
		List<SalutationEntity> salutationEntity = salutationRepo.findAllByOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(salutationEntity)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<SalutationResponseDTO> salutationResponseData = salutationEntity.stream()
				.map(salutationMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(salutationResponseData, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getAllActive() {
		List<SalutationEntity> salutationList = salutationRepo.findAllByStatusOrderByModifiedDateDesc(Boolean.TRUE);
		if (CollectionUtils.isEmpty(salutationList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<SalutationResponseDTO> ResponseList = salutationList.stream()
				.map(salutationMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(ResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getById(Long id) {
		Optional<SalutationEntity> salutationEntity = salutationRepo.findById(id);
		if (!salutationEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(salutationMapper.convertEntityToResponseDTO(salutationEntity.get()),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getCode() {
		MenuPrefix prefix = MenuPrefix.getType(SALUTATION);
		String code = prefix.toString() + RandomUtil.getRandomNumber();
		while (true) {
			Optional<SalutationEntity> salutationEntity = salutationRepo.findByCodeIgnoreCase(code);
			if (salutationEntity.isPresent()) {
				code = prefix.toString() + RandomUtil.getRandomNumber();
			} else {
				break;
			}
		}
		return Library.getSuccessfulResponse(code, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse searchByFilter(PaginationRequestDTO paginationDto) {
		Pageable pageable = null;
		Page<SalutationEntity> list = null;
		String name = null;
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
			if (Objects.nonNull(paginationDto.getFilters().get("name"))
					&& !paginationDto.getFilters().get("name").toString().trim().isEmpty()) {
				try {
					// id = Long.valueOf(paginationDto.getFilters().get(ID).toString());
					name = (String) paginationDto.getFilters().get("name");
				} catch (Exception e) {
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

		if (Objects.nonNull("name") && Objects.nonNull(status)) {
			list = salutationRepo.getBySubStringAndStatus(name, status, pageable);
		}
		else if (Objects.nonNull("name") && Objects.isNull(status)) {
			list = salutationRepo.getBySubString(name, pageable);
		} else if (Objects.isNull("name") && Objects.nonNull(status)) {
			list = salutationRepo.getByStatus(status, pageable);
		}

		
		if (Objects.isNull(list)) {
			list = salutationRepo.getAllSubString(name, pageable);
		}
		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		// list =
		// list.stream().filter(item->item.getSalutationname().contains("name")).collect(Collectors.toList());
		Page<SalutationResponseDTO> finalResponse = list.map(salutationMapper::convertEntityToResponseDTO);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}

}
