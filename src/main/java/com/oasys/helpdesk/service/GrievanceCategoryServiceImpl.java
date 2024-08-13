package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.CATEGORY_NAME;
import static com.oasys.helpdesk.constant.Constant.CODE;
import static com.oasys.helpdesk.constant.Constant.DESC;
import static com.oasys.helpdesk.constant.Constant.ID;
import static com.oasys.helpdesk.constant.Constant.MODIFIED_DATE;
import static com.oasys.helpdesk.constant.Constant.STATUS;
import static com.oasys.helpdesk.constant.Constant.TYPE;

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

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.GrievanceCategoryRequestDTO;
import com.oasys.helpdesk.dto.GrievanceCategoryResponsetDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.entity.GrievanceCategoryEntity;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.repository.GrievanceCategoryRepository;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.RandomUtil;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class GrievanceCategoryServiceImpl implements GrievanceCategoryService {

	@Autowired
	private GrievanceCategoryRepository grievanceCategoryRepository;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private CommonDataController commonDataController;
	
	@Autowired
	private PaginationMapper paginationMapper;
	
	
	public static final String GRIEVANCE_CATEGORY = "grievanceCategory";

	
	
	
	public GenericResponse createCategory(GrievanceCategoryRequestDTO categoryRequestDto)
	{
//		GrievanceCategoryEntity category = grievanceCategoryRepository
//				.findByName(categoryRequestDto.getCategoryName().toUpperCase());
//		if (Objects.nonNull(category)) {
//			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
//					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { CATEGORY_NAME }));
//		}
		Optional<GrievanceCategoryEntity> categoryOptional = grievanceCategoryRepository
				.findByCodeIgnoreCase(categoryRequestDto.getCode());
		if (categoryOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { CODE }));
		}
		categoryRequestDto.setId(null);
		GrievanceCategoryEntity GrievanceCategory = commonUtil.modalMap(categoryRequestDto, GrievanceCategoryEntity.class);
		grievanceCategoryRepository.save(GrievanceCategory);
		return Library.getSuccessfulResponse(GrievanceCategory, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_CREATED);
		
	} 
	
	public GenericResponse editCategory(GrievanceCategoryRequestDTO requestDTO)  
	{
		if(Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		Optional<GrievanceCategoryEntity> categoryOptional = grievanceCategoryRepository
				.findByCategoryNameIgnoreCaseNotInId(requestDTO.getCategoryName().toUpperCase(), requestDTO.getId());
		if (categoryOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { TYPE }));
		}
		categoryOptional = grievanceCategoryRepository.findById(requestDTO.getId());
		if (!categoryOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		GrievanceCategoryEntity category = categoryOptional.get();
		category.setCategoryName(requestDTO.getCategoryName());
		category.setActive(requestDTO.isActive());
		category.setTypeofUser(requestDTO.getTypeofUser());
		grievanceCategoryRepository.save(category);
		return Library.getSuccessfulResponse(category, ErrorCode.CREATED.getErrorCode(),ErrorMessages.RECORED_UPDATED);
	}
	
	public GenericResponse getAllCategory() {
		List<GrievanceCategoryEntity> CategoryList = grievanceCategoryRepository.findAllByOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(CategoryList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<GrievanceCategoryResponsetDTO> responseList = CategoryList.stream()
				.map(this::convertgrievanceCategoryToDto).collect(Collectors.toList());
		return Library.getSuccessfulResponse(responseList,
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}
	
	public GenericResponse getCategoryById(Long id) throws RecordNotFoundException {
		GrievanceCategoryEntity grievanceCategory = grievanceCategoryRepository.getById(id);
		if (grievanceCategory== null ) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		}
		if (grievanceCategory!= null && grievanceCategory.getId() != null) {
			return Library.getSuccessfulResponse(convertgrievanceCategoryToDto(grievanceCategory), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException();
		}
	}
	
	
	public GenericResponse searchCategory(PaginationRequestDTO paginationDto) {
		Pageable pageable = null;
		Page<GrievanceCategoryEntity> list = null;
		Long id = null;
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
		if (Objects.nonNull(id) && Objects.nonNull(status)) {
			list = grievanceCategoryRepository.getByIdAndStatus(id, status, pageable);
		} else if (Objects.nonNull(id) && Objects.isNull(status)) {
			list = grievanceCategoryRepository.getById(id, pageable);
		} else if (Objects.isNull(id) && Objects.nonNull(status)) {
			list = grievanceCategoryRepository.getByStatus(status, pageable);
		}
		if (Objects.isNull(list)) {
			list = grievanceCategoryRepository.getAll(pageable);
		}
		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		Page<GrievanceCategoryResponsetDTO> finalResponse = list.map(this::convertgrievanceCategoryToDto);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}
	
	public GenericResponse getCode() {
		MenuPrefix prefix = MenuPrefix.getType(GRIEVANCE_CATEGORY);
		String code = prefix.toString() + RandomUtil.getRandomNumber();
		while (true) {
			Optional<GrievanceCategoryEntity> entity = grievanceCategoryRepository.findByCodeIgnoreCase(code);
			if (entity.isPresent()) {
				code = prefix.toString() + RandomUtil.getRandomNumber();
			} else {
				break;
			}
		}
		return Library.getSuccessfulResponse(code, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	public GenericResponse getAllActive() {
		List<GrievanceCategoryEntity> CategoryList = grievanceCategoryRepository.findAllByStatusOrderByModifiedDateDesc(Boolean.TRUE);
		if (CollectionUtils.isEmpty(CategoryList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<GrievanceCategoryResponsetDTO> responseList = CategoryList.stream()
				.map(this::convertgrievanceCategoryToDto).collect(Collectors.toList());
		return Library.getSuccessfulResponse(responseList,
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}
	
	
	public GenericResponse getAllActivetypeofuser(GrievanceCategoryRequestDTO categoryRequestDto) {
		//List<GrievanceCategoryEntity> CategoryList = grievanceCategoryRepository.findAllByTypeofUser(categoryRequestDto.getTypeofUser());
		
		List<GrievanceCategoryEntity> CategoryList = grievanceCategoryRepository.findAllByTypeofUserAndActive(categoryRequestDto.getTypeofUser(),Boolean.TRUE);

		if (CollectionUtils.isEmpty(CategoryList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<GrievanceCategoryResponsetDTO> responseList = CategoryList.stream()
				.map(this::convertgrievanceCategoryToDto).collect(Collectors.toList());
		return Library.getSuccessfulResponse(responseList,
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
		
		
		
		
		
		
	}
	
	
	private GrievanceCategoryResponsetDTO convertgrievanceCategoryToDto(GrievanceCategoryEntity grievanceCategory) {

		GrievanceCategoryResponsetDTO grievanceCategoryResponseDto = commonUtil.modalMap(grievanceCategory,
				GrievanceCategoryResponsetDTO.class);
		String createduser=commonDataController.getUserNameById(grievanceCategory.getCreatedBy());
		grievanceCategoryResponseDto.setCreatedBy(createduser);
		grievanceCategoryResponseDto.setCreatedDate(grievanceCategory.getCreatedDate());
		String modifieduser=commonDataController.getUserNameById(grievanceCategory.getModifiedBy());
		grievanceCategoryResponseDto.setModifiedBy(modifieduser);
		grievanceCategoryResponseDto.setModifiedDate(grievanceCategory.getModifiedDate());
		grievanceCategoryResponseDto.setCode(grievanceCategory.getCode());
		grievanceCategoryResponseDto.setTypeofUser(grievanceCategory.getTypeofUser());
		return grievanceCategoryResponseDto;

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
