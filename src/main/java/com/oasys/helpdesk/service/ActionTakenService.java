package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.ACTION_TAKEN;
import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.CATEGORYID;
import static com.oasys.helpdesk.constant.Constant.CODE;
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
import com.oasys.helpdesk.entity.ActionTaken;
import com.oasys.helpdesk.mapper.ActionTakenMapper;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.repository.ActionTakenRepository;
import com.oasys.helpdesk.repository.AcutalProblemRepository;
import com.oasys.helpdesk.request.ActionTakenRequestDto;
import com.oasys.helpdesk.response.ActionTakenResponseDto;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.RandomUtil;

import lombok.extern.log4j.Log4j2;

  

@Service
@Log4j2
public class ActionTakenService {

	@Autowired
	ActionTakenRepository helpDeskActionTakenRepository;
	
	@Autowired
	AcutalProblemRepository helpDeskAcutalProblemRepository;
	
	@Autowired
	CommonDataController commonDataController ;
	
	@Autowired
	EntityManager entityManager;
	
	@Autowired
	ActionTakenMapper actionTakenMapper;
	
	@Autowired
	PaginationMapper paginationMapper;
	
	public static final String CATEGORY= "category.id";
	public static final String SUBCATEGORY= "subCategory.id";

	public static final String ACTION_TAKEN_TYPE= "Action Taken";
	
