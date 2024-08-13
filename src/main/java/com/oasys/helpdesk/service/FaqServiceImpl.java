package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.CATEGORYID;
import static com.oasys.helpdesk.constant.Constant.CODE;
import static com.oasys.helpdesk.constant.Constant.ID;
import static com.oasys.helpdesk.constant.Constant.QUESTION;
import static com.oasys.helpdesk.constant.Constant.QUESTION_ID;
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

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.entity.Faq;
import com.oasys.helpdesk.mapper.FAQMapper;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.repository.CategoryRepository;
import com.oasys.helpdesk.repository.FaqRepository;
import com.oasys.helpdesk.repository.SubCategoryRepository;
import com.oasys.helpdesk.request.FaqRequestDto;
import com.oasys.helpdesk.response.FaqResponseDto;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.RandomUtil;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class FaqServiceImpl implements FaqService{
	@Autowired
	FaqRepository helpDeskFaqRepository;
	
	@Autowired
	CategoryRepository helpDeskTicketCategoryRepository;
	
	@Autowired
	SubCategoryRepository helpDeskTicketSubCategoryRepository;
	@Autowired
	CommonDataController commonDataController;
	
	@Autowired
	private FAQMapper faqMapper;
	
	
	@Autowired
	private PaginationMapper paginationMapper;
	
	public static final String FAQ= "FAQ";
	public static final String SUB_CATEGORY_SORT_FIELD= "subCategoryId.id";
	public static final String CATEGORY_SORT_FIELD= "categoryId.id";

	@Override
	public GenericResponse getAllfaq() {
		List<Faq> assetBrandTypeList = helpDeskFaqRepository.findAllByOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(assetBrandTypeList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<FaqResponseDto> responseList = assetBrandTypeList.stream()
				.map(faqMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(responseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	@Override
	public GenericResponse getFaqById(Long id) {
		Faq helpDeskFaq = helpDeskFaqRepository.getById(id);
		if (Objects.isNull(helpDeskFaq)) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(), ErrorMessages.NO_RECORD_FOUND);
		}
		if (helpDeskFaq!= null && helpDeskFaq.getId() != null) {
			return Library.getSuccessfulResponse(faqMapper.convertEntityToResponseDTO(helpDeskFaq), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException();
		}
	}
	
	
	@Override
	public GenericResponse createFaq(FaqRequestDto helpDeskFaqRequestDto)  
	{
		Optional<Faq> faqOptional = helpDeskFaqRepository.findByQuestionIgnoreCase(helpDeskFaqRequestDto.getQuestion());
		if (faqOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { QUESTION }));
		}
		faqOptional = helpDeskFaqRepository.findByCodeIgnoreCase(helpDeskFaqRequestDto.getCode());
		if (faqOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { CODE }));
		}
		helpDeskFaqRequestDto.setId(null);
		Faq faq = faqMapper.convertRequestDTOToEntity(helpDeskFaqRequestDto, null);
		helpDeskFaqRepository.save(faq);
		return Library.getSuccessfulResponse(faqMapper.convertEntityToResponseDTO(faq), ErrorCode.CREATED.getErrorCode(),
				ErrorMessages.RECORED_CREATED);
	}

	@Override
	public GenericResponse editFaq(FaqRequestDto helpDeskFaqRequestDto) 
	{
		if (Objects.isNull(helpDeskFaqRequestDto.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { ID }));
		}

		Faq helpDeskFaq = helpDeskFaqRepository.getById(helpDeskFaqRequestDto.getId());
		if (Objects.isNull(helpDeskFaq)) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		Optional<Faq> faqOptional = helpDeskFaqRepository.findByQuestionNotInId(
				helpDeskFaqRequestDto.getQuestion().trim().toUpperCase(), helpDeskFaqRequestDto.getId());
		if (faqOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { QUESTION }));
		}
		Faq entity = faqMapper.convertRequestDTOToEntity(helpDeskFaqRequestDto, helpDeskFaq);
		helpDeskFaqRepository.save(entity);
		return Library.getSuccessfulResponse(faqMapper.convertEntityToResponseDTO(entity),
				ErrorCode.CREATED.getErrorCode(), ErrorMessages.RECORED_UPDATED);
	}
	
	@Override
	public GenericResponse getCode() {
		MenuPrefix prefix = MenuPrefix.getType(FAQ);
		String code = prefix.toString() + RandomUtil.getRandomNumber();
		while (true) {
			Optional<Faq> entity = helpDeskFaqRepository.findByCodeIgnoreCase(code);
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
	public GenericResponse getAllActive() {
		List<Faq> assetBrandTypeList = helpDeskFaqRepository.findByStatusOrderByModifiedDateDesc(Boolean.TRUE);
		if (CollectionUtils.isEmpty(assetBrandTypeList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<FaqResponseDto> responseList = assetBrandTypeList.stream()
				.map(faqMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(responseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	@Override
	public GenericResponse searchByFilter(PaginationRequestDTO paginationDto) {
		Pageable pageable = null;
		Page<Faq> list = null;
		Long subCategoryId = null;
		Long id = null;
		Long categoryId = null;
		if(StringUtils.isNotBlank(paginationDto.getSortField())) {
			if(Constant.SUB_CATEGORY_ID.equals(paginationDto.getSortField())) {
				paginationDto.setSortField(SUB_CATEGORY_SORT_FIELD);
			}
			if(Constant.CATEGORYID.equals(paginationDto.getSortField())) {
				paginationDto.setSortField(CATEGORY_SORT_FIELD);
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
			if (Objects.nonNull(paginationDto.getFilters().get(SUB_CATEGORY_ID))
					&& !paginationDto.getFilters().get(SUB_CATEGORY_ID).toString().trim().isEmpty()) {
				try {
					subCategoryId = Long.valueOf(paginationDto.getFilters().get(SUB_CATEGORY_ID).toString());
					}catch(Exception e) {
						log.error("error occurred while parsing subCategoryId :: {}", e);
						return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
								ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
					}
			}
			if (Objects.nonNull(paginationDto.getFilters().get(CATEGORYID))
					&& !paginationDto.getFilters().get(CATEGORYID).toString().trim().isEmpty()) {
				
				try {
					categoryId = Long.valueOf(paginationDto.getFilters().get(CATEGORYID).toString());
					}catch(Exception e) {
						log.error("error occurred while parsing categoryId :: {}", e);
						return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
								ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
					}
			}
			if (Objects.nonNull(paginationDto.getFilters().get(QUESTION_ID))
					&& !paginationDto.getFilters().get(QUESTION_ID).toString().trim().isEmpty()) {
				try {
					id = Long.valueOf(paginationDto.getFilters().get(QUESTION_ID).toString());
					}catch(Exception e) {
						log.error("error occurred while parsing questionId :: {}", e);
						return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
								ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
					}
			}
		}
		list = getByFilter(subCategoryId, categoryId, id, pageable);
		if (Objects.isNull(list)) {
			list = helpDeskFaqRepository.getAll(pageable);
		}
		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		Page<FaqResponseDto> finalResponse = list.map(faqMapper::convertEntityToResponseDTO);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	private Page<Faq> getByFilter(Long subCategoryId, Long categoryId, Long faqId, Pageable pageable){
		Page<Faq> list = null;
		if (Objects.nonNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.nonNull(faqId)) {
			list = helpDeskFaqRepository.getByIdCategoryIdAndSubCategoryId(categoryId, faqId, subCategoryId, pageable);
		} 
		if (Objects.isNull(list) && Objects.nonNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.isNull(faqId)) {
			list = helpDeskFaqRepository.getByCategoryIdAndSubCategoryId(categoryId, subCategoryId, pageable);
		} 
		if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.nonNull(faqId)) {
			list = helpDeskFaqRepository.getBySubCategoryAndId(subCategoryId, faqId, pageable);
		}
		if (Objects.isNull(list) && Objects.nonNull(categoryId) && Objects.isNull(subCategoryId) && Objects.nonNull(faqId)) {
			list = helpDeskFaqRepository.getByCategoryIdAndId(categoryId, faqId, pageable);
		}
		if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.isNull(subCategoryId) && Objects.nonNull(faqId)) {
			list = helpDeskFaqRepository.getById(faqId, pageable);
		}
		if (Objects.isNull(list) && Objects.nonNull(categoryId) && Objects.isNull(subCategoryId) && Objects.isNull(faqId)) {
			list = helpDeskFaqRepository.getByCategoryId(categoryId, pageable);
		}
		if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.isNull(faqId)) {
			list = helpDeskFaqRepository.getBySubCategoryId(subCategoryId, pageable);
		}
		return list;
	}
	
	
	@Override
	public GenericResponse getById(Long subCategoryId, Long categoryId) {
		Optional<Faq> list = helpDeskFaqRepository.getById(subCategoryId,categoryId);
		if (!list.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(faqMapper.convertEntityToResponseDTO(list.get()), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

}
