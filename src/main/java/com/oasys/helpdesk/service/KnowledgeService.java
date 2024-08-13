package com.oasys.helpdesk.service;

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.KnowledgeBaseRequestDTO;
import com.oasys.helpdesk.dto.KnowledgeBaseResponseDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.entity.KnowledgeBase;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.repository.KnowledgeRepository;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.oasys.helpdesk.constant.Constant.*;
import static com.oasys.helpdesk.service.KnowledgeServiceSupport.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.StringUtils.isEmpty;


@Service
@Log4j2
public class KnowledgeService {

	@Autowired
	private KnowledgeRepository helpDeskKnowledgeRepository;
	
	@Autowired
	private KnowledgeServiceSupport knowledgeServiceSupport;
	
	@Autowired
	private PaginationMapper paginationMapper;

	public GenericResponse getKnowledgeBaseById(Long id) {
		Optional<KnowledgeBase> entityMasterTypeOptional = helpDeskKnowledgeRepository.findById(id);
		if (entityMasterTypeOptional.isPresent())
			return Library.getSuccessfulResponse(knowledgeServiceSupport.toKnowledgeBaseResponseDTO(entityMasterTypeOptional.get()),
					ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
		else
			throw new RecordNotFoundException("Record with id " + id + "not found");
	}

	public GenericResponse getKnowledgeBaseSolutions() {
		return Library.getSuccessfulResponse(KnowledgeServiceSupport.getKnowledgeBaseSolutions(), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	public GenericResponse getAllKnowledgeBase() {
		List<KnowledgeBaseResponseDTO> helpDeskTemplateDTOS = helpDeskKnowledgeRepository
				.findAllByOrderByModifiedDateDesc()				
				.stream()
				.map(k -> knowledgeServiceSupport.toKnowledgeBaseResponseDTO(k))
				.collect(toList());
		if (CollectionUtils.isEmpty(helpDeskTemplateDTOS)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}

		return Library.getSuccessfulResponse(helpDeskTemplateDTOS,
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}

	public GenericResponse getByCategoryAndSubCategoryAndIssueDetailsAndStatus(PaginationRequestDTO paginationDto) {

		Pageable pageable;
		Page<KnowledgeBase> list;
		Long category = null;
		Long subCategory = null;
		String knowledgeStatus = null;
		Long issueDetails = null;
		String issueDetailsname=null;

		if (paginationDto.getSortOrder().equalsIgnoreCase(ASC)) {
			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
					Sort.by(Sort.Direction.ASC, paginationDto.getSortField()));
		} else {
			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
					Sort.by(Sort.Direction.DESC, paginationDto.getSortField()));
		}
		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get(CATEGORY_ID))
					&& !paginationDto.getFilters().get(CATEGORY_ID).toString().trim().isEmpty()) {
				category = Long.valueOf(paginationDto.getFilters().get(CATEGORY_ID).toString());
			}

			if (Objects.nonNull(paginationDto.getFilters().get(SUB_CATEGORY_ID))
					&& !paginationDto.getFilters().get(SUB_CATEGORY_ID).toString().trim().isEmpty()) {
				subCategory = Long.valueOf(paginationDto.getFilters().get(SUB_CATEGORY_ID).toString());
			}

			if (Objects.nonNull(paginationDto.getFilters().get("issueDetailsId"))
					&& !paginationDto.getFilters().get("issueDetailsId").toString().trim().isEmpty()) {
				issueDetails = Long.valueOf(paginationDto.getFilters().get("issueDetailsId").toString());
			}
			

			if (Objects.nonNull(paginationDto.getFilters().get("issueDetails"))
					&& !paginationDto.getFilters().get("issueDetails").toString().trim().isEmpty()) {
				issueDetailsname = String.valueOf(paginationDto.getFilters().get("issueDetails").toString());
			}
			

			if (Objects.nonNull(paginationDto.getFilters().get(STATUS))
					&& !paginationDto.getFilters().get(STATUS).toString().trim().isEmpty()) {
				knowledgeStatus = String.valueOf(paginationDto.getFilters().get(STATUS).toString());
			}
		}
		list = getByFilter(category, subCategory, issueDetails, issueDetailsname,knowledgeStatus, pageable);
		if (isNull(list)) {
			list = helpDeskKnowledgeRepository.getAll(pageable);
		}
		if (isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		Page<KnowledgeBaseResponseDTO> finalResponse = list.map(k -> knowledgeServiceSupport.toKnowledgeBaseResponseDTO(k));
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	private Page<KnowledgeBase> getByFilter(Long category, Long subCategory, Long issueDetails,String issueDetailsname, String knowledgeStatus, Pageable pageable) {
		Page<KnowledgeBase> list = null;
		knowledgeStatus = nonNull(knowledgeStatus) ? knowledgeStatus.toUpperCase() : knowledgeStatus;
		if (Objects.nonNull(category) && Objects.nonNull(subCategory) && Objects.nonNull(issueDetails) && Objects.nonNull(knowledgeStatus)) {
			list = helpDeskKnowledgeRepository.getByCategoryAndSubCategoryAndIssueDetailsAndStatus(category, subCategory, issueDetails, knowledgeStatus.toUpperCase(), pageable);
		}
		
		
		if (Objects.nonNull(category) && Objects.nonNull(subCategory) && Objects.nonNull(issueDetails) && Objects.isNull(knowledgeStatus)) {
			list = helpDeskKnowledgeRepository.getByCategoryAndSubCategoryAndIssueDetails(category, subCategory, issueDetails, pageable);
		}

		if (Objects.nonNull(category) && Objects.nonNull(subCategory) && Objects.isNull(issueDetails) && Objects.nonNull(knowledgeStatus)) {
			list = helpDeskKnowledgeRepository.getByCategoryAndSubCategoryAndStatus(category, subCategory, knowledgeStatus.toUpperCase(), pageable);
		}
		if (Objects.nonNull(category) && Objects.nonNull(subCategory) && Objects.isNull(issueDetails) && Objects.isNull(issueDetailsname) &&Objects.isNull(knowledgeStatus)) {
			list = helpDeskKnowledgeRepository.getByCategoryAndSubCategory(category, subCategory, pageable);
		}
		
		if (Objects.nonNull(category) && Objects.nonNull(subCategory) && Objects.isNull(issueDetails)&& Objects.isNull(issueDetailsname) && Objects.isNull(knowledgeStatus)) {
			list = helpDeskKnowledgeRepository.getByCategoryAndSubCategory(category, subCategory, pageable);
		}

		if (Objects.nonNull(category) && Objects.isNull(subCategory) && Objects.isNull(issueDetails) && Objects.nonNull(knowledgeStatus)) {
			list = helpDeskKnowledgeRepository.getByCategoryAndStatus(category, knowledgeStatus.toUpperCase(), pageable);
		}

		if (Objects.nonNull(category) && Objects.isNull(subCategory) && Objects.nonNull(issueDetails) && Objects.isNull(knowledgeStatus)) {
			list = helpDeskKnowledgeRepository.getByCategoryAndIssueDetails(category, issueDetails, pageable);
		}

		if (Objects.isNull(category) && Objects.nonNull(subCategory) && Objects.isNull(issueDetails) && Objects.nonNull(knowledgeStatus)) {
			list = helpDeskKnowledgeRepository.getBySubCategoryAndStatus(subCategory, knowledgeStatus.toUpperCase(),pageable);
		}

		if (Objects.isNull(category) && Objects.nonNull(subCategory) && Objects.nonNull(issueDetails) && Objects.isNull(knowledgeStatus)) {
			list = helpDeskKnowledgeRepository.getBySubCategoryAndIssueDetails(subCategory, issueDetails, pageable);
		}

		if (Objects.isNull(category) && Objects.isNull(subCategory) && Objects.nonNull(issueDetails) && Objects.nonNull(knowledgeStatus)) {
			list = helpDeskKnowledgeRepository.getByIssueDetailsAndStatus(issueDetails, knowledgeStatus.toUpperCase(), pageable);
		}

		if (Objects.nonNull(category) && Objects.isNull(subCategory) && Objects.isNull(issueDetails) && Objects.isNull(knowledgeStatus)) {
			list = helpDeskKnowledgeRepository.getByCategory(category, pageable);
		}

		if (Objects.isNull(category) && Objects.nonNull(subCategory) && Objects.isNull(issueDetails) && Objects.isNull(knowledgeStatus)) {
			list = helpDeskKnowledgeRepository.getBySubCategory(subCategory, pageable);
		}

		if (Objects.isNull(category) && Objects.isNull(subCategory) && Objects.nonNull(issueDetails) && Objects.isNull(knowledgeStatus)) {
			list = helpDeskKnowledgeRepository.getByIssueDetails(issueDetails, pageable);
		}

		if (Objects.isNull(category) && Objects.isNull(subCategory) && Objects.isNull(issueDetails) && Objects.nonNull(knowledgeStatus)) {
			list = helpDeskKnowledgeRepository.getByStatus(knowledgeStatus.toUpperCase(), pageable);
		}
		
		if (Objects.nonNull(category) && Objects.nonNull(subCategory) && Objects.nonNull(issueDetailsname)
				&& Objects.isNull(issueDetails) && Objects.isNull(knowledgeStatus)) {
			list = helpDeskKnowledgeRepository.getByCategoryAndSubCategoryAndIssueDetailsname(category, subCategory,
					issueDetailsname, pageable);
		}

		return list;
	}

	public GenericResponse createKnowledge(KnowledgeBaseRequestDTO knowledgeRequestDto) {
		Optional<KnowledgeBase> entityMasterTypeEntity = helpDeskKnowledgeRepository
				.findByKbId(knowledgeRequestDto.getKbId());
		if (entityMasterTypeEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[]{KB_ID}));
		}