	public GenericResponse getAllActionTaken(AuthenticationDTO authenticationDTO) {
		
		List<ActionTaken> HelpDeskActionTakenList = helpDeskActionTakenRepository.findAllByOrderByModifiedDateDesc();
	//	List<ActionTaken> HelpDeskActionTakenList = helpDeskActionTakenRepository.findAllByIsActiveOrderByModifiedDateDesc(true);
		if (CollectionUtils.isEmpty(HelpDeskActionTakenList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<ActionTakenResponseDto> actionTakenResponseList = HelpDeskActionTakenList.stream()
				.map(actionTakenMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(actionTakenResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	public GenericResponse getActionTakenById(Long id)  {
				
		Optional<ActionTaken> helpDeskActionTakenEntity = helpDeskActionTakenRepository.findById(id);
		if (!helpDeskActionTakenEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(actionTakenMapper.convertEntityToResponseDTO(helpDeskActionTakenEntity.get()), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	/*
	 * private ActionTakenResponseDto convertHelpDeskActionTakenToDto(ActionTaken
	 * helpDeskActionTaken) {
	 * 
	 * ActionTakenResponseDto helpDeskActionTakenResponseDto = new
	 * ActionTakenResponseDto();
	 * helpDeskActionTakenResponseDto.setId(helpDeskActionTaken.getId());
	 * helpDeskActionTakenResponseDto.setActionTaken(helpDeskActionTaken.
	 * getActionTaken()); if(helpDeskActionTaken.getActualProblem() !=null) {
	 * Optional<ActualProblem> helpDeskActualProblem=
	 * helpDeskAcutalProblemRepository.findById(helpDeskActionTaken.getActualProblem
	 * ().getId());
	 * helpDeskActionTakenResponseDto.setActualProblemName(helpDeskActualProblem.get
	 * ()); }
	 * helpDeskActionTakenResponseDto.setActive(helpDeskActionTaken.isActive());
	 * String createduser=commonDataController.getUserNameById(helpDeskActionTaken.
	 * getCreatedBy()); String
	 * modifieduser=commonDataController.getUserNameById(helpDeskActionTaken.
	 * getModifiedBy()); helpDeskActionTakenResponseDto.setCreatedBy(createduser);
	 * helpDeskActionTakenResponseDto.setCreatedDate(helpDeskActionTaken.
	 * getCreatedDate().toString());
	 * helpDeskActionTakenResponseDto.setModifiedBy(modifieduser);
	 * helpDeskActionTakenResponseDto.setModifiedDate(helpDeskActionTaken.
	 * getModifiedDate().toString());
	 * helpDeskActionTakenResponseDto.setCategoryName(helpDeskActionTaken.
	 * getCategory().getCategoryName());
	 * helpDeskActionTakenResponseDto.setSubCategoryName(helpDeskActionTaken.
	 * getSubCategory().getSubCategoryName()); return
	 * helpDeskActionTakenResponseDto;
	 * 
	 * }
	 */
	
	public GenericResponse getActionTakenByActionProblemId(Long actionproblemid) {
			
		List<ActionTaken> HelpDeskActionTakenList = helpDeskActionTakenRepository.getActionTakenByActionProblemId(actionproblemid);
	        if(HelpDeskActionTakenList==null ||HelpDeskActionTakenList.size()==0 ) {
	        	throw new RecordNotFoundException("No record found");
	        }
	        if (HelpDeskActionTakenList.size() > 0) {
	        	List<ActionTakenResponseDto> actionTakenResponseList = HelpDeskActionTakenList.stream()
						.map(actionTakenMapper::convertEntityToResponseDTO).collect(Collectors.toList());
				return Library.getSuccessfulResponse(actionTakenResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
						ErrorMessages.RECORED_FOUND);
				
			}  else {
				throw new RecordNotFoundException();
			}
		}

	public GenericResponse createActionTaken(AuthenticationDTO authenticationDTO ,ActionTakenRequestDto actionTakenRequestDto) 
	{
		
		if (actionTakenRequestDto != null ) {
			Optional<ActionTaken> actionTakenEntity = helpDeskActionTakenRepository
					.findByCodeIgnoreCase(actionTakenRequestDto.getCode());
			if (actionTakenEntity.isPresent()) {
				return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
						ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { CODE }));
			}
			actionTakenEntity = helpDeskActionTakenRepository.findByActualProblemIdAndActionTaken(
					actionTakenRequestDto.getActualProblemId(), actionTakenRequestDto.getActionTaken().toUpperCase());
			if (actionTakenEntity.isPresent()) {
				return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
						ResponseMessageConstant.RECORD_ALREADY_EXISTS.getMessage());
			}      
	       
	       
	        actionTakenRequestDto.setId(null);
			ActionTaken entity = actionTakenMapper.convertRequestDTOToEntity(actionTakenRequestDto,null);
	       
			helpDeskActionTakenRepository.save(entity);
			
			return Library.getSuccessfulResponse(entity, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_CREATED);
			
		} else {
			throw new RecordNotFoundException();
		}
	} 
	public GenericResponse getAllActionTakenForPending() {
		List<ActionTaken> HelpDeskActionTakenList = helpDeskActionTakenRepository.getAllActionTakenForPending();
		if(HelpDeskActionTakenList==null ||HelpDeskActionTakenList.size()==0 ) {
	        	throw new RecordNotFoundException("No record found");
	    }
		if (HelpDeskActionTakenList.size() > 0) {
			List<ActionTakenResponseDto> actionTakenResponseList = HelpDeskActionTakenList.stream()
					.map(actionTakenMapper::convertEntityToResponseDTO).collect(Collectors.toList());
			return Library.getSuccessfulResponse(actionTakenResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
			
		} else {
			throw new RecordNotFoundException();
		}
	}

	public GenericResponse getActionTakenForPendingById(Long id) throws RecordNotFoundException {
		Optional<ActionTaken> helpDeskActionTaken = helpDeskActionTakenRepository.getActionTakenForPendingById(id);
		
		if (helpDeskActionTaken.isPresent()) {
			return Library.getSuccessfulResponse(actionTakenMapper.convertEntityToResponseDTO(helpDeskActionTaken.get()), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException();
		}
	}
	
	
	public GenericResponse searchByFilter(PaginationRequestDTO paginationDto) {
		Pageable pageable = null;
		Page<ActionTaken> list = null;
		Long categoryId = null;
		String actionTaken = null;
		Long subCategoryId = null;
		Long actualproblemId = null;
		
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
			if (Objects.nonNull(paginationDto.getFilters().get(ACTION_TAKEN))
					&& !paginationDto.getFilters().get(ACTION_TAKEN).toString().trim().isEmpty()) {
				actionTaken = String.valueOf(paginationDto.getFilters().get(ACTION_TAKEN).toString());
			}
			if (Objects.nonNull(paginationDto.getFilters().get(STATUS))
					&& !paginationDto.getFilters().get(STATUS).toString().trim().isEmpty()) {
				status = Boolean.valueOf(paginationDto.getFilters().get(STATUS).toString());
			}
			
			if (Objects.nonNull(paginationDto.getFilters().get("actualProblemId"))
					&& !paginationDto.getFilters().get("actualProblemId").toString().trim().isEmpty()) {
				actualproblemId = Long.valueOf(paginationDto.getFilters().get("actualProblemId").toString());
			}
		}
		list = getByFilter(categoryId, subCategoryId, actionTaken,status,actualproblemId, pageable);
		if (Objects.isNull(list) || list.isEmpty()) {
			list = helpDeskActionTakenRepository.getAll(pageable);
		}
		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		Page<ActionTakenResponseDto> finalResponse = list.map(actionTakenMapper::convertEntityToResponseDTO);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	private Page<ActionTaken> getByFilter(Long categoryId, Long subCategoryId, String actionTaken, Boolean status,Long actualproblemId, Pageable pageable){
		Page<ActionTaken> list = null;
		if (Objects.nonNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.nonNull(actionTaken) && Objects.nonNull(status)) {
			list = helpDeskActionTakenRepository.getByCategoryActionTSubcategoryAndStatus(categoryId, subCategoryId, actionTaken, status, pageable);
		} 
		if (Objects.isNull(list) && Objects.nonNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.nonNull(actionTaken) &&  Objects.isNull(status)) {
			list = helpDeskActionTakenRepository.getByCategorySubcategoryAndActionT(categoryId, subCategoryId, actionTaken, pageable);
		} 
		if (Objects.isNull(list) && Objects.nonNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.nonNull(status) && Objects.isNull(actionTaken)) {
			list = helpDeskActionTakenRepository.getByCategorySubcategoryAndStatus(categoryId, subCategoryId, status, pageable);
		}
		
		if (Objects.isNull(list) && Objects.nonNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.isNull(status) && Objects.isNull(actionTaken)) {
			list = helpDeskActionTakenRepository.getByCategoryAndSubcategory(categoryId, subCategoryId, pageable);
		}
		if (Objects.isNull(list) && Objects.nonNull(categoryId) && Objects.isNull(subCategoryId) && Objects.nonNull(status) && Objects.isNull(actionTaken)) {
			list = helpDeskActionTakenRepository.getByCategoryIdAndStatus(categoryId, status, pageable);
		}
		if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.nonNull(status) && Objects.isNull(actionTaken)) {
			list = helpDeskActionTakenRepository.getBySubCategoryIdAndStatus(subCategoryId, status, pageable);
		}
		if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.isNull(subCategoryId) && Objects.nonNull(status) && Objects.nonNull(actionTaken)) {
			list = helpDeskActionTakenRepository.getByActionTAndStatus(actionTaken, status, pageable);
		}
		if (Objects.isNull(list) && Objects.nonNull(categoryId) && Objects.isNull(subCategoryId) && Objects.isNull(status) && Objects.isNull(actionTaken)) {
			list = helpDeskActionTakenRepository.getByCategoryId(categoryId, pageable);
		}
		if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.isNull(status) && Objects.isNull(actionTaken)) {
			list = helpDeskActionTakenRepository.getBySubCategoryId(subCategoryId, pageable);
		}if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.isNull(subCategoryId) && Objects.nonNull(status) && Objects.isNull(actionTaken)) {
			list = helpDeskActionTakenRepository.getByStatus(status, pageable);
		}
		if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.isNull(subCategoryId) && Objects.isNull(status) && Objects.nonNull(actionTaken)) {
			list = helpDeskActionTakenRepository.getByActionTaken(actionTaken, pageable);
		}
		
		if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.isNull(subCategoryId) && Objects.isNull(status) && Objects.isNull(actionTaken)) {
			list = helpDeskActionTakenRepository.getByActualProblem(actualproblemId, pageable);
		}
		if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.isNull(subCategoryId) && Objects.isNull(status) && Objects.nonNull(actionTaken)) {
			list = helpDeskActionTakenRepository.getByActualProblemAndActionTaken(actualproblemId,actionTaken, pageable);
		}
		
		
		return list;
	}
	
