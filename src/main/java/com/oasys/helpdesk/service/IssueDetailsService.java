package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.STATUS;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

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
import com.oasys.helpdesk.entity.Category;
import com.oasys.helpdesk.entity.IssueDetails;
import com.oasys.helpdesk.entity.SubCategory;
import com.oasys.helpdesk.mapper.IssueDetMapper;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.repository.CategoryRepository;
import com.oasys.helpdesk.repository.IssueDetailsRepository;
import com.oasys.helpdesk.repository.SubCategoryRepository;
import com.oasys.helpdesk.request.IssueRequestDto;
import com.oasys.helpdesk.response.IsssueDetresdto;
import com.oasys.helpdesk.response.IssueDetailsResponseDto;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.RandomUtil;

import lombok.extern.log4j.Log4j2;



@Service
@Log4j2
public class IssueDetailsService {

	@Autowired
	IssueDetailsRepository issueDetailsRepository;
	
	@Autowired
	SubCategoryRepository helpDeskTicketSubCategoryRepository;
	
	@Autowired
	CategoryRepository helpDeskTicketCategoryRepository;
	
	@Autowired
	CommonDataController commonDataController;
	
	@Autowired
	EntityManager entityManager;
	
	@Autowired
	PaginationMapper paginationMapper;
	
	@Autowired
	IssueDetMapper issuedetmapper;

	public static final String ISSUE_CODE = "issue Code";

	public static final String CATEGORY = "category_id";

	public static final String SUBCATEGORY = "subcategory_id";
	

	public GenericResponse getAllIssueDetails() {
		List<IssueDetails> IssueDetailsList = issueDetailsRepository.findAllByOrderByModifiedDateDesc();
		if(IssueDetailsList==null ||IssueDetailsList.size()==0 ) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
	    }
		if (IssueDetailsList.size() > 0) {
			List<IssueDetailsResponseDto> IssueDetailsResponseDtoList = new ArrayList<IssueDetailsResponseDto>();
			IssueDetailsList.forEach(pt -> {
				IssueDetailsResponseDtoList.add(convertIssueDetailsToDto(pt));
			});

			return Library.getSuccessfulResponse(IssueDetailsResponseDtoList,
					ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
		} else {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		}
	}
	
	public GenericResponse getIssueDetailsById(Long id) throws RecordNotFoundException {
		IssueDetails issueDetails = issueDetailsRepository.getById(id);
		
		
		if (issueDetails== null ) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		}
		if (issueDetails!= null && issueDetails.getId() != null) {
			return Library.getSuccessfulResponse(convertIssueDetailsToDto(issueDetails), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		} else {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		}
	}
	
