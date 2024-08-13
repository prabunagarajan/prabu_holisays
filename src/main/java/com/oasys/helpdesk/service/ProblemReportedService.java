package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.CODE;
import static com.oasys.helpdesk.constant.Constant.STATUS;

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
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.entity.PriorityMaster;
import com.oasys.helpdesk.entity.ProblemReported;
import com.oasys.helpdesk.entity.SubCategory;
import com.oasys.helpdesk.mapper.PRMapper;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.repository.CategoryRepository;
import com.oasys.helpdesk.repository.PriorityRepository;
import com.oasys.helpdesk.repository.ProblemReportedRepository;
import com.oasys.helpdesk.repository.SubCategoryRepository;
import com.oasys.helpdesk.request.ProblemReportedRequestDto;
import com.oasys.helpdesk.response.PRResponseDTO;
import com.oasys.helpdesk.response.ProblemReportedResponseDto;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.RandomUtil;

import lombok.extern.log4j.Log4j2;


@Service
@Log4j2
public class ProblemReportedService {

	@Autowired
	ProblemReportedRepository helpDeskProblemReportedRepository;
	
	@Autowired
	PriorityRepository helpDeskTicketPriorityRepository;
	
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
	
	public static final String PR_CODE = "PR Code";
	
	
	public static final String CATEGORY= "category_id";
	
	public static final String SUBCATEGORY= "ticket_sub_category_id";
	
    @Autowired
    PRMapper  prMapper;
	

	public GenericResponse getAllProblemReported() {
		List<ProblemReported> HelpDeskProblemReportedList = helpDeskProblemReportedRepository.findAllByOrderByModifiedDateDesc();
		//List<ProblemReported> HelpDeskProblemReportedList = helpDeskProblemReportedRepository.findAllByIsActiveOrderByModifiedDateDesc();
			if(HelpDeskProblemReportedList==null ||HelpDeskProblemReportedList.size()==0 ) {
	        	throw new RecordNotFoundException("No record found");
	    }
		if (HelpDeskProblemReportedList.size() > 0) {
			List<ProblemReportedResponseDto> HelpDeskProblemReportedResponseDtoList = new ArrayList<ProblemReportedResponseDto>();
			HelpDeskProblemReportedList.forEach(pt -> {
				HelpDeskProblemReportedResponseDtoList.add(convertHelpDeskProblemReportedToDto(pt));
			});

			return Library.getSuccessfulResponse(HelpDeskProblemReportedResponseDtoList,
					ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException();
		}
	}
	
	public GenericResponse getProblemReportedById(Long id) throws RecordNotFoundException {
		ProblemReported helpDeskProblemReported = helpDeskProblemReportedRepository.getById(id);
		if (helpDeskProblemReported== null ) {
			throw new RecordNotFoundException("No record found");
		}
		if (helpDeskProblemReported!= null && helpDeskProblemReported.getId() != null) {
			return Library.getSuccessfulResponse(convertHelpDeskProblemReportedToDto(helpDeskProblemReported), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException();
		}
	}
	

	
	private ProblemReportedResponseDto convertHelpDeskProblemReportedToDto(ProblemReported helpDeskProblemReported) {

		ProblemReportedResponseDto helpDeskProblemReportedResponseDto = new ProblemReportedResponseDto();
		helpDeskProblemReportedResponseDto.setId(helpDeskProblemReported.getId());
		helpDeskProblemReportedResponseDto.setProblem(helpDeskProblemReported.getProblem());
//		Priority helpDeskTicketPriority= helpDeskTicketPriorityRepository.getById(helpDeskProblemReported.getPriority().getId());
//		helpDeskProblemReportedResponseDto.setPriorityName(helpDeskTicketPriority.getPriority());
		if (Objects.nonNull(helpDeskProblemReported.getSubCategoryId())) {
			helpDeskProblemReportedResponseDto
					.setTicketSubcategoryName(helpDeskProblemReported.getSubCategoryId().getSubCategoryName());
			helpDeskProblemReportedResponseDto.setSubCategoryId(helpDeskProblemReported.getSubCategoryId().getId());
			if (Objects.nonNull(helpDeskProblemReported.getSubCategoryId().getHelpDeskTicketCategory())) {
				helpDeskProblemReportedResponseDto.setTicketCategoryName(
						helpDeskProblemReported.getSubCategoryId().getHelpDeskTicketCategory().getCategoryName());
				helpDeskProblemReportedResponseDto
						.setCategoryId(helpDeskProblemReported.getSubCategoryId().getHelpDeskTicketCategory().getId());

			}
		}
		helpDeskProblemReportedResponseDto.setActive(helpDeskProblemReported.isActive());
		String createduser=commonDataController.getUserNameById(helpDeskProblemReported.getCreatedBy());
		String modifieduser=commonDataController.getUserNameById(helpDeskProblemReported.getModifiedBy());
		helpDeskProblemReportedResponseDto.setCreatedBy(createduser);
		helpDeskProblemReportedResponseDto.setCreatedDate(helpDeskProblemReported.getCreatedDate());
		helpDeskProblemReportedResponseDto.setModifiedBy(modifieduser);
		helpDeskProblemReportedResponseDto.setModifiedDate(helpDeskProblemReported.getModifiedDate());
		helpDeskProblemReportedResponseDto.setPrCode(helpDeskProblemReported.getPrCode());
		
		return helpDeskProblemReportedResponseDto;

	}
	
	public GenericResponse createProblemReported(ProblemReportedRequestDto helpDeskProblemReportedRequestDto)
	{
		Optional<ProblemReported> optional = helpDeskProblemReportedRepository.findByPrCodeIgnoreCase(helpDeskProblemReportedRequestDto.getPrCode());
		if (optional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { Constant.PR_CODE }));

		}
		ProblemReported helpDeskProblemReported = new ProblemReported();
		helpDeskProblemReported.setProblem(helpDeskProblemReportedRequestDto.getProblem());

		SubCategory helpDeskTicketSubCategory = helpDeskTicketSubCategoryRepository
				.getById(helpDeskProblemReportedRequestDto.getTicketsubcategoryid());

		if (Objects.isNull(helpDeskTicketSubCategory)) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM
							.getMessage(new Object[] { Constant.TICKET_SUB_CATEGORY_ID }));
		}
		helpDeskProblemReported.setSubCategoryId(helpDeskTicketSubCategory);
		helpDeskProblemReported.setPrCode(helpDeskProblemReportedRequestDto.getPrCode());

		helpDeskProblemReported.setActive(helpDeskProblemReportedRequestDto.isActive());
		helpDeskProblemReportedRepository.save(helpDeskProblemReported);

		return Library.getSuccessfulResponse(helpDeskProblemReported, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_CREATED);

	} 
	
