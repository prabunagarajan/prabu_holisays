package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.CATEGORYID;
import static com.oasys.helpdesk.constant.Constant.CODE;
import static com.oasys.helpdesk.constant.Constant.ID;
import static com.oasys.helpdesk.constant.Constant.ISSUE_DETAILS;
import static com.oasys.helpdesk.constant.Constant.SLA_ALREADY_EXISTS;
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

import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.entity.SlaMasterEntity;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.mapper.SlaMasterMapper;
import com.oasys.helpdesk.repository.SlaMasterRepository;
import com.oasys.helpdesk.request.SlaMasterRequestDto;
import com.oasys.helpdesk.response.SlaMasterResponseDto;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.RandomUtil;

@Service
public class SlaMasterServiceImpl implements SlaMasterService{

	@Autowired
	SlaMasterRepository slaMasterRepository;
	
	@Autowired
	SlaMasterMapper slaMasterMapper;

	
	@Autowired
	private PaginationMapper paginationMapper;
	
	public static final String SLA= "Sla";
	public static final String CATEGORY= "category.id";
	public static final String SUBCATEGORY= "subCategory.id";
	
	@Override
	public GenericResponse getAll() {
		List<SlaMasterEntity> slaList = slaMasterRepository.findAllByOrderByModifiedDateDesc();
		
		
		if (CollectionUtils.isEmpty(slaList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());                                                 
		}
		List<SlaMasterResponseDto> slaResponseDtoList = slaList.stream()
				.map(slaMasterMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(slaResponseDtoList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse create(SlaMasterRequestDto slaMasterRequestDto) {
		
		Optional<SlaMasterEntity> slaOptional = slaMasterRepository.findByCodeIgnoreCase(slaMasterRequestDto.getCode());
			if(slaOptional.isPresent())
			{
					return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
							ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { CODE }));
				
			}
		if (Objects.nonNull(slaMasterRequestDto.getIssueDetailsId())) {
			slaOptional = slaMasterRepository.getByIssueDetailsId(slaMasterRequestDto.getIssueDetailsId());
			if (slaOptional.isPresent()) {
				return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(), Constant.SLA_ALREADY_EXISTS);
			}
		}
			slaMasterRequestDto.setId(null);
			//SlaMasterEntity slaMasterEntity = commonUtil.modalMap(slaMasterRequestDto, SlaMasterEntity.class);
			SlaMasterEntity entity = slaMasterMapper.convertRequestDTOToEntity(slaMasterRequestDto,null);
			slaMasterRepository.save(entity);
			
			return Library.getSuccessfulResponse(slaMasterMapper.convertEntityToResponseDTO(entity), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_CREATED);
	}

