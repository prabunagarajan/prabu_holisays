package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.ACTUAL_PROBLEM_TYPE;
import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.CATEGORYID;
import static com.oasys.helpdesk.constant.Constant.CODE;
import static com.oasys.helpdesk.constant.Constant.ID;
import static com.oasys.helpdesk.constant.Constant.STATUS;
import static com.oasys.helpdesk.constant.Constant.SUB_CATEGORY_ID;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.entity.ActualProblem;
import com.oasys.helpdesk.mapper.ActualProblemMapper;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.repository.AcutalProblemRepository;
import com.oasys.helpdesk.request.ActualProblemRequestDto;
import com.oasys.helpdesk.response.ActualProblemResponseDto;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.RandomUtil;

import lombok.extern.log4j.Log4j2;



@Service
@Log4j2
public class ActualProblemService {

	@Autowired
	AcutalProblemRepository helpDeskAcutalProblemRepository;
	
	@Autowired
	CommonDataController commonDataController;
	
	@Autowired
	ActualProblemMapper actualProblemMapper;
	
	@Autowired
	EntityManager entityManager;
	
	@Autowired
	private PaginationMapper paginationMapper;
	
	public static final String ACTUAL_PROBLEM= "Actual Problem";
	public static final String CATEGORY= "category.id";
	public static final String SUBCATEGORY= "subCategory.id";