	private IssueDetailsResponseDto convertIssueDetailsToDto(IssueDetails issueDetails) {

		IssueDetailsResponseDto issueDetailsResponseDto = new IssueDetailsResponseDto();
		issueDetailsResponseDto.setId(issueDetails.getId());
		issueDetailsResponseDto.setIssueName(issueDetails.getIssueName());
		issueDetailsResponseDto.setIssuecode(issueDetails.getIssuecode());
		SubCategory subcategory = helpDeskTicketSubCategoryRepository.getById(issueDetails.getSubcategoryId().getId());
		if (Objects.nonNull(subcategory)) {
			issueDetailsResponseDto.setSubCategoryId(issueDetails.getSubcategoryId().getId());
			issueDetailsResponseDto.setSubCategoryName(subcategory.getSubCategoryName());
			if (Objects.nonNull(subcategory.getHelpDeskTicketCategory())) {
				issueDetailsResponseDto
						.setCategoryId(issueDetails.getSubcategoryId().getHelpDeskTicketCategory().getId());
				issueDetailsResponseDto
						.setCategoryName(issueDetails.getSubcategoryId().getHelpDeskTicketCategory().getCategoryName());
			}
		}
		
		///HelpDeskTicketCategory category= helpDeskTicketCategoryRepository.getById(subcategory.getId());
		Category category= helpDeskTicketCategoryRepository.getById(subcategory.getHelpDeskTicketCategory().getId());
		issueDetailsResponseDto.setCategoryName(category.getCategoryName());
		issueDetailsResponseDto.setActive(issueDetails.isActive());
		issueDetailsResponseDto.setIssuetype(issueDetails.isIssuetype());
		Boolean software=issueDetailsResponseDto.isIssuetype();
		Boolean ver=true;
			if(software.equals(ver)) {
				issueDetailsResponseDto.setIssuetypeName("Software");	
			}
			
			else {
				issueDetailsResponseDto.setIssuetypeName("Hardware");		
			}
			
		
		
		String createduser=commonDataController.getUserNameById(issueDetails.getCreatedBy());
		String modifieduser=commonDataController.getUserNameById(issueDetails.getModifiedBy());
		issueDetailsResponseDto.setCreatedBy(createduser);
		issueDetailsResponseDto.setCreatedDate(issueDetails.getCreatedDate());
		issueDetailsResponseDto.setModifiedBy(modifieduser);
		issueDetailsResponseDto.setModifiedDate(issueDetails.getModifiedDate());
		//issueDetailsResponseDto.setSubCategoryId(subcategory.getHelpDeskTicketCategory());
		
		
		return issueDetailsResponseDto;

	}
	


public GenericResponse createIssue(IssueRequestDto issueRequestDto) {
	
	if (issueRequestDto != null ) {
		IssueDetails issueDetails = new IssueDetails();
		issueDetails.setIssueName(issueRequestDto.getIssueName());
        if(issueRequestDto.getSubCategoryId() !=null && issueRequestDto.getSubCategoryId()>0) {
        	SubCategory helpDeskTicketSubCategory= helpDeskTicketSubCategoryRepository.getById(issueRequestDto.getSubCategoryId());
        	if (Objects.isNull(helpDeskTicketSubCategory)) {
				return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
						ResponseMessageConstant.INVALID_REQUEST_PARM
								.getMessage(new Object[] { Constant.SUB_CATEGORY_ID }));
			}
        	issueDetails.setSubcategoryId(helpDeskTicketSubCategory);	
        }
        issueDetails.setActive(issueRequestDto.isActive());
        issueDetails.setIssuecode(issueRequestDto.getIssuecode());
        issueDetails.setIssuetype(issueRequestDto.isIssuetype());
		issueDetailsRepository.save(issueDetails);
		
		return Library.getSuccessfulResponse(issueDetails, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_CREATED);
		
	} else {
		return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
				ErrorMessages.NO_RECORD_FOUND);
	}
	
} 

