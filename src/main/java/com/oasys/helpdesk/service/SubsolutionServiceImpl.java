package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.CODE;
import static com.oasys.helpdesk.constant.Constant.DEPARTMENT_NAME;
import static com.oasys.helpdesk.constant.Constant.ID;
import static com.oasys.helpdesk.constant.Constant.STATUS;
import static com.oasys.helpdesk.constant.Constant.SUBSOL_NAME;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.oasys.helpdesk.dto.SubsolutionRequestDTO;
import com.oasys.helpdesk.dto.SubsolutionResponseDTO;
import com.oasys.helpdesk.dto.TicketstausRequestDTO;
import com.oasys.helpdesk.dto.TicketstausResponseDTO;
import com.oasys.helpdesk.entity.Category;
import com.oasys.helpdesk.entity.IssueDetails;
import com.oasys.helpdesk.entity.SubCategory;
import com.oasys.helpdesk.entity.SubsolutionEntity;
import com.oasys.helpdesk.entity.TicketStatusEntity;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.mapper.SubsolutionMapper;
import com.oasys.helpdesk.mapper.TicketstatusMapper;
import com.oasys.helpdesk.repository.CategoryRepository;
import com.oasys.helpdesk.repository.SubCategoryRepository;
import com.oasys.helpdesk.repository.SubsolutionRepository;
import com.oasys.helpdesk.repository.TicketStatusrepository;
import com.oasys.helpdesk.response.IsssueDetresdto;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.RandomUtil;

import lombok.extern.log4j.Log4j2;
@Service
@Log4j2
public class SubsolutionServiceImpl  implements SubsolutionService {
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private SubsolutionRepository subsolutionRepository;
	
	
	@Autowired
	private PaginationMapper paginationMapper;
	
	public static final String SUBSOL_CODE = "Subsol Code";
	
	
	
	@Autowired
	private SubsolutionMapper subsolutionmapper;
	
	
	@Autowired
	SubCategoryRepository helpDeskTicketSubCategoryRepository;
	