	public GenericResponse getAllActualProblem() {
				
		List<ActualProblem> HelpDeskActualProblemList = helpDeskAcutalProblemRepository.findAllByOrderByModifiedDateDesc();
		
		//List<ActualProblem> HelpDeskActualProblemList = helpDeskAcutalProblemRepository.findAllByIsActiveOrderByModifiedDateDesc(true);
		
		if (CollectionUtils.isEmpty(HelpDeskActualProblemList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<ActualProblemResponseDto> assetBrandResponseList = HelpDeskActualProblemList.stream()
				.map(actualProblemMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(assetBrandResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	public GenericResponse getActualProblemById(Long id) throws RecordNotFoundException {		
		
		Optional<ActualProblem> helpDeskActualEntity = helpDeskAcutalProblemRepository.findById(id);
		if (!helpDeskActualEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(actualProblemMapper.convertEntityToResponseDTO(helpDeskActualEntity.get()), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	public GenericResponse createActualProblem(ActualProblemRequestDto actualProblemRequestDto)
	{
				
		Optional<ActualProblem> actOptionalualProblemEntity = helpDeskAcutalProblemRepository
				.findByCodeIgnoreCaseAndActualProblem(actualProblemRequestDto.getCode(),actualProblemRequestDto.getActualproblem());
		if (actOptionalualProblemEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { CODE }));
		}
		
		
		actOptionalualProblemEntity = helpDeskAcutalProblemRepository.findBySubcategoryAndActualProblem(
				actualProblemRequestDto.getSubCategoryId(), actualProblemRequestDto.getActualproblem().toUpperCase());
		if (actOptionalualProblemEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.RECORD_ALREADY_EXISTS.getMessage());
		}
		actualProblemRequestDto.setId(null);
		ActualProblem entity = actualProblemMapper.convertRequestDTOToEntity(actualProblemRequestDto,null);
		helpDeskAcutalProblemRepository.save(entity);
		return Library.getSuccessfulResponse(actualProblemMapper.convertEntityToResponseDTO(entity), ErrorCode.CREATED.getErrorCode(), ErrorMessages.RECORED_CREATED);

	} 
	
	
	
	public GenericResponse searchByFilter(PaginationRequestDTO paginationDto) {
		Pageable pageable = null;
		Page<ActualProblem> list = null;
		Long categoryId = null;
		String actualProblem = null;
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
			if (Objects.nonNull(paginationDto.getFilters().get(ACTUAL_PROBLEM_TYPE))
					&& !paginationDto.getFilters().get(ACTUAL_PROBLEM_TYPE).toString().trim().isEmpty()) {
				actualProblem = String.valueOf(paginationDto.getFilters().get(ACTUAL_PROBLEM_TYPE).toString());
			}
			if (Objects.nonNull(paginationDto.getFilters().get(STATUS))
					&& !paginationDto.getFilters().get(STATUS).toString().trim().isEmpty()) {
				status = Boolean.valueOf(paginationDto.getFilters().get(STATUS).toString());
			}
		}
		list = getByFilter(categoryId, subCategoryId, actualProblem,status, pageable);
		if (Objects.isNull(list)) {
			list = helpDeskAcutalProblemRepository.getAll(pageable);
		}
		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		Page<ActualProblemResponseDto> finalResponse = list.map(actualProblemMapper::convertEntityToResponseDTO);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	private Page<ActualProblem> getByFilter(Long categoryId, Long subCategoryId, String actualProblem, Boolean status, Pageable pageable){
		Page<ActualProblem> list = null;
		if (Objects.nonNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.nonNull(actualProblem) && Objects.nonNull(status)) {
			list = helpDeskAcutalProblemRepository.getByCategoryActualPSubcategoryAndStatus(categoryId, subCategoryId, actualProblem, status, pageable);
		} 
		if (Objects.isNull(list) && Objects.nonNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.nonNull(actualProblem) &&  Objects.isNull(status)) {
			list = helpDeskAcutalProblemRepository.getByCategorySubcategoryAndActualP(categoryId, subCategoryId, actualProblem, pageable);
		} 
		if (Objects.isNull(list) && Objects.nonNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.nonNull(status) && Objects.isNull(actualProblem)) {
			list = helpDeskAcutalProblemRepository.getByCategorySubcategoryAndStatus(categoryId, subCategoryId, status, pageable);
		}
		
		if (Objects.isNull(list) && Objects.nonNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.isNull(status) && Objects.isNull(actualProblem)) {
			list = helpDeskAcutalProblemRepository.getByCategoryAndSubcategory(categoryId, subCategoryId, pageable);
		}
		if (Objects.isNull(list) && Objects.nonNull(categoryId) && Objects.isNull(subCategoryId) && Objects.nonNull(status) && Objects.isNull(actualProblem)) {
			list = helpDeskAcutalProblemRepository.getByCategoryIdAndStatus(categoryId, status, pageable);
		}
		if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.nonNull(status) && Objects.isNull(actualProblem)) {
			list = helpDeskAcutalProblemRepository.getBySubCategoryIdAndStatus(subCategoryId, status, pageable);
		}
		if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.isNull(subCategoryId) && Objects.nonNull(status) && Objects.nonNull(actualProblem)) {
			list = helpDeskAcutalProblemRepository.getByActualPAndStatus(actualProblem, status, pageable);
		}
		if (Objects.isNull(list) && Objects.nonNull(categoryId) && Objects.isNull(subCategoryId) && Objects.isNull(status) && Objects.isNull(actualProblem)) {
			list = helpDeskAcutalProblemRepository.getByCategoryId(categoryId, pageable);
		}
		if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.isNull(status) && Objects.isNull(actualProblem)) {
			list = helpDeskAcutalProblemRepository.getBySubCategoryId(subCategoryId, pageable);
		}if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.isNull(subCategoryId) && Objects.nonNull(status) && Objects.isNull(actualProblem)) {
			list = helpDeskAcutalProblemRepository.getByStatus(status, pageable);
		}
		if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.isNull(subCategoryId) && Objects.isNull(status) && Objects.nonNull(actualProblem)) {
			list = helpDeskAcutalProblemRepository.getByActualProblem(actualProblem, pageable);
		}
		return list;
	}

	
	public GenericResponse editActualProblem(ActualProblemRequestDto requestDTO)  	{
			
		if(Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		try {
			Optional<ActualProblem> actualProblemEntity = helpDeskAcutalProblemRepository
					.findBySubcategoryAndActualProblemNotInId(requestDTO.getSubCategoryId(), requestDTO.getId(),
							requestDTO.getActualproblem().toUpperCase());
			if (actualProblemEntity.isPresent()
					&& requestDTO.getActualproblem().equalsIgnoreCase(actualProblemEntity.get().getActualProblem())) {
				return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
						ResponseMessageConstant.RECORD_ALREADY_EXISTS.getMessage());
			}
		} catch (Exception e) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.RECORD_ALREADY_EXISTS.getMessage());
		}
		
		
		Optional<ActualProblem> actualProblemEntity = helpDeskAcutalProblemRepository.findById(requestDTO.getId());
		if (!actualProblemEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		requestDTO.setCode(actualProblemEntity.get().getCode());
		
		ActualProblem entity = actualProblemMapper.convertRequestDTOToEntity(requestDTO, actualProblemEntity.get());
		helpDeskAcutalProblemRepository.save(entity);
		return Library.getSuccessfulResponse(actualProblemMapper.convertEntityToResponseDTO(entity), ErrorCode.CREATED.getErrorCode(),ErrorMessages.RECORED_UPDATED);

	} 
	
	
	
	public GenericResponse getCode() {
		MenuPrefix prefix = MenuPrefix.getType(ACTUAL_PROBLEM);
		String code = prefix.toString() + RandomUtil.getRandomNumber();
		while (true) {
			Optional<ActualProblem> actualProblemEntity = helpDeskAcutalProblemRepository.findByCodeIgnoreCase(code);
			if (actualProblemEntity.isPresent()) {
				code = prefix.toString() + RandomUtil.getRandomNumber();
			} else {
				break;
			}
		}
		return Library.getSuccessfulResponse(code, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	
	public GenericResponse getAllActive() {
		List<ActualProblem> actualProblems = helpDeskAcutalProblemRepository.findByIsActiveOrderByModifiedDateDesc(Boolean.TRUE);
		if (CollectionUtils.isEmpty(actualProblems)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<ActualProblemResponseDto> actualProblemResponseDtos = actualProblems.stream()
				.map(actualProblemMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(actualProblemResponseDtos, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	public GenericResponse getById(Long categoryId, Long subcategoryId) {
		List<ActualProblem> actualProblems = helpDeskAcutalProblemRepository.findByCategoryAndSubCategory(categoryId,subcategoryId);
		if (CollectionUtils.isEmpty(actualProblems)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<ActualProblemResponseDto> actualProblemResponseDtos = actualProblems.stream()
				.map(actualProblemMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(actualProblemResponseDtos, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
}
