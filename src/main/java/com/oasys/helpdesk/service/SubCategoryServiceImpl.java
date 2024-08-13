package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.CATEGORYID;
import static com.oasys.helpdesk.constant.Constant.CATEGORY_ID;
import static com.oasys.helpdesk.constant.Constant.CATEGORY_NAME;
import static com.oasys.helpdesk.constant.Constant.CODE;
import static com.oasys.helpdesk.constant.Constant.DESC;
import static com.oasys.helpdesk.constant.Constant.ID;
import static com.oasys.helpdesk.constant.Constant.MODIFIED_DATE;
import static com.oasys.helpdesk.constant.Constant.STATUS;
import static com.oasys.helpdesk.constant.Constant.SUB_CATEGORY_ID;
import static com.oasys.helpdesk.constant.Constant.SUB_CATEGORY_NAME;

import java.util.ArrayList;
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
import com.oasys.helpdesk.entity.SubCategory;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.repository.CategoryRepository;
import com.oasys.helpdesk.repository.SubCategoryRepository;
import com.oasys.helpdesk.request.SubCategoryRequestDto;
import com.oasys.helpdesk.response.SubCategoryResponseDto;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.RandomUtil;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class SubCategoryServiceImpl implements SubCategoryService{

	@Autowired
	SubCategoryRepository helpDeskTicketSubCategoryRepository;
	
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
	
	public static final String SUB_CATEGORY = "Sub Category";
	public static final String HELPDESK_CATEGORY_ID= "helpDeskTicketCategory.id";
	
	@Override
    public GenericResponse getAllSubCategory() {
		
	List<SubCategory> helpDeskTicketCategoryList = helpDeskTicketSubCategoryRepository.findAllByOrderByModifiedDateDesc();
		
		//List<SubCategory> helpDeskTicketCategoryList = helpDeskTicketSubCategoryRepository.findAllByActiveOrderByModifiedDateDesc(true);
		
		
		if (CollectionUtils.isEmpty(helpDeskTicketCategoryList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<SubCategoryResponseDto> responseList = helpDeskTicketCategoryList.stream()
				.map(this::convertHelpDeskTicketSubCategoryToDto).collect(Collectors.toList());
		return Library.getSuccessfulResponse(responseList,
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}

    @Override
	public GenericResponse getSubCategoryByCategoryId(Long categoryid) {
		
		List<SubCategory> HelpDeskTicketSubCategoryList = helpDeskTicketSubCategoryRepository.getSubCategoryByCategoryId(categoryid);
        if(HelpDeskTicketSubCategoryList==null ||HelpDeskTicketSubCategoryList.size()==0 ) {
        	throw new RecordNotFoundException("No record found");
        }
		if (HelpDeskTicketSubCategoryList.size() > 0) {
			List<SubCategoryResponseDto> HelpDeskTicketSubCategoryResponseDtoList = new ArrayList<SubCategoryResponseDto>();
			HelpDeskTicketSubCategoryList.forEach(pt -> {
				HelpDeskTicketSubCategoryResponseDtoList.add(convertHelpDeskTicketSubCategoryToDto(pt));
			});

			return Library.getSuccessfulResponse(HelpDeskTicketSubCategoryResponseDtoList,
					ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException();
		}
	}
	
	@Override
	public GenericResponse getSubCategoryById(Long id) throws RecordNotFoundException {
		SubCategory helpDeskTicketSubCategory = helpDeskTicketSubCategoryRepository.getById(id);
		if (helpDeskTicketSubCategory== null ) {
			throw new RecordNotFoundException("No record found");
		}
		if (helpDeskTicketSubCategory!= null && helpDeskTicketSubCategory.getId() != null) {
			return Library.getSuccessfulResponse(convertHelpDeskTicketSubCategoryToDto(helpDeskTicketSubCategory), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException();
		}
	
	}
	
	private SubCategoryResponseDto convertHelpDeskTicketSubCategoryToDto(SubCategory helpDeskTicketSubCategory) {

		SubCategoryResponseDto helpDeskTicketSubCategoryResponseDto = commonUtil.modalMap(helpDeskTicketSubCategory,
				SubCategoryResponseDto.class);
		helpDeskTicketSubCategoryResponseDto.setSubcategoryName(helpDeskTicketSubCategory.getSubCategoryName());
		helpDeskTicketSubCategoryResponseDto.setCreatedDate(helpDeskTicketSubCategory.getCreatedDate());
		helpDeskTicketSubCategoryResponseDto.setModifiedDate(helpDeskTicketSubCategory.getModifiedDate());
		Category helpDeskTicketCategory = helpDeskTicketCategoryRepository
				.getById(helpDeskTicketSubCategory.getHelpDeskTicketCategory().getId());
		if (Objects.nonNull(helpDeskTicketCategory)) {
			helpDeskTicketSubCategoryResponseDto.setCategoryid(helpDeskTicketCategory.getId());
			helpDeskTicketSubCategoryResponseDto.setCategoryName(helpDeskTicketCategory.getCategoryName());
			if (Objects.nonNull(helpDeskTicketCategory.getCreatedBy())) {
				helpDeskTicketSubCategoryResponseDto
						.setCreatedBy(commonDataController.getUserNameById(helpDeskTicketCategory.getCreatedBy()));
			}
			if (Objects.nonNull(helpDeskTicketCategory.getModifiedBy())) {
				helpDeskTicketSubCategoryResponseDto
						.setModifiedBy(commonDataController.getUserNameById(helpDeskTicketCategory.getModifiedBy()));
			}
		}
		helpDeskTicketSubCategoryResponseDto.setCode(helpDeskTicketSubCategory.getCode());
		helpDeskTicketSubCategoryResponseDto.setSubcategoryName(helpDeskTicketSubCategory.getSubCategoryName());
		return helpDeskTicketSubCategoryResponseDto;
	}
	
	@Override
	public GenericResponse createSubCategory(SubCategoryRequestDto helpDeskSubCategoryRequestDto)  
	{
			
			Category helpDeskTicketCategory= helpDeskTicketCategoryRepository.getById(helpDeskSubCategoryRequestDto.getCategoryid());
			if(Objects.isNull(helpDeskTicketCategory)) {
				return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { CATEGORY_ID }) );
			}
			
			Optional<SubCategory> SubCategoryOptional = helpDeskTicketSubCategoryRepository
					.findByCategoryIdAndSubCategoryNameIgnoreCase(helpDeskSubCategoryRequestDto.getCategoryid(),
							helpDeskSubCategoryRequestDto.getSubcategoryName().toUpperCase());
			if (SubCategoryOptional.isPresent()) {
				return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
						ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { SUB_CATEGORY_NAME }));
			}
			SubCategoryOptional = helpDeskTicketSubCategoryRepository
					.findByCodeIgnoreCase(helpDeskSubCategoryRequestDto.getCode());
			if (SubCategoryOptional.isPresent()) {
				return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
						ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { CODE }));
			}
			helpDeskSubCategoryRequestDto.setId(null);
			SubCategory entity = commonUtil.modalMap(helpDeskSubCategoryRequestDto, SubCategory.class);
			entity.setSubCategoryName(helpDeskSubCategoryRequestDto.getSubcategoryName());
			entity.setHelpDeskTicketCategory(helpDeskTicketCategory);
			helpDeskTicketSubCategoryRepository.save(entity);
			return Library.getSuccessfulResponse(convertHelpDeskTicketSubCategoryToDto(entity), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_CREATED);
			
		
	} 
	
	@Override
	public GenericResponse searchSubCategory(PaginationRequestDTO paginationDto) {
		Pageable pageable = null;
		Page<SubCategory> list = null;
		Long categoryId = null;
		Long subCategoryId = null;
		Boolean status = null;
		if (StringUtils.isBlank(paginationDto.getSortField())) {
			paginationDto.setSortField(MODIFIED_DATE);
		}
		if (StringUtils.isBlank(paginationDto.getSortOrder())) {
			paginationDto.setSortOrder(DESC);
		}
		if(StringUtils.isNotBlank(paginationDto.getSortField())) {
			if(CATEGORYID.equals(paginationDto.getSortField())) {
				paginationDto.setSortField(HELPDESK_CATEGORY_ID);
			}
			if(SUB_CATEGORY_ID.equals(paginationDto.getSortField())) {
				paginationDto.setSortField(ID);
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
				try {
					categoryId = Long.valueOf(paginationDto.getFilters().get(CATEGORYID).toString());
				} catch (Exception e) {
					log.error("error occurred while parsing categoryId :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}
			if (Objects.nonNull(paginationDto.getFilters().get(SUB_CATEGORY_ID))
					&& !paginationDto.getFilters().get(SUB_CATEGORY_ID).toString().trim().isEmpty()) {
				try {
					subCategoryId = Long.valueOf(paginationDto.getFilters().get(SUB_CATEGORY_ID).toString());
				} catch (Exception e) {
					log.error("error occurred while parsing subCategoryId :: {}", e);
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
		list = getByFilter(categoryId, subCategoryId, status, pageable);
		if (Objects.isNull(list)) {
			list = helpDeskTicketSubCategoryRepository.getAll(pageable);
		}
		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		Page<SubCategoryResponseDto> finalResponse = list.map(this::convertHelpDeskTicketSubCategoryToDto);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}
	private Page<SubCategory> getByFilter(Long categoryId, Long subCategoryId, Boolean status, Pageable pageable){
		Page<SubCategory> list = null;
		if (Objects.nonNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.nonNull(status)) {
			list = helpDeskTicketSubCategoryRepository.getBycategoryIdStatusAndSubCategoryId(categoryId, status, subCategoryId, pageable);
		} 
		if (Objects.isNull(list) && Objects.nonNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.isNull(status)) {
			list = helpDeskTicketSubCategoryRepository.getByCategoryIdAndSubCategoryId(categoryId, subCategoryId, pageable);
		} 
		if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.nonNull(status)) {
			list = helpDeskTicketSubCategoryRepository.getBySubCategoryIdAndStatus(subCategoryId, status, pageable);
		}
		if (Objects.isNull(list) && Objects.nonNull(categoryId) && Objects.isNull(subCategoryId) && Objects.nonNull(status)) {
			list = helpDeskTicketSubCategoryRepository.getByCategoryIdAndStatus(categoryId, status, pageable);
		}
		if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.isNull(subCategoryId) && Objects.nonNull(status)) {
			list = helpDeskTicketSubCategoryRepository.getByStatus(status, pageable);
		}
		if (Objects.isNull(list) && Objects.nonNull(categoryId) && Objects.isNull(subCategoryId) && Objects.isNull(status)) {
			list = helpDeskTicketSubCategoryRepository.getByCategoryId(categoryId, pageable);
		}
		if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.isNull(status)) {
			list = helpDeskTicketSubCategoryRepository.getBySubCategoryId(categoryId, pageable);
		}
		return list;
	}
	
	@Override
	public GenericResponse editSubCategory(SubCategoryRequestDto requestDTO) 
	{
		if(Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		SubCategory helpDeskTicketSubCategory = helpDeskTicketSubCategoryRepository.getById(requestDTO.getId());
		if (Objects.isNull(helpDeskTicketSubCategory)) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		Category helpDeskTicketCategory= helpDeskTicketCategoryRepository.getById(requestDTO.getCategoryid());
		if(Objects.isNull(helpDeskTicketCategory)) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { CATEGORY_ID }) );
		}
		helpDeskTicketSubCategory.setHelpDeskTicketCategory(helpDeskTicketCategory);
		Optional<SubCategory> SubCategoryOptional = helpDeskTicketSubCategoryRepository
				.findByCategoryIdAndSubCategoryNameIgnoreCaseNotInId(requestDTO.getCategoryid(),
						requestDTO.getSubcategoryName().toUpperCase(),requestDTO.getId());
		if (SubCategoryOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { CATEGORY_NAME }));
		}
		helpDeskTicketSubCategory.setSubCategoryName(requestDTO.getSubcategoryName());
		helpDeskTicketSubCategory.setActive(requestDTO.getActive());
		helpDeskTicketSubCategoryRepository.save(helpDeskTicketSubCategory);
		return Library.getSuccessfulResponse(convertHelpDeskTicketSubCategoryToDto(helpDeskTicketSubCategory), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_UPDATED);
		
	} 
	
	@Override
	public GenericResponse getCode() {
		MenuPrefix prefix = MenuPrefix.getType(SUB_CATEGORY);
		String code = prefix.toString() + RandomUtil.getRandomNumber();
		while (true) {
			Optional<SubCategory> entity = helpDeskTicketSubCategoryRepository.findByCodeIgnoreCase(code);
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
		List<SubCategory> subCategoryList = helpDeskTicketSubCategoryRepository.findAllByStatusOrderByModifiedDateDesc(Boolean.TRUE);
		if (CollectionUtils.isEmpty(subCategoryList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<SubCategoryResponseDto> responseList = subCategoryList.stream()
				.map(this::convertHelpDeskTicketSubCategoryToDto).collect(Collectors.toList());
		return Library.getSuccessfulResponse(responseList,
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}
}
