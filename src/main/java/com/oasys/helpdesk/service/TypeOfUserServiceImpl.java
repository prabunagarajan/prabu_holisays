package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.CODE;
import static com.oasys.helpdesk.constant.Constant.DESC;
import static com.oasys.helpdesk.constant.Constant.ID;
import static com.oasys.helpdesk.constant.Constant.MODIFIED_DATE;
import static com.oasys.helpdesk.constant.Constant.STATUS;
import static com.oasys.helpdesk.constant.Constant.TYPE_OF_USER;

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

import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.TypeOfUserRequestDTO;
import com.oasys.helpdesk.dto.TypeOfUserResponseDTO;
import com.oasys.helpdesk.entity.TypeOfUserEntity;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.mapper.TypeOfUserMapper;
import com.oasys.helpdesk.repository.TypeOfUserRepository;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.RandomUtil;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class TypeOfUserServiceImpl implements TypeOfUserService{

	@Autowired
	private TypeOfUserRepository  typeOfUserRepository;

	@Autowired
	private CommonUtil commonUtil;


	@Autowired
	private PaginationMapper paginationMapper;

	@Autowired
	private TypeOfUserMapper typeOfUserMapper;


	@Override
	public GenericResponse addTypeOfUser(TypeOfUserRequestDTO requestDTO) {

		Optional<TypeOfUserEntity> typeOfUserOptional = typeOfUserRepository
				.findByCodeIgnoreCase(requestDTO.getCode().toUpperCase());
		if (typeOfUserOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { CODE }));
		}
		typeOfUserOptional = typeOfUserRepository.findByTypeOfUserIgnoreCase(requestDTO.getTypeOfUser());
		if (typeOfUserOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { Constant.TYPEOFUSER }));
		}
		requestDTO.setId(null);
		TypeOfUserEntity typeOfUserEntity = commonUtil.modalMap(requestDTO, TypeOfUserEntity.class);
		typeOfUserRepository.save(typeOfUserEntity);
		return Library.getSuccessfulResponse(typeOfUserEntity, ErrorCode.CREATED.getErrorCode(),
				ErrorMessages.RECORED_CREATED);
	}


	@Override
	public GenericResponse searchByTypeOfUser(PaginationRequestDTO paginationDto){
		Pageable pageable = null;
		Page<TypeOfUserEntity> list = null;
		String typeOfUser = null;
		Boolean status = null;
		Long id = null;
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
			if (Objects.nonNull(paginationDto.getFilters().get("typeOfUser"))
					&& !paginationDto.getFilters().get("typeOfUser").toString().trim().isEmpty()) {
				try {
					// id = Long.valueOf(paginationDto.getFilters().get(ID).toString());
					typeOfUser = (String) paginationDto.getFilters().get("typeOfUser");
				} catch (Exception e) {
					log.error("error occurred while parsing typeOfUser :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}
			if (Objects.nonNull(paginationDto.getFilters().get(ID))
					&& !paginationDto.getFilters().get(ID).toString().trim().isEmpty()) {
				try {
					 id = Long.valueOf(paginationDto.getFilters().get(ID).toString());
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
		/*
		 * if (Objects.nonNull(typeOfUser) && Objects.nonNull(status)) { list =
		 * typeOfUserRepository.getBySubStringAndStatus(typeOfUser, status, pageable); }
		 * else if (Objects.nonNull(typeOfUser) && Objects.isNull(status)) { list =
		 * typeOfUserRepository.getBySubString(typeOfUser, pageable); } else if
		 * (Objects.isNull(typeOfUser) && Objects.nonNull(status)) { list =
		 * typeOfUserRepository.getByStatus(status, pageable); }
		 * 
		 * 
		 * if (Objects.isNull(list)) { list =
		 * typeOfUserRepository.getAllSubString(typeOfUser, pageable); }
		 */
		if(StringUtils.isNotBlank(typeOfUser)) {
			typeOfUser = typeOfUser.trim().toUpperCase();
		}
		list = this.getByFilter(typeOfUser, id, status, pageable);
		if (Objects.isNull(list)) {
			list = typeOfUserRepository.getAll(pageable);
		}
		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		Page<TypeOfUserResponseDTO> finalResponse = list.map(typeOfUserMapper::convertEntityTOResponseDTO);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}
	
	private Page<TypeOfUserEntity> getByFilter(String typeOfUser, Long id, Boolean status, Pageable pageable){
		Page<TypeOfUserEntity> list = null;
		if (Objects.nonNull(id) && StringUtils.isNotBlank(typeOfUser) && Objects.nonNull(status)) {
			list = typeOfUserRepository.getByIdStatusAndTypeOfUser(id, status, typeOfUser, pageable);
		} 
		if (Objects.isNull(list) && Objects.nonNull(id) && StringUtils.isNotBlank(typeOfUser) && Objects.isNull(status)) {
			list = typeOfUserRepository.getByIdAndTypeOfUser(id, typeOfUser, pageable);
		} 
		if (Objects.isNull(list) && Objects.isNull(id) && StringUtils.isNotBlank(typeOfUser) && Objects.nonNull(status)) {
			list = typeOfUserRepository.getByTypeOfUserAndStatus(typeOfUser, status, pageable);
		}
		if (Objects.isNull(list) && Objects.nonNull(id) && StringUtils.isBlank(typeOfUser) && Objects.nonNull(status)) {
			list = typeOfUserRepository.getByIdAndStatus(id, status, pageable);
		}
		if (Objects.isNull(list) && Objects.isNull(id) && StringUtils.isBlank(typeOfUser) && Objects.nonNull(status)) {
			list = typeOfUserRepository.getByStatus(status, pageable);
		}
		if (Objects.isNull(list) && Objects.nonNull(id) && StringUtils.isBlank(typeOfUser) && Objects.isNull(status)) {
			list = typeOfUserRepository.getById(id, pageable);
		}
		if (Objects.isNull(list) && Objects.isNull(id) && StringUtils.isNotBlank(typeOfUser) && Objects.isNull(status)) {
			list = typeOfUserRepository.getByTypeOfUser(typeOfUser, pageable);
		}
		return list;
	}

	@Override
	public GenericResponse updateTypeOfUser(TypeOfUserRequestDTO requestDTO) {
		if (Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		Optional<TypeOfUserEntity> entityOptional = typeOfUserRepository
				.findByTypeOfUserNotInId(requestDTO.getTypeOfUser().toUpperCase(), requestDTO.getId());
		if (entityOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { Constant.TYPEOFUSER }));
		}
		entityOptional = typeOfUserRepository.findById(requestDTO.getId());
		if (!entityOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		TypeOfUserEntity typeOfUserEntity = entityOptional.get();
		typeOfUserEntity.setTypeOfUser(requestDTO.getTypeOfUser());
		typeOfUserEntity.setStatus(requestDTO.getStatus());
		typeOfUserRepository.save(typeOfUserEntity);
		return Library.getSuccessfulResponse(typeOfUserEntity, ErrorCode.CREATED.getErrorCode(),
				ErrorMessages.RECORED_UPDATED);
		
	}


	@Override
	public GenericResponse getByTypeOfUser(String typeOfUser) {
		List<TypeOfUserEntity> typeOfUserEntity = typeOfUserRepository.findAllByTypeOfUser(typeOfUser);
		if (typeOfUserEntity == null) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<TypeOfUserResponseDTO> typeOfUserEntitylist = typeOfUserEntity.stream()
				.map(typeOfUserMapper::convertEntityTOResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(typeOfUserEntitylist, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

	@Override
	public GenericResponse getById(Long Id) {
		Optional<TypeOfUserEntity> typeOfUserEntity = typeOfUserRepository.findById(Id);
		if (!typeOfUserEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(typeOfUserMapper.convertEntityTOResponseDTO(typeOfUserEntity.get()),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}


	@Override
	public GenericResponse getAll() {
		List<TypeOfUserEntity> typeOfUserEntity = typeOfUserRepository.findAllByOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(typeOfUserEntity)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<TypeOfUserResponseDTO> shiftConfigResponseData = typeOfUserEntity.stream()
				.map(typeOfUserMapper::convertEntityTOResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(shiftConfigResponseData, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getCode() {
		MenuPrefix prefix = MenuPrefix.getType(TYPE_OF_USER);
		String code = prefix.toString() + RandomUtil.getRandomNumber();
		while (true) {
			Optional<TypeOfUserEntity> typeOfUserEntity = typeOfUserRepository.findByCodeIgnoreCase(code);
			if (typeOfUserEntity.isPresent()) {
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
		List<TypeOfUserEntity> configList = typeOfUserRepository.findAllByStatusOrderByModifiedDateDesc(Boolean.TRUE);
		if (CollectionUtils.isEmpty(configList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<TypeOfUserResponseDTO> shiftResponseList = configList.stream()
				.map(typeOfUserMapper::convertEntityTOResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(shiftResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}





}
