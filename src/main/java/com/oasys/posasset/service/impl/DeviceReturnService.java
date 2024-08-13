package com.oasys.posasset.service.impl;

import static com.oasys.posasset.constant.Constant.ASC;
import static com.oasys.posasset.constant.Constant.CREATED_DATE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.PaginationResponseDTO;
import com.oasys.helpdesk.dto.WorkflowDTO;
import com.oasys.helpdesk.entity.AssetAccessoriesEntity;
import com.oasys.helpdesk.entity.AssetTypeEntity;
import com.oasys.helpdesk.repository.AssetAccessoriesRepository;
import com.oasys.helpdesk.repository.AssetTypeRepository;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.posasset.constant.ApprovalStatus;
import com.oasys.posasset.dto.ApprovalDTO;
import com.oasys.posasset.dto.DeviceReturnDTO;
import com.oasys.posasset.entity.DeviceReturnEntity;
import com.oasys.posasset.entity.DeviceReturnLogEntity;
import com.oasys.posasset.repository.DeviceReturnLogRepository;
import com.oasys.posasset.repository.DeviceReturnRepository;
import com.oasys.posasset.repository.DevicedamageRepository;
import com.oasys.posasset.repository.DevicelostRepository;
import com.oasys.posasset.service.WorkFlowService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class DeviceReturnService {

	@Autowired
	DeviceReturnRepository deviceReturnRepository;
	
	@Autowired
	private DevicelostRepository devicelostrepository;
	
	@Autowired
	private DevicedamageRepository devicedamagerepository;
	
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	WorkFlowService workFlowService;
	
	@Autowired
	private AssetTypeRepository assetyperepository;
	
	@Autowired
	DeviceReturnLogRepository deviceReturnLogRepository;
	
	@Autowired
	private AssetAccessoriesRepository assetaccessoriesrepository;
	
	public  GenericResponse save(DeviceReturnDTO deviceReturnDTO) {
		try {			
			Optional<DeviceReturnEntity> deviceReturnDetails=deviceReturnRepository
					.findByLicenseNoAndShopId(deviceReturnDTO.getLicenseNo(), deviceReturnDTO.getShopId());
			if(!deviceReturnDetails.isPresent()) {
				DeviceReturnEntity deviceReturnEntity=commonUtil.modalMap(deviceReturnDTO, DeviceReturnEntity.class);
				Optional<AssetTypeEntity> assetTypeEntity = assetyperepository.findById(deviceReturnDTO.getAccessoriesId());
				if (!assetTypeEntity.isPresent()) {
					throw new InvalidDataValidation(
							ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Accessories Id" }));
				}

				deviceReturnEntity.setAccessoriesId(assetTypeEntity.get());
				
				Optional<AssetAccessoriesEntity> assetaccesoruesEntity = assetaccessoriesrepository.findById(deviceReturnDTO.getDevice());
				if (!assetaccesoruesEntity.isPresent()) {
					throw new InvalidDataValidation(
							ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Device ID" }));
				}
				
				deviceReturnEntity.setDevice(assetaccesoruesEntity.get());
				deviceReturnEntity.setApplicationDate(deviceReturnDTO.getApplicationDate());
				deviceReturnEntity.setStatus(deviceReturnDTO.getStatus());
				deviceReturnEntity=deviceReturnRepository.save(deviceReturnEntity);
								
				WorkflowDTO workflowStatusUpdateDto = new WorkflowDTO();
				workflowStatusUpdateDto.setApplicationNumber(deviceReturnDTO.getApplicationNumber());
				workflowStatusUpdateDto.setModuleNameCode(deviceReturnDTO.getModuleNameCode());
				workflowStatusUpdateDto.setSubModuleNameCode(deviceReturnDTO.getSubModuleNameCode());
				workflowStatusUpdateDto.setEvent(deviceReturnDTO.getEvent());
				workflowStatusUpdateDto.setLevel(deviceReturnDTO.getLevel());
				workFlowService.callDeviceReturnWorkFlowService(workflowStatusUpdateDto);	
								
				DeviceReturnLogEntity deviceReturnLogEntity=new DeviceReturnLogEntity();
				
				deviceReturnLogEntity.setApplicationNo(deviceReturnDTO.getApplicationNumber());
				deviceReturnLogEntity.setAction("INITIATED");
				deviceReturnLogEntity.setComments("Application submitted successfully");
				deviceReturnLogEntity.setUserName(deviceReturnDTO.getCreatedbyName());
				deviceReturnLogEntity.setLevel(deviceReturnDTO.getLevel());
				
				deviceReturnLogRepository.save(deviceReturnLogEntity);
				
				return Library.getSuccessfulResponse(deviceReturnEntity, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
						ErrorMessages.RECORED_CREATED);
			} else {
				return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), "Record already exist");
			}			
		} catch (Exception e) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), e.getMessage());
		}		
	}
	
	public  GenericResponse update(DeviceReturnDTO deviceReturnDTO) {
		try {			
			Optional<DeviceReturnEntity> deviceReturnDetails=deviceReturnRepository.findById(deviceReturnDTO.getId());
			if(!deviceReturnDetails.isPresent()) {
				return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), "Record not exist");
			} else {				
				DeviceReturnEntity deviceReturnEntity=commonUtil.modalMap(deviceReturnDTO, DeviceReturnEntity.class);
				Optional<AssetTypeEntity> assetTypeEntity = assetyperepository.findById(deviceReturnDTO.getAccessoriesId());
				if (!assetTypeEntity.isPresent()) {
					throw new InvalidDataValidation(
							ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Accessories Id" }));
				}

				deviceReturnEntity.setAccessoriesId(assetTypeEntity.get());
				
				Optional<AssetAccessoriesEntity> assetaccesoruesEntity = assetaccessoriesrepository.findById(deviceReturnDTO.getDevice());
				if (!assetaccesoruesEntity.isPresent()) {
					throw new InvalidDataValidation(
							ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Device ID" }));
				}
				
				deviceReturnEntity.setDevice(assetaccesoruesEntity.get());
				deviceReturnEntity=deviceReturnRepository.save(deviceReturnEntity);
				
				WorkflowDTO workflowStatusUpdateDto = new WorkflowDTO();
				workflowStatusUpdateDto.setApplicationNumber(deviceReturnDTO.getApplicationNumber());
				workflowStatusUpdateDto.setModuleNameCode(deviceReturnDTO.getModuleNameCode());
				workflowStatusUpdateDto.setSubModuleNameCode(deviceReturnDTO.getSubModuleNameCode());
				workflowStatusUpdateDto.setEvent(deviceReturnDTO.getEvent());
				workflowStatusUpdateDto.setLevel(deviceReturnDTO.getLevel());
				workFlowService.callDeviceReturnWorkFlowService(workflowStatusUpdateDto);	
								
				DeviceReturnLogEntity deviceReturnLogEntity=new DeviceReturnLogEntity();
				
				deviceReturnLogEntity.setApplicationNo(deviceReturnDTO.getApplicationNumber());
				deviceReturnLogEntity.setAction("INITIATED");
				deviceReturnLogEntity.setComments("Application updated successfully");
				deviceReturnLogEntity.setUserName(deviceReturnDTO.getCreatedbyName());
				deviceReturnLogEntity.setLevel(deviceReturnDTO.getLevel());
				
				deviceReturnLogRepository.save(deviceReturnLogEntity);
				
				return Library.getSuccessfulResponse(deviceReturnEntity, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
						ErrorMessages.RECORED_UPDATED);
			}			
		} catch (Exception e) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), e.getMessage());
		}		
	}
	
	public GenericResponse getById(Long id) {
		Optional<DeviceReturnEntity> deviceReturnEntity =deviceReturnRepository.findById(id);
		if(deviceReturnEntity.isPresent()) {
			return Library.getSuccessfulResponse(deviceReturnEntity.get(), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		} else {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), ErrorMessages.NO_RECORD_FOUND);
		}
	}
	
	public GenericResponse getLogByApplicationNo(String applicationNo) {
		List<DeviceReturnLogEntity> deviceReturnLogEntity =deviceReturnLogRepository.findByApplicationNoOrderByIdDesc(applicationNo);
		if(deviceReturnLogEntity.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), ErrorMessages.NO_RECORD_FOUND);			
		} else {
			return Library.getSuccessfulResponse(deviceReturnLogEntity, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}
	}
	
	public GenericResponse getAllByUserId(Long userId) {
		List<DeviceReturnEntity> deviceReturnEntityList = deviceReturnRepository.findByCreatedByOrderByIdDesc(userId);
		List<DeviceReturnDTO> deviceReturnnewcominedres = new ArrayList<DeviceReturnDTO>();
		deviceReturnEntityList.stream().forEach(devicereturn -> {
			DeviceReturnDTO devicere = new DeviceReturnDTO();
			devicere.setId(devicereturn.getId());
			devicere.setApplicationDate(devicereturn.getApplicationDate());
			devicere.setApplicationNumber(devicereturn.getApplicationNumber());
			devicere.setInformHelpdesk(devicereturn.getInformHelpdesk());
			devicere.setComplaintNumber(devicereturn.getComplaintNumber());
			devicere.setLicenseType(devicereturn.getLicenseType());
			devicere.setLicenseName(devicereturn.getLicenseName());
			devicere.setLicenseNo(devicereturn.getLicenseNo());
			devicere.setShopId(devicereturn.getShopId());
			devicere.setShopName(devicereturn.getShopName());
			devicere.setReturnDevice(devicereturn.getDevice().toString());
			devicere.setDeviceId(devicereturn.getDeviceId());
			devicere.setDeviceName(devicereturn.getDeviceName());
			devicere.setDistrict(devicereturn.getDistrict());
			devicere.setDeviceStatus(devicereturn.getDeviceStatus());
			devicere.setStatus(devicereturn.getStatus());
			devicere.setReason(devicereturn.getReason());
			devicere.setLicenseNo(devicereturn.getLicenseNo());
			devicere.setAccessoriesId(devicereturn.getAccessoriesId().getId());
			devicere.setAccessoriestype(devicereturn.getAccessoriesId().getType());
			devicere.setDevice(devicereturn.getDevice().getId());
			devicere.setDeviceaccessoriesName(devicereturn.getDevice().getAccessoriesName());
			devicere.setUploadDocumentUrl(devicereturn.getUploadDocumentUrl());
			devicere.setUploadDocumentUuid(devicereturn.getUploadDocumentUuid());
			devicere.setUploadPodUrl(devicereturn.getUploadPodUrl());
			devicere.setUploadPodUuid(devicereturn.getUploadPodUuid());
			devicere.setCreatedbyName(devicereturn.getCreatedbyName());
			devicere.setRemarks(devicereturn.getRemarks());
			devicere.setActive(devicereturn.getActive());
			String tablecuurentlyworkwith = devicereturn.getCurrentlyWorkwith();
			if(tablecuurentlyworkwith!=null) {
			if (tablecuurentlyworkwith.equalsIgnoreCase("COMMINSOR")) {
				devicere.setCurrentlyWorkwith("COMMISSIONER");
			} else {
				devicere.setCurrentlyWorkwith(devicereturn.getCurrentlyWorkwith());

			}
			}
			deviceReturnnewcominedres.add(devicere);

		});

		if (deviceReturnEntityList.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		} else {
			return Library.getSuccessfulResponse(deviceReturnnewcominedres, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}
		
	}
	
	public GenericResponse getAllByDesignationCode(String designationCode) {
		List<DeviceReturnEntity> deviceReturnEntityList =deviceReturnRepository.findByCurrentlyWorkwithOrderByIdDesc(designationCode);
		if(deviceReturnEntityList.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), ErrorMessages.NO_RECORD_FOUND);			
		} else {
			return Library.getSuccessfulResponse(deviceReturnEntityList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}
	}
	
	public GenericResponse updateApproval(ApprovalDTO approvalDto) {
		GenericResponse resp = null;
		Optional<DeviceReturnEntity> deviceReturnEntity =deviceReturnRepository.findById(approvalDto.getId());
		if (deviceReturnEntity.isPresent()) {			
			DeviceReturnEntity deviceReturn = deviceReturnEntity.get();
			deviceReturn.setStatus(approvalDto.getStatus());
			deviceReturn.setModifiedBy(approvalDto.getUserId());
			deviceReturn.setModifiedDate(new Date());
			deviceReturnRepository.save(deviceReturn);
			
			WorkflowDTO workflowStatusUpdateDto = new WorkflowDTO();
			
			DeviceReturnLogEntity deviceReturnLogEntity=new DeviceReturnLogEntity();
			
			if (approvalDto.getEvent().equals("APPROVED") 
					&& ApprovalStatus.INPROGRESS.equals(approvalDto.getStatus())) {
				
				deviceReturnLogEntity.setAction(approvalDto.getAction());
				
				workflowStatusUpdateDto.setEvent(approvalDto.getEvent());
				
				deviceReturnLogEntity.setComments("Application forwarded successfully");
				
				resp = Library.getSuccessfulResponse(approvalDto,ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.FORWARDED);
				
			} else if (ApprovalStatus.APPROVED.equals(approvalDto.getStatus())) {
				
				deviceReturnLogEntity.setAction(approvalDto.getAction());
				
				workflowStatusUpdateDto.setEvent(approvalDto.getEvent());
				
				deviceReturnLogEntity.setComments("Application approved successfully");
				
				resp = Library.getSuccessfulResponse(approvalDto,ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.APPROVED);
				
			} else if (ApprovalStatus.REQUESTFORCLARIFICATION.equals(approvalDto.getStatus())) {
				
				deviceReturnLogEntity.setAction(approvalDto.getAction());
				
				workflowStatusUpdateDto.setEvent(approvalDto.getEvent());
				
				if (Objects.nonNull(approvalDto.getRemarks())) {
					deviceReturnLogEntity.setComments(approvalDto.getRemarks());
				}
				
				resp = Library.getSuccessfulResponse(approvalDto,ErrorCode.SUCCESS_RESPONSE.getErrorCode(), 
						ErrorMessages.CLARIFICATION_REQUEST_SENT_SUCCESSFULLY);
				workflowStatusUpdateDto.setSendBackTo("Level 1");
			}
			
			workflowStatusUpdateDto.setApplicationNumber(deviceReturnEntity.get().getApplicationNumber());
			workflowStatusUpdateDto.setModuleNameCode(approvalDto.getModuleNameCode());
			workflowStatusUpdateDto.setSubModuleNameCode(approvalDto.getSubModuleNameCode());
			workflowStatusUpdateDto.setLevel(approvalDto.getLevel());
			workFlowService.callDeviceReturnWorkFlowService(workflowStatusUpdateDto);								
						
			deviceReturnLogEntity.setApplicationNo(deviceReturnEntity.get().getApplicationNumber());
			deviceReturnLogEntity.setUserName(approvalDto.getUserName());
			deviceReturnLogEntity.setLevel(approvalDto.getLevel());
			deviceReturnLogEntity.setCreatedBy(approvalDto.getUserId());
			
			deviceReturnLogRepository.save(deviceReturnLogEntity);	
			
			return resp;
		} else {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}		
	}
	
	public GenericResponse generateAppNo() {
		
		HashMap<String, String>  map=new HashMap<>();
		try {
			Long count = deviceReturnRepository.findCurrentDateDeviceReturnCount();
			
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("A");
			stringBuilder.append(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			stringBuilder.append(count.toString());	
			
			map.put("applicationNo", stringBuilder.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return Library.getSuccessfulResponse(map, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);


	}
	
	public GenericResponse getsubPagesearchNewByFilter(PaginationRequestDTO requestData) {
	PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
	List<DeviceReturnEntity> list = this.getSubRecordsByFilterDTO1(requestData);
	List<DeviceReturnEntity> list1 = this.getSubRecordsByFilterDTO2(requestData);
	if (CollectionUtils.isEmpty(list) && CollectionUtils.isEmpty(list1)) {
		return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(), "No Record Found");
	}
 	if (!list.isEmpty()) {
 		paginationResponseDTO.setContents(list);
	}	
	Long count1=(long) list1.size();
	paginationResponseDTO.setNumberOfElements(Objects.nonNull(list1.size()) ? list1.size() : null);
	paginationResponseDTO.setTotalElements(count1);		
	return Library.getSuccessfulResponse(paginationResponseDTO, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
			ErrorMessages.RECORED_FOUND);
	}
	
	
	
	public GenericResponse generateAppNodevicelost() {
		
	HashMap<String, String>  map=new HashMap<>();
		try {
			Long count = devicelostrepository.findCurrentDateDeviceReturnCount();
			
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("A");
			stringBuilder.append(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			stringBuilder.append(count.toString());	
			
			map.put("applicationNo", stringBuilder.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return Library.getSuccessfulResponse(map, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}
	
	public GenericResponse generateAppNodevicedamage() {
		
		HashMap<String, String>  map=new HashMap<>();
			try {
				Long count = devicedamagerepository.findCurrentDateDeviceReturnCount();
				
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append("A");
				stringBuilder.append(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
				stringBuilder.append(count.toString());	
				
				map.put("applicationNo", stringBuilder.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return Library.getSuccessfulResponse(map, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);

		}
	
	
	public List<DeviceReturnEntity> getSubRecordsByFilterDTO1(PaginationRequestDTO filterRequestDTO) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<DeviceReturnEntity> cq = cb.createQuery(DeviceReturnEntity.class);
		Root<DeviceReturnEntity> from = cq.from(DeviceReturnEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<DeviceReturnEntity> typedQuery = null;
		addSubCriteria(cb, list, filterRequestDTO, from);	
		cq.where(cb.and(list.toArray(new Predicate[list.size()])));
		cq.distinct(true);
		if (StringUtils.isBlank(filterRequestDTO.getSortField())) {
			filterRequestDTO.setSortField(CREATED_DATE);
		}
		if (StringUtils.isNotBlank(filterRequestDTO.getSortOrder())
				&& filterRequestDTO.getSortOrder().equalsIgnoreCase(ASC)) {
			cq.orderBy(cb.asc(from.get(filterRequestDTO.getSortField())));

		} else {
			cq.orderBy(cb.desc(from.get(CREATED_DATE)));
		}
		if (Objects.nonNull(filterRequestDTO.getPageNo())
				&& Objects.nonNull(filterRequestDTO.getPaginationSize())) {
			typedQuery = entityManager.createQuery(cq)
					.setFirstResult((filterRequestDTO.getPageNo() * filterRequestDTO.getPaginationSize()))
					.setMaxResults(filterRequestDTO.getPaginationSize());
		} else {
			typedQuery = entityManager.createQuery(cq);
		}				
		return typedQuery.getResultList();
	}
	
	public List<DeviceReturnEntity> getSubRecordsByFilterDTO2(PaginationRequestDTO filterRequestDTO) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<DeviceReturnEntity> cq = cb.createQuery(DeviceReturnEntity.class);
		Root<DeviceReturnEntity> from = cq.from(DeviceReturnEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<DeviceReturnEntity> typedQuery1 = null;
		addSubCriteria(cb, list, filterRequestDTO, from);			
		cq.where(cb.and(list.toArray(new Predicate[list.size()])));
		cq.distinct(true);
		if (StringUtils.isBlank(filterRequestDTO.getSortField())) {
			filterRequestDTO.setSortField(CREATED_DATE);
		}
		if (StringUtils.isNotBlank(filterRequestDTO.getSortOrder())
				&& filterRequestDTO.getSortOrder().equalsIgnoreCase(ASC)) {
			cq.orderBy(cb.asc(from.get(filterRequestDTO.getSortField())));
		} else {
			cq.orderBy(cb.desc(from.get(filterRequestDTO.getSortField())));
		}
		typedQuery1 = entityManager.createQuery(cq);		
		return typedQuery1.getResultList();
	}
	
	private void addSubCriteria(CriteriaBuilder cb, List<Predicate> list, PaginationRequestDTO filterRequestDTO,
			Root<DeviceReturnEntity> from) {

		if (Objects.nonNull(filterRequestDTO.getFilters().get("shopName"))
				&& !filterRequestDTO.getFilters().get("shopName").toString().trim().isEmpty()) {

			String id =filterRequestDTO.getFilters().get("shopName").toString();
			list.add(cb.equal(from.get("shopName"), id));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("shopId"))
				&& !filterRequestDTO.getFilters().get("shopId").toString().trim().isEmpty()) {

			String id = (filterRequestDTO.getFilters().get("shopId").toString());
			list.add(cb.equal(from.get("shopId"), id));
		}
		
		if (Objects.nonNull(filterRequestDTO.getFilters().get("deviceId"))
				&& !filterRequestDTO.getFilters().get("deviceId").toString().trim().isEmpty()) {
			list.add(cb.equal(from.get("deviceId"), filterRequestDTO.getFilters().get("deviceId").toString()));
		}
		
		if ((Objects.nonNull(filterRequestDTO.getFilters().get("isCustomer"))
				&& !filterRequestDTO.getFilters().get("isCustomer").toString().trim().isEmpty())
				&& (Objects.nonNull(filterRequestDTO.getFilters().get("filterBy"))
						&& !filterRequestDTO.getFilters().get("filterBy").toString().trim().isEmpty())) {
			
			if(Boolean.parseBoolean(filterRequestDTO.getFilters().get("isCustomer").toString())) {
				list.add(cb.equal(from.get("createdBy"), Long.parseLong(filterRequestDTO.getFilters().get("filterBy").toString())));
			} else {
				list.add(cb.equal(from.get("currentlyWorkwith"), filterRequestDTO.getFilters().get("filterBy").toString()));
			}			
		}
		
		if (Objects.nonNull(filterRequestDTO.getFilters().get("status"))
				&& !filterRequestDTO.getFilters().get("status").toString().trim().isEmpty()) {
			ApprovalStatus status = null;
			if(filterRequestDTO.getFilters().get("status").toString().equals(ApprovalStatus.DRAFT.name())) {
				status = ApprovalStatus.DRAFT;
			} else if(filterRequestDTO.getFilters().get("status").toString().equals(ApprovalStatus.INPROGRESS.name())) {
				status = ApprovalStatus.INPROGRESS;
			} else if(filterRequestDTO.getFilters().get("status").toString().equals(ApprovalStatus.REQUESTFORCLARIFICATION.name())) {
				status = ApprovalStatus.REQUESTFORCLARIFICATION;
			} else if(filterRequestDTO.getFilters().get("status").toString().equals(ApprovalStatus.APPROVED.name())) {
				status = ApprovalStatus.APPROVED;
			}
			list.add(cb.equal(from.get("status"), status));
		}
		
	}
		
	
	
}