	@Autowired
	CategoryRepository helpDeskTicketCategoryRepository;
	
	
	@Override
	public GenericResponse addsubsolution(SubsolutionRequestDTO requestDTO)	{

		Optional<SubsolutionEntity> ticketOptional=subsolutionRepository.findBySubsolutionIgnoreCase(requestDTO.getSubSolution().toUpperCase());

		if (ticketOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] {SUBSOL_NAME}));
		}
		ticketOptional = subsolutionRepository.findBySubcodeIgnoreCase(requestDTO.getSubCode());
		if (ticketOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { CODE }));
		}
		requestDTO.setId(null);
		
		SubsolutionEntity tcEntity = commonUtil.modalMap(requestDTO, SubsolutionEntity.class);
		
		 SubCategory helpDeskTicketSubCategory= helpDeskTicketSubCategoryRepository.getById(requestDTO.getSubCategoryId());
         Category helpDeskTicketCategory=helpDeskTicketCategoryRepository.getById(requestDTO.getCategoryId());
         
				if (Objects.isNull(helpDeskTicketSubCategory)) {
					return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
							ResponseMessageConstant.INVALID_REQUEST_PARM
									.getMessage(new Object[] { Constant.TICKET_SUB_CATEGORY_ID }));
				}
				tcEntity.setSubcategoryId(helpDeskTicketSubCategory);
				tcEntity.setCategoryId(helpDeskTicketCategory);
				tcEntity.setSubcode(requestDTO.getSubCode());
				tcEntity.setIssuedetails(requestDTO.getIssueDetails());
				tcEntity.setSubsolution(requestDTO.getSubSolution());
		subsolutionRepository.save(tcEntity);
		return Library.getSuccessfulResponse(tcEntity, ErrorCode.CREATED.getErrorCode(),ErrorMessages.RECORED_CREATED);
	}
	
	
	
	
	@Override
	public GenericResponse getAll() {
		List<SubsolutionEntity> DepList = subsolutionRepository.findAllByStatusOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(DepList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<SubsolutionResponseDTO> depResponseList = DepList.stream()
				.map(subsolutionmapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(depResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	
	@Override
	public GenericResponse getById(Long id) {
		Optional<SubsolutionEntity> depTypeEntity = subsolutionRepository.findById(id);
		if (!depTypeEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(subsolutionmapper.convertEntityToResponseDTO(depTypeEntity.get()), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	
	
	

	@Override
	public GenericResponse updatesubsolution(SubsolutionRequestDTO requestDTO)	{
		if(Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { ID }));
		}

		Optional<SubsolutionEntity> DeptOptional = subsolutionRepository
				.findByIssuedetailsIgnoreCaseNotInId(requestDTO.getSubSolution(), requestDTO.getId());
		
		if (DeptOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { SUBSOL_NAME}));
		}
		DeptOptional = subsolutionRepository.findById(requestDTO.getId());
		if (!DeptOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		SubsolutionEntity deptEntity = DeptOptional.get();
		deptEntity.setSubsolution(requestDTO.getSubSolution());
		deptEntity.setStatus(requestDTO.isStatus());
		deptEntity.setRemarks(requestDTO.getRemarks());
		
		subsolutionRepository.save(deptEntity);
		return Library.getSuccessfulResponse(deptEntity, ErrorCode.CREATED.getErrorCode(),ErrorMessages.RECORED_UPDATED);
	}
	
	
	@Override
	public GenericResponse getCode() {
		MenuPrefix prefix = MenuPrefix.getType(SUBSOL_CODE);
		String code = prefix.toString() + RandomUtil.getRandomNumber();
		while (true) {
			Optional<SubsolutionEntity> TsEntity = subsolutionRepository.findBySubcodeIgnoreCase(code);
			if (TsEntity.isPresent()) {
				code = prefix.toString() + RandomUtil.getRandomNumber();
			} else {
				break;
			}
		}
		return Library.getSuccessfulResponse(code, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	
	
//	public GenericResponse searchIssueDetails(PaginationRequestDTO paginationDto) {
//		Pageable pageable = null;
//		Page<SubsolutionEntity> list = null;
//		Long categoryId = null;
//		String issuename = null;
//		Long subCategoryId = null;
//		String problem=null;
//		Boolean status = null;
//
//		if (paginationDto.getSortOrder().equalsIgnoreCase(ASC)) {
//			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
//					Sort.by(Direction.ASC, paginationDto.getSortField()));
//		} else {
//			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
//					Sort.by(Direction.DESC, paginationDto.getSortField()));
//		}
//		if (Objects.nonNull(paginationDto.getFilters())) {
//			if (Objects.nonNull(paginationDto.getFilters().get("categoryId"))
//					&& !paginationDto.getFilters().get("categoryId").toString().trim().isEmpty()) {
//				categoryId = Long.valueOf(paginationDto.getFilters().get("categoryId").toString());
//			}
//			if (Objects.nonNull(paginationDto.getFilters().get("subCategoryId"))
//					&& !paginationDto.getFilters().get("subCategoryId").toString().trim().isEmpty()) {
//				subCategoryId = Long.valueOf(paginationDto.getFilters().get("subCategoryId").toString());
//			}
//			if (Objects.nonNull(paginationDto.getFilters().get("issueDetails"))
//					&& !paginationDto.getFilters().get("issueDetails").toString().trim().isEmpty()) {
//				issuename = String.valueOf(paginationDto.getFilters().get("issueDetails").toString());
//			}
//			if (Objects.nonNull(paginationDto.getFilters().get(STATUS))
//					&& !paginationDto.getFilters().get(STATUS).toString().trim().isEmpty()) {
//				status = Boolean.valueOf(paginationDto.getFilters().get(STATUS).toString());
//			}
//		}
//		
//		
//		list = getByFilter(categoryId, subCategoryId, issuename,status, pageable);
//		if (Objects.isNull(list)) {
//			list = subsolutionRepository.getAll(pageable);
//		}
//		
//		
//		if (Objects.isNull(list) || list.isEmpty()) {
//			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
//					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
//		}
//		Page<IsssueDetresdto> finalResponse = list.map(issuedetmapper::convertEntityToResponseDTO);
//		
//		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
//				ErrorMessages.RECORED_FOUND);
//		
//	}
//
//	
//	private Page<IssueDetails> getByFilter(Long categoryId, Long subCategoryId, String issuename, Boolean status, Pageable pageable){
//		Page<IssueDetails> list = null;
//		
//		if (Objects.isNull(list) && Objects.nonNull(categoryId) && Objects.isNull(subCategoryId) && Objects.isNull(status)) {
//			list = issueDetailsRepository.getByCategoryId(categoryId, pageable);
//		}
//		
//		if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.isNull(status)) {
//			list = issueDetailsRepository.getBySubcategoryId(subCategoryId, pageable);
//		}
//		
//		
//		if (Objects.isNull(list) && Objects.nonNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.nonNull(issuename) &&  Objects.isNull(status)) {
//			list = issueDetailsRepository.getByCategorySubcategoryAndIssueName(categoryId, subCategoryId, issuename, pageable);
//		} 
//		
//		if (Objects.isNull(list) && Objects.nonNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.nonNull(status) && Objects.isNull(issuename)) {
//			list = issueDetailsRepository.getByCategorySubcategoryAndStatus(categoryId, subCategoryId, status, pageable);
//		}
//		
//		if (Objects.isNull(list) && Objects.nonNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.isNull(status) && Objects.isNull(issuename)) {
//			list = issueDetailsRepository.getByCategoryAndSubcategory(categoryId, subCategoryId, pageable);
//		}
//		
//		if (Objects.isNull(list) && Objects.nonNull(categoryId) && Objects.isNull(subCategoryId) && Objects.nonNull(status) && Objects.isNull(issuename)) {
//			list = issueDetailsRepository.getByCategoryIdAndStatus(categoryId, status, pageable);
//		}
//		
//		if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.nonNull(status) && Objects.isNull(issuename)) {
//			list = issueDetailsRepository.getBySubCategoryIdAndStatus(subCategoryId, status, pageable);
//		}
//		
//		if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.isNull(subCategoryId) && Objects.nonNull(status) && Objects.nonNull(issuename)) {
//			list = issueDetailsRepository.getByIssueNameAndStatus(issuename, status, pageable);
//		}
//		
//		if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.isNull(subCategoryId) && Objects.nonNull(status) && Objects.isNull(issuename)) {
//			list = issueDetailsRepository.getByStatus(status, pageable);
//		}
//		
//		if (Objects.isNull(list) && Objects.isNull(categoryId) && Objects.isNull(subCategoryId) && Objects.isNull(status) && Objects.nonNull(issuename)) {
//			list = issueDetailsRepository.getByIssueName(issuename, pageable);
//		}
//		
//		
//		if (Objects.nonNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.nonNull(issuename) && Objects.nonNull(status)) {
//			list = issueDetailsRepository.getByCategoryIdSubcategoryIdIssueNameAndIsActive(categoryId, subCategoryId, issuename, status, pageable);
//		} 
//		
//		
//		return list;
//	}

	
	
	@Override
	public GenericResponse getAllActive() {
		List<SubsolutionEntity> assetTypeList = subsolutionRepository.findAllByStatusOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(assetTypeList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<SubsolutionResponseDTO> assetTypeResponseList = assetTypeList.stream()
				.map(subsolutionmapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(assetTypeResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

}