	public GenericResponse editActionTaken(AuthenticationDTO authenticationDTO ,ActionTakenRequestDto actionTakenRequestDto) {
		ActionTaken helpDeskActionTaken= helpDeskActionTakenRepository.getById(actionTakenRequestDto.getId());
		if (Objects.isNull(helpDeskActionTaken.getId())) {
			throw new RecordNotFoundException();
			
		} 
		Optional<ActionTaken> actionTakenEntity = helpDeskActionTakenRepository.findByActualProblemIdAndActionTakenNotInId(
				actionTakenRequestDto.getActualProblemId(), actionTakenRequestDto.getId(),
				actionTakenRequestDto.getActionTaken().toUpperCase());
		if (actionTakenEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.RECORD_ALREADY_EXISTS.getMessage());
		}  
		helpDeskActionTaken = actionTakenMapper.convertRequestDTOToEntity(actionTakenRequestDto,helpDeskActionTaken);
		helpDeskActionTakenRepository.save(helpDeskActionTaken);
		return Library.getSuccessfulResponse(helpDeskActionTaken, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_CREATED);
	} 

	public GenericResponse getCode() {
		MenuPrefix prefix = MenuPrefix.getType(ACTION_TAKEN_TYPE);
		String code = prefix.toString() + RandomUtil.getRandomNumber();
		while (true) {
			Optional<ActionTaken> actionTakenEntity = helpDeskActionTakenRepository.findByCodeIgnoreCase(code);
			if (actionTakenEntity.isPresent()) {
				code = prefix.toString() + RandomUtil.getRandomNumber();
			} else {
				break;
			}
		}
		return Library.getSuccessfulResponse(code, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	
	public GenericResponse getAllActive() {
		List<ActionTaken> actionTaken = helpDeskActionTakenRepository.findByIsActiveOrderByModifiedDateDesc(Boolean.TRUE);
		if (CollectionUtils.isEmpty(actionTaken)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<ActionTakenResponseDto> actionTakenResponseDtos = actionTaken.stream()
				.map(actionTakenMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(actionTakenResponseDtos, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	public Object getById(Long categoryId, Long subcategoryId, Long actualProblemId) {
		List<ActionTaken> actualProblems = helpDeskActionTakenRepository.findByCategoryAndSubcategoryIdAndActualProblem(categoryId,subcategoryId,actualProblemId);
		if (CollectionUtils.isEmpty(actualProblems)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<ActionTakenResponseDto> actualProblemResponseDtos = actualProblems.stream()
				.map(actionTakenMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(actualProblemResponseDtos, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

}
