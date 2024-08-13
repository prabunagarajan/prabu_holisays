package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.CATEGORYID;
import static com.oasys.helpdesk.constant.Constant.CODE;
import static com.oasys.helpdesk.constant.Constant.ID;
import static com.oasys.helpdesk.constant.Constant.PRIORITY_TYPE;
import static com.oasys.helpdesk.constant.Constant.STATUS;
import static com.oasys.helpdesk.constant.Constant.SUB_CATEGORY_ID;

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

import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.entity.PriorityMaster;
import com.oasys.helpdesk.entity.SubCategory;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.mapper.PriorityMasterMapper;
import com.oasys.helpdesk.repository.PriorityMasterRepository;
import com.oasys.helpdesk.repository.SubCategoryRepository;
import com.oasys.helpdesk.request.PriorityMasterRequestDto;
import com.oasys.helpdesk.response.PriorityMasterResponseDto;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.RandomUtil;

@Service
public class PriorityMasterServiceImpl implements PriorityMasterService{

	@Autowired
	PriorityMasterRepository priorityMasterRepository;
	
	@Autowired
	PriorityMasterMapper priorityMasterMapper;
	
	@Autowired
	private PaginationMapper paginationMapper;
	
	public static final String PRIORITY= "priority";
	public static final String CATEGORY= "category.id";
	public static final String SUBCATEGORY= "subCategory.id";
	
	@Autowired
	private SubCategoryRepository subCategoryRepository;
	
