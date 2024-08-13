package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.DESC;
import static com.oasys.helpdesk.constant.Constant.ID;
import static com.oasys.helpdesk.constant.Constant.MODIFIED_DATE;
import static com.oasys.helpdesk.constant.Constant.STATUS;
import static java.util.Objects.isNull;

import java.util.ArrayList;
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
import com.oasys.helpdesk.dto.GrievanceTicketCountResponseDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.entity.GrievanceCategoryEntity;
import com.oasys.helpdesk.entity.GrievanceIssueDetails;
import com.oasys.helpdesk.entity.GrievanceWorkflowEntity;
import com.oasys.helpdesk.mapper.GrievanceIssueDetailsMapper;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.repository.GrievanceCategoryRepository;
import com.oasys.helpdesk.repository.GrievanceIssueDetailsRepository;
import com.oasys.helpdesk.repository.GrievanceWorkflowRepository;
import com.oasys.helpdesk.request.GrievanceIssueRequestDto;
import com.oasys.helpdesk.response.GrievanceIssueResponseDto;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.RandomUtil;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class GrievanceIssueDetailsServiceImpl implements GrievanceIssueDetailsService {


	@Autowired
	private GrievanceIssueDetailsRepository gidRepository;

	@Autowired
	private GrievanceCategoryRepository grievanceCategoryRepository;

	@Autowired
	private PaginationMapper paginationMapper;

	@Autowired
	private GrievanceIssueDetailsMapper GrievanceIssueDetailsMapper;
	
	@Autowired
	private GrievanceWorkflowRepository grievanceworkflowrepository;
	


	public static final String GRIEVANCE_CATEGORY = "categoryid";

	public static final String GRIEVANCE_ISSUE_CODE = "category_issue_Code";


	@Override
	public GenericResponse createIssue(GrievanceIssueRequestDto issueRequestDto)
	{
		Optional<GrievanceIssueDetails> entity = gidRepository.findById(issueRequestDto.getId());
		if (entity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[]{ID}));
		}
		if(isNull(issueRequestDto.getId()) || isNull(issueRequestDto.getCategoryId()) ||
				isNull(issueRequestDto.getIssueName())) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[]{"MANDATORY_FIELDS"}));
		}
			GrievanceIssueDetails issueDetails = new GrievanceIssueDetails();
			issueDetails.setIssueName(issueRequestDto.getIssueName());
			if(issueRequestDto.getCategoryId() !=null) {
				GrievanceCategoryEntity grievancecategory =grievanceCategoryRepository.getById(issueRequestDto.getCategoryId());
				if (Objects.isNull(grievancecategory)) {
					return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
							ResponseMessageConstant.INVALID_REQUEST_PARM
							.getMessage(new Object[] { GRIEVANCE_CATEGORY }));
				}
				issueDetails.setCategory(grievancecategory);
			}
			issueDetails.setActive(issueRequestDto.isActive());
			issueDetails.setIssuecode(issueRequestDto.getIssuecode());
			issueDetails.setTypeofUser(issueRequestDto.getTypeofUser());
			gidRepository.save(issueDetails);

			return Library.getSuccessfulResponse(issueDetails, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_CREATED);
		

	} 

	public GenericResponse updateIssue(GrievanceIssueRequestDto issueRequestDto)
	{
			if (Objects.isNull(issueRequestDto.getId())) {
				return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
						ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { Constant.ID }));
			}
			GrievanceIssueDetails issueDetails = gidRepository.getById(issueRequestDto.getId());
			if(Objects.isNull(issueDetails)) {
				return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { Constant.ID }));
			}
			issueDetails.setIssueName(issueRequestDto.getIssueName());
			issueDetails.setActive(issueRequestDto.isActive());
			issueDetails.setTypeofUser(issueRequestDto.getTypeofUser());
			if(issueRequestDto.getCategoryId() !=null) {
				GrievanceCategoryEntity grievancecategory = grievanceCategoryRepository.getById(issueRequestDto.getCategoryId());
				if (Objects.isNull(grievancecategory)) {
					return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
							ResponseMessageConstant.INVALID_REQUEST_PARM
							.getMessage(new Object[] { GRIEVANCE_CATEGORY }));
				}
				issueDetails.setCategory(grievancecategory);	
			}

			gidRepository.save(issueDetails);

			return Library.getSuccessfulResponse(issueDetails, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_UPDATED);

	} 


	public GenericResponse getAllGIssueDetails() {
		List<GrievanceIssueDetails> IssueDetailsList = gidRepository.findAllByIsActiveOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(IssueDetailsList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<GrievanceIssueResponseDto> ResponseList = IssueDetailsList.stream()
				.map(GrievanceIssueDetailsMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(ResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	public GenericResponse getGIssueDetailsById(Long id) throws RecordNotFoundException {
		GrievanceIssueDetails issueDetails = gidRepository.getById(id);

		if (issueDetails== null ) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		}
		if (issueDetails!= null && issueDetails.getId() != null) {
			return Library.getSuccessfulResponse(GrievanceIssueDetailsMapper.convertEntityToResponseDTO(issueDetails), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		} else {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		}
	}



	public GenericResponse getGCode() {
		MenuPrefix prefix = MenuPrefix.getType(GRIEVANCE_ISSUE_CODE);
		String code = prefix.toString() + RandomUtil.getRandomNumber();
		while (true) {
			Optional<GrievanceIssueDetails> gciEntity = gidRepository.findByIssuecodeIgnoreCase(code);
			if (gciEntity.isPresent()) {
				code = prefix.toString() + RandomUtil.getRandomNumber();
			} else {
				break;
			}
		}
		return Library.getSuccessfulResponse(code, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	public GenericResponse getAllActive() {
		List<GrievanceIssueDetails> gcidList = gidRepository.findAllByIsActiveOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(gcidList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<GrievanceIssueResponseDto> ResponseList = gcidList.stream()
				.map(GrievanceIssueDetailsMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(ResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

	public GenericResponse getByCategory(Long categoryId) {
//		GrievanceCategoryEntity categoryNameO = new GrievanceCategoryEntity();
//		categoryNameO.setCategoryName(categoryName);
		
		List<GrievanceIssueDetails> gcidList = gidRepository.findAllByCategory(categoryId);
		if (CollectionUtils.isEmpty(gcidList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<GrievanceIssueResponseDto> ResponseList = gcidList.stream()
				.map(GrievanceIssueDetailsMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(ResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}






	public GenericResponse searchCategory(PaginationRequestDTO paginationDto) {
		Pageable pageable = null;
		Page<GrievanceIssueDetails> list = null;
		Long categoryId = null;
		String issueDetails = null;
		String typeofUser=null;
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
			if (Objects.nonNull(paginationDto.getFilters().get("categoryId"))
					&& !paginationDto.getFilters().get("categoryId").toString().trim().isEmpty()) {
				try {
					categoryId = Long.valueOf(paginationDto.getFilters().get("categoryId").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing categoryId :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}

			if (Objects.nonNull(paginationDto.getFilters().get("issueName"))
					&& !paginationDto.getFilters().get("issueName").toString().trim().isEmpty()) {
				try {
					issueDetails = String.valueOf(paginationDto.getFilters().get("issueName").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing issueDetails :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}
			if (Objects.nonNull(paginationDto.getFilters().get("typeofUser"))
					&& !paginationDto.getFilters().get("typeofUser").toString().trim().isEmpty()) {
				try {
					typeofUser = String.valueOf(paginationDto.getFilters().get("typeofUser").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing typeofUser :: {}", e);
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



		if (Objects.nonNull(categoryId) && Objects.nonNull(issueDetails)) {
			list = gidRepository.getBySubStringAndStatus(categoryId, issueDetails, pageable);
		}
		else if (Objects.nonNull(categoryId) && Objects.isNull(issueDetails)) {
			list = gidRepository.getByCategoryNameO(categoryId, pageable);
		}
		else if (Objects.isNull(categoryId) && Objects.nonNull(issueDetails)) {
			list = gidRepository.getByIssueDetail(issueDetails, pageable);
		}
		else if (Objects.nonNull(categoryId) && Objects.nonNull(issueDetails) && Objects.nonNull(typeofUser))  {
			list = gidRepository. getBySubStringAndStatusType(categoryId, issueDetails,typeofUser, pageable);
		}
		else if (Objects.nonNull(categoryId) && Objects.isNull(typeofUser)) {
			list = gidRepository.getByCategoryNameType(categoryId, pageable);
		}
		else if (Objects.isNull(categoryId) && Objects.nonNull(typeofUser)) {
			list = gidRepository.getByIssueDetailType(typeofUser, pageable);
		}
		else if (Objects.isNull(categoryId) && Objects.isNull(issueDetails) && Objects.isNull(typeofUser)) {
			list = gidRepository.getByCategoryNull( pageable);
		}
		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		Page<GrievanceIssueResponseDto> finalResponse = list.map(GrievanceIssueDetailsMapper::convertEntityToResponseDTO);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}

	
	
	
	

	
	
	
	
	//inspecting & excise officer work flow added new thing condition
		//grievanceworkflowrepository
		
	public GenericResponse getByCategoryid(GrievanceIssueRequestDto issueRequestDto) {

		//List<GrievanceIssueDetails> gcidList = gidRepository.findAllByCategory(issueRequestDto.getCategoryId());
		List<GrievanceIssueDetails> gcidList = gidRepository.findAllByCategoryAndTypeofUser(issueRequestDto.getCategoryId(),issueRequestDto.getTypeofUser());
		
		if (CollectionUtils.isEmpty(gcidList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<GrievanceIssueResponseDto> ResponseList = gcidList.stream()
				.map(GrievanceIssueDetailsMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(ResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
		
		
	
		/*
		 * Long category_id=issueRequestDto.getCategoryId();
		 * 
		 * List<GrievanceWorkflowEntity> gcidList =
		 * grievanceworkflowrepository.findAllByCategory(category_id); if
		 * (CollectionUtils.isEmpty(gcidList)) { return
		 * Library.getFailResponseCode(ErrorCode.THIS_CATEGORY_NOTMAPPED.getErrorCode(),
		 * ResponseMessageConstant.THIS_CATEGORY_NOTMAPPED .getMessage()); }
		 * 
		 * List<GrievanceIssueResponseDto> ticketCountResponseDTOList = new
		 * ArrayList<>();
		 * 
		 * Long issuedetailsid=(long) 0;
		 * 
		 * //Optional<GrievanceIssueDetails> depTypeEntity=null;
		 * 
		 * List<GrievanceIssueDetails> issuedetilslist=null;
		 * 
		 * Integer isid;
		 * 
		 * for (int i = 0; i < gcidList.size(); i++) { GrievanceIssueResponseDto
		 * issueresponsedto=new GrievanceIssueResponseDto();
		 * 
		 * //prints the elements of the List
		 * System.out.println("Listsize:::"+gcidList.size());
		 * issuedetailsid=gcidList.get(i).getGissueDetails().getId();
		 * 
		 * issuedetilslist=gidRepository.findAllById(issuedetailsid);
		 * 
		 * System.out.println("IssuedetailsLISTSIZE::::" + issuedetilslist.size());
		 * 
		 * System.out.println("ISSUEDETAILSID::" + issuedetailsid); // depTypeEntity =
		 * gidRepository.findById(issuedetailsid);
		 * issueresponsedto.setId(issuedetailsid);
		 * issueresponsedto.setIssueName(gcidList.get(i).getGissueDetails().getIssueName
		 * ()); ticketCountResponseDTOList.add(issueresponsedto);
		 * 
		 * }
		 * return Library.getSuccessfulResponse(ticketCountResponseDTOList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);	
		 */
			
			
	
	}
	
	
	
	

}