//	public GenericResponse searchProblemReported(PaginationRequestDTO paginationDto) throws ParseException {
//		List<ProblemReportedResponseDto> problemReportedResponseDtoList = new ArrayList<>();
//		StringBuilder recordQuery = new StringBuilder("Select * from problem_reported reported  ");
//		StringBuilder countQuery = new StringBuilder("Select count(*) from problem_reported reported "); 
//
//		
//		
//		if (paginationDto.getFilters() != null) {
//			log.info("ProblemReported filters :::" + paginationDto.getFilters());
//
//			if (paginationDto.getFilters().get("id") != null
//					&& !paginationDto.getFilters().get("id").toString().trim().isEmpty()) {
//				Long id = Long.parseLong(paginationDto.getFilters().get("id").toString());
//
//				recordQuery.append(" and reported.id = " + id);
//				countQuery.append(" and reported.id = " + id);
//
//			}
//			if (paginationDto.getFilters().get("problem") != null
//					&& !paginationDto.getFilters().get("problem").toString().trim().isEmpty()) {
//				String problem = paginationDto.getFilters().get("problem").toString();
//				log.debug("problem :"+ paginationDto.getFilters().get("problem"));
//				recordQuery.append( " and reported.problem = '"+problem +"'");
//				countQuery.append( " and reported.problem = '"+problem+"'");
//			}
//			
//			
//			if (paginationDto.getFilters().get("priorityid") != null
//					&& !paginationDto.getFilters().get("priorityid").toString().trim().isEmpty()) {
//				String priorityid = paginationDto.getFilters().get("priorityid").toString();
//				log.debug("priorityid :"+ paginationDto.getFilters().get("priorityid"));
//				recordQuery.append( " and reported.priority_id = '"+priorityid +"'");
//				countQuery.append( " and reported.priority_id = '"+priorityid+"'");
//			}
//			
//			if (paginationDto.getFilters().get("ticketsubcategoryid") != null
//					&& !paginationDto.getFilters().get("ticketsubcategoryid").toString().trim().isEmpty()) {
//				String ticketsubcategoryid = paginationDto.getFilters().get("ticketsubcategoryid").toString();
//				log.debug("ticketsubcategoryid :"+ paginationDto.getFilters().get("ticketsubcategoryid"));
//				recordQuery.append( " and reported.ticket_sub_category_id = '"+ticketsubcategoryid +"'");
//				countQuery.append( " and reported.ticket_sub_category_id = '"+ticketsubcategoryid+"'");
//			}
//			
//			if (paginationDto.getFilters().get("category_id") != null
//					&& !paginationDto.getFilters().get("category_id").toString().trim().isEmpty()) {
//				String categoryid = paginationDto.getFilters().get("category_id").toString();
//				log.debug("categoryid :"+ paginationDto.getFilters().get("category_id"));
//				recordQuery.append( " and reported.category_id = '"+categoryid +"'");
//				countQuery.append( " and reported.category_id = '"+categoryid+"'");
//			}
//			
//			
//			
//			if (paginationDto.getFilters().get("isActive") != null
//					&& !paginationDto.getFilters().get("isActive").toString().trim().isEmpty()) {
//				String isActive = paginationDto.getFilters().get("isActive").toString();
//				log.debug("isActive :"+ paginationDto.getFilters().get("isActive"));
//				recordQuery.append( " and reported.is_active = '"+isActive +"'");
//				countQuery.append( " and reported.is_active = '"+isActive+"'");
//			}
//			
//			
//
//		}
//		
//		BigInteger totalCount1 = (BigInteger) entityManager.createNativeQuery(countQuery.toString()).getSingleResult();
//		Integer totalCount = totalCount1.intValue();
//
//		javax.persistence.Query query = entityManager.createNativeQuery(recordQuery.toString(),
//				ProblemReported.class);
//		query.setFirstResult(paginationDto.getPageNo() * paginationDto.getPaginationSize());
//		query.setMaxResults(paginationDto.getPaginationSize());
//		List<ProblemReported> resultList = query.getResultList();
//		if (resultList.isEmpty()) {
//			throw new RecordNotFoundException();
//		} else {
//			for (ProblemReported problemReported : resultList) {
//				ProblemReportedResponseDto problemReportedResponseDto = new ProblemReportedResponseDto();
//				problemReportedResponseDto.setId(problemReported.getId());
//				problemReportedResponseDto.setProblem(problemReported.getProblem());
//				Priority helpDeskTicketPriority= helpDeskTicketPriorityRepository.getById(problemReported.getPriority().getId());
//				problemReportedResponseDto.setPriorityName(helpDeskTicketPriority.getPriority());
//				SubCategory helpDeskTicketSubCategory= helpDeskTicketSubCategoryRepository.getById(problemReported.getSubCategoryId().getId());
//				problemReportedResponseDto.setTicketSubcategoryName(helpDeskTicketSubCategory.getSubCategoryName());
//				Category helpDeskTicketCategory= helpDeskTicketCategoryRepository.getById(helpDeskTicketSubCategory.getHelpDeskTicketCategory().getId());
//				problemReportedResponseDto.setTicketCategoryName(helpDeskTicketCategory.getCategoryName());
//				problemReportedResponseDto.setActive(problemReported.isActive());
//				String createduser=commonDataController.getUserNameById(problemReported.getCreatedBy());
//				String modifieduser=commonDataController.getUserNameById(problemReported.getModifiedBy());
//				problemReportedResponseDto.setCreatedBy(createduser);
//				problemReportedResponseDto.setCreatedDate(problemReported.getCreatedDate());
//				problemReportedResponseDto.setModifiedBy(modifieduser);
//				problemReportedResponseDto.setModifiedDate(problemReported.getModifiedDate());
//				problemReportedResponseDtoList.add(problemReportedResponseDto);
//
//			}
//			log.debug("found records : " + problemReportedResponseDtoList.size());
//
//			PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
//			paginationResponseDTO.setContents(problemReportedResponseDtoList);
//			paginationResponseDTO.setTotalElements(totalCount.longValue());
//			paginationResponseDTO.setNumberOfElements(resultList.size());
//			if (paginationDto.getPaginationSize() > 0 && totalCount > 0) {
//				paginationResponseDTO.setTotalPages(totalCount / paginationDto.getPaginationSize());
//				if (totalCount < paginationDto.getPaginationSize())
//					paginationResponseDTO.setTotalPages(1);
//				else if (totalCount % paginationDto.getPaginationSize() > 0) {
//					paginationResponseDTO.setTotalPages(paginationResponseDTO.getTotalPages() + 1);
//				}
//
//			} else
//				paginationResponseDTO.setTotalPages(0);
//
//			return Library.getSuccessfulResponse(paginationResponseDTO, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
//					ErrorMessages.RECORED_FOUND);
//
//		}
//	}
//	
	
	
	public GenericResponse searchProblemReported(PaginationRequestDTO paginationDto) {
		Pageable pageable = null;
		Page<ProblemReported> list = null;
		Long categoryId = null;
		String probReported = null;
		Long subCategoryId = null;
		String problem=null;
		Boolean status = null;
		if(StringUtils.isNotBlank(paginationDto.getSortField())) {
//			if(PRCAT_ID.equals(paginationDto.getSortField())) {
//				paginationDto.setSortField(CATEGORY);
//			}
//			if(PRSUBCAT_ID.equals(paginationDto.getSortField())) {
//				paginationDto.setSortField(SUBCATEGORY);
//			}
		}
		if (paginationDto.getSortOrder().equalsIgnoreCase(ASC)) {
			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
					Sort.by(Direction.ASC, paginationDto.getSortField()));
		} else {
			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
					Sort.by(Direction.DESC, paginationDto.getSortField()));
		}
		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("category_id"))
					&& !paginationDto.getFilters().get("category_id").toString().trim().isEmpty()) {
				categoryId = Long.valueOf(paginationDto.getFilters().get("category_id").toString());
			}
			if (Objects.nonNull(paginationDto.getFilters().get("ticketsubcategoryid"))
					&& !paginationDto.getFilters().get("ticketsubcategoryid").toString().trim().isEmpty()) {
				subCategoryId = Long.valueOf(paginationDto.getFilters().get("ticketsubcategoryid").toString());
			}
			if (Objects.nonNull(paginationDto.getFilters().get("problem"))
					&& !paginationDto.getFilters().get("problem").toString().trim().isEmpty()) {
				probReported = String.valueOf(paginationDto.getFilters().get("problem").toString());
			}
			if (Objects.nonNull(paginationDto.getFilters().get(STATUS))
					&& !paginationDto.getFilters().get(STATUS).toString().trim().isEmpty()) {
				status = Boolean.valueOf(paginationDto.getFilters().get(STATUS).toString());
			}
		}
		list = getByFilter(categoryId, subCategoryId, probReported,status, pageable);
		if (Objects.isNull(list)) {
			list = helpDeskProblemReportedRepository.getAll(pageable);
		}
		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		Page<PRResponseDTO> finalResponse = list.map(prMapper::convertEntityToResponseDTO);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	
	
	private Page<ProblemReported> getByFilter(Long categoryId, Long subCategoryId, String probReported, Boolean status, Pageable pageable){
		Page<ProblemReported> list = null;
		if (Objects.nonNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.nonNull(probReported)
				&& Objects.nonNull(status)) {
			// list =
			// helpDeskProblemReportedRepository.getByCategoryIdSubCategoryIdProblemAndIsActive(categoryId,
			// subCategoryId, probReported, status, pageable);

			list = helpDeskProblemReportedRepository.getBySubCategoryIdProblemAndIsActive(subCategoryId, probReported,
					status, pageable);

		}

		if (Objects.nonNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.nonNull(probReported)
				&& Objects.isNull(status)) {
			list = helpDeskProblemReportedRepository.getByCategoryIdAndSubCategoryIdAndProblem(categoryId,subCategoryId, probReported,
					pageable);
		}

		if (Objects.isNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.isNull(probReported)
				&& Objects.isNull(status)) {
			list = helpDeskProblemReportedRepository.getBySubCategoryId(subCategoryId, pageable);
		}
		
		if (Objects.nonNull(categoryId) && Objects.isNull(subCategoryId) && Objects.isNull(probReported)
				&& Objects.isNull(status)) {
			list = helpDeskProblemReportedRepository.getByCategoryId(categoryId, pageable);
		}
		
		if (Objects.nonNull(categoryId) && Objects.isNull(subCategoryId) && Objects.nonNull(probReported)
				&& Objects.isNull(status)) {
			list = helpDeskProblemReportedRepository.getByCategoryIdAndProblem(categoryId, probReported,
					pageable);
		}
		
		if (Objects.isNull(categoryId) && Objects.isNull(subCategoryId) && Objects.nonNull(probReported)
				&& Objects.isNull(status)) {
			list = helpDeskProblemReportedRepository.getByProblem( probReported,pageable);
		}
		
	
		return list;
	}
	
	
	
	
	
	
	
	public GenericResponse editProblemReported(ProblemReportedRequestDto helpDeskProblemReportedRequestDto)
	{
		ProblemReported helpDeskProblemReported = helpDeskProblemReportedRepository.getById(helpDeskProblemReportedRequestDto.getId());
		if (helpDeskProblemReported.getId() != null ) {
	        helpDeskProblemReported.setProblem(helpDeskProblemReportedRequestDto.getProblem());
			
			if(helpDeskProblemReportedRequestDto.getTicketsubcategoryid() !=null && helpDeskProblemReportedRequestDto.getTicketsubcategoryid()>0) {
            SubCategory helpDeskTicketSubCategory= helpDeskTicketSubCategoryRepository.getById(helpDeskProblemReportedRequestDto.getTicketsubcategoryid());
				if (Objects.isNull(helpDeskTicketSubCategory)) {
					return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
							ResponseMessageConstant.INVALID_REQUEST_PARM
									.getMessage(new Object[] { Constant.TICKET_SUB_CATEGORY_ID }));
				}
            helpDeskProblemReported.setSubCategoryId(helpDeskTicketSubCategory);
			}
			helpDeskProblemReported.setActive(helpDeskProblemReportedRequestDto.isActive());
            helpDeskProblemReportedRepository.save(helpDeskProblemReported);
			
			return Library.getSuccessfulResponse(helpDeskProblemReported, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_CREATED);
			
		} else {
			throw new RecordNotFoundException();
		}
	} 
	
	
	
	public GenericResponse getCode() {
		MenuPrefix prefix = MenuPrefix.getType(PR_CODE);
		String code = prefix.toString() + RandomUtil.getRandomNumber();
		while (true) {
			Optional<ProblemReported> PrEntity = helpDeskProblemReportedRepository.findByPrCodeIgnoreCase(code);
			if (PrEntity.isPresent()) {
				code = prefix.toString() + RandomUtil.getRandomNumber();
			} else {
				break;
			}
		}
		return Library.getSuccessfulResponse(code, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	
	

	public GenericResponse getAllActive() {
	
		
		List<ProblemReported> assetTypeList = helpDeskProblemReportedRepository.findAllByIsActiveOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(assetTypeList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<PRResponseDTO> assetTypeResponseList = assetTypeList.stream()
				.map(prMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(assetTypeResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	public GenericResponse getById(Long categoryId, Long subcategoryId) {
		List<ProblemReported> actualProblems = helpDeskProblemReportedRepository.findByCategoryAndSubcategoryId(categoryId,subcategoryId);
		if (CollectionUtils.isEmpty(actualProblems)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<PRResponseDTO> actualProblemResponseDtos = actualProblems.stream()
				.map(prMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(actualProblemResponseDtos, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	
	
	
	
}
