package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.CODE;
import static com.oasys.helpdesk.constant.Constant.DESC;
import static com.oasys.helpdesk.constant.Constant.GRIEVANCE_CATEGORY_WORKFLOW;
import static com.oasys.helpdesk.constant.Constant.ID;
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

import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.entity.GrievanceCategoryEntity;
import com.oasys.helpdesk.entity.GrievanceIssueDetails;
import com.oasys.helpdesk.entity.GrievanceSlaEntity;
import com.oasys.helpdesk.entity.GrievanceWorkflowEntity;
import com.oasys.helpdesk.entity.RoleMaster;
import com.oasys.helpdesk.entity.UserEntity;
import com.oasys.helpdesk.mapper.GrievanceWorkflowMapper;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.repository.GrievanceIssueDetailsRepository;
import com.oasys.helpdesk.repository.GrievancePriorityRepository;
import com.oasys.helpdesk.repository.GrievanceSlaRepository;
import com.oasys.helpdesk.repository.GrievanceWorkflowRepository;
import com.oasys.helpdesk.repository.RoleMasterRepository;
import com.oasys.helpdesk.repository.UserRepository;
import com.oasys.helpdesk.request.GrievanceWorkflowRequestDTO;
import com.oasys.helpdesk.response.GrievanceWorkflowResponseDTO;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.RandomUtil;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class GrievanceWorkflowServiceimpl implements GrievanceWorkflowService{

	@Autowired
	private GrievanceWorkflowRepository workflowRepository;
	
	@Autowired
	private GrievanceSlaRepository grievanceSlaRepository;

	@Autowired
	private GrievanceIssueDetailsRepository gidRepository;
	
	@Autowired
	private GrievancePriorityRepository gpriorityRepository;
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleMasterRepository roleMasterRepo;
	
	@Autowired
	private GrievanceWorkflowMapper grievanceWorkflowMapper;
	@Autowired
	private PaginationMapper paginationMapper;
	
	
	@Override
	public GenericResponse createWorkflow(GrievanceWorkflowRequestDTO requestDto)
	
	{
		Optional<GrievanceWorkflowEntity> gwfOptional = workflowRepository.findByCodeIgnoreCase(requestDto.getCode());
		if (gwfOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { CODE }));
		}
		
		requestDto.setId(null);
		
		Long mansla=(long) 0;
		GrievanceWorkflowEntity grievanceWorkflow = new GrievanceWorkflowEntity();
		
		GrievanceSlaEntity slaentity=new GrievanceSlaEntity();
		Optional<GrievanceSlaEntity> grievanceSlaEntity =null;
		if(requestDto.getSla().equals(mansla)) {
			System.out.println("SLA IS 0");
	
		}
		else {
			 grievanceSlaEntity = grievanceSlaRepository.findById(requestDto.getSla());
			if (!grievanceSlaEntity.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "sla" }));
			}
		grievanceWorkflow.setSla(grievanceSlaEntity.get());
		}
		
		List<GrievanceWorkflowEntity> workflowEntities = workflowRepository
				.findByIssueDetailId(grievanceSlaEntity.get().getGIssueDetails().getId());
		if (!CollectionUtils.isEmpty(workflowEntities)) {
			throw new InvalidDataValidation(ErrorMessages.WORKFLOW_ALREADY_EXIST);
		}
		
		grievanceWorkflow.setCode(requestDto.getCode());
		grievanceWorkflow.setStatus(requestDto.isStatus());

//		UserEntity Entity = userRepository.getById(requestDto.getAssignToId());
//		if (Objects.isNull(Entity)) {
//			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
//					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "assignToId " }));
//		}

		grievanceWorkflow.setAssignto_id(requestDto.getAssignToId());

