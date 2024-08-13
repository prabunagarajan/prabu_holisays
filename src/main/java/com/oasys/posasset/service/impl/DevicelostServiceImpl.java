package com.oasys.posasset.service.impl;
import static com.oasys.helpdesk.constant.Constant.ID;
import static com.oasys.posasset.constant.Constant.ASC;
import static com.oasys.posasset.constant.Constant.CREATED_DATE;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.PaginationResponseDTO;
import com.oasys.helpdesk.dto.WorkflowDTO;
import com.oasys.helpdesk.entity.AssetAccessoriesEntity;
import com.oasys.helpdesk.entity.AssetTypeEntity;
import com.oasys.helpdesk.entity.CreateTicketEntity;
import com.oasys.helpdesk.repository.AssetAccessoriesRepository;
import com.oasys.helpdesk.repository.AssetTypeRepository;
import com.oasys.helpdesk.repository.CreateTicketRepository;
import com.oasys.helpdesk.security.ServiceHeader;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.posasset.constant.ApprovalStatus;
import com.oasys.posasset.dto.ApprovalDTO;
import com.oasys.posasset.dto.DevicelostResponseDTO;
import com.oasys.posasset.dto.Devicelostrequestdto;
import com.oasys.posasset.entity.DeviceLostLogEntity;
import com.oasys.posasset.entity.DeviceReturnEntity;
import com.oasys.posasset.entity.DevicelostEntity;
import com.oasys.posasset.mapper.DeviceLostMapper;
import com.oasys.posasset.repository.DevicelostRepository;
import com.oasys.posasset.repository.DevicelostlogRepository;
import com.oasys.posasset.service.DevicelostService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class DevicelostServiceImpl implements DevicelostService {
	
	@Autowired
	private CommonUtil commonUtil;
	
	
	@Autowired
	private DevicelostRepository devicelostrepository;
	
	@Autowired
	private DevicelostlogRepository devicelostlogrepository;
	
	@Autowired
	private AssetTypeRepository assetyperepository;
	
	@Autowired
	private AssetAccessoriesRepository assetaccessoriesrepository;
	
	@Autowired
	private DeviceLostMapper devicelostmapper;
	
	
	@Value("${workflow.domain}")
	private String workflow;

	@Value("${domain}")
	private String domain;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	ServiceHeader serviceHeader;

	@Autowired
	HttpServletRequest headerRequest;
	
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	CreateTicketRepository createTicketRepository;
	
	@Value("${spring.common.devtoken}")
	private String authtoken;
	
	
	@Override
	public GenericResponse adddevicelost(Devicelostrequestdto requestDto)	{
		
		Optional<DevicelostEntity> deviceReturnDetails = devicelostrepository
				.findByLicenseNoAndShopIdAndDeviceId(requestDto.getLicenseNo(), requestDto.getShopId(),requestDto.getDeviceId());
		if (!deviceReturnDetails.isPresent()) {
			requestDto.setId(null);
			DevicelostEntity tcEntity = commonUtil.modalMap(requestDto, DevicelostEntity.class);

			Optional<AssetTypeEntity> assetTypeEntity = assetyperepository.findById(requestDto.getAccessoriesId());
			if (!assetTypeEntity.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Accessories Id" }));
			}

			tcEntity.setAccessoriesId(assetTypeEntity.get());

			Optional<AssetAccessoriesEntity> assetaccesoruesEntity = assetaccessoriesrepository
					.findById(requestDto.getDeviceId());
			if (!assetaccesoruesEntity.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Device ID" }));
			}

			tcEntity.setDeviceId(assetaccesoruesEntity.get());
			devicelostrepository.save(tcEntity);
			/// device lost log maintain process
			try {
				devicelostlogadd(requestDto);
			} catch (Exception e) {
				log.info("devicelostlog Add error" + e);
			}
			/// workflow initiate process

			try {
				WorkflowDTO workflowStatusUpdateDto = new WorkflowDTO();
				workflowStatusUpdateDto.setApplicationNumber(requestDto.getApplicationNumber());
				workflowStatusUpdateDto.setModuleNameCode(requestDto.getModuleNameCode());
				workflowStatusUpdateDto.setSubModuleNameCode(requestDto.getSubModuleNameCode());
				workflowStatusUpdateDto.setEvent(requestDto.getEvent());
				workflowStatusUpdateDto.setLevel(requestDto.getLevel());
				callWorkFlowService(workflowStatusUpdateDto);

			} catch (Exception e) {
				log.info("workflow initaiate error" + e);
			}
			return Library.getSuccessfulResponse(tcEntity, ErrorCode.CREATED.getErrorCode(),
					ErrorMessages.RECORED_CREATED);
		}

		else {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), "Record already exist");
		}
	
	}
	
	@Override
	public GenericResponse adddevicelost1(Devicelostrequestdto requestDto)	{
		requestDto.setId(null);
		DevicelostEntity tcEntity = commonUtil.modalMap(requestDto, DevicelostEntity.class);
		
		Optional<AssetTypeEntity> assetTypeEntity = assetyperepository.findById(requestDto.getAccessoriesId());
		if (!assetTypeEntity.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Accessories Id" }));
		}

		tcEntity.setAccessoriesId(assetTypeEntity.get());
		
		Optional<AssetAccessoriesEntity> assetaccesoruesEntity = assetaccessoriesrepository.findById(requestDto.getDeviceId());
		if (!assetaccesoruesEntity.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Device ID" }));
		}
		
		tcEntity.setDeviceId(assetaccesoruesEntity.get());
		//devicelostrepository.save(tcEntity);
		///device lost log maintain process
		try {
		//devicelostlogadd(requestDto);
		}
		catch(Exception e) {
		log.info("devicelostlog Add error"+ e);	
		}
		///workflow initiate process
		
		try {
			WorkflowDTO workflowStatusUpdateDto =new WorkflowDTO();
			workflowStatusUpdateDto.setApplicationNumber(requestDto.getApplicationNumber());
			workflowStatusUpdateDto.setModuleNameCode(requestDto.getModuleNameCode());
			workflowStatusUpdateDto.setSubModuleNameCode(requestDto.getSubModuleNameCode());
			workflowStatusUpdateDto.setEvent(requestDto.getEvent());
			workflowStatusUpdateDto.setLevel(requestDto.getLevel());
			callWorkFlowService(workflowStatusUpdateDto);	
		}
		catch(Exception e) {
			log.info("workflow initaiate error"+ e);		
		}
		
		return Library.getSuccessfulResponse(tcEntity, ErrorCode.CREATED.getErrorCode(),ErrorMessages.RECORED_CREATED);
	}
	
	
	
	public String callWorkFlowService(WorkflowDTO workflowStatusUpdateDto) {
	WorkflowDTO workflowDto = new WorkflowDTO();
	HttpHeaders headers = new HttpHeaders();
	workflowDto.setApplicationNumber(workflowStatusUpdateDto.getApplicationNumber());
	workflowDto.setSubModuleNameCode(workflowStatusUpdateDto.getSubModuleNameCode());
	workflowDto.setCallbackURL(domain + "/helpdesk/devicelost/updateWorkFlow");// this is call back URL to update the
																			// table with required data after the
	// workflow completion
	workflowDto.setModuleNameCode(workflowStatusUpdateDto.getModuleNameCode());
	workflowDto.setSendBackTo(workflowStatusUpdateDto.getSendBackTo());
	workflowDto.setComments("");
	workflowDto.setEvent(workflowStatusUpdateDto.getEvent());
	workflowDto.setLevel(workflowStatusUpdateDto.getLevel());
	// workflow service calling
	
	try {
       StringBuffer uri = new StringBuffer(workflow);
		uri.append("/api/master/startExecution");
		Gson gson = new Gson();
		if (uri != null) {

			// call the method and set the header in service call

			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", "Bearer ".concat(authtoken));
			
			String accessToken = headerRequest.getHeader("X-Authorization");
			headers.set("X-Authorization", accessToken);	
			
			HttpEntity<?> httpRequestEntity = new HttpEntity<>(workflowDto,headers);

			log.info("=======callWorkFlowService URL============" + uri.toString());

			log.info("=======jjjjjjjjjjj============" + gson.toJson(workflowDto));
			
			String response = restTemplate.postForObject(uri.toString(), httpRequestEntity, String.class);

			log.info("=======callWorkFlowService Response============" + response);

		}
	} catch (Exception exception) {
		// general error
		log.error("=======callWorkFlowService catch block============", exception);
	}

return "a";
}


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void devicelostlogadd(Devicelostrequestdto devicelostlogrequest) {
		DeviceLostLogEntity devicelostlog=new DeviceLostLogEntity();
		devicelostlog.setAction("INITIATED");
		devicelostlog.setActionPerformedby(devicelostlogrequest.getCreatedbyName());
		devicelostlog.setComments("Application Submitted Successfully");
		devicelostlog.setDesignation(devicelostlogrequest.getDesignation());
		devicelostlog.setApplicationNumber(devicelostlogrequest.getApplicationNumber());
		devicelostlogrepository.save(devicelostlog);
	}
	
	
	public void devicelostlogupdate(Devicelostrequestdto devicelostlogrequest) {
		DeviceLostLogEntity devicelostlog=new DeviceLostLogEntity();
		devicelostlog.setAction("INITIATED");
		devicelostlog.setActionPerformedby(devicelostlogrequest.getCreatedbyName());
		devicelostlog.setComments("Application updated successfully");
		devicelostlog.setDesignation(devicelostlogrequest.getDesignation());
		devicelostlog.setApplicationNumber(devicelostlogrequest.getApplicationNumber());
		devicelostlogrepository.save(devicelostlog);
	}
	
	
	@Override
	public GenericResponse getAll() {
		List<DevicelostEntity> DepList = devicelostrepository.findAllByOrderByCreatedDateDesc();
		if (CollectionUtils.isEmpty(DepList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<DevicelostResponseDTO> depResponseList = DepList.stream()
				.map(devicelostmapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(depResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}	
	
	public GenericResponse getBydesignationCode(String designationCode) {
		List<DevicelostEntity> fetchRecords=devicelostrepository
				.findByCurrentlyWorkwithOrderByCreatedDateDesc(designationCode);
		if(fetchRecords.isEmpty()) {
			return Library.getSuccessfulResponse(fetchRecords, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		} else {
			//fetchRecords=fetchRecords.stream().map(this::convert).collect(Collectors.toList());
			return Library.getSuccessfulResponse(fetchRecords, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}
	}
	
	public GenericResponse getByUserId(Long userId) {
		List<DevicelostEntity> fetchRecords=devicelostrepository.findByCreatedByOrderByIdDesc(userId);
		if(fetchRecords.isEmpty()) {
			return Library.getSuccessfulResponse(fetchRecords, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		} else {
			//fetchRecords=fetchRecords.stream().map(this::convert).collect(Collectors.toList());
			return Library.getSuccessfulResponse(fetchRecords, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}	
	}
//	
	
	@Override
	public GenericResponse getById(Long id) {
		Optional<DevicelostEntity> devicelostEntity = devicelostrepository.findById(id);
		if (!devicelostEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(devicelostmapper.convertEntityToResponseDTO(devicelostEntity.get()), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	
	
	@Override
	public GenericResponse update(Devicelostrequestdto requestDTO)	{
		if(Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		Optional<DevicelostEntity> DeptOptional = devicelostrepository.findById(requestDTO.getId());
		if (!DeptOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		//DevicelostEntity devicelostEntity=commonUtil.modalMap(requestDTO, DevicelostEntity.class);
		DevicelostEntity devicelostEntity =DeptOptional.get();
		Optional<AssetTypeEntity> assetTypeEntity = assetyperepository.findById(requestDTO.getAccessoriesId());
		if (!assetTypeEntity.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Accessories Id" }));
		}
		
		Optional<AssetAccessoriesEntity> assetaccesoruesEntity = assetaccessoriesrepository.findById(requestDTO.getDeviceId());
		if (!assetaccesoruesEntity.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Device ID" }));
		}
		
		devicelostEntity.setAccessoriesId(assetTypeEntity.get());
		devicelostEntity.setDeviceId(assetaccesoruesEntity.get());
		devicelostEntity.setUploadFIRCopy(requestDTO.getUploadFIRCopy());
		devicelostEntity.setFirCopyuuid(requestDTO.getFirCopyuuid());
		devicelostEntity.setReplyRemarks(requestDTO.getReplyRemarks());
		devicelostEntity.setLostDevice(requestDTO.getReplyRemarks());
		devicelostEntity.setUploadApplication(requestDTO.getUploadApplication());
		devicelostEntity.setApplicationUuid(requestDTO.getApplicationUuid());
		devicelostEntity.setUploadPod(requestDTO.getUploadPod());
		devicelostEntity.setPodUuid(requestDTO.getPodUuid());
		devicelostEntity.setShopId(requestDTO.getShopId());
		devicelostEntity.setApplicationNumber(requestDTO.getApplicationNumber());
		devicelostEntity.setApplicableDate(requestDTO.getApplicableDate());
		devicelostEntity.setComplaintNumber(requestDTO.getComplaintNumber());
		devicelostEntity.setDeviceName(requestDTO.getDeviceName());
		devicelostEntity.setDistrict(requestDTO.getDistrict());
		devicelostEntity.setInformHelpdesk(requestDTO.getInformHelpdesk());
		devicelostEntity.setShopName(requestDTO.getShopName());
		devicelostEntity.setLicenseName(requestDTO.getLicenseName());
		devicelostEntity.setLicenseNo(requestDTO.getLicenseNo());
		devicelostEntity.setLicenseType(requestDTO.getLicenseType());
		devicelostEntity.setReason(requestDTO.getReason());
		devicelostEntity.setDeviceserialNo(requestDTO.getDeviceserialNo());
		devicelostEntity.setStatus(ApprovalStatus.INPROGRESS);
		devicelostEntity.setLostDevice(requestDTO.getLostDevice());
		devicelostrepository.save(devicelostEntity);
		
		try {
			devicelostlogupdate(requestDTO);
			}
			catch(Exception e) {
			log.info("devicelostlog update error"+ e);	
			}
		WorkflowDTO workflowStatusUpdateDto = new WorkflowDTO();
		workflowStatusUpdateDto.setApplicationNumber(requestDTO.getApplicationNumber());
		workflowStatusUpdateDto.setModuleNameCode(requestDTO.getModuleNameCode());
		workflowStatusUpdateDto.setSubModuleNameCode(requestDTO.getSubModuleNameCode());
		workflowStatusUpdateDto.setLevel(requestDTO.getLevel());
		workflowStatusUpdateDto.setEvent(requestDTO.getEvent());
		try {
		callWorkFlowService(workflowStatusUpdateDto);								
		}
		catch(Exception e) {
		log.info("callWorkFlowService::" + e);
		}
			
		
		return Library.getSuccessfulResponse(devicelostEntity, ErrorCode.CREATED.getErrorCode(),ErrorMessages.RECORED_UPDATED);
	}

	
	
	public GenericResponse getsubPagesearchNewByFilter(PaginationRequestDTO requestData) {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		List<DevicelostEntity> list = this.getSubRecordsByFilterDTO1(requestData);
		List<DevicelostEntity> list1 = this.getSubRecordsByFilterDTO2(requestData);
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
	
	public List<DevicelostEntity> getSubRecordsByFilterDTO1(PaginationRequestDTO filterRequestDTO) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<DevicelostEntity> cq = cb.createQuery(DevicelostEntity.class);
		Root<DevicelostEntity> from = cq.from(DevicelostEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<DevicelostEntity> typedQuery = null;
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
	
	public List<DevicelostEntity> getSubRecordsByFilterDTO2(PaginationRequestDTO filterRequestDTO) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<DevicelostEntity> cq = cb.createQuery(DevicelostEntity.class);
		Root<DevicelostEntity> from = cq.from(DevicelostEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<DevicelostEntity> typedQuery1 = null;
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
			Root<DevicelostEntity> from) {

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
			
			Long deviceid=Long.valueOf((filterRequestDTO.getFilters().get("deviceId").toString()));
			list.add(cb.equal(from.get("deviceId"),deviceid ));
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
		
		
		
		if (Objects.nonNull(filterRequestDTO.getFilters().get("licenseType"))
				&& !filterRequestDTO.getFilters().get("licenseType").toString().trim().isEmpty()) {

			String licenseType =filterRequestDTO.getFilters().get("licenseType").toString();
			list.add(cb.equal(from.get("licenseType"), licenseType));
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
		
		if (Objects.nonNull(filterRequestDTO.getFilters().get("deviceserialNo"))
				&& !filterRequestDTO.getFilters().get("deviceserialNo").toString().trim().isEmpty()) {

			String deviceserialno =filterRequestDTO.getFilters().get("deviceserialNo").toString();
			list.add(cb.equal(from.get("deviceserialNo"), deviceserialno));
		}
		
		
	}
	
	
	public GenericResponse updateApproval(ApprovalDTO approvalDto) {
		GenericResponse resp = null;
		Optional<DevicelostEntity> devicelostEntity =devicelostrepository.findById(approvalDto.getId());
		if (devicelostEntity.isPresent()) {			
			DevicelostEntity devicelost = devicelostEntity.get();
			devicelost.setStatus(approvalDto.getStatus());
			devicelost.setModifiedBy(approvalDto.getUserId());
			devicelost.setModifiedDate(new Date());
			devicelostrepository.save(devicelost);
			
			WorkflowDTO workflowStatusUpdateDto = new WorkflowDTO();
			
			DeviceLostLogEntity devicelostLogEntity=new DeviceLostLogEntity();
			
				
				if (approvalDto.getEvent().equals("APPROVED") && ApprovalStatus.INPROGRESS.equals(approvalDto.getStatus())) {	
				
				devicelostLogEntity.setAction(approvalDto.getAction());
				
				workflowStatusUpdateDto.setEvent(approvalDto.getEvent());
				
				devicelostLogEntity.setComments("Application forwarded successfully");
				
				resp = Library.getSuccessfulResponse(approvalDto,ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.APPROVED);
				
			} else if (ApprovalStatus.APPROVED.equals(approvalDto.getStatus())) {
				
				devicelostLogEntity.setAction(approvalDto.getAction());
				
				workflowStatusUpdateDto.setEvent(approvalDto.getEvent());
				
				devicelostLogEntity.setComments("Application approved successfully");
				
				resp = Library.getSuccessfulResponse(approvalDto,ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.FORWARDED);
				
			} else if (ApprovalStatus.REQUESTFORCLARIFICATION.equals(approvalDto.getStatus())) {
				
				devicelostLogEntity.setAction(approvalDto.getAction());
				
				workflowStatusUpdateDto.setEvent(approvalDto.getEvent());
				
				if (Objects.nonNull(approvalDto.getRemarks())) {
					devicelostLogEntity.setComments(approvalDto.getRemarks());
				}
				
				resp = Library.getSuccessfulResponse(approvalDto,ErrorCode.SUCCESS_RESPONSE.getErrorCode(), 
						ErrorMessages.CLARIFICATION_REQUEST_SENT_SUCCESSFULLY);
				workflowStatusUpdateDto.setSendBackTo("Level 1");
			}
			
			workflowStatusUpdateDto.setApplicationNumber(devicelostEntity.get().getApplicationNumber());
			workflowStatusUpdateDto.setModuleNameCode(approvalDto.getModuleNameCode());
			workflowStatusUpdateDto.setSubModuleNameCode(approvalDto.getSubModuleNameCode());
			workflowStatusUpdateDto.setLevel(approvalDto.getLevel());
			try {
			callWorkFlowService(workflowStatusUpdateDto);								
			}
			catch(Exception e) {
			log.info("callWorkFlowService::" + e);
			}
			devicelostLogEntity.setApplicationNumber(devicelostEntity.get().getApplicationNumber());
			devicelostLogEntity.setActionPerformedby(approvalDto.getUserName());
			//devicelostLogEntity.setDesignation(approvalDto.getUserName());
			devicelostlogrepository.save(devicelostLogEntity);	
			
			return resp;
		} else {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}		
	}
	
	
	
	public GenericResponse licnoverify(Devicelostrequestdto Devicelostrequestdto) {
		Long ticketsatusclosed=(long) 56;
		Long tickedtresolved=(long) 59;
		Optional<CreateTicketEntity> fetchRecords=createTicketRepository.findByTicketNumberAndLicenceNumber(Devicelostrequestdto.getComplaintNumber(),Devicelostrequestdto.getLicenseNo());
		if(fetchRecords.isPresent()) {
			Optional<CreateTicketEntity> ticketclosedcheck=createTicketRepository.findByStatusAndTicketNumberAndLicenceNumber(ticketsatusclosed,Devicelostrequestdto.getComplaintNumber(),Devicelostrequestdto.getLicenseNo());
					
			if(!ticketclosedcheck.isPresent()) {	
			return Library.getSuccessfulResponse("Matched Complaint Number", ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
			}
			
			else {
				Optional<CreateTicketEntity> ticketresolvedcheck=createTicketRepository.findByStatusAndTicketNumberAndLicenceNumber(tickedtresolved,Devicelostrequestdto.getComplaintNumber(),Devicelostrequestdto.getLicenseNo());
				if(!ticketresolvedcheck.isPresent()) {
					return Library.getSuccessfulResponse("Matched Complaint Number", ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
							ErrorMessages.NO_RECORD_FOUND);	
				}
				else {
				return Library.getFailedfulResponse("Complaint Number Closed or Resolved",ErrorCode.FAILURE_RESPONSE.getErrorCode(),
							ErrorMessages.NO_RECORD_FOUND);
				}
				
			}
			
		} else {
			return Library.getFailedfulResponse("Invalid Complaint Number", ErrorCode.FAILURE_RESPONSE.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		}	
	}
	

	
	
	
}
