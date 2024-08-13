package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.CODE;
import static com.oasys.helpdesk.constant.Constant.DESC;
import static com.oasys.helpdesk.constant.Constant.GRIEVANCE_CATEGORY_SLA;
import static com.oasys.helpdesk.constant.Constant.ID;
import static com.oasys.helpdesk.constant.Constant.ISSUE_DETAILS;
import static com.oasys.helpdesk.constant.Constant.MODIFIED_DATE;

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

import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.entity.GrievanceIssueDetails;
import com.oasys.helpdesk.entity.GrievancePriorityEntity;
import com.oasys.helpdesk.entity.GrievanceSlaEntity;
import com.oasys.helpdesk.mapper.GrievanceSlaMapper;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.repository.GrievanceCategoryRepository;
import com.oasys.helpdesk.repository.GrievanceIssueDetailsRepository;
import com.oasys.helpdesk.repository.GrievancePriorityRepository;
import com.oasys.helpdesk.repository.GrievanceSlaRepository;
import com.oasys.helpdesk.request.GrievanceSlaRequestDTO;
//import com.oasys.helpdesk.request.GrievanceSlaRequestDTO;
import com.oasys.helpdesk.response.GrievanceSlaResponseDTO;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.RandomUtil;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class GrievanceSlaServiceImpl implements GrievanceSlaService{

	@Autowired
	private GrievanceSlaRepository grievanceSlaRepository;


	@Autowired
	private GrievanceCategoryRepository grievanceCategoryRepository;
	
	@Autowired
	private GrievancePriorityRepository gpriorityRepository;
	

	@Autowired
	private PaginationMapper paginationMapper;

	@Autowired
	private GrievanceSlaMapper grievanceSlaMapper;
	
	@Autowired
	private GrievanceIssueDetailsRepository gissueDetailsrepo;


	public static final String GRIEVANCE_CATEGORY = "categoryName";

	@Override
	public GenericResponse createSla(GrievanceSlaRequestDTO requestDto)
	{
		Optional<GrievanceSlaEntity> slaOptional = grievanceSlaRepository.findByCodeIgnoreCase(requestDto.getCode());
		if (slaOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { CODE }));
		}
		
		slaOptional = grievanceSlaRepository.getByIssueDetailId(requestDto.getIssueDetailsId());
		if(slaOptional.isPresent())
		{
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { ISSUE_DETAILS }));
		}
		
		requestDto.setId(null);
		GrievanceSlaEntity grievanceSla = new GrievanceSlaEntity();
		
		GrievanceIssueDetails gissueDetails = gissueDetailsrepo.getById(requestDto.getIssueDetailsId());
		if (Objects.isNull(gissueDetails)) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM
					.getMessage(new Object[] { "GRIEVANCE_ISSUEDETAILS"}));
		}	
		grievanceSla.setGIssueDetails(gissueDetails);
		grievanceSla.setSla(requestDto.getSla());
		
		GrievancePriorityEntity grievance = gpriorityRepository.getById(requestDto.getPriority());
		if (Objects.isNull(grievance)) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM
					.getMessage(new Object[] { "GRIEVANCE_PRIORITY"}));
		}
		if (gissueDetails.getCategory().getId() != grievance.getCategory().getId()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ErrorMessages.CATEGORY_MISMATCH_IN_SLA);
		}

		grievanceSla.setPriority(grievance);
		grievanceSla.setCode(requestDto.getCode());
		grievanceSla.setStatus(requestDto.getStatus());
		grievanceSla.setTypeofUser(requestDto.getTypeofUser());
		grievanceSlaRepository.save(grievanceSla);

		return Library.getSuccessfulResponse(grievanceSla, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_CREATED);
	} 

	@Override
	public GenericResponse updateSla(GrievanceSlaRequestDTO requestDTO){
		if (Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		Optional<GrievanceSlaEntity> grievanceSlaOptional = grievanceSlaRepository
				.getByIssueDetailIdNotInSlaId(requestDTO.getIssueDetailsId(), requestDTO.getId());
		if (grievanceSlaOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { ISSUE_DETAILS }));
		}
		 grievanceSlaOptional = grievanceSlaRepository
				.findById(requestDTO.getId());
		if (!grievanceSlaOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		GrievanceSlaEntity grievanceSla = grievanceSlaOptional.get();
		grievanceSla.setSla(requestDTO.getSla());

		GrievancePriorityEntity grievance = gpriorityRepository.getById(requestDTO.getPriority());
		if (Objects.isNull(grievance)) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM
					.getMessage(new Object[] { "GRIEVANCE_PRIORITY"}));
		}
		grievanceSla.setPriority(grievance);
		
		GrievanceIssueDetails gissueDetails = gissueDetailsrepo.getById(requestDTO.getIssueDetailsId());
		if (Objects.isNull(gissueDetails)) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM
					.getMessage(new Object[] { "GRIEVANCE_ISSUEDETAILS"}));
		}	
		grievanceSla.setGIssueDetails(gissueDetails);
		
		if (gissueDetails.getCategory().getId() != grievance.getCategory().getId()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ErrorMessages.CATEGORY_MISMATCH_IN_SLA);
		}
		grievanceSla.setStatus(requestDTO.getStatus());
		grievanceSla.setTypeofUser(requestDTO.getTypeofUser());
		grievanceSlaRepository.save(grievanceSla);
		return Library.getSuccessfulResponse(grievanceSla, ErrorCode.CREATED.getErrorCode(),
				ErrorMessages.RECORED_UPDATED);
	}

	@Override
	public GenericResponse getById(Long Id) {
		Optional<GrievanceSlaEntity> GrievanceSlaEntity = grievanceSlaRepository.findById(Id);
		if (!GrievanceSlaEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(grievanceSlaMapper.convertEntityToResponseDTO(GrievanceSlaEntity.get()),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getAll() {
		List<GrievanceSlaEntity> grievanceSla = grievanceSlaRepository.findAllByOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(grievanceSla)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<GrievanceSlaResponseDTO> ResponseData = grievanceSla.stream()
				.map(grievanceSlaMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(ResponseData, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getAllActive() {
		List<GrievanceSlaEntity> configList = grievanceSlaRepository.findAllByStatusOrderByModifiedDateDesc(Boolean.TRUE);
		if (CollectionUtils.isEmpty(configList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<GrievanceSlaResponseDTO> ResponseData = configList.stream()
				.map(grievanceSlaMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(ResponseData, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getCode() {
		MenuPrefix prefix = MenuPrefix.getType(GRIEVANCE_CATEGORY_SLA);
		String code = prefix.toString() + RandomUtil.getRandomNumber();
		while (true) {
			Optional<GrievanceSlaEntity> grievancePriority = grievanceSlaRepository.findByCodeIgnoreCase(code);
			if (grievancePriority.isPresent()) {
				code = prefix.toString() + RandomUtil.getRandomNumber();
			} else {
				break;
			}
		}
		return Library.getSuccessfulResponse(code, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse searchBySla(PaginationRequestDTO paginationDto){
		Pageable pageable = null;
		Page<GrievanceSlaEntity> list = null;
		Long categoryId = null;
		String typeofUser=null;
		

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
					// id = Long.valueOf(paginationDto.getFilters().get(ID).toString());
					categoryId =  Long.valueOf(paginationDto.getFilters().get("categoryId").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing categoryId :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}

		}
		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("typeofUser"))
					&& !paginationDto.getFilters().get("typeofUser").toString().trim().isEmpty()) {
				try {
					// id = Long.valueOf(paginationDto.getFilters().get(ID).toString());
					typeofUser =  String.valueOf(paginationDto.getFilters().get("typeofUser").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing typeofUser :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}

		}

//		GrievanceCategoryEntity categoryNameO = new GrievanceCategoryEntity();
//		categoryNameO.setCategoryName(categoryName);

		
		if (Objects.nonNull(categoryId) ) {
			list = grievanceSlaRepository.getByCategoryNameO(categoryId, pageable);
		}
		else if (Objects.nonNull(categoryId) && Objects.isNull(typeofUser)) {
			list = grievanceSlaRepository.getByCategoryName(categoryId, pageable);
		}
		else if (Objects.nonNull(categoryId) && Objects.nonNull(typeofUser)) {
			list = grievanceSlaRepository.getByCategoryUser(categoryId,typeofUser, pageable);
		}
		else if (Objects.isNull(categoryId) && Objects.nonNull(typeofUser)) {
			list = grievanceSlaRepository.getByCategoryU(typeofUser, pageable);
		}
		else if (Objects.isNull(categoryId) && Objects.isNull(typeofUser)) {
			list = grievanceSlaRepository.getByCategoryNull( pageable);
		}

		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		Page<GrievanceSlaResponseDTO> finalResponse = list.map(grievanceSlaMapper::convertEntityToResponseDTO);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getById(Long categoryId, Long issueDetailsId) {
		Optional<GrievanceSlaEntity> list = grievanceSlaRepository.getByCategoryIdAndIssueDetailId(categoryId, issueDetailsId);
		if (!list.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(grievanceSlaMapper.convertEntityToResponseDTO(list.get()), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	
	
	
	
	
	
	
	
	
}
