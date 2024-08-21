package com.oasys.helpdesk.service;

import static com.oasys.posasset.constant.Constant.ASC;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import static com.oasys.helpdesk.constant.Constant.USER_ID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.google.gson.Gson;
import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.ChangereqCountDTO;
import com.oasys.helpdesk.dto.ChangereqCountRequestDTO;
import com.oasys.helpdesk.dto.ChangereqRequestDTO;
import com.oasys.helpdesk.dto.ChangereqSummaryDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.PaginationResponseDTO;
import com.oasys.helpdesk.dto.WorkflowDTO;
import com.oasys.helpdesk.entity.ChangeRequestEntity;
import com.oasys.helpdesk.entity.ChangeRequestFeaturesEntity;
import com.oasys.helpdesk.entity.ChangeRequestLogEntity;
import com.oasys.helpdesk.entity.UserEntity;
import com.oasys.helpdesk.enums.ApplnStatus;
import com.oasys.helpdesk.enums.ChangereqStatus;
import com.oasys.helpdesk.mapper.ChangereqMapper;
import com.oasys.helpdesk.repository.ChangeRequestFeaturesRepository;
import com.oasys.helpdesk.repository.ChangeRequestLogRepository;
import com.oasys.helpdesk.repository.ChangeRequestRepository;
import com.oasys.helpdesk.repository.UserRepository;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.security.SecurityUtils;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.RandomUtil;
import com.oasys.posasset.dto.ApprovalDTO;
import com.oasys.posasset.dto.WorkFlowStatusUpdateDTO;
import com.oasys.posasset.entity.DeviceLostLogEntity;
import com.oasys.posasset.entity.DeviceReturnLogEntity;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ChangereqServiceImpl implements ChangerequestService {

	@Autowired
	ChangeRequestRepository changereqrepo;

	@Autowired
	ChangeRequestFeaturesRepository changereqfeaturerepo;

	@Autowired
	private CommonUtil commonUtil;

//	@Autowired
//	WorkFlowService workFlowService;

	@Autowired
	ChangereqMapper changeremapper;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	ChangeRequestLogRepository changereqlog;

	@Autowired
	private UserRepository userRepository;

	@Value("${workflow.domain}")
	private String workflow;

	@Value("${domain}")
	private String domain;

	@Value("${spring.common.devtoken}")
	private String authtoken;

	@Autowired
	HttpServletRequest headerRequest;

	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	ChangeRequestRepository changerequestrepository;

	@Override
	public GenericResponse addchnagerequest(ChangereqRequestDTO requestDTO) {
		ChangeRequestEntity entity = null;
		WorkflowDTO workflowStatusUpdateDto = new WorkflowDTO();
		try {
			Optional<ChangeRequestEntity> optional = null;
			String code = RandomUtil.generatechangereqapplnno();
			while (true) {
				optional = changereqrepo.findByChangereqApplnNoIgnoreCase(code);
				if (optional.isPresent()) {
					code = RandomUtil.getRandomNumber();
				} else {
					break;
				}
			}
			requestDTO.setChangereqApplnNo(code);
			requestDTO.setId(null);
			entity = commonUtil.modalMap(requestDTO, ChangeRequestEntity.class);
			entity.setEmailId(requestDTO.getEmailId());
			entity.setChangereqStatus(requestDTO.getChangereqStatus());
			entity.setApplnStatus(requestDTO.getApplnStatus());
			entity.setRaisedBy(requestDTO.getRaisedBy());
			entity.setUserMobileNumber(requestDTO.getUserMobileNumber());

			Optional<ChangeRequestFeaturesEntity> changereqfeaentity = changereqfeaturerepo
					.findById(requestDTO.getFeatureId());

			if (!changereqfeaentity.isPresent())

			{
				return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Status" }));
			}

			entity.setFeatureId(changereqfeaentity.get());
			changereqrepo.save(entity);
			log.info("::Workflow Entry:::");
			workflowStatusUpdateDto.setApplicationNumber(requestDTO.getChangereqApplnNo());
			workflowStatusUpdateDto.setModuleNameCode(requestDTO.getModuleNameCode());
			if (requestDTO.getEntityType().equalsIgnoreCase("BREWERY")
					|| requestDTO.getEntityType().equalsIgnoreCase("BOTTELING")) {
				workflowStatusUpdateDto.setSubModuleNameCode("HELPDESK_CHANGE_REQUEST_AEC");
			}
			if (requestDTO.getEntityType().equalsIgnoreCase("DISTILLERY")) {
				workflowStatusUpdateDto.setSubModuleNameCode("HELPDESK_CHANGE_REQUEST_DISTILLERY");
			} else {
				workflowStatusUpdateDto.setSubModuleNameCode(requestDTO.getSubModuleNameCode());
			}
			workflowStatusUpdateDto.setEvent(requestDTO.getEvent());
			workflowStatusUpdateDto.setLevel(requestDTO.getLevel());
			String worflowresponse = callWorkFlowChangerequestService(workflowStatusUpdateDto);
			changerequestlogadd(requestDTO);
			if (worflowresponse.equalsIgnoreCase("Success")) {
				return Library.getSuccessfulResponse(entity, ErrorCode.CREATED.getErrorCode(),
						ErrorMessages.RECORED_CREATED);
			} else {
				Optional<ChangeRequestEntity> deptOptional = changereqrepo
						.findByChangereqApplnNoIgnoreCase(workflowStatusUpdateDto.getApplicationNumber());
				ChangeRequestEntity changereqStatus = deptOptional.get();
				requestDTO.setChangereqStatus(ChangereqStatus.DRAFT);
				changereqStatus.setChangereqStatus(requestDTO.getChangereqStatus());
				changereqrepo.save(changereqStatus);
				return Library.getFailedfulResponse(entity,
						ErrorCode.ERROR_RESPONSE_FROM_CALLING_SERVICE.getErrorCode(), ErrorMessages.TECHNICAL_ISSUE);

			}

		} catch (Exception e) {
			log.info(":::Change Request save:::" + e);
		}
//		return Library.getSuccessfulResponse(entity, ErrorCode.CREATED.getErrorCode(), ErrorMessages.RECORED_CREATED);
		return null;

	}

	public void changerequestlogadd(ChangereqRequestDTO requestDTO) {
		try {
			ChangeRequestLogEntity changerequestlog = new ChangeRequestLogEntity();
			changerequestlog.setApplnNo(requestDTO.getChangereqApplnNo());
			changerequestlog.setUserName(requestDTO.getUserName());
			changerequestlog.setComments(requestDTO.getDescription());
			changerequestlog.setAction(requestDTO.getAction());
			changerequestlog.setRemarks(requestDTO.getRemarks());
			changerequestlog.setActionperformedby(requestDTO.getActionperformedby());
			AuthenticationDTO authenticationDTO = findAuthenticationObject();
			if (requestDTO.getDesignation().equalsIgnoreCase("null")) {
				changerequestlog.setDesignation(authenticationDTO.getUserName());
			} else {
				changerequestlog.setDesignation(requestDTO.getDesignation());
			}

			changereqlog.save(changerequestlog);
		} catch (Exception e) {
			log.info("changerequest log" + e);
		}
	}

	public AuthenticationDTO findAuthenticationObject() {
		if (SecurityContextHolder.getContext() != null
				&& SecurityContextHolder.getContext().getAuthentication() != null) {
			AuthenticationDTO authenticationDTO = (AuthenticationDTO) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
			return authenticationDTO;
		}
		return null;
	}

	public void changerequestlogupdate(ApprovalDTO requestDTO) {
		try {
			ChangeRequestLogEntity changerequestlog = new ChangeRequestLogEntity();
			changerequestlog.setApplnNo(requestDTO.getApplnNo());
			changerequestlog.setUserName(requestDTO.getUserName());
			changerequestlog.setComments(requestDTO.getDescription());
			changerequestlog.setAction(requestDTO.getAction());
			changerequestlog.setRemarks(requestDTO.getRemarks());
			changerequestlog.setActionperformedby(requestDTO.getActionperformedby());
			AuthenticationDTO authenticationDTO = findAuthenticationObject();
			if (requestDTO.getDesignation() == null) {
				changerequestlog.setDesignation(authenticationDTO.getUserName());
			} else {
				changerequestlog.setDesignation(requestDTO.getDesignation());
			}
			changerequestlog.setDesignation(requestDTO.getDesignation());
			changereqlog.save(changerequestlog);
		} catch (Exception e) {
			log.info("changerequest log" + e);
		}
	}

	@Override
	public GenericResponse getById(Long id) {
		List<ChangeRequestEntity> entity = changereqrepo.getById(id);

		List<ChangeRequestEntity> list = new ArrayList<ChangeRequestEntity>();
		list.addAll(entity);
		if (entity.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(list, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	public GenericResponse getsubPagesearchNewByFilter(PaginationRequestDTO requestData) {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		List<ChangeRequestEntity> list = this.getSubRecordsByFilterDTO1(requestData);
		List<ChangeRequestEntity> list1 = this.getSubRecordsByFilterDTO2(requestData);
		if (CollectionUtils.isEmpty(list) && CollectionUtils.isEmpty(list1)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(), "No Record Found");
		}
		if (!list.isEmpty()) {
			paginationResponseDTO.setContents(list);
		}
		Long count1 = (long) list1.size();
		paginationResponseDTO.setNumberOfElements(Objects.nonNull(list1.size()) ? list1.size() : null);
		paginationResponseDTO.setTotalElements(count1);
		return Library.getSuccessfulResponse(paginationResponseDTO, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	public List<ChangeRequestEntity> getSubRecordsByFilterDTO1(PaginationRequestDTO filterRequestDTO) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<ChangeRequestEntity> cq = cb.createQuery(ChangeRequestEntity.class);
		Root<ChangeRequestEntity> from = cq.from(ChangeRequestEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<ChangeRequestEntity> typedQuery = null;
		addSubCriteria(cb, list, filterRequestDTO, from);
		cq.where(cb.and(list.toArray(new Predicate[list.size()])));
		cq.distinct(true);
		if (StringUtils.isBlank(filterRequestDTO.getSortField())) {
			filterRequestDTO.setSortField("createdDate");
		}
		if (StringUtils.isNotBlank(filterRequestDTO.getSortOrder())
				&& filterRequestDTO.getSortOrder().equalsIgnoreCase(ASC)) {
			cq.orderBy(cb.asc(from.get(filterRequestDTO.getSortField())));

		} else {
			cq.orderBy(cb.desc(from.get("createdDate")));
		}
		if (Objects.nonNull(filterRequestDTO.getPageNo()) && Objects.nonNull(filterRequestDTO.getPaginationSize())) {
			typedQuery = entityManager.createQuery(cq)
					.setFirstResult((filterRequestDTO.getPageNo() * filterRequestDTO.getPaginationSize()))
					.setMaxResults(filterRequestDTO.getPaginationSize());
		} else {
			typedQuery = entityManager.createQuery(cq);
		}
		return typedQuery.getResultList();
	}

	public List<ChangeRequestEntity> getSubRecordsByFilterDTO2(PaginationRequestDTO filterRequestDTO) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<ChangeRequestEntity> cq = cb.createQuery(ChangeRequestEntity.class);
		Root<ChangeRequestEntity> from = cq.from(ChangeRequestEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<ChangeRequestEntity> typedQuery1 = null;
		addSubCriteria(cb, list, filterRequestDTO, from);
		cq.where(cb.and(list.toArray(new Predicate[list.size()])));
		cq.distinct(true);
		if (StringUtils.isBlank(filterRequestDTO.getSortField())) {
			filterRequestDTO.setSortField("createdDate");
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
			Root<ChangeRequestEntity> from) {

		Date fromDate = null;

		Date toDate = null;

		String fdate = null;

		String tdate = null;

		if (Objects.nonNull(filterRequestDTO.getFilters().get("fromDate"))
				&& !filterRequestDTO.getFilters().get("fromDate").toString().trim().isEmpty()) {
			try {
				try {
					fdate = String.valueOf(filterRequestDTO.getFilters().get("fromDate").toString());
					fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fdate + " " + "00:00:00");
				} catch (ParseException e) {
					log.error("error occurred while parsing date : {}", e.getMessage());
					throw new InvalidDataValidation("Invalid date parameter passed");
				}
			} catch (Exception e) {
				log.error("error occurred while parsing refertic_number :: {}", e);
			}
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("toDate"))
				&& !filterRequestDTO.getFilters().get("toDate").toString().trim().isEmpty()) {
			try {
				try {
					tdate = String.valueOf(filterRequestDTO.getFilters().get("toDate").toString());
					toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(tdate + " " + "23:59:59");
					list.add(cb.between(from.get("createdDate"), fromDate, toDate));
				} catch (ParseException e) {
					log.error("error occurred while parsing date : {}", e.getMessage());
					throw new InvalidDataValidation("Invalid date parameter passed");
				}
			} catch (Exception e) {
				log.error("error occurred while parsing refertic_number :: {}", e);
			}
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("licenseType"))
				&& !filterRequestDTO.getFilters().get("licenseType").toString().trim().isEmpty()) {

			String rating = String.valueOf(filterRequestDTO.getFilters().get("licenseType").toString());
			list.add(cb.equal(from.get("licenseType"), rating));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("entityType"))
				&& !filterRequestDTO.getFilters().get("entityType").toString().trim().isEmpty()) {

			String rating = String.valueOf(filterRequestDTO.getFilters().get("entityType").toString());
			list.add(cb.equal(from.get("entityType"), rating));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("unitName"))
				&& !filterRequestDTO.getFilters().get("unitName").toString().trim().isEmpty()) {

			String rating = String.valueOf(filterRequestDTO.getFilters().get("unitName").toString());
			list.add(cb.equal(from.get("unitName"), rating));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("licenseNo"))
				&& !filterRequestDTO.getFilters().get("licenseNo").toString().trim().isEmpty()) {
			List<String> licenseNo = (List<String>) (filterRequestDTO.getFilters().get("licenseNo"));
			if (!licenseNo.isEmpty()) {
				Expression<String> mainModule = from.get("licenseNo");
				list.add(mainModule.in(licenseNo));
			}
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("licenseStatus"))
				&& !filterRequestDTO.getFilters().get("licenseStatus").toString().trim().isEmpty()) {

			String rating = String.valueOf(filterRequestDTO.getFilters().get("licenseStatus").toString());
			list.add(cb.equal(from.get("licenseStatus"), rating));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("address"))
				&& !filterRequestDTO.getFilters().get("address").toString().trim().isEmpty()) {

			String rating = String.valueOf(filterRequestDTO.getFilters().get("address").toString());
			list.add(cb.equal(from.get("address"), rating));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("mobileNo"))
				&& !filterRequestDTO.getFilters().get("mobileNo").toString().trim().isEmpty()) {

			String rating = String.valueOf(filterRequestDTO.getFilters().get("mobileNo").toString());
			list.add(cb.equal(from.get("mobileNo"), rating));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("emailId"))
				&& !filterRequestDTO.getFilters().get("emailId").toString().trim().isEmpty()) {

			String rating = String.valueOf(filterRequestDTO.getFilters().get("emailId").toString());
			list.add(cb.equal(from.get("emailId"), rating));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("shopCode"))
				&& !filterRequestDTO.getFilters().get("shopCode").toString().trim().isEmpty()) {

			List<String> shopCode = (List<String>) (filterRequestDTO.getFilters().get("shopCode"));
			if (!shopCode.isEmpty()) {
				Expression<String> mainModule = from.get("shopCode");
				list.add(mainModule.in(shopCode));
			}
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("shopName"))
				&& !filterRequestDTO.getFilters().get("shopName").toString().trim().isEmpty()) {

			String rating = String.valueOf(filterRequestDTO.getFilters().get("shopName").toString());
			list.add(cb.equal(from.get("shopName"), rating));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("district"))
				&& !filterRequestDTO.getFilters().get("district").toString().trim().isEmpty()) {

			String rating = String.valueOf(filterRequestDTO.getFilters().get("district").toString());
			list.add(cb.equal(from.get("district"), rating));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("changereqApplnNo"))
				&& !filterRequestDTO.getFilters().get("changereqApplnNo").toString().trim().isEmpty()) {

			String rating = String.valueOf(filterRequestDTO.getFilters().get("changereqApplnNo").toString());
			list.add(cb.equal(from.get("changereqApplnNo"), rating));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("description"))
				&& !filterRequestDTO.getFilters().get("description").toString().trim().isEmpty()) {

			String rating = String.valueOf(filterRequestDTO.getFilters().get("description").toString());
			list.add(cb.equal(from.get("description"), rating));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("iescmsUrl"))
				&& !filterRequestDTO.getFilters().get("iescmsUrl").toString().trim().isEmpty()) {

			String rating = String.valueOf(filterRequestDTO.getFilters().get("iescmsUrl").toString());
			list.add(cb.equal(from.get("iescmsUrl"), rating));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("iescmsUuid"))
				&& !filterRequestDTO.getFilters().get("iescmsUuid").toString().trim().isEmpty()) {

			String rating = String.valueOf(filterRequestDTO.getFilters().get("iescmsUuid").toString());
			list.add(cb.equal(from.get("iescmsUuid"), rating));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("departmentUrl"))
				&& !filterRequestDTO.getFilters().get("departmentUrl").toString().trim().isEmpty()) {

			String rating = String.valueOf(filterRequestDTO.getFilters().get("departmentUrl").toString());
			list.add(cb.equal(from.get("departmentUrl"), rating));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("departmentUuid"))
				&& !filterRequestDTO.getFilters().get("departmentUuid").toString().trim().isEmpty()) {

			String rating = String.valueOf(filterRequestDTO.getFilters().get("departmentUuid").toString());
			list.add(cb.equal(from.get("departmentUuid"), rating));
		}

		if (filterRequestDTO.getFilters().containsKey("currentlyWorkwith")
				&& !filterRequestDTO.getFilters().get("currentlyWorkwith").toString().trim().isEmpty()) {
			Predicate currentlyWorkwith = cb.equal(from.get("currentlyWorkwith"),
					filterRequestDTO.getFilters().get("currentlyWorkwith"));
			Predicate approvedBy = cb.equal(from.get("approvedBy"),
					filterRequestDTO.getFilters().get("currentlyWorkwith"));
			Predicate currentlyWorkWithApprovedBy = cb.or(currentlyWorkwith, approvedBy);
			list.add(currentlyWorkWithApprovedBy);
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("approvedBy"))
				&& !filterRequestDTO.getFilters().get("approvedBy").toString().trim().isEmpty()) {

			String rating = String.valueOf(filterRequestDTO.getFilters().get("approvedBy").toString());
			list.add(cb.equal(from.get("approvedBy"), rating));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("userName"))
				&& !filterRequestDTO.getFilters().get("userName").toString().trim().isEmpty()) {

			String rating = String.valueOf(filterRequestDTO.getFilters().get("userName").toString());
			list.add(cb.equal(from.get("userName"), rating));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("featureId"))
				&& !filterRequestDTO.getFilters().get("featureId").toString().trim().isEmpty()) {

			String rating = String.valueOf(filterRequestDTO.getFilters().get("featureId").toString());
			list.add(cb.equal(from.get("featureId"), rating));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("modifiedBy"))
				&& !filterRequestDTO.getFilters().get("modifiedBy").toString().trim().isEmpty()) {

			String modifiedBy = (filterRequestDTO.getFilters().get("modifiedBy").toString());
			list.add(cb.equal(from.get("modifiedBy"), modifiedBy));
		}
		if ((Objects.nonNull(filterRequestDTO.getFilters().get("isCustomer"))
				&& !filterRequestDTO.getFilters().get("isCustomer").toString().trim().isEmpty())
				&& (Objects.nonNull(filterRequestDTO.getFilters().get("filterBy"))
						&& !filterRequestDTO.getFilters().get("filterBy").toString().trim().isEmpty())) {

			if (Boolean.parseBoolean(filterRequestDTO.getFilters().get("isCustomer").toString())) {
				list.add(cb.equal(from.get("createdBy"),
						Long.parseLong(filterRequestDTO.getFilters().get("filterBy").toString())));
			} else {
				list.add(cb.equal(from.get("currentlyWorkwith"),
						filterRequestDTO.getFilters().get("filterBy").toString()));
			}
		}
		if (Objects.nonNull(filterRequestDTO.getFilters().get("applnStatus"))
				&& !filterRequestDTO.getFilters().get("applnStatus").toString().trim().isEmpty()) {
			ApplnStatus status = null;
			if (filterRequestDTO.getFilters().get("applnStatus").toString().equals(ApplnStatus.ACCEPTED.name())) {
				status = ApplnStatus.ACCEPTED;
			} else if (filterRequestDTO.getFilters().get("applnStatus").toString()
					.equals(ApplnStatus.INPROGRESS.name())) {
				status = ApplnStatus.INPROGRESS;
			} else if (filterRequestDTO.getFilters().get("applnStatus").toString()
					.equals(ApplnStatus.REQUESTFORCLARIFICATION.name())) {
				status = ApplnStatus.REQUESTFORCLARIFICATION;
			} else if (filterRequestDTO.getFilters().get("applnStatus").toString()
					.equals(ApplnStatus.REJECTED.name())) {
				status = ApplnStatus.REJECTED;
			}

			else if (filterRequestDTO.getFilters().get("applnStatus").toString().equals(ApplnStatus.FORWARDED.name())) {
				status = ApplnStatus.FORWARDED;
			} else if (filterRequestDTO.getFilters().get("applnStatus").toString()
					.equals(ApplnStatus.APPROVED.name())) {
				status = ApplnStatus.APPROVED;
			}

			else if (filterRequestDTO.getFilters().get("applnStatus").toString().equals(ApplnStatus.CANCELLED.name())) {
				status = ApplnStatus.CANCELLED;
			}

			list.add(cb.equal(from.get("applnStatus"), status));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("changereqStatus"))
				&& !filterRequestDTO.getFilters().get("changereqStatus").toString().trim().isEmpty()) {

			ChangereqStatus status = null;

			if (filterRequestDTO.getFilters().get("changereqStatus").toString()
					.equals(ChangereqStatus.ACCEPTED.name())) {
				status = ChangereqStatus.ACCEPTED;
			} else if (filterRequestDTO.getFilters().get("changereqStatus").toString()
					.equals(ChangereqStatus.INPROGRESS.name())) {
				status = ChangereqStatus.INPROGRESS;
			} else if (filterRequestDTO.getFilters().get("changereqStatus").toString()
					.equals(ChangereqStatus.REQUESTFORCLARIFICATION.name())) {
				status = ChangereqStatus.REQUESTFORCLARIFICATION;
			} else if (filterRequestDTO.getFilters().get("changereqStatus").toString()
					.equals(ChangereqStatus.REJECTED.name())) {
				status = ChangereqStatus.REJECTED;
			}

			else if (filterRequestDTO.getFilters().get("changereqStatus").toString()
					.equals(ChangereqStatus.APPROVED.name())) {
				status = ChangereqStatus.APPROVED;
			} else if (filterRequestDTO.getFilters().get("changereqStatus").toString()
					.equals(ChangereqStatus.PENDING.name())) {
				status = ChangereqStatus.PENDING;
			} else if (filterRequestDTO.getFilters().get("changereqStatus").toString()
					.equals(ChangereqStatus.ASSIGNED.name())) {
				status = ChangereqStatus.ASSIGNED;
			}

			else if (filterRequestDTO.getFilters().get("changereqStatus").toString()
					.equals(ChangereqStatus.CANCELLED.name())) {
				status = ChangereqStatus.CANCELLED;
			}

			else if (filterRequestDTO.getFilters().get("changereqStatus").toString()
					.equals(ChangereqStatus.COMPLETED.name())) {
				status = ChangereqStatus.COMPLETED;
			}

			list.add(cb.equal(from.get("changereqStatus"), status));

		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("raisedBy"))
				&& !filterRequestDTO.getFilters().get("raisedBy").toString().trim().isEmpty()) {

			String raisedBy = String.valueOf(filterRequestDTO.getFilters().get("raisedBy").toString());
			list.add(cb.equal(from.get("raisedBy"), raisedBy));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("userMobileNumber"))
				&& !filterRequestDTO.getFilters().get("userMobileNumber").toString().trim().isEmpty()) {

			String userMobileNumber = String.valueOf(filterRequestDTO.getFilters().get("userMobileNumber").toString());
			list.add(cb.equal(from.get("userMobileNumber"), userMobileNumber));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("assignTo"))
				&& !filterRequestDTO.getFilters().get("assignTo").toString().trim().isEmpty()) {

			Long assignTo = Long.valueOf(filterRequestDTO.getFilters().get("assignTo").toString());
			list.add(cb.equal(from.get("assignTo"), assignTo));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("districtCode"))
				&& !filterRequestDTO.getFilters().get("districtCode").toString().trim().isEmpty()) {
			List<String> districtCode = (List<String>) (filterRequestDTO.getFilters().get("districtCode"));
			if (!districtCode.isEmpty()) {
				Expression<String> mainModule = from.get("districtCode");
				list.add(mainModule.in(districtCode));
			}
		}

	}

	public GenericResponse updateApproval(ApprovalDTO approvalDto) {
		GenericResponse resp = null;
		Optional<ChangeRequestEntity> changeEntity = changereqrepo.findById(approvalDto.getId());
		if (changeEntity.isPresent()) {
			ChangeRequestEntity change = changeEntity.get();
			change.setChangereqStatus(approvalDto.getChangereqStatus());
			change.setApplnStatus(approvalDto.getApplnStatus());
			change.setModifiedBy(String.valueOf(approvalDto.getUserId()));
			change.setModifiedDate(new Date());
			change.setRemarks(approvalDto.getRemarks());
			change.setApprovedBy(changeEntity.get().getCurrentlyWorkwith());
			change.setDescription(changeEntity.get().getDescription());
			change.setIescmsUrl(changeEntity.get().getIescmsUrl());
			change.setIescmsUuid(changeEntity.get().getIescmsUuid());
			change.setDepartmentUrl(changeEntity.get().getDepartmentUrl());
			change.setDepartmentUuid(changeEntity.get().getDepartmentUuid());
			change.setLicenseType(changeEntity.get().getLicenseType());
			change.setEntityType(changeEntity.get().getEntityType());
			change.setUnitName(changeEntity.get().getUnitName());
			change.setLicenseNo(changeEntity.get().getLicenseNo());
			change.setLicenseStatus(changeEntity.get().getLicenseStatus());
			change.setAddress(changeEntity.get().getAddress());
			change.setEmailId(changeEntity.get().getEmailId());
			change.setMobileNo(changeEntity.get().getMobileNo());
			change.setShopCode(changeEntity.get().getShopCode());
			change.setShopName(changeEntity.get().getShopName());
			change.setDistrict(changeEntity.get().getDistrict());
			change.setChangereqApplnNo(changeEntity.get().getChangereqApplnNo());
			change.setCurrentlyWorkwith(changeEntity.get().getCurrentlyWorkwith());
			change.setApprovedBy(changeEntity.get().getApprovedBy());
			change.setUserName(changeEntity.get().getUserName());
			change.setRaisedBy(changeEntity.get().getRaisedBy());
			change.setUserMobileNumber(changeEntity.get().getUserMobileNumber());
			change.setRemarks(changeEntity.get().getRemarks());
			changereqrepo.save(change);
			ChangeRequestLogEntity changerequestlog = new ChangeRequestLogEntity();
			WorkflowDTO workflowStatusUpdateDto = new WorkflowDTO();

			if (approvalDto.getEvent().equals("APPROVED")
					&& ApplnStatus.INPROGRESS.equals(approvalDto.getApplnStatus())) {

				changerequestlog.setAction(approvalDto.getAction());
				changerequestlog.setApplnNo(approvalDto.getApplnNo());
				changerequestlog.setUserName(approvalDto.getUserName());
				changerequestlog.setComments("Application forwarded successfully");
				changerequestlog.setRemarks(approvalDto.getRemarks());
				changerequestlog.setActionperformedby(approvalDto.getActionperformedby());
				changerequestlog.setDesignation(approvalDto.getDesignation());
				workflowStatusUpdateDto.setEvent(approvalDto.getEvent());
				resp = Library.getSuccessfulResponse(approvalDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
						ErrorMessages.FORWARDED);

			}

			else if (ApplnStatus.FORWARDED.equals(approvalDto.getApplnStatus())) {
				changerequestlog.setAction(approvalDto.getAction());
				changerequestlog.setApplnNo(approvalDto.getApplnNo());
				changerequestlog.setUserName(approvalDto.getUserName());
				changerequestlog.setComments("Application forwarded successfully");
				changerequestlog.setRemarks(approvalDto.getRemarks());
				changerequestlog.setActionperformedby(approvalDto.getActionperformedby());
				changerequestlog.setDesignation(approvalDto.getDesignation());
				change.setApplnStatus(ApplnStatus.FORWARDED);
				change.setChangereqStatus(ChangereqStatus.INPROGRESS);
				changereqrepo.save(change);
				changereqlog.save(changerequestlog);
				workflowStatusUpdateDto.setEvent(approvalDto.getEvent());
				resp = Library.getSuccessfulResponse(approvalDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
						ErrorMessages.FORWARDED);
			}

			else if (ApplnStatus.REJECTED.equals(approvalDto.getApplnStatus())) {
				changerequestlog.setComments("Application rejected successfully");
				changerequestlog.setRemarks(approvalDto.getRemarks());
				changerequestlog.setApplnNo(approvalDto.getApplnNo());
				changerequestlog.setUserName(approvalDto.getUserName());
				changerequestlog.setAction(approvalDto.getAction());
				changerequestlog.setActionperformedby(approvalDto.getActionperformedby());
				changerequestlog.setDesignation(approvalDto.getDesignation());
				// eal.setVendorStatus(null);
				change.setChangereqStatus(ChangereqStatus.REJECTED);
				change.setCurrentlyWorkwith(null);
				changereqrepo.save(change);
				changereqlog.save(changerequestlog);
				workflowStatusUpdateDto.setEvent(approvalDto.getEvent());
				resp = Library.getSuccessfulResponse(approvalDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
						ErrorMessages.REJECTED);

			} else if (ApplnStatus.APPROVED.equals(approvalDto.getApplnStatus())) {
				Optional<ChangeRequestEntity> changeEntityApproved = changereqrepo.findById(approvalDto.getId());
				ChangeRequestEntity aprove = changeEntityApproved.get();
				aprove.setChangereqStatus(ChangereqStatus.PENDING);
				aprove.setCurrentlyWorkwith("");
				aprove=changerequestrepository.save(aprove);
				changerequestlog.setRemarks(approvalDto.getRemarks());
				changerequestlog.setComments("Application approved successfully");
				changerequestlog.setApplnNo(approvalDto.getApplnNo());
				changerequestlog.setUserName(approvalDto.getUserName());
				changerequestlog.setAction(approvalDto.getAction());
				changerequestlog.setActionperformedby(approvalDto.getActionperformedby());
				changerequestlog.setDesignation(approvalDto.getDesignation());
				changereqlog.save(changerequestlog);
				workflowStatusUpdateDto.setEvent(approvalDto.getEvent());
				resp = Library.getSuccessfulResponse(approvalDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
						ErrorMessages.APPROVED);

			} else if (ApplnStatus.REQUESTFORCLARIFICATION.equals(approvalDto.getApplnStatus())) {
				changerequestlog.setRemarks(approvalDto.getRemarks());
				changerequestlog.setComments("Application requestforclarification successfully");
				changerequestlog.setApplnNo(approvalDto.getApplnNo());
				changerequestlog.setUserName(approvalDto.getUserName());
				changerequestlog.setAction(approvalDto.getAction());
				changerequestlog.setActionperformedby(approvalDto.getActionperformedby());
				changerequestlog.setDesignation(approvalDto.getDesignation());
				change.setChangereqStatus(ChangereqStatus.INPROGRESS);
				changereqlog.save(changerequestlog);
				workflowStatusUpdateDto.setEvent(approvalDto.getEvent());
				resp = Library.getSuccessfulResponse(approvalDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
						ErrorMessages.CLARIFICATION_REQUEST_SENT_SUCCESSFULLY);
				workflowStatusUpdateDto.setSendBackTo("Level 1");
			}

//			else if (ApplnStatus.FORWARDED.equals(approvalDto.getApplnStatus())) {
//				changerequestlog.setComments("Application forwarded successfully");
//				changerequestlog.setRemarks(approvalDto.getRemarks());
//				changerequestlog.setApplnNo(approvalDto.getApplnNo());
//				changerequestlog.setUserName(approvalDto.getUserName());
//				changerequestlog.setAction(approvalDto.getAction());
//				changerequestlog.setActionperformedby(approvalDto.getActionperformedby());
//				changerequestlog.setDesignation(approvalDto.getDesignation());
//				changereqrepo.save(change);
//				changereqlog.save(changerequestlog);
//				workflowStatusUpdateDto.setEvent(approvalDto.getEvent());
//				resp = Library.getSuccessfulResponse(approvalDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
//						ErrorMessages.FORWARDED);
//
//			}

			if (approvalDto.getEntityType().equalsIgnoreCase("BREWERY")
					|| approvalDto.getEntityType().equalsIgnoreCase("BOTTELING")) {
				workflowStatusUpdateDto.setSubModuleNameCode("HELPDESK_CHANGE_REQUEST_AEC");
			}
			if (approvalDto.getEntityType().equalsIgnoreCase("DISTILLERY")) {
				workflowStatusUpdateDto.setSubModuleNameCode("HELPDESK_CHANGE_REQUEST_DISTILLERY");
			}

			else {
				workflowStatusUpdateDto.setSubModuleNameCode(approvalDto.getSubModuleNameCode());
			}

			workflowStatusUpdateDto.setApplicationNumber(changeEntity.get().getChangereqApplnNo());
			workflowStatusUpdateDto.setModuleNameCode(approvalDto.getModuleNameCode());
			// workflowStatusUpdateDto.setSubModuleNameCode(approvalDto.getSubModuleNameCode());
			workflowStatusUpdateDto.setLevel(approvalDto.getLevel());
			callWorkFlowChangerequestService(workflowStatusUpdateDto);
			return resp;
		} else {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}

	}

	@Override
	public GenericResponse update(ChangereqRequestDTO requestDTO) {
		try {
			Optional<ChangeRequestEntity> existingEntity = changereqrepo.findById(requestDTO.getId());

			if (!existingEntity.isPresent()) {
				return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), "Record not exist");
			}

			ChangeRequestEntity changereq = existingEntity.get();
			changereq.setApplnStatus(ApplnStatus.INPROGRESS);
			changereq.setEmailId(changereq.getEmailId());
			changereq.setChangereqStatus(changereq.getChangereqStatus());
			changereq.setLicenseType(changereq.getLicenseType());
			changereq.setEntityType(changereq.getEntityType());
			changereq.setUnitName(changereq.getUnitName());
			changereq.setLicenseNo(changereq.getLicenseNo());
			changereq.setLicenseStatus(changereq.getLicenseStatus());
			changereq.setAddress(changereq.getAddress());
			changereq.setMobileNo(changereq.getMobileNo());
			changereq.setShopCode(changereq.getShopCode());
			changereq.setShopName(changereq.getShopName());
			changereq.setDistrict(changereq.getDistrict());
			changereq.setDescription(requestDTO.getDescription());
			changereq.setIescmsUrl(requestDTO.getIescmsUrl());
			changereq.setIescmsUuid(requestDTO.getIescmsUuid());
			changereq.setDepartmentUrl(requestDTO.getDepartmentUrl());
			changereq.setDepartmentUuid(requestDTO.getDepartmentUuid());
			changereq.setUserName(changereq.getUserName());
			changereq.setModifiedBy(requestDTO.getModifiedBy());
			changereq.setModifiedDate(new Date());
			changereq.setFeatureId(changereq.getFeatureId());
			changereq.setRaisedBy(changereq.getRaisedBy());
			changereq.setUserMobileNumber(changereq.getUserMobileNumber());
			changereq.setApplnStatus(changereq.getApplnStatus());
			changereq.setChangereqApplnNo(changereq.getChangereqApplnNo());
			changereq.setCurrentlyWorkwith(changereq.getCurrentlyWorkwith());
			changereq.setApprovedBy(changereq.getApprovedBy());
			changereqrepo.save(changereq);

			try {
				WorkflowDTO workflowStatusUpdateDto = new WorkflowDTO();
				workflowStatusUpdateDto.setApplicationNumber(changereq.getChangereqApplnNo());
				workflowStatusUpdateDto.setModuleNameCode(requestDTO.getModuleNameCode());
				if (requestDTO.getEntityType().equalsIgnoreCase("BREWERY")
						|| requestDTO.getEntityType().equalsIgnoreCase("BOTTELING")) {
					workflowStatusUpdateDto.setSubModuleNameCode("HELPDESK_CHANGE_REQUEST_AEC");
				}
				if (requestDTO.getEntityType().equalsIgnoreCase("DISTILLERY")) {
					workflowStatusUpdateDto.setSubModuleNameCode("HELPDESK_CHANGE_REQUEST_DISTILLERY");
				} else {
					workflowStatusUpdateDto.setSubModuleNameCode(requestDTO.getSubModuleNameCode());
				}
				workflowStatusUpdateDto.setEvent(requestDTO.getEvent());
				workflowStatusUpdateDto.setLevel(requestDTO.getLevel());
				callWorkFlowChangerequestService(workflowStatusUpdateDto);
				changerequestlogadd(requestDTO);
			} catch (Exception e) {
				log.error("Change Request workflow initiate error", e);
				// Log the error using log.error instead of log.info
			}

			return Library.getSuccessfulResponse(changereq, ErrorCode.CREATED.getErrorCode(),
					ErrorMessages.RECORED_CREATED);
		} catch (Exception e) {
			log.error("Error while updating change request", e);
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), "Internal server error");
		}
	}

	public GenericResponse changereqStatusUpdate(ApprovalDTO requestDTO) {
		try {
			Optional<ChangeRequestEntity> changereqDetails = changereqrepo
					.findByChangereqApplnNoIgnoreCase(requestDTO.getApplnNo());
			if (!changereqDetails.isPresent()) {
				return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), "Record not exist");
			} else {
				ChangeRequestEntity changereqStatus = changereqDetails.get();
				changereqStatus.setChangereqStatus(requestDTO.getChangereqStatus());

				if (requestDTO.getAssignTo() != null) {
					Optional<UserEntity> uOptional = userRepository.findById(requestDTO.getAssignTo());
					if (!uOptional.isPresent()) {
						throw new InvalidDataValidation(
								ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { USER_ID }));
					}
					changereqStatus.setAssignTo(uOptional.get());
				}

				changereqStatus.setRemarks(requestDTO.getRemarks());
				changereqrepo.save(changereqStatus);
				changerequestlogupdate(requestDTO);
				return Library.getSuccessfulResponse(changereqStatus, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
						ErrorMessages.RECORED_UPDATED);
			}

		} catch (Exception e) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), e.getMessage());
		}
	}

	@Override
	public GenericResponse changereqCount(ChangereqCountRequestDTO requestDto) {
		List<ChangereqCountDTO> finallist = new ArrayList<>();
		List<ChangereqCountDTO> ticketCountResponseDTOList = new ArrayList<>();
		final Date fromDate;
		final Date toDate;
		try {
			fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(requestDto.getFromDate() + " " + "00:00:00");
		} catch (ParseException e) {
			log.error("error occurred while parsing date : {}", e.getMessage());
			throw new InvalidDataValidation("Invalid date parameter passed");
		}
		try {
			toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(requestDto.getToDate() + " " + "23:59:59");
		} catch (ParseException e) {
			log.error("error occurred while parsing date : {}", e.getMessage());
			throw new InvalidDataValidation("Invalid date parameter passed");
		}

		List<ChangereqStatus> ticketStatusList = Arrays.asList(ChangereqStatus.values());

		ticketStatusList.forEach(ticketStatus -> {
			Integer appcount = 0;

			if (Objects.isNull(ticketStatus.getId()) || StringUtils.isBlank(ticketStatus.getName())) {
				return; // Skipping the iteration if the conditions are met
			}

			ChangereqCountDTO changereqCountDTO = new ChangereqCountDTO();
			changereqCountDTO.setStatus(ticketStatus.getName());

			List<String> licnumber;
			if (requestDto.getLicenseNumber().isEmpty()) {
				licnumber = null;
			} else {
				licnumber = requestDto.getLicenseNumber();
			}
			List<String> shopCode;
			if (requestDto.getShopCode().isEmpty()) {
				shopCode = null;
			} else {
				shopCode = requestDto.getShopCode();
			}

			appcount = changereqrepo.getAppCountByStatusAndDateRangeAndLicenseNumber(ticketStatus.getId(), fromDate,
					toDate, licnumber, shopCode);

			changereqCountDTO.setCount(appcount);
			ticketCountResponseDTOList.add(changereqCountDTO);
		});

		finallist.addAll(ticketCountResponseDTOList);

		return Library.getSuccessfulResponse(finallist, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

	public GenericResponse getCount() {
		try {
			List<ChangereqSummaryDTO> entitysummary = changereqrepo.getCount();
			if (entitysummary.isEmpty()) {
				throw new RecordNotFoundException("No record found");
			}

			else {
				return Library.getSuccessfulResponse(entitysummary, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
						ErrorMessages.RECORED_FOUND);
			}

		} catch (RecordNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"An error occurred while retrieving summary count.", e);
		}
	}

	public String callWorkFlowChangerequestService(WorkflowDTO workflowStatusUpdateDto) {
		log.info(":::::WORKFLOW:::");
		Optional<ChangeRequestEntity> deptOptional = changereqrepo
				.findByChangereqApplnNoIgnoreCase(workflowStatusUpdateDto.getApplicationNumber());
		if (deptOptional.isPresent()) {
			String applnno = deptOptional.get().getChangereqApplnNo();
			WorkflowDTO workflowDto = new WorkflowDTO();
			HttpHeaders headers = new HttpHeaders();
			workflowDto.setApplicationNumber(applnno);
			workflowDto.setSubModuleNameCode(workflowStatusUpdateDto.getSubModuleNameCode());
			workflowDto.setCallbackURL(domain + "/helpdesk/changerequest/updateWorkFlow");// this is call back URL to
																							// update the
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
					HttpEntity<?> httpRequestEntity = new HttpEntity<>(workflowDto, headers);

					log.info("=======callWorkFlowService URL============" + uri.toString());

					log.info("=======jjjjjjjjjjj============" + gson.toJson(workflowDto));

					if (restTemplate != null) {
						String response = restTemplate.postForObject(uri.toString(), httpRequestEntity, String.class);
						log.info("=======callWorkFlowService Response============" + response);
						return "Success";
					} else {
						log.error("::::RestTemplate is null.:::");
						log.error("Due to technical issue.Please try after some time");
						return "Due to technical issue.Please try after some time";

					}

				}
			} catch (Exception exception) {
				log.error("=======callWorkFlowService catch block============", exception);
				return "Due to technical issue.Please try after some time";

			}
		}

		else {
			log.info(":::Application number not available::");
		}

		return "a";
	}

	public Boolean updateChangeRequestWorkFlowDetails(WorkFlowStatusUpdateDTO workflowStatusUpdateDto) {
		Boolean isSuccess = true;
		log.info("::::::APPLICATIONNO::::::" + workflowStatusUpdateDto.getApplicationNumber());
		try {
			Optional<ChangeRequestEntity> deptOptional = changereqrepo
					.findByChangereqApplnNo(workflowStatusUpdateDto.getApplicationNumber().trim());
			if (deptOptional.isPresent()) {
				log.info("::::::Change request Enter::::::" + deptOptional.get().getChangereqApplnNo());

				deptOptional.get().setCurrentlyWorkwith(workflowStatusUpdateDto.getStage());

				deptOptional.get().setModifiedDate(new Date());

				changereqrepo.save(deptOptional.get());

				log.info("ChangeRequestENTITY::::" + deptOptional.get());

				log.info("currentlywk" + workflowStatusUpdateDto.getStage());

				log.info("Change request workflowStatusUpdateDto" + workflowStatusUpdateDto);

			} else {
				log.info("No ChangeRequestEntity found for application number:"
						+ workflowStatusUpdateDto.getApplicationNumber());
			}

		} catch (Exception exception) {
			log.error("===========error while updaing updateWorkFlowDetails data change request===",
					exception.fillInStackTrace());
			isSuccess = false;
		}
		return isSuccess;
	}

	public GenericResponse getLogByApplicationNo(String applicationNo) {
		List<ChangeRequestLogEntity> changereqLogEntity = changereqlog.findByApplnNoOrderByIdDesc(applicationNo);
		if (changereqLogEntity.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		} else {
			return Library.getSuccessfulResponse(changereqLogEntity, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}
	}

	@Override
	public GenericResponse draftcall(ChangereqRequestDTO requestDTO) {
		ChangeRequestEntity entity = null;
		try {
			requestDTO.setChangereqApplnNo(requestDTO.getChangereqApplnNo());
			log.info("::Workflow Entry:::");
			WorkflowDTO workflowStatusUpdateDto = new WorkflowDTO();
			workflowStatusUpdateDto.setApplicationNumber(requestDTO.getChangereqApplnNo());
			workflowStatusUpdateDto.setModuleNameCode(requestDTO.getModuleNameCode());
			if (requestDTO.getEntityType().equalsIgnoreCase("BREWERY")
					|| requestDTO.getEntityType().equalsIgnoreCase("BOTTELING")) {
				workflowStatusUpdateDto.setSubModuleNameCode("HELPDESK_CHANGE_REQUEST_AEC");
			}
			if (requestDTO.getEntityType().equalsIgnoreCase("DISTILLERY")) {
				workflowStatusUpdateDto.setSubModuleNameCode("HELPDESK_CHANGE_REQUEST_DISTILLERY");
			} else {
				workflowStatusUpdateDto.setSubModuleNameCode(requestDTO.getSubModuleNameCode());
			}
			workflowStatusUpdateDto.setEvent(requestDTO.getEvent());
			workflowStatusUpdateDto.setLevel(requestDTO.getLevel());
			String worflowresponse = draftcallWorkFlowChangerequestService(workflowStatusUpdateDto);
			changerequestlogadd(requestDTO);
			if (worflowresponse.equalsIgnoreCase("Success")) {
				Thread.sleep(3000);
				Optional<ChangeRequestEntity> deptOptional = changereqrepo
						.findByChangereqApplnNoIgnoreCase(workflowStatusUpdateDto.getApplicationNumber());
				ChangeRequestEntity changereqStatus = deptOptional.get();
				requestDTO.setChangereqStatus(ChangereqStatus.INPROGRESS);
				changereqStatus.setChangereqStatus(requestDTO.getChangereqStatus());
				changereqStatus.setDepartmentUrl(requestDTO.getDepartmentUrl());
				changereqStatus.setIescmsUrl(requestDTO.getIescmsUrl());
				changereqStatus.setDescription(requestDTO.getDescription());
				changereqrepo.save(changereqStatus);

				return Library.getSuccessfulResponse(changereqStatus, ErrorCode.CREATED.getErrorCode(),
						ErrorMessages.DRAFT_RECORED_CREATED);
			} else {
				Thread.sleep(3000);
				Optional<ChangeRequestEntity> deptOptional = changereqrepo
						.findByChangereqApplnNoIgnoreCase(workflowStatusUpdateDto.getApplicationNumber());
				ChangeRequestEntity changereqStatus = deptOptional.get();
				requestDTO.setChangereqStatus(ChangereqStatus.DRAFT);
				changereqStatus.setChangereqStatus(requestDTO.getChangereqStatus());
				changereqrepo.save(changereqStatus);
				return Library.getFailedfulResponse(changereqStatus,
						ErrorCode.ERROR_RESPONSE_FROM_CALLING_SERVICE.getErrorCode(), ErrorMessages.TECHNICAL_ISSUE);

			}

		} catch (Exception e) {
			log.info(":::Change Request save:::" + e);
		}
		return null;

	}

	public String draftcallWorkFlowChangerequestService(WorkflowDTO workflowStatusUpdateDto) {
		log.info(":::::WORKFLOW:::");
		Optional<ChangeRequestEntity> deptOptional = changereqrepo
				.findByChangereqApplnNoIgnoreCase(workflowStatusUpdateDto.getApplicationNumber());
		if (deptOptional.isPresent()) {
			String applnno = deptOptional.get().getChangereqApplnNo();
			WorkflowDTO workflowDto = new WorkflowDTO();
			HttpHeaders headers = new HttpHeaders();
			workflowDto.setApplicationNumber(applnno);
			workflowDto.setSubModuleNameCode(workflowStatusUpdateDto.getSubModuleNameCode());
			workflowDto.setCallbackURL(domain + "/helpdesk/changerequest/updateWorkFlow");// this is call back URL to
																							// update the
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
					HttpEntity<?> httpRequestEntity = new HttpEntity<>(workflowDto, headers);

					log.info("=======callWorkFlowService URL============" + uri.toString());

					log.info("=======jjjjjjjjjjj============" + gson.toJson(workflowDto));

					if (restTemplate != null) {
						String response = restTemplate.postForObject(uri.toString(), httpRequestEntity, String.class);
						log.info("=======callWorkFlowService Response============" + response);
						return "Success";
					} else {
						log.error("::::RestTemplate is null.:::");
						log.error("Due to technical issue.Please try after some time");
						return "Due to technical issue.Please try after some time";

					}

				}
			} catch (Exception exception) {
				log.error("=======callWorkFlowService catch block============", exception);
				return "Due to technical issue.Please try after some time";

			}
		}

		else {
			log.info(":::Application number not available::");
		}

		return "a";
	}

}