	@Override
	public GenericResponse getById(Long id) {
		Optional<PriorityMaster> entity = priorityMasterRepository.findById(id);
		if (!entity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(priorityMasterMapper.convertEntityToResponseDTO(entity.get()), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse updateAssetType(PriorityMasterRequestDto requestDTO) {
		if(Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		Optional<PriorityMaster> optional = priorityMasterRepository.findBySubCategoryIdNotInId(requestDTO.getSubCategoryId(), requestDTO.getId());
		if (optional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { SUB_CATEGORY_ID }));
		}
				
		optional = 	priorityMasterRepository.findById(requestDTO.getId());
		if (!optional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		Optional<SubCategory> subCategoryEntity = subCategoryRepository.findById(requestDTO.getSubCategoryId());
		if (!subCategoryEntity.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { SUB_CATEGORY_ID }));
		}
		
		
		PriorityMaster entity = optional.get();
		entity.setPriority(requestDTO.getPriority());
		entity.setActive(requestDTO.isActive());
		entity.setSubCategory(subCategoryEntity.get());
		priorityMasterRepository.save(entity);
		return Library.getSuccessfulResponse(priorityMasterMapper.convertEntityToResponseDTO(entity), ErrorCode.CREATED.getErrorCode(),ErrorMessages.RECORED_UPDATED);
	}

	@Override
	public GenericResponse getCode() {
		MenuPrefix prefix = MenuPrefix.getType(PRIORITY);
		String code = prefix.toString() + RandomUtil.getRandomNumber();
		while (true) {
			Optional<PriorityMaster> entity = priorityMasterRepository.findByCodeIgnoreCase(code);
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
	public GenericResponse getAll() {
		List<PriorityMaster> list = priorityMasterRepository.findAllByOrderByModifiedDateDesc();
		
		
		if (CollectionUtils.isEmpty(list)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());                                                 
		}
		List<PriorityMasterResponseDto> responseList = list.stream()
				.map(priorityMasterMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(responseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse create(PriorityMasterRequestDto requestDTO) {
		Optional<PriorityMaster> optional = priorityMasterRepository.findByCodeIgnoreCase(requestDTO.getCode());
		if (optional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { CODE }));

		}
		optional = priorityMasterRepository.findBySubCategoryId(requestDTO.getSubCategoryId());
		if (optional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { SUB_CATEGORY_ID }));
		}
		 
		
		requestDTO.setId(null);
		//PriorityMaster entity = commonUtil.modalMap(requestDTO, PriorityMaster.class);
		PriorityMaster entity = priorityMasterMapper.convertRequestDTOToEntity(requestDTO,null);
		priorityMasterRepository.save(entity);
		
		return Library.getSuccessfulResponse(priorityMasterMapper.convertEntityToResponseDTO(entity), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_CREATED);
	}

	@Override
	public GenericResponse searchByFilter(PaginationRequestDTO paginationDto) {
		Pageable pageable = null;
		Page<PriorityMaster> list = null;
		Long categoryId = null;
		String priority = null;
		Long subCategoryId = null;
		Boolean status = null;
		if(StringUtils.isNotBlank(paginationDto.getSortField())) {
			if(CATEGORYID.equals(paginationDto.getSortField())) {
				paginationDto.setSortField(CATEGORY);
			}
			if(SUB_CATEGORY_ID.equals(paginationDto.getSortField())) {
				paginationDto.setSortField(SUBCATEGORY);
			}
		}
		if (paginationDto.getSortOrder().equalsIgnoreCase(ASC)) {
			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
					Sort.by(Direction.ASC, paginationDto.getSortField()));
		} else {
			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
					Sort.by(Direction.DESC, paginationDto.getSortField()));
		}
		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get(CATEGORYID))
					&& !paginationDto.getFilters().get(CATEGORYID).toString().trim().isEmpty()) {
				categoryId = Long.valueOf(paginationDto.getFilters().get(CATEGORYID).toString());
			}
			if (Objects.nonNull(paginationDto.getFilters().get(SUB_CATEGORY_ID))
					&& !paginationDto.getFilters().get(SUB_CATEGORY_ID).toString().trim().isEmpty()) {
				subCategoryId = Long.valueOf(paginationDto.getFilters().get(SUB_CATEGORY_ID).toString());
			}
			if (Objects.nonNull(paginationDto.getFilters().get(PRIORITY_TYPE))
					&& !paginationDto.getFilters().get(PRIORITY_TYPE).toString().trim().isEmpty()) {
				priority = String.valueOf(paginationDto.getFilters().get(PRIORITY_TYPE).toString());
			}
			if (Objects.nonNull(paginationDto.getFilters().get(STATUS))
					&& !paginationDto.getFilters().get(STATUS).toString().trim().isEmpty()) {
				status = Boolean.valueOf(paginationDto.getFilters().get(STATUS).toString());
			}
		}
		list = getByFilter(categoryId, subCategoryId, priority, status, pageable);
		if (Objects.isNull(list)) {
			list = priorityMasterRepository.getAll(pageable);
		}
		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		Page<PriorityMasterResponseDto> finalResponse = list.map(priorityMasterMapper::convertEntityToResponseDTO);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	private Page<PriorityMaster> getByFilter(Long categoryId, Long subCategoryId, String priority, Boolean status,
			Pageable pageable) {
		Page<PriorityMaster> list = null;
		if (Objects.nonNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.nonNull(priority) && Objects.nonNull(status)) {
			list = priorityMasterRepository.getByCategoryActualPSubcategoryAndStatus(categoryId, subCategoryId, priority, status, pageable);
		} 
		if (Objects.isNull(list) && Objects.nonNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.nonNull(priority) &&  Objects.isNull(status)) {
			list = priorityMasterRepository.getByCategorySubcategoryAndActualP(categoryId, subCategoryId, priority, pageable);
		} 
		
		if (Objects.isNull(list) && Objects.nonNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.nonNull(status) && Objects.isNull(priority)) {
			list = priorityMasterRepository.getByCategorySubcategoryAndStatus(categoryId, subCategoryId, status, pageable);
		}
		
		if (Objects.isNull(list) && Objects.nonNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.isNull(status) && Objects.isNull(priority)) {
			list = priorityMasterRepository.getByCategoryAndSubcategory(categoryId, subCategoryId, pageable);
		}
		
		if (Objects.isNull(list) && Objects.nonNull(categoryId) && Objects.isNull(subCategoryId) && Objects.nonNull(status) && Objects.isNull(priority)) {
			list = priorityMasterRepository.getByCategoryIdAndStatus(categoryId, status, pageable);
		}
		
		if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.nonNull(status) && Objects.isNull(priority)) {
			list = priorityMasterRepository.getBySubCategoryIdAndStatus(subCategoryId, status, pageable);
		}
		
		if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.isNull(subCategoryId) && Objects.nonNull(status) && Objects.nonNull(priority)) {
			list = priorityMasterRepository.getByActualPAndStatus(priority, status, pageable);
		}
		if (Objects.isNull(list) && Objects.nonNull(categoryId) && Objects.isNull(subCategoryId) && Objects.isNull(status) && Objects.isNull(priority)) {
			list = priorityMasterRepository.getByCategoryId(categoryId, pageable);
		}
		if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.isNull(status) && Objects.isNull(priority)) {
			list = priorityMasterRepository.getBySubCategoryId(subCategoryId, pageable);
			
			
		}if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.isNull(subCategoryId) && Objects.nonNull(status) && Objects.isNull(priority)) {
			list = priorityMasterRepository.getByStatus(status, pageable);
		}
		if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.isNull(subCategoryId) && Objects.isNull(status) && Objects.nonNull(priority)) {
			list = priorityMasterRepository.getByActualProblem(priority, pageable);
		}
		return list;
	}

	@Override
	public GenericResponse getAllActive() {
		List<PriorityMaster> list = priorityMasterRepository.findByIsActiveOrderByModifiedDateDesc(Boolean.TRUE);
		if (CollectionUtils.isEmpty(list)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<PriorityMasterResponseDto> responseDto = list.stream()
				.map(priorityMasterMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(responseDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	
	@Override
	public GenericResponse getById(Long subCategoryId, Long categoryId) {
		Optional<PriorityMaster> list = priorityMasterRepository.getById(subCategoryId,categoryId);
		if (!list.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(priorityMasterMapper.convertEntityToResponseDTO(list.get()), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

}