//		RoleMaster entity = roleMasterRepo.getById(requestDto.getAssignGroup());
//		if (Objects.isNull(entity)) {
//			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
//					ResponseMessageConstant.INVALID_REQUEST_PARM
//							.getMessage(new Object[] { "assignGroup " }));
//		}
		grievanceWorkflow.setAssignto_Group(requestDto.getAssignGroup());
		grievanceWorkflow.setTypeofUser(requestDto.getTypeofUser());
		workflowRepository.save(grievanceWorkflow);

		return Library.getSuccessfulResponse(grievanceWorkflow, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_CREATED);
	} 
	
	@Override
	public GenericResponse updateWorkflow(GrievanceWorkflowRequestDTO requestDTO){
		if (Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		
		Optional<GrievanceWorkflowEntity> grievanceOptional = workflowRepository.findById(requestDTO.getId());
		if (!grievanceOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}

		GrievanceWorkflowEntity grievanceWorkflow = grievanceOptional.get();

		Optional<GrievanceSlaEntity> grievanceSlaEntity = grievanceSlaRepository.findById(requestDTO.getSla());
		if (!grievanceSlaEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "sla " }));
		}
		List<GrievanceWorkflowEntity> workflowEntities = workflowRepository
				.findByIssueDetailIdNotInWorkflowId(grievanceSlaEntity.get().getGIssueDetails().getId(), requestDTO.getId());
		if (!CollectionUtils.isEmpty(workflowEntities)) {
			throw new InvalidDataValidation(ErrorMessages.WORKFLOW_ALREADY_EXIST);
		}
		
		grievanceWorkflow.setSla(grievanceSlaEntity.get());
		grievanceWorkflow.setAssignto_Group(requestDTO.getAssignGroup());
		grievanceWorkflow.setAssignto_id(requestDTO.getAssignToId());
		grievanceWorkflow.setStatus(requestDTO.isStatus());
		grievanceWorkflow.setTypeofUser(requestDTO.getTypeofUser());
		workflowRepository.save(grievanceWorkflow);
		return Library.getSuccessfulResponse(grievanceWorkflow, ErrorCode.CREATED.getErrorCode(),
				ErrorMessages.RECORED_UPDATED);
	}
	
	
	@Override
	public GenericResponse getById(Long Id) {
		Optional<GrievanceWorkflowEntity> grievancewfEntity = workflowRepository.findById(Id);
		if (!grievancewfEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(grievanceWorkflowMapper.convertEntityToResponseDTO(grievancewfEntity.get()),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}
	
	
	@Override
	public GenericResponse getAll() {
		List<GrievanceWorkflowEntity> grievanceSla = workflowRepository.findAllByOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(grievanceSla)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<GrievanceWorkflowResponseDTO> ResponseData = grievanceSla.stream()
				.map(grievanceWorkflowMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(ResponseData, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	@Override
	public GenericResponse getAllActive() {
		List<GrievanceWorkflowEntity> List = workflowRepository.findAllByStatusOrderByModifiedDateDesc(Boolean.TRUE);
		if (CollectionUtils.isEmpty(List)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<GrievanceWorkflowResponseDTO> ResponseData = List.stream()
				.map(grievanceWorkflowMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(ResponseData, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	
	@Override
	public GenericResponse getCode() {
		MenuPrefix prefix = MenuPrefix.getType(GRIEVANCE_CATEGORY_WORKFLOW);
		String code = prefix.toString() + RandomUtil.getRandomNumber();
		while (true) {
			Optional<GrievanceWorkflowEntity> grievance = workflowRepository.findByCodeIgnoreCase(code);
			if (grievance.isPresent()) {
				code = prefix.toString() + RandomUtil.getRandomNumber();
			} else {
				break;
			}
		}
		return Library.getSuccessfulResponse(code, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}	
	
	
	@Override
	public GenericResponse searchByGWorkflow(PaginationRequestDTO paginationDto){
		Pageable pageable = null;
		Page<GrievanceWorkflowEntity> list = null;
		Long categoryId = null;
		Long issueDetailsId = null;
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
					categoryId = Long.valueOf(paginationDto.getFilters().get("categoryId").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing categoryId :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}

		}
		
		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("issueDetailsId"))
					&& !paginationDto.getFilters().get("issueDetailsId").toString().trim().isEmpty()) {
				try {
					// id = Long.valueOf(paginationDto.getFilters().get(ID).toString());
					issueDetailsId = Long.valueOf(paginationDto.getFilters().get("issueDetailsId").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing issueDetailsId :: {}", e);
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
		
		GrievanceCategoryEntity categoryNameO = new GrievanceCategoryEntity();
		categoryNameO.setId(categoryId);
		GrievanceIssueDetails  issueDetailsO = new GrievanceIssueDetails();
		issueDetailsO.setId(issueDetailsId);
		
		if (Objects.nonNull(categoryId) && Objects.nonNull(issueDetailsId)) {
			list = workflowRepository.getBySubStringAndStatus(categoryNameO, issueDetailsO, pageable);
		}
		else if (Objects.nonNull(categoryId) && Objects.isNull(issueDetailsId) ) {
			list = workflowRepository.getByCategoryNameO(categoryNameO, pageable);
		}
		
		else if (Objects.nonNull(issueDetailsId) && Objects.isNull(categoryId) ) {
			list = workflowRepository.getByIssueDetails(issueDetailsO, pageable);
		}
		else if (Objects.nonNull(issueDetailsId) && Objects.nonNull(categoryId)  && Objects.nonNull(typeofUser)) {
			list = workflowRepository.getByIssueDetailsAll(issueDetailsO,categoryNameO,typeofUser, pageable);
		}
		else if (Objects.nonNull(issueDetailsId) && Objects.nonNull(categoryId)  && Objects.isNull(typeofUser)) {
			list = workflowRepository.getByIssueDetailsAllNull(issueDetailsO,categoryNameO, pageable);
		}
		else if (Objects.isNull(issueDetailsId) && Objects.isNull(categoryId)  && Objects.isNull(typeofUser)) {
			list = workflowRepository.getByIssueDetailsAllDataNull(pageable);
		}
		else if (Objects.isNull(issueDetailsId) && Objects.isNull(categoryId)  && Objects.nonNull(typeofUser)) {
			list = workflowRepository.getByIssueDetailsType(typeofUser,pageable);
		}

		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		Page<GrievanceWorkflowResponseDTO> finalResponse = list.map(grievanceWorkflowMapper::convertEntityToResponseDTO);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
