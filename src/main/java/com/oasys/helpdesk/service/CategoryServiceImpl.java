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
import com.oasys.helpdesk.entity.Category;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.repository.CategoryRepository;
import com.oasys.helpdesk.request.CategoryRequestDto;
import com.oasys.helpdesk.response.CategoryResponseDto;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.RandomUtil;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class CategoryServiceImpl implements CategoryService{

	@Autowired
	CategoryRepository helpDeskTicketCategoryRepository;
	
	@Autowired
	CommonDataController commonDataController;
	
	@Autowired
	EntityManager entityManager;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private PaginationMapper paginationMapper;
	
	public static final String CATEGORY = "Category";
	
	public GenericResponse createCategory(CategoryRequestDto helpDeskCategoryRequestDto)  
	{
		Category category = helpDeskTicketCategoryRepository
				.findByName(helpDeskCategoryRequestDto.getCategoryName().toUpperCase());
		if (Objects.nonNull(category)) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { CATEGORY_NAME }));
		}
		Optional<Category> categoryOptional = helpDeskTicketCategoryRepository
				.findByCodeIgnoreCase(helpDeskCategoryRequestDto.getCode());
		if (categoryOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { CODE }));
		}
		helpDeskCategoryRequestDto.setId(null);
		Category helpDeskTicketCategory = commonUtil.modalMap(helpDeskCategoryRequestDto, Category.class);
		helpDeskTicketCategoryRepository.save(helpDeskTicketCategory);
		return Library.getSuccessfulResponse(helpDeskTicketCategory, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_CREATED);
		
	} 

	public GenericResponse getAllCategory() {
		List<Category> helpDeskTicketCategoryList = helpDeskTicketCategoryRepository.findAllByOrderByModifiedDateDesc();
		
		//List<Category> helpDeskTicketCategoryList = helpDeskTicketCategoryRepository.findAllByActiveOrderByModifiedDateDesc(true);	
		if (CollectionUtils.isEmpty(helpDeskTicketCategoryList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<CategoryResponseDto> responseList = helpDeskTicketCategoryList.stream()
				.map(this::convertHelpDeskTicketCategoryToDto).collect(Collectors.toList());
		return Library.getSuccessfulResponse(responseList,
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}
	
	
	public GenericResponse getCategoryById(Long id) throws RecordNotFoundException {
		Category helpDeskTicketCategory = helpDeskTicketCategoryRepository.getById(id);
		if (helpDeskTicketCategory== null ) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		}
		if (helpDeskTicketCategory!= null && helpDeskTicketCategory.getId() != null) {
			return Library.getSuccessfulResponse(convertHelpDeskTicketCategoryToDto(helpDeskTicketCategory), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException();
		}
	}
	private CategoryResponseDto convertHelpDeskTicketCategoryToDto(Category helpDeskTicketCategory) {

		CategoryResponseDto helpDeskTicketCategoryResponseDto = commonUtil.modalMap(helpDeskTicketCategory,
				CategoryResponseDto.class);
		String createduser=commonDataController.getUserNameById(helpDeskTicketCategory.getCreatedBy());
		helpDeskTicketCategoryResponseDto.setCreatedBy(createduser);
		helpDeskTicketCategoryResponseDto.setCreatedDate(helpDeskTicketCategory.getCreatedDate());
		String modifieduser=commonDataController.getUserNameById(helpDeskTicketCategory.getModifiedBy());
		helpDeskTicketCategoryResponseDto.setModifiedBy(modifieduser);
		helpDeskTicketCategoryResponseDto.setModifiedDate(helpDeskTicketCategory.getModifiedDate());
		helpDeskTicketCategoryResponseDto.setCode(helpDeskTicketCategory.getCode());
		return helpDeskTicketCategoryResponseDto;

	}
	public GenericResponse searchCategory(PaginationRequestDTO paginationDto) {
		Pageable pageable = null;
		Page<Category> list = null;
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
			list = helpDeskTicketCategoryRepository.getByIdAndStatus(id, status, pageable);
		} else if (Objects.nonNull(id) && Objects.isNull(status)) {
			list = helpDeskTicketCategoryRepository.getById(id, pageable);
		} else if (Objects.isNull(id) && Objects.nonNull(status)) {
			list = helpDeskTicketCategoryRepository.getByStatus(status, pageable);
		}
		if (Objects.isNull(list)) {
			list = helpDeskTicketCategoryRepository.getAll(pageable);
		}
		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		Page<CategoryResponseDto> finalResponse = list.map(this::convertHelpDeskTicketCategoryToDto);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}
	
	public GenericResponse editCategory(CategoryRequestDto requestDTO)  
	{
		if(Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		Optional<Category> categoryOptional = helpDeskTicketCategoryRepository
				.findByCategoryNameIgnoreCaseNotInId(requestDTO.getCategoryName().toUpperCase(), requestDTO.getId());
		if (categoryOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { TYPE }));
		}
		categoryOptional = helpDeskTicketCategoryRepository.findById(requestDTO.getId());
		if (!categoryOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		Category category = categoryOptional.get();
		category.setCategoryName(requestDTO.getCategoryName());
		category.setActive(requestDTO.isActive());
		helpDeskTicketCategoryRepository.save(category);
		return Library.getSuccessfulResponse(category, ErrorCode.CREATED.getErrorCode(),ErrorMessages.RECORED_UPDATED);
	}

	public GenericResponse getCode() {
		MenuPrefix prefix = MenuPrefix.getType(CATEGORY);
		String code = prefix.toString() + RandomUtil.getRandomNumber();
		while (true) {
			Optional<Category> entity = helpDeskTicketCategoryRepository.findByCodeIgnoreCase(code);
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
		List<Category> helpDeskTicketCategoryList = helpDeskTicketCategoryRepository.findAllByStatusOrderByModifiedDateDesc(Boolean.TRUE);
		if (CollectionUtils.isEmpty(helpDeskTicketCategoryList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<CategoryResponseDto> responseList = helpDeskTicketCategoryList.stream()
				.map(this::convertHelpDeskTicketCategoryToDto).collect(Collectors.toList());
		return Library.getSuccessfulResponse(responseList,
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}
}