		GenericResponse mandatoryCheck = performMandatoryCheck(knowledgeRequestDto);
		if (mandatoryCheck != null)
			return mandatoryCheck;

		KnowledgeBase entity = helpDeskKnowledgeRepository.save(knowledgeServiceSupport.toKnowledgeBaseEntity(knowledgeRequestDto));
		return Library.getSuccessfulResponse(knowledgeServiceSupport.toKnowledgeBaseResponseDTO(entity), ErrorCode.CREATED.getErrorCode(), ErrorMessages.RECORED_CREATED);
	}

	public GenericResponse updateKnowledge(KnowledgeBaseRequestDTO requestDTO) {

		if (isEmpty(requestDTO.getKbId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[]{KB_ID}));
		}

		GenericResponse mandatoryCheck = performMandatoryCheck(requestDTO);
		if (mandatoryCheck != null)
			return mandatoryCheck;

		Optional<KnowledgeBase> knowledgeBaseOptional = helpDeskKnowledgeRepository.findByKbId(requestDTO.getKbId());
		if (!knowledgeBaseOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage(new Object[]{KB_ID}));
		}
		KnowledgeBase knowledgeBase = helpDeskKnowledgeRepository.save(knowledgeServiceSupport.toKnowledgeBaseUpdateEntity(requestDTO, knowledgeBaseOptional.get()));
		return Library.getSuccessfulResponse(knowledgeServiceSupport.toKnowledgeBaseResponseDTO(knowledgeBase), ErrorCode.CREATED.getErrorCode(), ErrorMessages.RECORED_UPDATED);

	}

	public GenericResponse getUniqueKbId() {
		long code = getUniqueCode();
		while (true) {
			if (!isUniqueEntityTypeCode(code)) {
				code = getUniqueCode();
			} else
				break;
		}
		return Library.getSuccessfulResponse(code, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	protected boolean isUniqueEntityTypeCode(long uniqueCode) {
		Optional<KnowledgeBase> byEntityCode = helpDeskKnowledgeRepository.findById(uniqueCode);
		return byEntityCode.isPresent() ? Boolean.FALSE : Boolean.TRUE;
	}

	public GenericResponse getKnowledgeBaseCountByStatus() {
		final Map<String, String> result = helpDeskKnowledgeRepository.getKnowledgeCountByStatus()
				.stream()
				.collect(Collectors.toMap(s -> String.valueOf(s.get("status")),
						s -> String.valueOf(s.get("COUNT"))));
		return Library.getSuccessfulResponse(result,
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}
	
	public GenericResponse updateResolvedCount(Long id) {
		Optional<KnowledgeBase> KnowledgeBaseOptional = helpDeskKnowledgeRepository.findById(id);
		if(!KnowledgeBaseOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[]{Constant.ID}));
		}
		KnowledgeBase knowledgeBase = KnowledgeBaseOptional.get();
		Integer count = knowledgeBase.getCount();
		if(Objects.isNull(count)) {
			count = 0;
		}
		knowledgeBase.setCount(count+1);
		helpDeskKnowledgeRepository.save(knowledgeBase);
		return Library.getSuccessfulResponse(knowledgeServiceSupport.toKnowledgeBaseResponseDTO(knowledgeBase), ErrorCode.CREATED.getErrorCode(), ErrorMessages.RECORED_UPDATED);

	}

	public GenericResponse getSolutionByIssueDetailsId(Long id) {
		//KnowledgeBaseResponseDTO response = new KnowledgeBaseResponseDTO();
		Integer solutionCount = helpDeskKnowledgeRepository.getByIssueDetailsId(id);
		if (Objects.nonNull(solutionCount)) {
			if(solutionCount != 0)
			return Library.getSuccessfulResponse(solutionCount,
					ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
			else
				return Library.getFailResponseCode(
						ErrorCode.FAILURE_RESPONSE.getErrorCode(), "No Solution Found For Id " + id);	
		}
		else {
			return Library.getFailResponseCode(
					ErrorCode.FAILURE_RESPONSE.getErrorCode(), ErrorMessages.NO_RECORD_FOUND);
		}
	}

	
	
}