public GenericResponse updateIssue(IssueRequestDto issueRequestDto){
		if (Objects.isNull(issueRequestDto.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { Constant.ID }));
		}
		IssueDetails issueDetails = issueDetailsRepository.getById(issueRequestDto.getId());
		if (Objects.isNull(issueDetails)) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { Constant.ID }));
		}
		issueDetails.setIssueName(issueRequestDto.getIssueName());
		issueDetails.setActive(issueRequestDto.isActive());
		issueDetails.setIssuetype(issueRequestDto.isIssuetype());
		SubCategory helpDeskTicketSubCategory = helpDeskTicketSubCategoryRepository
				.getById(issueRequestDto.getSubCategoryId());
		if (Objects.isNull(helpDeskTicketSubCategory)) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { Constant.SUB_CATEGORY_ID }));
		}
		issueDetails.setSubcategoryId(helpDeskTicketSubCategory);
		issueDetailsRepository.save(issueDetails);
		return Library.getSuccessfulResponse(issueDetails, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_UPDATED);
	
} 
//public GenericResponse searchIssueDetails(PaginationRequestDTO paginationDto) throws ParseException {
//	List<IssueDetailsResponseDto> isseDetailsResponseDtoList = new ArrayList<>();
//	StringBuilder recordQuery = new StringBuilder("Select * from issue_details issue ");
//	StringBuilder countQuery = new StringBuilder("Select count(*) from issue_details issue "); 
//
//	
//	
//	if (paginationDto.getFilters() != null) {
//		log.info("issue filters :::" + paginationDto.getFilters());
//
//		if (paginationDto.getFilters().get("id") != null
//				&& !paginationDto.getFilters().get("id").toString().trim().isEmpty()) {
//			Long id = Long.parseLong(paginationDto.getFilters().get("id").toString());
//
//			recordQuery.append(" and issue.id = " + id);
//			countQuery.append(" and issue.id = " + id);
//
//		}
//		if (paginationDto.getFilters().get("issueName") != null
//				&& !paginationDto.getFilters().get("issueName").toString().trim().isEmpty()) {
//			String issueName = paginationDto.getFilters().get("issueName").toString();
//			log.debug("issueName :"+ paginationDto.getFilters().get("issueName"));
//			recordQuery.append( " and issue.issue_name = '"+issueName +"'");
//			countQuery.append( " and issue.issue_name = '"+issueName+"'");
//		}
//		
//		
//		
//		if (paginationDto.getFilters().get("subCategoryId") != null
//				&& !paginationDto.getFilters().get("subCategoryId").toString().trim().isEmpty()) {
//			String subCategoryId = paginationDto.getFilters().get("subCategoryId").toString();
//			log.debug("subCategoryId :"+ paginationDto.getFilters().get("subCategoryId"));
//			recordQuery.append( " and issue.subcategory_id = '"+subCategoryId +"'");
//			countQuery.append( " and issue.subcategory_id = '"+subCategoryId+"'");
//		}
//		
//		if (paginationDto.getFilters().get("isActive") != null
//				&& !paginationDto.getFilters().get("isActive").toString().trim().isEmpty()) {
//			String isActive = paginationDto.getFilters().get("isActive").toString();
//			log.debug("isActive :"+ paginationDto.getFilters().get("isActive"));
//			recordQuery.append( " and issue.is_active = '"+isActive +"'");
//			countQuery.append( " and issue.is_active = '"+isActive+"'");
//		}
//		
//		
//
//	}
//	
//	BigInteger totalCount1 = (BigInteger) entityManager.createNativeQuery(countQuery.toString()).getSingleResult();
//	Integer totalCount = totalCount1.intValue();
//
//	javax.persistence.Query query = entityManager.createNativeQuery(recordQuery.toString(),
//			IssueDetails.class);
//	query.setFirstResult(paginationDto.getPageNo() * paginationDto.getPaginationSize());
//	query.setMaxResults(paginationDto.getPaginationSize());
//	List<IssueDetails> resultList = query.getResultList();
//	if (resultList.isEmpty()) {
//		throw new RecordNotFoundException();
//	} else {
//		for (IssueDetails issueDetails : resultList) {
//			IssueDetailsResponseDto issueDetailsResponseDto = new IssueDetailsResponseDto();
//			issueDetailsResponseDto.setId(issueDetails.getId());
//			issueDetailsResponseDto.setIssueName(issueDetails.getIssueName());
//			SubCategory subcategory = helpDeskTicketSubCategoryRepository.getById(issueDetails.getSubcategoryId().getId());
//			issueDetailsResponseDto.setSubCategoryName(subcategory.getSubCategoryName());
//			Category category= helpDeskTicketCategoryRepository.getById(subcategory.getHelpDeskTicketCategory().getId());
//			issueDetailsResponseDto.setCategoryName(category.getCategoryName());
//			issueDetailsResponseDto.setActive(issueDetails.isActive());
//			String createduser=commonDataController.getUserNameById(issueDetails.getCreatedBy());
//			String modifieduser=commonDataController.getUserNameById(issueDetails.getModifiedBy());
//			issueDetailsResponseDto.setCreatedBy(createduser);
//			issueDetailsResponseDto.setCreatedDate(issueDetails.getCreatedDate());
//			issueDetailsResponseDto.setModifiedBy(modifieduser);
//			issueDetailsResponseDto.setModifiedDate(issueDetails.getModifiedDate());
//			isseDetailsResponseDtoList.add(issueDetailsResponseDto);
//		}
//		log.debug("found records : " + isseDetailsResponseDtoList.size());
//
//		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
//		paginationResponseDTO.setContents(isseDetailsResponseDtoList);
//		paginationResponseDTO.setTotalElements(totalCount.longValue());
//		paginationResponseDTO.setNumberOfElements(resultList.size());
//		if (paginationDto.getPaginationSize() > 0 && totalCount > 0) {
//			paginationResponseDTO.setTotalPages(totalCount / paginationDto.getPaginationSize());
//			if (totalCount < paginationDto.getPaginationSize())
//				paginationResponseDTO.setTotalPages(1);
//			else if (totalCount % paginationDto.getPaginationSize() > 0) {
//				paginationResponseDTO.setTotalPages(paginationResponseDTO.getTotalPages() + 1);
//			}
//
//		} else
//			paginationResponseDTO.setTotalPages(0);
//
//		return Library.getSuccessfulResponse(paginationResponseDTO, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
//				ErrorMessages.RECORED_FOUND);
//
//	}
//}
 