	@Override
	public GenericResponse getById(Long id) {
		Optional<SlaMasterEntity> slaMasterEntity = slaMasterRepository.findById(id);
		if (!slaMasterEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(slaMasterMapper.convertEntityToResponseDTO(slaMasterEntity.get()), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse update(SlaMasterRequestDto requestDTO) {
		if(Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		Optional<SlaMasterEntity> slaOptional = null;
		if (Objects.nonNull(requestDTO.getIssueDetailsId())) {
			slaOptional = slaMasterRepository.getByIssueDetailsIdNotInId(requestDTO.getIssueDetailsId(), requestDTO.getId());
			if (slaOptional.isPresent()) {
				return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(), Constant.SLA_ALREADY_EXISTS);
			}
		}
		
		 slaOptional = slaMasterRepository.findById(requestDTO.getId());
		if (!slaOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		SlaMasterEntity slaEntity = slaOptional.get();
		
		SlaMasterEntity entity = slaMasterMapper.convertRequestDTOToEntity(requestDTO,slaEntity);
		//slaEntity.setActive(requestDTO.isActive());
		slaMasterRepository.save(entity);
		return Library.getSuccessfulResponse(slaMasterMapper.convertEntityToResponseDTO(slaEntity), ErrorCode.CREATED.getErrorCode(),ErrorMessages.RECORED_UPDATED);
	
	}

	@Override
	public GenericResponse getCode() {
		MenuPrefix prefix = MenuPrefix.getType(SLA);
		String code = prefix.toString() + RandomUtil.getRandomNumber();
		while (true) {
			Optional<SlaMasterEntity> slaEntity = slaMasterRepository.findByCodeIgnoreCase(code);
			if (slaEntity.isPresent()) {
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
		Page<SlaMasterEntity> list = null;
		Long categoryId = null;
		Long issueDetails = null;
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
			if (Objects.nonNull(paginationDto.getFilters().get(ISSUE_DETAILS))
					&& !paginationDto.getFilters().get(ISSUE_DETAILS).toString().trim().isEmpty()) {
				issueDetails = Long.valueOf(paginationDto.getFilters().get(ISSUE_DETAILS).toString());
			}
			if (Objects.nonNull(paginationDto.getFilters().get(STATUS))
					&& !paginationDto.getFilters().get(STATUS).toString().trim().isEmpty()) {
				status = Boolean.valueOf(paginationDto.getFilters().get(STATUS).toString());
			}
		}
		list = getByFilter(categoryId, subCategoryId, issueDetails, status, pageable);
		if (Objects.isNull(list)) {
			list = slaMasterRepository.getAll(pageable);
		}
		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		Page<SlaMasterResponseDto> finalResponse = list.map(slaMasterMapper::convertEntityToResponseDTO);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	private Page<SlaMasterEntity> getByFilter(Long categoryId, Long subCategoryId, Long issueDetails, Boolean status, Pageable pageable){
		Page<SlaMasterEntity> list = null;
		if (Objects.nonNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.nonNull(issueDetails) && Objects.nonNull(status)) {
			list = slaMasterRepository.getByCategoryIssueDSubcategoryAndStatus(categoryId, subCategoryId, issueDetails, status, pageable);
		} 
		if (Objects.isNull(list) && Objects.nonNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.nonNull(issueDetails) &&  Objects.isNull(status)) {
			list = slaMasterRepository.getByCategorySubcategoryAndIssueD(categoryId, subCategoryId, issueDetails, pageable);
		} 
		if (Objects.isNull(list) && Objects.nonNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.nonNull(status) && Objects.isNull(issueDetails)) {
			list = slaMasterRepository.getByCategorySubcategoryAndStatus(categoryId, subCategoryId, status, pageable);
		}
		
		if (Objects.isNull(list) && Objects.nonNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.isNull(status) && Objects.isNull(issueDetails)) {
			list = slaMasterRepository.getByCategoryAndSubcategory(categoryId, subCategoryId, pageable);
		}
		if (Objects.isNull(list) && Objects.nonNull(categoryId) && Objects.isNull(subCategoryId) && Objects.nonNull(status) && Objects.isNull(issueDetails)) {
			list = slaMasterRepository.getByCategoryIdAndStatus(categoryId, status, pageable);
		}
		if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.nonNull(status) && Objects.isNull(issueDetails)) {
			list = slaMasterRepository.getBySubCategoryIdAndStatus(subCategoryId, status, pageable);
		}
		if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.isNull(subCategoryId) && Objects.nonNull(status) && Objects.nonNull(issueDetails)) {
			list = slaMasterRepository.getByIssueDAndStatus(issueDetails, status, pageable);
		}
		if (Objects.isNull(list) && Objects.nonNull(categoryId) && Objects.isNull(subCategoryId) && Objects.isNull(status) && Objects.isNull(issueDetails)) {
			list = slaMasterRepository.getByCategoryId(categoryId, pageable);
		}
		if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.isNull(status) && Objects.isNull(issueDetails)) {
			list = slaMasterRepository.getBySubCategoryId(subCategoryId, pageable);
		}if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.isNull(subCategoryId) && Objects.nonNull(status) && Objects.isNull(issueDetails)) {
			list = slaMasterRepository.getByStatus(status, pageable);
		}
		if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.isNull(subCategoryId) && Objects.isNull(status) && Objects.nonNull(issueDetails)) {
			list = slaMasterRepository.getByIssueD(issueDetails, pageable);
		}
		return list;
	}
	
	@Override
	public GenericResponse getAllActive() {
		List<SlaMasterEntity> list = slaMasterRepository.findByIsActiveOrderByModifiedDateDesc(Boolean.TRUE);
		if (CollectionUtils.isEmpty(list)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<SlaMasterResponseDto> responseDto = list.stream()
				.map(slaMasterMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(responseDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getById(Long subCategoryId, Long categoryId) {
		Optional<SlaMasterEntity> list = slaMasterRepository.getByCategoryAndSubcategory(subCategoryId,categoryId);
		if (!list.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(slaMasterMapper.convertEntityToResponseDTO(list.get()), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getSla(Long categoryId, Long subcategoryId, Long issueDetailsId) {
		List<SlaMasterEntity> list = slaMasterRepository.findByCategory_idAndSubCategory_idAndIssueDetails_id(categoryId,subcategoryId,issueDetailsId);
		if (CollectionUtils.isEmpty(list)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<SlaMasterResponseDto> responseDto = list.stream()
				.map(slaMasterMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(responseDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

}
