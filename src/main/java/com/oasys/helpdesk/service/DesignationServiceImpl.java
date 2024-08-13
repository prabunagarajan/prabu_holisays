package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.CODE;
import static com.oasys.helpdesk.constant.Constant.DESC;
import static com.oasys.helpdesk.constant.Constant.DESIGNATION;
import static com.oasys.helpdesk.constant.Constant.ID;
import static com.oasys.helpdesk.constant.Constant.ISSUE_TYPE;
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
import com.oasys.helpdesk.dto.AssetTypeResponseDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.entity.AssetTypeEntity;
import com.oasys.helpdesk.entity.DesignationEntity;
import com.oasys.helpdesk.entity.IssueFromEntity;
import com.oasys.helpdesk.mapper.DesignationMapper;
import com.oasys.helpdesk.mapper.IssueFromMapper;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.repository.DesignationRepository;
import com.oasys.helpdesk.repository.IssueFromRepository;
import com.oasys.helpdesk.request.DesignationRequestDto;
import com.oasys.helpdesk.request.IssueFromRequestDto;
import com.oasys.helpdesk.response.DesignationResponseDto;
import com.oasys.helpdesk.response.IssueFromResponseDto;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.RandomUtil;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class DesignationServiceImpl implements DesignationService{
	@Autowired
	DesignationRepository designationRepository;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	DesignationMapper designationMapper;
	
	@Autowired
	PaginationMapper paginationMapper;
	
	public static final String DESIGNATION_TYPE= "Designation";
	
	@Override
	public GenericResponse getAll() {
		List<DesignationEntity> list = designationRepository.findAllByOrderByModifiedDateDesc();
		
		
		if (CollectionUtils.isEmpty(list)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<DesignationResponseDto> responseDto = list.stream()
				.map(designationMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(responseDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	
	@Override
	public GenericResponse create(DesignationRequestDto requestDto) {
		Optional<DesignationEntity> optional = designationRepository.findByDesignationNameIgnoreCase(requestDto.getDesignationName());
		
		if(optional.isPresent())
		{
				return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
						ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { DESIGNATION }));
			
		}
		
			optional = designationRepository.findByCodeIgnoreCase(requestDto.getCode());
			if(optional.isPresent())
			{
					return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
							ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { CODE }));
				
			}
			
			requestDto.setId(null);
			DesignationEntity entity = commonUtil.modalMap(requestDto, DesignationEntity.class);
			designationRepository.save(entity);
			
			return Library.getSuccessfulResponse(designationMapper.convertEntityToResponseDTO(entity), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_CREATED);
	}

	@Override
	public GenericResponse getCode() {
		MenuPrefix prefix = MenuPrefix.getType(DESIGNATION_TYPE);
		String code = prefix.toString() + RandomUtil.getRandomNumber();
		while (true) {
			Optional<DesignationEntity> entity = designationRepository.findByCodeIgnoreCase(code);
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
	public GenericResponse getById(Long id) {
		Optional<DesignationEntity> entity = designationRepository.findById(id);
		if (!entity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(designationMapper.convertEntityToResponseDTO(entity.get()), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse update(DesignationRequestDto requestDTO) {
		if(Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		Optional<DesignationEntity> optional = designationRepository
				.findByDesignationNameIgnoreCaseNotInId(requestDTO.getDesignationName(), requestDTO.getId());
		if (optional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { DESIGNATION }));
		}
		optional = designationRepository.findById(requestDTO.getId());
		if (!optional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		DesignationEntity entity = optional.get();
		entity.setDesignationName(requestDTO.getDesignationName());
		entity.setActive(requestDTO.isActive());
		designationRepository.save(entity);
		return Library.getSuccessfulResponse(designationMapper.convertEntityToResponseDTO(entity), ErrorCode.CREATED.getErrorCode(),ErrorMessages.RECORED_UPDATED);
	}

	@Override
	public GenericResponse getAllActive() {
		List<DesignationEntity> list = designationRepository.findAllByStatusOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(list)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<DesignationResponseDto> responseDto = list.stream()
				.map(designationMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(responseDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	


	@Override
	public Object searchByFilter(PaginationRequestDTO paginationDto) {
		
		Pageable pageable = null;
		Page<DesignationEntity> list = null;
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
			if (Objects.nonNull(paginationDto.getFilters().get(DESIGNATION))
					&& !paginationDto.getFilters().get(DESIGNATION).toString().trim().isEmpty()) {
				try {
					name =paginationDto.getFilters().get(DESIGNATION).toString();
					}catch(Exception e) {
						//log.error("error occurred while parsing id :: {}", e);
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
		if (Objects.nonNull(name) && Objects.nonNull(status)) {
			list = designationRepository.getByDesignationNameAndStatus(name, status, pageable);
		} else if (Objects.nonNull(name) && Objects.isNull(status)) {
			list = designationRepository.getByDesignationName(name, pageable);
		} else if (Objects.isNull(name) && Objects.nonNull(status)) {
			list = designationRepository.getByStatus(status, pageable);
		}
		if (Objects.isNull(list)) {
			list = designationRepository.getAll(pageable);
		}
		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		Page<DesignationResponseDto> finalResponse = list.map(designationMapper::convertEntityToResponseDTO);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	

}