public GenericResponse searchIssueDetails(PaginationRequestDTO paginationDto) {
	Pageable pageable = null;
	Page<IssueDetails> list = null;
	Long categoryId = null;
	String issuename = null;
	Long subCategoryId = null;
	String problem=null;
	Boolean status = null;
//	if(StringUtils.isNotBlank(paginationDto.getSortField())) {
//		if(ISSUEDETCAT_ID.equals(paginationDto.getSortField())) {
//			paginationDto.setSortField(CATEGORY);
//		}
//		if(ISSUEDETSUBCAT_ID.equals(paginationDto.getSortField())) {
//			paginationDto.setSortField(SUBCATEGORY);
//		}
//	}
	if (paginationDto.getSortOrder().equalsIgnoreCase(ASC)) {
		pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
				Sort.by(Direction.ASC, paginationDto.getSortField()));
	} else {
		pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
				Sort.by(Direction.DESC, paginationDto.getSortField()));
	}
	if (Objects.nonNull(paginationDto.getFilters())) {
		if (Objects.nonNull(paginationDto.getFilters().get("categoryId"))
				&& !paginationDto.getFilters().get("categoryId").toString().trim().isEmpty()) {
			categoryId = Long.valueOf(paginationDto.getFilters().get("categoryId").toString());
		}
		if (Objects.nonNull(paginationDto.getFilters().get("subCategoryId"))
				&& !paginationDto.getFilters().get("subCategoryId").toString().trim().isEmpty()) {
			subCategoryId = Long.valueOf(paginationDto.getFilters().get("subCategoryId").toString());
		}
		if (Objects.nonNull(paginationDto.getFilters().get("issueName"))
				&& !paginationDto.getFilters().get("issueName").toString().trim().isEmpty()) {
			issuename = String.valueOf(paginationDto.getFilters().get("issueName").toString());
		}
		if (Objects.nonNull(paginationDto.getFilters().get(STATUS))
				&& !paginationDto.getFilters().get(STATUS).toString().trim().isEmpty()) {
			status = Boolean.valueOf(paginationDto.getFilters().get(STATUS).toString());
		}
	}
	
	
	list = getByFilter(categoryId, subCategoryId, issuename,status, pageable);
	if (Objects.isNull(list)) {
		list = issueDetailsRepository.getAll(pageable);
	}
	
	
	if (Objects.isNull(list) || list.isEmpty()) {
		return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
				ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
	}
	Page<IsssueDetresdto> finalResponse = list.map(issuedetmapper::convertEntityToResponseDTO);
	
	return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
			ErrorMessages.RECORED_FOUND);
	
}



