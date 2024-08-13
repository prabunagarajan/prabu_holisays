package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.CATEGORYID;
import static com.oasys.helpdesk.constant.Constant.CODE;
import static com.oasys.helpdesk.constant.Constant.ID;
import static com.oasys.helpdesk.constant.Constant.ISSUE_DETAILS;
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
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.entity.SlaMasterEntity;
import com.oasys.helpdesk.entity.UserEntity;
import com.oasys.helpdesk.entity.WorkflowEntity;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.mapper.WorkflowMapper;
import com.oasys.helpdesk.repository.SlaMasterRepository;
import com.oasys.helpdesk.repository.UserRepository;
import com.oasys.helpdesk.repository.WorkflowRepository;
import com.oasys.helpdesk.request.WorkflowRequestDto;
import com.oasys.helpdesk.response.WorkflowResponseDto;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.RandomUtil;

@Service
public class WorkflowServiceImpl implements WorkflowService{
	
	@Autowired
	WorkflowRepository workflowRepository;
	
	@Autowired
	WorkflowMapper workflowMapper;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	private PaginationMapper paginationMapper;
	
	@Autowired
	private SlaMasterRepository slaMasterRepository;
	
	public static final String WORKFLOW= "Workflow";
	public static final String CATEGORY= "category.id";
	public static final String SUBCATEGORY= "subCategory.id";
	public static final String ISSUED= "issueDetails.id";