private Page<IssueDetails> getByFilter(Long categoryId, Long subCategoryId, String issuename, Boolean status, Pageable pageable){
	Page<IssueDetails> list = null;
	
	if (Objects.isNull(list) && Objects.nonNull(categoryId) && Objects.isNull(subCategoryId) && Objects.isNull(status)) {
		list = issueDetailsRepository.getByCategoryId(categoryId, pageable);
	}
	
	if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.isNull(status)) {
		list = issueDetailsRepository.getBySubcategoryId(subCategoryId, pageable);
	}
	
	
	if (Objects.isNull(list) && Objects.nonNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.nonNull(issuename) &&  Objects.isNull(status)) {
		list = issueDetailsRepository.getByCategorySubcategoryAndIssueName(categoryId, subCategoryId, issuename, pageable);
	} 
	
	if (Objects.isNull(list) && Objects.nonNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.nonNull(status) && Objects.isNull(issuename)) {
		list = issueDetailsRepository.getByCategorySubcategoryAndStatus(categoryId, subCategoryId, status, pageable);
	}
	
	if (Objects.isNull(list) && Objects.nonNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.isNull(status) && Objects.isNull(issuename)) {
		list = issueDetailsRepository.getByCategoryAndSubcategory(categoryId, subCategoryId, pageable);
	}
	
	if (Objects.isNull(list) && Objects.nonNull(categoryId) && Objects.isNull(subCategoryId) && Objects.nonNull(status) && Objects.isNull(issuename)) {
		list = issueDetailsRepository.getByCategoryIdAndStatus(categoryId, status, pageable);
	}
	
	if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.nonNull(status) && Objects.isNull(issuename)) {
		list = issueDetailsRepository.getBySubCategoryIdAndStatus(subCategoryId, status, pageable);
	}
	
	if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.isNull(subCategoryId) && Objects.nonNull(status) && Objects.nonNull(issuename)) {
		list = issueDetailsRepository.getByIssueNameAndStatus(issuename, status, pageable);
	}
	
	if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.isNull(subCategoryId) && Objects.nonNull(status) && Objects.isNull(issuename)) {
		list = issueDetailsRepository.getByStatus(status, pageable);
	}
	
	if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.isNull(subCategoryId) && Objects.isNull(status) && Objects.nonNull(issuename)) {
		list = issueDetailsRepository.getByIssueName(issuename, pageable);
	}
	
	
	if (Objects.nonNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.nonNull(issuename) && Objects.nonNull(status)) {
		list = issueDetailsRepository.getByCategoryIdSubcategoryIdIssueNameAndIsActive(categoryId, subCategoryId, issuename, status, pageable);
	} 
	
	
	return list;
}








public GenericResponse getAllActive() {
	List<IssueDetails> assetTypeList = issueDetailsRepository.findAllByIsActiveOrderByModifiedDateDesc();
	if (CollectionUtils.isEmpty(assetTypeList)) {
		return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
				ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
	}
	List<IsssueDetresdto> assetTypeResponseList = assetTypeList.stream()
			.map(issuedetmapper::convertEntityToResponseDTO).collect(Collectors.toList());
	return Library.getSuccessfulResponse(assetTypeResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
			ErrorMessages.RECORED_FOUND);

}






public GenericResponse getCode() {
	MenuPrefix prefix = MenuPrefix.getType(ISSUE_CODE);
	String code = prefix.toString() + RandomUtil.getRandomNumber();
	while (true) {
		Optional<IssueDetails> TsEntity = issueDetailsRepository.findByIssuecodeIgnoreCase(code);
		if (TsEntity.isPresent()) {
			code = prefix.toString() + RandomUtil.getRandomNumber();
		} else {
			break;
		}
	}
	return Library.getSuccessfulResponse(code, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
			ErrorMessages.RECORED_FOUND);
}

	public GenericResponse getById(Long subCategoryId, Long categoryId) {
		List<IssueDetails> list = issueDetailsRepository.getByIds(subCategoryId, categoryId);
		if (list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<IssueDetailsResponseDto> IssueDetailsResponseDtoList = new ArrayList<IssueDetailsResponseDto>();
		list.forEach(pt -> {
			IssueDetailsResponseDtoList.add(convertIssueDetailsToDto(pt));
		});

		return Library.getSuccessfulResponse(IssueDetailsResponseDtoList,
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}

	
}