	@Override
	public GenericResponse getAll(AuthenticationDTO authenticationDTO) {
		List<WorkflowEntity> workflowList = workflowRepository.findAllByOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(workflowList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<WorkflowResponseDto> workflowResponseList = workflowList.stream()
				.map(workflowMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(workflowResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse create(WorkflowRequestDto requestDTO) {
		Optional<WorkflowEntity> optional = workflowRepository.findByCodeIgnoreCase(requestDTO.getCode());
		if(optional.isPresent())
		{
				return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
						ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { CODE }));
			
		}
		
		requestDTO.setId(null);
		WorkflowEntity entity = workflowMapper.convertRequestDTOToEntity(requestDTO,null);
		workflowRepository.save(entity);
		
		return Library.getSuccessfulResponse(workflowMapper.convertEntityToResponseDTO(entity), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_CREATED);
	}

	@Override
	public GenericResponse getById(Long id) {
		Optional<WorkflowEntity> entity = workflowRepository.findById(id);
		if (!entity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(workflowMapper.convertEntityToResponseDTO(entity.get()), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse update(WorkflowRequestDto requestDTO) {
		if(Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		
		Optional<WorkflowEntity> optional = workflowRepository.findById(requestDTO.getId());
		if (!optional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		WorkflowEntity entity = optional.get();
		Optional<SlaMasterEntity> slaMasterEntity =slaMasterRepository.findById(requestDTO.getSla());
		if (!slaMasterEntity.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { Constant.SLAID }));
		}
		entity.setSlaDetails(slaMasterEntity.get());
		if(Objects.nonNull(requestDTO.getAssignTo())) {
			Optional<UserEntity> uOptional = userRepository.findById(requestDTO.getAssignTo());
			if (!uOptional.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { Constant.USER_ID }));
			}
			entity.setAssignTo(uOptional.get());
		}
		entity.setActive(requestDTO.isActive());
		workflowRepository.save(entity);
		return Library.getSuccessfulResponse(workflowMapper.convertEntityToResponseDTO(entity), ErrorCode.CREATED.getErrorCode(),ErrorMessages.RECORED_UPDATED);
	
	}

	@Override
	public GenericResponse getCode() {
		MenuPrefix prefix = MenuPrefix.getType(WORKFLOW);
		String code = prefix.toString() + RandomUtil.getRandomNumber();
		while (true) {
			Optional<WorkflowEntity> entity = workflowRepository.findByCodeIgnoreCase(code);
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
	public GenericResponse searchByFilter(PaginationRequestDTO paginationDto) {
		Pageable pageable = null;
		Page<WorkflowEntity> list = null;
		Long categoryId = null;
		String issueDetails = null;
		Long subCategoryId = null;
		Boolean status = null;
		if(StringUtils.isNotBlank(paginationDto.getSortField())) {
			if(CATEGORYID.equals(paginationDto.getSortField())) {
				paginationDto.setSortField(CATEGORY);
			}
			if(SUB_CATEGORY_ID.equals(paginationDto.getSortField())) {
				paginationDto.setSortField(SUBCATEGORY);
			}
			if(ISSUE_DETAILS.equals(paginationDto.getSortField()))	{
				paginationDto.setSortField(ISSUED);
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
			if (Objects.nonNull(paginationDto.getFilters().get(ISSUE_DETAILS))
					&& !paginationDto.getFilters().get(ISSUE_DETAILS).toString().trim().isEmpty()) {
				issueDetails = String.valueOf(paginationDto.getFilters().get(ISSUE_DETAILS).toString());
			}
			if (Objects.nonNull(paginationDto.getFilters().get(STATUS))
					&& !paginationDto.getFilters().get(STATUS).toString().trim().isEmpty()) {
				status = Boolean.valueOf(paginationDto.getFilters().get(STATUS).toString());
			}
		}
		list = getByFilter(categoryId, subCategoryId, issueDetails, status, pageable);
		if (Objects.isNull(list)) {
			list = workflowRepository.getAll(pageable);
		}
		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		Page<WorkflowResponseDto> finalResponse = list.map(workflowMapper::convertEntityToResponseDTO);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	private Page<WorkflowEntity> getByFilter(Long categoryId, Long subCategoryId, String issueDetails, Boolean status, Pageable pageable){
		Page<WorkflowEntity> list = null;
		if (Objects.nonNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.nonNull(issueDetails) && Objects.nonNull(status)) {
			list = workflowRepository.getByCategoryActualPSubcategoryAndStatus(categoryId, subCategoryId, issueDetails, status, pageable);
		} 
		if (Objects.isNull(list) && Objects.nonNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.nonNull(issueDetails) &&  Objects.isNull(status)) {
			list = workflowRepository.getByCategorySubcategoryAndActualP(categoryId, subCategoryId, issueDetails, pageable);
		} 
		if (Objects.isNull(list) && Objects.nonNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.nonNull(status) && Objects.isNull(issueDetails)) {
			list = workflowRepository.getByCategorySubcategoryAndStatus(categoryId, subCategoryId, status, pageable);
		}
		
		if (Objects.isNull(list) && Objects.nonNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.isNull(status) && Objects.isNull(issueDetails)) {
			list = workflowRepository.getByCategoryAndSubcategory(categoryId, subCategoryId, pageable);
		}
		if (Objects.isNull(list) && Objects.nonNull(categoryId) && Objects.isNull(subCategoryId) && Objects.nonNull(status) && Objects.isNull(issueDetails)) {
			list = workflowRepository.getByCategoryIdAndStatus(categoryId, status, pageable);
		}
		if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.nonNull(status) && Objects.isNull(issueDetails)) {
			list = workflowRepository.getBySubCategoryIdAndStatus(subCategoryId, status, pageable);
		}
		if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.isNull(subCategoryId) && Objects.nonNull(status) && Objects.nonNull(issueDetails)) {
			list = workflowRepository.getByActualPAndStatus(issueDetails, status, pageable);
		}
		if (Objects.isNull(list) && Objects.nonNull(categoryId) && Objects.isNull(subCategoryId) && Objects.isNull(status) && Objects.isNull(issueDetails)) {
			list = workflowRepository.getByCategoryId(categoryId, pageable);
		}
		if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.isNull(status) && Objects.isNull(issueDetails)) {
			list = workflowRepository.getBySubCategoryId(subCategoryId, pageable);
		}if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.isNull(subCategoryId) && Objects.nonNull(status) && Objects.isNull(issueDetails)) {
			list = workflowRepository.getByStatus(status, pageable);
		}
		if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.isNull(subCategoryId) && Objects.isNull(status) && Objects.nonNull(issueDetails)) {
			list = workflowRepository.getByActualProblem(issueDetails, pageable);
		}
		return list;
	}
	
	@Override
	public GenericResponse getAllActive() {
		List<WorkflowEntity> list = workflowRepository.findByIsActiveOrderByModifiedDateDesc(Boolean.TRUE);
		if (CollectionUtils.isEmpty(list)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<WorkflowResponseDto> responseDto = list.stream()
				.map(workflowMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(responseDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getSla(Long categoryId, Long subcategoryId, Long issueDetailsId) {
		List<WorkflowEntity> list = workflowRepository.findByCategory_idAndSubCategory_idAndIssueDetails_id(categoryId,subcategoryId,issueDetailsId);
		if (CollectionUtils.isEmpty(list)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<WorkflowResponseDto> responseDto = list.stream()
				.map(workflowMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(responseDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	

}
