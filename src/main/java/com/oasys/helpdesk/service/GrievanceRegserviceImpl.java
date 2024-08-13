package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.CODE;
import static com.oasys.helpdesk.constant.Constant.DEPARTMENT_NAME;
import static com.oasys.helpdesk.constant.Constant.ID;
import static com.oasys.helpdesk.constant.Constant.STATUS;
import static com.oasys.helpdesk.constant.Constant.TICKETSTATUS_NAME;

import static com.oasys.helpdesk.constant.Constant.CREATED_BY;
import static com.oasys.helpdesk.constant.Constant.CREATED_DATE;
import static com.oasys.helpdesk.constant.Constant.DATE_FORMAT;
import static com.oasys.helpdesk.constant.Constant.DESC;
import static com.oasys.helpdesk.constant.Constant.END_TIME;
import static com.oasys.helpdesk.constant.Constant.ID;
import static com.oasys.helpdesk.constant.Constant.MODIFIED_DATE;
import static com.oasys.helpdesk.constant.Constant.START_TIME;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import javax.validation.Valid;

//gitlab.oasys.co/up_excise/api/helpdeskservice.git

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.oasys.helpdesk.async.AsyncCommunicationExecution;
import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.DashboardCount;
import com.oasys.helpdesk.dto.GrievanceDashboardByMonthCountDTO;
import com.oasys.helpdesk.dto.GrievanceDashboardCountDTO;
import com.oasys.helpdesk.dto.GrievanceRegRequestDTO;
import com.oasys.helpdesk.dto.GrievanceRegResponseDTO;
import com.oasys.helpdesk.dto.GrievanceTicketCountResponseDTO;
import com.oasys.helpdesk.dto.GrievanceTicketStatusDTO;
import com.oasys.helpdesk.dto.GrievanceTicketStatusResponseDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.PaginationResponseDTO;
import com.oasys.helpdesk.dto.SMSDetails;
import com.oasys.helpdesk.dto.TicketCountResponseDTO;
import com.oasys.helpdesk.dto.TicketstausRequestDTO;
import com.oasys.helpdesk.dto.TicketstausResponseDTO;
import com.oasys.helpdesk.entity.ApplicationConstantEntity;
import com.oasys.helpdesk.entity.CreateTicketEntity;
import com.oasys.helpdesk.entity.GrievanceCategoryEntity;
import com.oasys.helpdesk.entity.GrievanceEscalationWorkflowEntity;
import com.oasys.helpdesk.entity.GrievanceIssueDetails;
import com.oasys.helpdesk.entity.GrievanceOTPVerificationEntity;
import com.oasys.helpdesk.entity.GrievanceSlaEntity;
import com.oasys.helpdesk.entity.GrievanceTicketStatusEntity;
import com.oasys.helpdesk.entity.GrievanceWorkflowEntity;
import com.oasys.helpdesk.entity.GrievanceregisterEntity;
import com.oasys.helpdesk.entity.HelpdeskTicketAuditEntity;
import com.oasys.helpdesk.entity.TemplateEntity;
import com.oasys.helpdesk.entity.TicketStatusEntity;
import com.oasys.helpdesk.entity.UserEntity;
import com.oasys.helpdesk.enums.TemplateCode;
import com.oasys.helpdesk.mapper.GrievanceTicketStatusMapper;
import com.oasys.helpdesk.mapper.GrivenaceRegMapper;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.repository.ApplicationConstantRepository;
import com.oasys.helpdesk.repository.CategoryRepository;
import com.oasys.helpdesk.repository.CreateTicketRepository;
import com.oasys.helpdesk.repository.GrievanceCategoryRepository;
import com.oasys.helpdesk.repository.GrievanceEscalationWorkflowRepository;
import com.oasys.helpdesk.repository.GrievanceIssueDetailsRepository;
import com.oasys.helpdesk.repository.GrievanceOTPVerificationRepository;
import com.oasys.helpdesk.repository.GrievanceSlaRepository;
import com.oasys.helpdesk.repository.GrievanceTicketStatusRespository;
import com.oasys.helpdesk.repository.GrievanceWorkflowRepository;
import com.oasys.helpdesk.repository.GrievanceregRepository;
import com.oasys.helpdesk.repository.HelpDeskTicketAuditRepository;
import com.oasys.helpdesk.repository.TemplateRepository;
import com.oasys.helpdesk.repository.TicketStatusrepository;
import com.oasys.helpdesk.repository.UserRepository;
import com.oasys.helpdesk.request.CreateTicketRequestDto;
import com.oasys.helpdesk.request.GrievanceOTPVerificationRequestDTO;
import com.oasys.helpdesk.security.ApiResponse;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.security.SecurityUtils;
import com.oasys.helpdesk.utility.CommonDataController;
//gitlab.oasys.co/up_excise/api/helpdeskservice.git
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.HelpDeskTicketAction;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.RandomUtil;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class GrievanceRegserviceImpl implements GrievanceregService {

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private GrievanceregRepository grievanceRepository;

	@Autowired
	private GrievanceIssueDetailsRepository grievanceIssueDetailsRepository;

	@Autowired
	private GrievanceSlaRepository grievanceSlaRepository;

	@Autowired
	private PaginationMapper paginationMapper;

	public static final String GS_CODE = "grievanceid";

	@Autowired
	private GrivenaceRegMapper grievancemapper;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	CategoryRepository helpDeskTicketCategoryRepository;

	@Autowired
	GrievanceCategoryRepository grievanceCategoryRepository;

	@Autowired
	CreateTicketRepository createTicketRepository;

	@Autowired
	private TicketStatusrepository ticketStatusrepository;

	@Autowired
	private GrievanceWorkflowRepository workflowRepository;

	@Autowired
	private AsyncCommunicationExecution asyncCommunicationExecution;

	@Autowired
	private GrievanceOTPVerificationRepository grievanceOTPVerificationRepository;

	@Autowired
	private TemplateRepository templateRepository;

	@Autowired
	private CommonDataController commonDataController;

	@Autowired
	private ApplicationConstantRepository applicationConstantRepository;

	@Autowired
	private GrievanceEscalationWorkflowRepository escalationWorkflowRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private GrievanceTicketStatusRespository grievanceTicketStatusRespository;

	@Autowired
	private GrievanceTicketStatusMapper grievanceTicketStatusMapper;

//	@Autowired
//	private GrievanceTicketAuditRepository grievanceTicketAuditRepository;
//	

	@Override
	public GenericResponse addreg(GrievanceRegRequestDTO requestDTO) {

		Boolean status = true;

//		Optional<GrievanceregisterEntity> ticketOptional = grievanceRepository.findByLiceneceNumberIgnoreCase(requestDTO.getLiceneceNumber().toUpperCase());
//
//		if (ticketOptional.isPresent()) {
//			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
//					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { "liceneceNumber" }));
//		}
//		Optional<GrievanceregisterEntity>	ticketOptional = grievanceRepository.findByGrievanceidIgnoreCase(requestDTO.getGrievanceId());
//		if (ticketOptional.isPresent()) {
//			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
//					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { "grievanceId" }));
//		}
		requestDTO.setId(null);
		requestDTO.setStatus(requestDTO.getStatus());
		requestDTO.setAddressofComplaint(requestDTO.getAddressofComplaint());
		GrievanceregisterEntity tcEntity = commonUtil.modalMap(requestDTO, GrievanceregisterEntity.class);
		GrievanceCategoryEntity grievanceCategory = grievanceCategoryRepository.getById(requestDTO.getCategoryId());
		// GrievanceWorkflowEntity
		// assigntoid=workflowRepository.getByIssueDetailsId(requestDTO.getAssignto(),requestDTO.getCategoryId(),status);
		// //in request assign to is a issue details id
		try {
			Long issuedetailsid = requestDTO.getIssuedetailsId();
			GrievanceWorkflowEntity assigntoid = workflowRepository.getByIssueDetailsId(issuedetailsid, status); // in
																													// request
																													// assign
																													// to
																													// is
																													// a
																													// issue
																													// details
																													// id
			if (assigntoid == null) {
				log.info("workflow not configured : {}", requestDTO.getCategoryId());
				return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
						"Workflow not configured. or Inactive Record shown");
			}

			tcEntity.setAppln_date(requestDTO.getApplnDate());
			tcEntity.setAddressof_complaint(requestDTO.getAddressofComplaint());
			tcEntity.setDepartment(requestDTO.getDepartment());
			tcEntity.setDistrict(requestDTO.getDistrict());
			tcEntity.setEmailid(requestDTO.getEmailId());
			tcEntity.setGrie_category(requestDTO.getGrieCategory());
			tcEntity.setGrie_desc(requestDTO.getGrieDesc());
			tcEntity.setGrievanceid(requestDTO.getGrievanceId());
			tcEntity.setIssuefrom(requestDTO.getIssueFrom());
			tcEntity.setLicence_status(requestDTO.getLicenceStatus());
			tcEntity.setLicencetype(requestDTO.getLicenceType());
			tcEntity.setLiceneceNumber(requestDTO.getLiceneceNumber());
			tcEntity.setNameof_complaint(requestDTO.getNameofComplaint());
			tcEntity.setPhone_number(requestDTO.getPhoneNumber());
			tcEntity.setReferticnumber(requestDTO.getReferticNumber());
			tcEntity.setStatus(requestDTO.getStatus());
			tcEntity.setTypeofuser(requestDTO.getTypeofUser());
			tcEntity.setUnitname(requestDTO.getUnitName());
			tcEntity.setUploaddoc(requestDTO.getUploadDoc());
			tcEntity.setUsername(requestDTO.getUserName());
			tcEntity.setCategoryId(grievanceCategory);
			tcEntity.getCategoryId().getId();
			tcEntity.setGrievancetcstatus(requestDTO.getGrievancetcStatus());
			tcEntity.setResolvegrievance(requestDTO.getResolvegrievance());
			tcEntity.setQuality_response(requestDTO.getQualityresponse());
			tcEntity.setGrievanceresolved(requestDTO.getGrievanceresolved());
			tcEntity.setValuableinput(requestDTO.getValuableinput());
			tcEntity.setIssuedetails(assigntoid.getSla().getGIssueDetails());
			tcEntity.setResolutiondetails(requestDTO.getResolutiondetails());
			tcEntity.setResolutiondate(requestDTO.getResolutiondate());
			tcEntity.setUserremarks(requestDTO.getUserremarks());
			tcEntity.setOfficerremarks(requestDTO.getOfficerremarks());
			tcEntity.setNotes(requestDTO.getNotes());
			tcEntity.setUserid(requestDTO.getUserid());
			tcEntity.setKnowledgebase(requestDTO.getKnowledgebase());
			// tcEntity.setAssigngroup(String.valueOf(assigntoid.getAssignGroup().getId()));
			tcEntity.setAssignto(assigntoid);
			tcEntity.getAssignto().getAssignto_id();
			// tcEntity.setPriority(String.valueOf(assigntoid.getSla().getPriority().getId()));
			tcEntity.setPriority(String.valueOf(assigntoid.getSla().getPriority().getPriority()));
			tcEntity.setSlaEntity(assigntoid.getSla());
			// tcEntity.setEscalated(String.valueOf(assigntoid.getAssignTo().getId()));

			tcEntity.setAssigntoid(String.valueOf(assigntoid.getAssignto_id()));
			tcEntity.setAssigngroup(String.valueOf(assigntoid.getAssignto_Group()));
			tcEntity.setUuid(requestDTO.getUuid());
			tcEntity.setFilename(requestDTO.getFilename());
			tcEntity.setDocfilename(requestDTO.getDocfileName());
			tcEntity.setDocuuid(requestDTO.getDocuuid());
			tcEntity.setFaqId(requestDTO.getFaqId());
			tcEntity.setHofficerId(requestDTO.getHofficerId());
			tcEntity.setUpdatedBy(requestDTO.getUpdatedBy());
			tcEntity.setCreatedbyName(requestDTO.getCreatedbyName());

			// Validate fileName parameter
			log.info("Validating fileName parameter: {}", requestDTO.getFilename());
			// System.out.println("Validating fileName parameter: " +
			// requestDTO.getFilename());
			if (requestDTO.getFilename() != null) {
				String[] allowedFormats = { "PNG", "JPEG", "JPG", "PDF" };
				String fileExtension = requestDTO.getFilename()
						.substring(requestDTO.getFilename().lastIndexOf(".") + 1);
				boolean isValidFormat = Arrays.stream(allowedFormats)
						.anyMatch(format -> format.equalsIgnoreCase(fileExtension));

				log.info("File extension: {}", fileExtension);
				if (!isValidFormat) {
					log.info("Invalid file format detected: {}", fileExtension);
					// System.out.println("Invalid file format detected: {}" + fileExtension);
					return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
							"Invalid file format. Only PNG, JPEG, JPG, PDF are allowed.");
				}
			}

			// Validate docfileName parameter
			log.info("Validating docfileName parameter: {}", requestDTO.getDocfileName());
			// System.out.println("Validating docfileName parameter: " +
			// requestDTO.getDocfileName());
			if (requestDTO.getDocfileName() != null) {
				String[] allowedFormats = { "PNG", "JPEG", "JPG", "PDF" };
				String fileExtension = requestDTO.getDocfileName()
						.substring(requestDTO.getDocfileName().lastIndexOf(".") + 1);
				boolean isValidFormat = Arrays.stream(allowedFormats)
						.anyMatch(format -> format.equalsIgnoreCase(fileExtension));

				log.info("File extension: {}", fileExtension);
				if (!isValidFormat) {
					log.info("Invalid file format detected: {}", fileExtension);
					// System.out.println("Invalid file format detected: {}" + fileExtension);
					return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
							"Invalid file format. Only PNG, JPEG, JPG, PDF are allowed.");
				}
			}

			grievanceRepository.save(tcEntity);
		} catch (Exception e) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), "Please Check Configuration");
		}
		// this.saveAuditHistory(tcEntity, HelpDeskTicketAction.CREATED,
		// tcEntity.getCreatedDate(), requestDTO.getUserremarks());
		return Library.getSuccessfulResponse(tcEntity, ErrorCode.CREATED.getErrorCode(), ErrorMessages.RECORED_CREATED);

	}

	@Override
	public GenericResponse getAll() {
		List<GrievanceregisterEntity> DepList = grievanceRepository.findAllByOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(DepList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<GrievanceRegResponseDTO> depResponseList = DepList.stream()
				.map(grievancemapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(depResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getById(Long id) {
		Optional<GrievanceregisterEntity> depTypeEntity = grievanceRepository.findById(id);
		if (!depTypeEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(grievancemapper.convertEntityToResponseDTO(depTypeEntity.get()),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getAllActive() {
		List<GrievanceregisterEntity> assetTypeList = grievanceRepository.findAllByStatusOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(assetTypeList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<GrievanceRegResponseDTO> assetTypeResponseList = assetTypeList.stream()
				.map(grievancemapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(assetTypeResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getCode() {
		// MenuPrefix prefix = MenuPrefix.getType(GS_CODE);
		Year y = Year.now();
		LocalDate currentdate = LocalDate.now();
		int currentDay = currentdate.getDayOfMonth();
		String code = "CNS/" + y + "-" + currentDay + "/" + RandomUtil.getRandomNumber();

		while (true) {
			Optional<GrievanceregisterEntity> TsEntity = grievanceRepository.findByGrievanceidIgnoreCase(code);
			if (TsEntity.isPresent()) {
				code = "CNS/" + y + "-" + "23" + "/" + RandomUtil.getRandomNumber();
			} else {
				break;
			}
		}
		return Library.getSuccessfulResponse(code, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getCodetypeofuser(GrievanceRegRequestDTO requestDto) {
		// MenuPrefix prefix = MenuPrefix.getType(GS_CODE);
		Year y = Year.now();
		LocalDate currentdate = LocalDate.now();
		int currentDay = currentdate.getDayOfMonth();
		String code = null;
		if (requestDto.getTypeofUser().equalsIgnoreCase("Consumer")) {
			code = "CTZ/" + y + "-" + "25" + "/" + RandomUtil.getRandomNumber();
			while (true) {
				Optional<GrievanceregisterEntity> TsEntity = grievanceRepository.findByGrievanceidIgnoreCase(code);
				if (TsEntity.isPresent()) {
					code = "CTZ/" + y + "-" + "25" + "/" + RandomUtil.getRandomNumber();
				} else {
					break;
				}
			}
		}
		if (requestDto.getTypeofUser().equalsIgnoreCase("Licensees")) {
			code = "LCN/" + y + "-" + "25" + "/" + RandomUtil.getRandomNumber();
			while (true) {
				Optional<GrievanceregisterEntity> TsEntity = grievanceRepository.findByGrievanceidIgnoreCase(code);
				if (TsEntity.isPresent()) {
					code = "LCN/" + y + "-" + "25" + "/" + RandomUtil.getRandomNumber();
				} else {
					break;
				}
			}
		}
		if (requestDto.getTypeofUser().equalsIgnoreCase("Other Stakeholder")) {
			code = "STK/" + y + "-" + "25" + "/" + RandomUtil.getRandomNumber();
			while (true) {
				Optional<GrievanceregisterEntity> TsEntity = grievanceRepository.findByGrievanceidIgnoreCase(code);
				if (TsEntity.isPresent()) {
					code = "STK/" + y + "-" + "25" + "/" + RandomUtil.getRandomNumber();
				} else {
					break;
				}
			}
		}

		if (requestDto.getTypeofUser().equalsIgnoreCase("Citizen/Public")) {
			code = "CIPU/" + y + "-" + "25" + "/" + RandomUtil.getRandomNumber();
			while (true) {
				Optional<GrievanceregisterEntity> TsEntity = grievanceRepository.findByGrievanceidIgnoreCase(code);
				if (TsEntity.isPresent()) {
					code = "CIPU/" + y + "-" + "25" + "/" + RandomUtil.getRandomNumber();
				} else {
					break;
				}
			}
		}

		return Library.getSuccessfulResponse(code, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getAllByRequestFilter1(PaginationRequestDTO requestData, AuthenticationDTO authenticationDTO)
			throws ParseException {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		Long count = this.getCountBySearchFields(requestData, authenticationDTO);
		log.info("total count :: {}", count);
		if (count > 0) {
			List<GrievanceregisterEntity> list = this.getRecordsByFilterDTO(requestData, authenticationDTO);
			if (CollectionUtils.isEmpty(list)) {
				throw new RecordNotFoundException("No Record Found");
			}
			List<GrievanceRegResponseDTO> dtoList = list.stream().map(grievancemapper::convertEntityToResponseDTO)
					.collect(Collectors.toList());

			paginationResponseDTO.setContents(dtoList);
			paginationResponseDTO.setNumberOfElements(Objects.nonNull(count) ? count.intValue() : null);
			paginationResponseDTO.setTotalElements(count);
			return Library.getSuccessfulResponse(paginationResponseDTO, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException("No Record Found");
		}
	}

	public List<GrievanceregisterEntity> getRecordsByFilterDTO(PaginationRequestDTO filterRequestDTO,
			AuthenticationDTO authenticationDTO) throws ParseException {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<GrievanceregisterEntity> cq = cb.createQuery(GrievanceregisterEntity.class);
		Root<GrievanceregisterEntity> from = cq.from(GrievanceregisterEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<GrievanceregisterEntity> typedQuery = null;
		addCriteria(cb, list, filterRequestDTO, from, authenticationDTO);
		cq.where(cb.and(list.toArray(new Predicate[list.size()])));
		cq.distinct(true);
		if (StringUtils.isBlank(filterRequestDTO.getSortField())) {
			filterRequestDTO.setSortField(MODIFIED_DATE);
		}
		if (StringUtils.isNotBlank(filterRequestDTO.getSortOrder())
				&& filterRequestDTO.getSortOrder().equalsIgnoreCase(ASC)) {
			cq.orderBy(cb.asc(from.get(filterRequestDTO.getSortField())));

		} else {
			cq.orderBy(cb.desc(from.get(filterRequestDTO.getSortField())));

		}
		if (Objects.nonNull(filterRequestDTO.getPaginationSize())
				&& Objects.nonNull(filterRequestDTO.getPaginationSize())) {
			typedQuery = entityManager.createQuery(cq)
					.setFirstResult((filterRequestDTO.getPageNo() * filterRequestDTO.getPaginationSize()))
					.setMaxResults(filterRequestDTO.getPaginationSize());
		} else {
			typedQuery = entityManager.createQuery(cq);
		}

		List<GrievanceregisterEntity> data = typedQuery.getResultList();
		if (CollectionUtils.isEmpty(data)) {
			throw new RecordNotFoundException("No Record Found");
		}
		return data;
	}

	private void addCriteria(CriteriaBuilder cb, List<Predicate> list, PaginationRequestDTO filterRequestDTO,
			Root<GrievanceregisterEntity> from, AuthenticationDTO authenticationDTO) throws ParseException {
		list.add(cb.equal(from.get(CREATED_BY), authenticationDTO.getUserId()));
		try {
			if (Objects.nonNull(filterRequestDTO.getFilters())) {
				log.info("filters ::" + filterRequestDTO.getFilters());

				if (Objects.nonNull(filterRequestDTO.getFilters().get(CREATED_DATE))
						&& !filterRequestDTO.getFilters().get(CREATED_DATE).toString().trim().isEmpty()) {

					Date fromDate = new SimpleDateFormat(DATE_FORMAT)
							.parse(filterRequestDTO.getFilters().get(CREATED_DATE).toString() + " " + START_TIME);
					Date toDate = new SimpleDateFormat(DATE_FORMAT)
							.parse(filterRequestDTO.getFilters().get(CREATED_DATE).toString() + " " + END_TIME);
					list.add(cb.between(from.get(CREATED_DATE), fromDate, toDate));

				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get(ID))
						&& !filterRequestDTO.getFilters().get(ID).toString().trim().isEmpty()) {

					Long id = Long.valueOf(filterRequestDTO.getFilters().get(ID).toString());
					list.add(cb.equal(from.get(ID), id));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("grievanceId"))
						&& !filterRequestDTO.getFilters().get("grievanceId").toString().trim().isEmpty()) {

					String griveid = String.valueOf(filterRequestDTO.getFilters().get("grievanceId").toString());
					list.add(cb.equal(from.get("refertic_number"), griveid));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("phoneNumber"))
						&& !filterRequestDTO.getFilters().get("phoneNumber").toString().trim().isEmpty()) {

					String phonenumber = String.valueOf(filterRequestDTO.getFilters().get("phoneNumber").toString());
					list.add(cb.equal(from.get("phone_number"), phonenumber));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("nameinfo"))
						&& !filterRequestDTO.getFilters().get("nameinfo").toString().trim().isEmpty()) {

					String nameinfo = String.valueOf(filterRequestDTO.getFilters().get("nameinfo").toString());
					list.add(cb.equal(from.get("nameinfo"), nameinfo));
				}

//				if (Objects.nonNull(filterRequestDTO.getFilters().get("issueFrom"))
//						&& !filterRequestDTO.getFilters().get("issueFrom").toString().trim().isEmpty()) {
//
//					String issuefrom = String.valueOf(filterRequestDTO.getFilters().get("issueFrom").toString());
//					list.add(cb.equal(from.get("issuefrom"), issuefrom));
//				}
//
//				if (Objects.nonNull(filterRequestDTO.getFilters().get("grieCategory"))
//						&& !filterRequestDTO.getFilters().get("grieCategory").toString().trim().isEmpty()) {
//
//					Long categoryId = Long.valueOf(filterRequestDTO.getFilters().get("grieCategory").toString());
//					list.add(cb.equal(from.get("categoryId"), categoryId));
//				}

//				if (Objects.nonNull(filterRequestDTO.getFilters().get("grievancetcStatus"))
//						&& !filterRequestDTO.getFilters().get("grievancetcStatus").toString().trim().isEmpty()) {
//
//					String grievanceticketstatus = String.valueOf(filterRequestDTO.getFilters().get("grievancetcStatus").toString());
//					list.add(cb.equal(from.get("grievancetcstatus"), grievanceticketstatus));
//				}

			}
		} catch (ParseException e) {
			throw new InvalidParameterException("No Record Found");
		}
	}

	@Override
	public GenericResponse getAllByRequestFilter(PaginationRequestDTO paginationDto,
			AuthenticationDTO authenticationDTO) {
		Pageable pageable = null;
		Page<GrievanceregisterEntity> list = null;
		String griveid = null;
		String phonenumber = null;
		String issueform = null;

		Long grievancecategory = null;

		String priorityid = null;

		String nameinfo = null;
		String createdDate = null;
		Date finalDate = null;
		String status = null;
		String referticnumber = null;

		if (StringUtils.isBlank(paginationDto.getSortField())) {
			paginationDto.setSortField(MODIFIED_DATE);
		}
		if (StringUtils.isBlank(paginationDto.getSortField())) {
			// paginationDto.setSortField(CREATED_DATE);
			paginationDto.setSortField("created_date");
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
			if (Objects.nonNull(paginationDto.getFilters().get("grievanceId"))
					&& !paginationDto.getFilters().get("grievanceId").toString().trim().isEmpty()) {
				try {
					// id = Long.valueOf(paginationDto.getFilters().get(ID).toString());
					griveid = String.valueOf(paginationDto.getFilters().get("grievanceId").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing griveid :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}
			if (Objects.nonNull(paginationDto.getFilters().get("phoneNumber"))
					&& !paginationDto.getFilters().get("phoneNumber").toString().trim().isEmpty()) {
				try {

					phonenumber = String.valueOf(paginationDto.getFilters().get("phoneNumber").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing phoneNumber :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}

			if (Objects.nonNull(paginationDto.getFilters().get("issueFrom"))
					&& !paginationDto.getFilters().get("issueFrom").toString().trim().isEmpty()) {
				try {

					issueform = String.valueOf(paginationDto.getFilters().get("issueFrom").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing issueFrom :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}

			if (Objects.nonNull(paginationDto.getFilters().get("categoryId"))
					&& !paginationDto.getFilters().get("categoryId").toString().trim().isEmpty()) {
				try {

					grievancecategory = Long.valueOf(paginationDto.getFilters().get("categoryId").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing grievancecategory :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}

			if (Objects.nonNull(paginationDto.getFilters().get("PriorityId"))
					&& !paginationDto.getFilters().get("PriorityId").toString().trim().isEmpty()) {
				try {

					priorityid = String.valueOf(paginationDto.getFilters().get("PriorityId").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing PriorityId :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}

			if (Objects.nonNull(paginationDto.getFilters().get("nameinfo"))
					&& !paginationDto.getFilters().get("nameinfo").toString().trim().isEmpty()) {
				try {

					nameinfo = String.valueOf(paginationDto.getFilters().get("nameinfo").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing question :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}

			if (Objects.nonNull(paginationDto.getFilters())) {
				if (Objects.nonNull(paginationDto.getFilters().get("createdDate"))
						&& !paginationDto.getFilters().get("createdDate").toString().trim().isEmpty()) {
					try {
						createdDate = String.valueOf(paginationDto.getFilters().get("createdDate").toString());

						try {
							finalDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
									.parse(createdDate + " " + "00:00:00");
						} catch (ParseException e) {
							log.error("error occurred while parsing date : {}", e.getMessage());
							throw new InvalidDataValidation("Invalid date parameter passed");
						}

					} catch (Exception e) {
						log.error("error occurred while parsing refertic_number :: {}", e);
						return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
								ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
					}
				}

			}

			if (Objects.nonNull(paginationDto.getFilters().get("status"))
					&& !paginationDto.getFilters().get("status").toString().trim().isEmpty()) {
				try {

					status = String.valueOf(paginationDto.getFilters().get("status").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing status :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}

		}

		GrievanceregisterEntity categoryNameO = new GrievanceregisterEntity();

		if (Objects.nonNull(griveid) && Objects.nonNull(issueform) && Objects.nonNull(grievancecategory)
				&& Objects.nonNull(finalDate) && Objects.nonNull(status) && Objects.nonNull(priorityid)) {
			list = grievanceRepository.getByReferticnumberIssuefromCategoryIdCreateddateStatusAndPriority(griveid,
					issueform, grievancecategory, finalDate, status, priorityid, pageable);
		} else if (Objects.isNull(griveid) && Objects.nonNull(issueform) && Objects.nonNull(grievancecategory)
				&& Objects.isNull(finalDate) && Objects.isNull(status) && Objects.nonNull(priorityid)) {
			list = grievanceRepository.getByIssuefromCategoryIdAndPriority(issueform, grievancecategory, priorityid,
					pageable);
		} else if (Objects.isNull(griveid) && Objects.nonNull(issueform) && Objects.nonNull(grievancecategory)
				&& Objects.isNull(finalDate) && Objects.isNull(status) && Objects.nonNull(priorityid)) {
			list = grievanceRepository.getByIssuefromCategoryIdAndPriority(issueform, grievancecategory, priorityid,
					pageable);
		}

		else if (Objects.isNull(griveid) && Objects.nonNull(issueform) && Objects.nonNull(grievancecategory)
				&& Objects.isNull(finalDate) && Objects.isNull(status) && Objects.isNull(priorityid)) {
			list = grievanceRepository.getByIssuefromAndCategoryId(issueform, grievancecategory, pageable);
		}

		else if (Objects.isNull(griveid) && Objects.nonNull(issueform) && Objects.nonNull(grievancecategory)
				&& Objects.isNull(finalDate) && Objects.isNull(status) && Objects.isNull(priorityid)) {
			list = grievanceRepository.getByIssuefromAndCategoryId(issueform, grievancecategory, pageable);
		}

		else if (Objects.isNull(griveid) && Objects.nonNull(issueform) && Objects.isNull(grievancecategory)
				&& Objects.isNull(finalDate) && Objects.nonNull(status) && Objects.isNull(priorityid)
				&& Objects.nonNull(nameinfo)) {
			list = grievanceRepository.getByIssuefromAndNameinfoAndGrievancetcstatus(issueform, nameinfo, status,
					pageable);
		}

		else if (Objects.isNull(griveid) && Objects.nonNull(issueform) && Objects.isNull(grievancecategory)
				&& Objects.isNull(finalDate) && Objects.isNull(status) && Objects.nonNull(priorityid)
				&& Objects.nonNull(nameinfo)) {
			list = grievanceRepository.getByIssuefromAndNameinfoAndpriority(issueform, nameinfo, priorityid, pageable);
		}

		else if (Objects.isNull(griveid) && Objects.nonNull(issueform) && Objects.isNull(grievancecategory)
				&& Objects.isNull(finalDate) && Objects.isNull(status) && Objects.isNull(priorityid)
				&& Objects.nonNull(nameinfo)) {
			list = grievanceRepository.getByIssuefromAndNameinfo(issueform, nameinfo, pageable);
		}

		else if (Objects.isNull(griveid) && Objects.nonNull(issueform) && Objects.isNull(grievancecategory)
				&& Objects.isNull(finalDate) && Objects.isNull(status) && Objects.isNull(priorityid)) {
			list = grievanceRepository.getByIssuefrom(issueform, pageable);
		}

		else if (Objects.isNull(griveid) && Objects.nonNull(issueform) && Objects.nonNull(grievancecategory)
				&& Objects.isNull(finalDate) && Objects.isNull(status) && Objects.isNull(priorityid)) {
			list = grievanceRepository.getByCategoryIdAndPriority(grievancecategory, priorityid, pageable);
		}

		else if (Objects.nonNull(griveid) && Objects.isNull(issueform) && Objects.isNull(grievancecategory)
				&& Objects.isNull(finalDate) && Objects.isNull(status) && Objects.isNull(priorityid)) {
			list = grievanceRepository.getByReferticnumber(griveid, pageable);
		}

		else if (Objects.nonNull(griveid) && Objects.isNull(issueform) && Objects.nonNull(grievancecategory)
				&& Objects.isNull(finalDate) && Objects.isNull(status) && Objects.isNull(priorityid)) {
			list = grievanceRepository.getByReferticnumberAndCategoryId(griveid, grievancecategory, pageable);
		}

		else if (Objects.isNull(griveid) && Objects.isNull(issueform) && Objects.nonNull(grievancecategory)
				&& Objects.isNull(finalDate) && Objects.isNull(status) && Objects.isNull(priorityid)) {
			list = grievanceRepository.getByCategoryId(grievancecategory, pageable);
		}

		else if (Objects.isNull(griveid) && Objects.isNull(issueform) && Objects.nonNull(grievancecategory)
				&& Objects.nonNull(finalDate) && Objects.isNull(status) && Objects.isNull(priorityid)) {
			list = grievanceRepository.getByCategoryIdAndCreateddate(grievancecategory, finalDate, pageable);
		}

		else if (Objects.isNull(griveid) && Objects.isNull(issueform) && Objects.isNull(grievancecategory)
				&& Objects.nonNull(finalDate) && Objects.isNull(status) && Objects.isNull(priorityid)) {
			list = grievanceRepository.getByCreateddate(finalDate, pageable);
		}

		else if (Objects.isNull(griveid) && Objects.isNull(issueform) && Objects.isNull(grievancecategory)
				&& Objects.isNull(finalDate) && Objects.nonNull(status) && Objects.isNull(priorityid)) {
			list = grievanceRepository.getByGrievancetcstatus(status, pageable);
		}

		else if (Objects.isNull(griveid) && Objects.isNull(issueform) && Objects.isNull(grievancecategory)
				&& Objects.isNull(finalDate) && Objects.isNull(status) && Objects.nonNull(priorityid)) {
			list = grievanceRepository.getByPriority(priorityid, pageable);
		}

		else if (Objects.nonNull(griveid) && Objects.isNull(issueform) && Objects.isNull(grievancecategory)
				&& Objects.nonNull(finalDate) && Objects.isNull(status) && Objects.isNull(priorityid)) {
			list = grievanceRepository.getByReferticnumberCreateddate(griveid, finalDate, pageable);
		}

		else if (Objects.isNull(griveid) && Objects.isNull(issueform) && Objects.isNull(grievancecategory)
				&& Objects.nonNull(finalDate) && Objects.nonNull(status) && Objects.isNull(priorityid)) {
			list = grievanceRepository.getByCreateddateAndGrievancetcstatus(status, finalDate, pageable);

		}

		else if (Objects.isNull(griveid) && Objects.isNull(issueform) && Objects.isNull(grievancecategory)
				&& Objects.isNull(finalDate) && Objects.isNull(status) && Objects.isNull(priorityid)
				&& Objects.isNull(referticnumber)) {
			list = grievanceRepository.getByAllParamtterIsEmpty(createdDate, referticnumber, priorityid,
					grievancecategory, status, pageable);
		}

		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		Page<GrievanceRegResponseDTO> finalResponse = list.map(grievancemapper::convertEntityToResponseDTO);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}

	private Long getCountBySearchFields(PaginationRequestDTO filterRequestDTO, AuthenticationDTO authenticationDTO)
			throws ParseException {
		log.info("getting total count by search fields :: {}", filterRequestDTO.toString());
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<GrievanceregisterEntity> from = cq.from(GrievanceregisterEntity.class);
		cq.select(cb.count(from));
		List<Predicate> list = new ArrayList<>();
		addCriteria(cb, list, filterRequestDTO, from, authenticationDTO);
		cq.where(cb.and(list.toArray(new Predicate[list.size()])));
		Long count = entityManager.createQuery(cq).getSingleResult();
		if (count > 0) {
			return count;
		} else {
			throw new RecordNotFoundException("No Record Found");
		}
	}

	@Override
	// public GenericResponse getCount(String date, AuthenticationDTO
	// authenticationDTO) {
	public GenericResponse getCount(GrievanceRegRequestDTO requestDto, AuthenticationDTO authenticationDTO) {
		String date = requestDto.getDate();
		String todate = requestDto.getTodate();
		String userid = (requestDto.getNameinfo());
		String typeofuser = requestDto.getTypeofUser();
		String type = requestDto.getType();
		String phonenumber = requestDto.getPhoneNumber();

		final Date fromDate;
		final Date toDate;

		try {
			fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date + " " + "00:00:00");
		} catch (ParseException e) {
			log.error("error occurred while parsing date : {}", e.getMessage());
			throw new InvalidDataValidation("Invalid date parameter passed");
		}
		try {
			toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(todate + " " + "00:00:00");
		} catch (ParseException e) {
			log.error("error occurred while parsing date : {}", e.getMessage());
			throw new InvalidDataValidation("Invalid date parameter passed");
		}

		List<TicketCountResponseDTO> ticketCountResponseDTOList = new ArrayList<>();
		List<TicketStatusEntity> ticketStatusList = ticketStatusrepository.findAllByStatusOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(ticketStatusList)) {
			throw new RecordNotFoundException("No Record Found");
		}

		if (type.equalsIgnoreCase("Login")) {
			ticketStatusList.forEach(ticketStatus -> {
				if (Objects.isNull(ticketStatus.getId()) || StringUtils.isBlank(ticketStatus.getTicketstatusname())) {
					return;
				}

				TicketCountResponseDTO ticketCountResponseDTO = new TicketCountResponseDTO();
				ticketCountResponseDTO.setStatus(ticketStatus.getTicketstatusname());
				if (Objects.isNull(authenticationDTO) || Objects.isNull(authenticationDTO.getUserId())
						|| CollectionUtils.isEmpty(authenticationDTO.getRoleCodes())) {
					log.error("invalid authentication details : {}", authenticationDTO);
					throw new RecordNotFoundException("No Record Found");
				}

				ticketCountResponseDTO.setCount(grievanceRepository.getCountByStatusAndCreatedDate(
						ticketStatus.getTicketstatusname(), userid, typeofuser, fromDate, toDate));

				ticketCountResponseDTOList.add(ticketCountResponseDTO);
			});
			return Library.getSuccessfulResponse(ticketCountResponseDTOList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}
		// Payment reconcilation module call that menthod
		if (type.equalsIgnoreCase("PaymentLogin")) {
			ticketStatusList.forEach(ticketStatus -> {
				if (Objects.isNull(ticketStatus.getId()) || StringUtils.isBlank(ticketStatus.getTicketstatusname())) {
					return;
				}

				TicketCountResponseDTO ticketCountResponseDTO = new TicketCountResponseDTO();
				ticketCountResponseDTO.setStatus(ticketStatus.getTicketstatusname());
				ticketCountResponseDTO.setCount(grievanceRepository.getCountByStatusAndCreatedDate(
						ticketStatus.getTicketstatusname(), userid, typeofuser, fromDate, toDate));

				ticketCountResponseDTOList.add(ticketCountResponseDTO);
			});
			return Library.getSuccessfulResponse(ticketCountResponseDTOList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}

		if (type.equalsIgnoreCase("OTP")) {
			ticketStatusList.forEach(ticketStatus -> {
				if (Objects.isNull(ticketStatus.getId()) || StringUtils.isBlank(ticketStatus.getTicketstatusname())) {
					return;
				}

				TicketCountResponseDTO ticketCountResponseDTO = new TicketCountResponseDTO();
				ticketCountResponseDTO.setStatus(ticketStatus.getTicketstatusname());
				if (Objects.isNull(authenticationDTO) || Objects.isNull(authenticationDTO.getUserId())
						|| CollectionUtils.isEmpty(authenticationDTO.getRoleCodes())) {
					log.error("invalid authentication details : {}", authenticationDTO);
					throw new RecordNotFoundException("No Record Found");
				}

				ticketCountResponseDTO.setCount(grievanceRepository.getCountByStatusAndCreatedDateAndPhone_number(
						ticketStatus.getTicketstatusname(), phonenumber, fromDate, toDate));

				ticketCountResponseDTOList.add(ticketCountResponseDTO);
			});
			return Library.getSuccessfulResponse(ticketCountResponseDTOList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}
		// Payment reconcilation module call that menthod
		if (type.equalsIgnoreCase("PayementOTP")) {
			ticketStatusList.forEach(ticketStatus -> {
				if (Objects.isNull(ticketStatus.getId()) || StringUtils.isBlank(ticketStatus.getTicketstatusname())) {
					return;
				}
				TicketCountResponseDTO ticketCountResponseDTO = new TicketCountResponseDTO();
				ticketCountResponseDTO.setStatus(ticketStatus.getTicketstatusname());
				ticketCountResponseDTO.setCount(grievanceRepository.getCountByStatusAndCreatedDateAndPhone_number(
						ticketStatus.getTicketstatusname(), phonenumber, fromDate, toDate));

				ticketCountResponseDTOList.add(ticketCountResponseDTO);
			});
			return Library.getSuccessfulResponse(ticketCountResponseDTOList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}

		return null;

	}

	@Override
	public GenericResponse getBytoglelist(GrievanceRegRequestDTO requestDTO) {
		String username = requestDTO.getNameinfo();
		String typeofuser = requestDTO.getTypeofUser();
		String type = requestDTO.getType();
		String phonenumber = requestDTO.getPhoneNumber();
		try {

			if (type.equalsIgnoreCase("Login")) {

				List<GrievanceregisterEntity> list = grievanceRepository.getByTypeofuserAndName_info(typeofuser,
						username);

				if (CollectionUtils.isEmpty(list)) {
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
				List<GrievanceRegResponseDTO> responseDto = list.stream()
						.map(grievancemapper::convertEntityToResponseDTO).collect(Collectors.toList());
				return Library.getSuccessfulResponse(responseDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
						ErrorMessages.RECORED_FOUND);

			}

			if (type.equalsIgnoreCase("OTP")) {

				// List<GrievanceregisterEntity> list =
				// grievanceRepository.getByPhone_number(phonenumber);

				List<GrievanceregisterEntity> list = grievanceRepository.getByPhone_numberAndTypeofuser(phonenumber,
						typeofuser);

				if (CollectionUtils.isEmpty(list)) {
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
				List<GrievanceRegResponseDTO> responseDto = list.stream()
						.map(grievancemapper::convertEntityToResponseDTO).collect(Collectors.toList());
				return Library.getSuccessfulResponse(responseDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
						ErrorMessages.RECORED_FOUND);

			}
		} catch (Exception e) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return null;

	}

	@Override
	public GenericResponse update(GrievanceRegRequestDTO requestDTO) {
		if (Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { ID }));
		}

		Optional<GrievanceregisterEntity> DeptOptional = grievanceRepository.findById(requestDTO.getId());
		if (!DeptOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		GrievanceregisterEntity deptEntity = DeptOptional.get();
		deptEntity.setNotes(requestDTO.getNotes());
		deptEntity.setStatus(requestDTO.getStatus());
		deptEntity.setUserremarks(requestDTO.getUserremarks());
		deptEntity.setResolutiondate(requestDTO.getResolutiondate());
		deptEntity.setGrievanceresolved(requestDTO.getGrievanceresolved());
		deptEntity.setResolvegrievance(requestDTO.getResolvegrievance());
		deptEntity.setQuality_response(requestDTO.getQualityresponse());
		deptEntity.setValuableinput(requestDTO.getValuableinput());
		deptEntity.setGrievancetcstatus(requestDTO.getGrievancetcStatus());
		deptEntity.setHofficerId(requestDTO.getHofficerId());
		deptEntity.setUpdatedBy(requestDTO.getUpdatedBy());
		grievanceRepository.save(deptEntity);
		return Library.getSuccessfulResponse(deptEntity, ErrorCode.CREATED.getErrorCode(),
				ErrorMessages.RECORED_UPDATED);
	}

	@Override
	public GenericResponse updateinspectex(GrievanceRegRequestDTO requestDTO) {
		if (Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { ID }));
		}

//		Optional<GrievanceregisterEntity> DeptOptional = grievanceRepository
//				.findByIssuedetailsIgnoreCaseNotInId(requestDTO.getIssuedetails(), requestDTO.getId());

		Optional<GrievanceregisterEntity> DeptOptional = grievanceRepository.findById(requestDTO.getId());

//		if (DeptOptional.isPresent()) {
//			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
//					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { DEPARTMENT_NAME }));
//		}
		DeptOptional = grievanceRepository.findById(requestDTO.getId());
		if (!DeptOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		Optional<GrievanceIssueDetails> iOptional = grievanceIssueDetailsRepository
				.findById(requestDTO.getIssuedetailsId());

		if (!iOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { Constant.ISSUE_DETAILS }));
		}

		Optional<GrievanceSlaEntity> sOptional = grievanceSlaRepository.findById(requestDTO.getSlaId());

		if (!sOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { Constant.SLA_ID }));
		}

		GrievanceregisterEntity deptEntity = DeptOptional.get();
		deptEntity.setGrievancetcstatus(requestDTO.getGrievancetcStatus());
		deptEntity.setIssuedetails(iOptional.get());
		deptEntity.setKnowledgebase(requestDTO.getKnowledgebase());
		deptEntity.setPriority(requestDTO.getPriority());
		deptEntity.setSlaEntity(sOptional.get());
		deptEntity.setOfficerremarks(requestDTO.getOfficerremarks());
//		GrievanceWorkflowEntity assigntoid=workflowRepository.getById(requestDTO.getAssignto());
//		if(assigntoid != null) {
//		deptEntity.setAssignto(assigntoid);
//		}else
//		{
//			deptEntity.setAssignto(null);
//		}

		deptEntity.setAssigntoid(requestDTO.getAssignToId());
		deptEntity.setAssigngroup(requestDTO.getAssigngroup());
		deptEntity.setResolutiondetails(requestDTO.getResolutiondetails());
		deptEntity.setStatus(requestDTO.getStatus());
		deptEntity.setHofficerId(requestDTO.getHofficerId());
		deptEntity.setUpdatedBy(requestDTO.getUpdatedBy());
		grievanceRepository.save(deptEntity);
		return Library.getSuccessfulResponse(deptEntity, ErrorCode.CREATED.getErrorCode(),
				ErrorMessages.RECORED_UPDATED);
	}

//	Dashboard work

	@Override

	public GenericResponse getCount_percentage(AuthenticationDTO authenticationDTO) {
		final Date finalDate;

		Integer tictotalcount = 0;
		// List<CreateTicketEntity> ticketStatusList1 =
		// createTicketRepository.getAllOfCurrentMonth();

		List<GrievanceTicketCountResponseDTO> ticketCountResponseDTOList = new ArrayList<>();

		List<GrievanceTicketCountResponseDTO> ticketCountResponseDTOList1 = new ArrayList<>();

		List<GrievanceregisterEntity> ticketStatusList = grievanceRepository.findAllByStatusOrderByModifiedDateDesc();

		List<GrievanceregisterEntity> ticketStatusList1 = grievanceRepository.findAllByStatusOrderByModifiedDateDesc();

		if (CollectionUtils.isEmpty(ticketStatusList)) {
			throw new RecordNotFoundException("No Record Found");
		}

		ticketStatusList.forEach(ticketStatus -> {
			if (Objects.isNull(ticketStatus.getId()) || StringUtils.isBlank(ticketStatus.getGrievancetcstatus())) {
				return;
			}
			GrievanceTicketCountResponseDTO ticketCountResponseDTO = new GrievanceTicketCountResponseDTO();
			ticketCountResponseDTO.setTotcount(grievanceRepository.getCountall());
			int total = ticketCountResponseDTO.getTotcount();
			double totalticket = total;
			System.out.println("Totalticket::" + totalticket);
			ticketCountResponseDTO.setStatus(ticketStatus.getGrievancetcstatus());
			if (Objects.isNull(authenticationDTO) || Objects.isNull(authenticationDTO.getUserId())) {
				log.error("invalid authentication details : {}", authenticationDTO);
				throw new RecordNotFoundException("No Record Found");
			}

			// Custom code sk
			if (authenticationDTO.getRoleCodes().stream().anyMatch(r -> Constant.HELPDESK_ADMIN.equals(r))) {
				ticketCountResponseDTO.setCount(grievanceRepository.getCountBySAdmin(ticketStatus.getId()));
			} else {
				ticketCountResponseDTO.setCount(grievanceRepository.getCountBySAdmin(ticketStatus.getId()));
			}

//			ticketCountResponseDTO.setCount(grievanceRepository.getCountByStatusAndCreatedByper(ticketStatus.getId(),
//					authenticationDTO.getUserId()));

			double earnedcount = (ticketCountResponseDTO.getCount());
			System.out.println("PERCENTAGE:::" + (earnedcount / totalticket) * 100);
			Double percentage = (earnedcount / totalticket) * 100;
			ticketCountResponseDTO.setPercentage(percentage);
			ticketCountResponseDTOList.add(ticketCountResponseDTO);
		});

		for (int i = 0; i < ticketCountResponseDTOList.size(); i++) {

			tictotalcount += ticketCountResponseDTOList.get(i).getCount();
		}

		System.out.println("TOTALCOUNT:::" + tictotalcount);
		Integer tot = tictotalcount;

		ticketStatusList1.forEach(ticketStatus1 -> {
			if (Objects.isNull(ticketStatus1.getId()) || StringUtils.isBlank(ticketStatus1.getGrievancetcstatus())) {
				return;
			}
			GrievanceTicketCountResponseDTO ticketCountResponseDTO1 = new GrievanceTicketCountResponseDTO();
			ticketCountResponseDTO1.setTotcount(grievanceRepository.getCountall());
			int total = ticketCountResponseDTO1.getTotcount();
			double totalticket = total;
			System.out.println("Totalticket::" + totalticket);
			ticketCountResponseDTO1.setStatus(ticketStatus1.getGrievancetcstatus());
			if (Objects.isNull(authenticationDTO) || Objects.isNull(authenticationDTO.getUserId())) {
				log.error("invalid authentication details : {}", authenticationDTO);
				throw new RecordNotFoundException("No Record Found");
			}

			// custom code sk
			if (authenticationDTO.getRoleCodes().stream().anyMatch(r -> Constant.HELPDESK_ADMIN.equals(r))) {
				ticketCountResponseDTO1.setCount(grievanceRepository.getCountBySAdmin(ticketStatus1.getId()));
			} else {
				ticketCountResponseDTO1.setCount(grievanceRepository.getCountBySAdmin(ticketStatus1.getId()));
			}

//			ticketCountResponseDTO1.setCount(grievanceRepository.getCountByStatusAndCreatedByper(ticketStatus1.getId(),
//					authenticationDTO.getUserId()));

			double earnedcount = (ticketCountResponseDTO1.getCount());
			System.out.println("PERCENTAGE:::" + (earnedcount / totalticket) * 100);
			Double percentage = (earnedcount / totalticket) * 100;
			ticketCountResponseDTO1.setPercentage(percentage);
			ticketCountResponseDTOList1.add(ticketCountResponseDTO1);
		});

		return Library.getSuccessfulResponse(ticketCountResponseDTOList1, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

	// Get by Month

	@Override
	public GenericResponse getbyMonth(AuthenticationDTO authenticationDTO) {

		LocalDate currentDate = LocalDate.now();
		LocalDate currentDateLastMonth = LocalDate.of(currentDate.getYear(), currentDate.getMonth().minus(0),
				currentDate.getDayOfMonth());

		LocalDate firstDayOfMonth = currentDateLastMonth.with(TemporalAdjusters.firstDayOfMonth());
		LocalDate lastDayOfMonth = currentDateLastMonth.with(TemporalAdjusters.lastDayOfMonth());

		final LocalDate finalDate1 = lastDayOfMonth;
		System.out.println("..." + finalDate1);
		final Date finalDate;
		try {
			finalDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(lastDayOfMonth + " " + "00:00:00");
		} catch (ParseException e) {
			log.error("error occurred while parsing date : {}", e.getMessage());
			throw new InvalidDataValidation("Invalid date parameter passed");
		}

		List<GrievanceTicketCountResponseDTO> ticketCountResponseDTOList = new ArrayList<>();

		List<GrievanceregisterEntity> ticketStatusList = grievanceRepository.findAllByStatusOrderByModifiedDateDesc();

		List<GrievanceregisterEntity> ticketStatusList1 = grievanceRepository.findAllByStatusOrderByModifiedDateDesc();

		List<GrievanceregisterEntity> ticketStatusListApril = grievanceRepository
				.findAllByStatusOrderByModifiedDateDesc();

		if (CollectionUtils.isEmpty(ticketStatusList1)) {
			throw new RecordNotFoundException("No Record Found");
		}

		if (CollectionUtils.isEmpty(ticketStatusList)) {
			throw new RecordNotFoundException("No Record Found");
		}

		ticketStatusList1.forEach(ticketStatus1 -> {
			if (Objects.isNull(ticketStatus1.getId()) || StringUtils.isBlank(ticketStatus1.getGrievancetcstatus())) {
				return;
			}
			GrievanceTicketCountResponseDTO ticketCountResponseDTO1 = new GrievanceTicketCountResponseDTO();
			ticketCountResponseDTO1.setTotcount(grievanceRepository.getCountfeb());
			int total1 = ticketCountResponseDTO1.getTotcount();
			double totalticket1 = total1;
			System.out.println("Totalticket::" + totalticket1);
			ticketCountResponseDTO1.setStatus(ticketStatus1.getGrievancetcstatus());
			if (Objects.isNull(authenticationDTO) || Objects.isNull(authenticationDTO.getUserId())) {
				log.error("invalid authentication details : {}", authenticationDTO);
				throw new RecordNotFoundException("No Record Found");
			}
			ticketCountResponseDTO1.setCount(grievanceRepository.getCountByStatusAndCreatedBy(ticketStatus1.getId(),
					authenticationDTO.getUserId()));
			double earnedcount = (ticketCountResponseDTO1.getCount());
			System.out.println("PERCENTAGE:::" + (earnedcount / totalticket1) * 100);
			Double percentage = (earnedcount / totalticket1) * 100;
			ticketCountResponseDTO1.setPercentage(percentage);
			ticketCountResponseDTO1.setMonth("November");
			ticketCountResponseDTOList.add(ticketCountResponseDTO1);
		});

		ticketStatusList.forEach(ticketStatus -> {
			if (Objects.isNull(ticketStatus.getId()) || StringUtils.isBlank(ticketStatus.getGrievancetcstatus())) {
				return;
			}
			GrievanceTicketCountResponseDTO ticketCountResponseDTO = new GrievanceTicketCountResponseDTO();
			ticketCountResponseDTO.setTotcount(grievanceRepository.getCount());
			int total = ticketCountResponseDTO.getTotcount();
			double totalticket = total;
			System.out.println("Totalticket::" + totalticket);
			ticketCountResponseDTO.setStatus(ticketStatus.getGrievancetcstatus());
			if (Objects.isNull(authenticationDTO) || Objects.isNull(authenticationDTO.getUserId())) {
				log.error("invalid authentication details : {}", authenticationDTO);
				throw new RecordNotFoundException("No Record Found");
			}

			ticketCountResponseDTO.setCount(grievanceRepository.getCountByStatusAndCreatedBy1(ticketStatus.getId(),
					authenticationDTO.getUserId()));
			double earnedcount = (ticketCountResponseDTO.getCount());
			System.out.println("PERCENTAGE:::" + (earnedcount / totalticket) * 100);
			Double percentage = (earnedcount / totalticket) * 100;
			ticketCountResponseDTO.setMonth("December");
			ticketCountResponseDTO.setPercentage(percentage);
			ticketCountResponseDTOList.add(ticketCountResponseDTO);

		});

		ticketStatusListApril.forEach(ticketStatus1 -> {
			if (Objects.isNull(ticketStatus1.getId()) || StringUtils.isBlank(ticketStatus1.getGrievancetcstatus())) {
				return;
			}
			GrievanceTicketCountResponseDTO ticketCountResponseDTO1 = new GrievanceTicketCountResponseDTO();
			ticketCountResponseDTO1.setTotcount(grievanceRepository.getCountap());
			int total1 = ticketCountResponseDTO1.getTotcount();
			double totalticket1 = total1;
			System.out.println("Totalticket::" + totalticket1);
			ticketCountResponseDTO1.setStatus(ticketStatus1.getGrievancetcstatus());
			if (Objects.isNull(authenticationDTO) || Objects.isNull(authenticationDTO.getUserId())) {
				log.error("invalid authentication details : {}", authenticationDTO);
				throw new RecordNotFoundException("No Record Found");
			}
			ticketCountResponseDTO1.setCount(grievanceRepository.getCountByStatusAndCreatedByAp(ticketStatus1.getId(),
					authenticationDTO.getUserId()));
			double earnedcount = (ticketCountResponseDTO1.getCount());
			System.out.println("PERCENTAGE:::" + (earnedcount / totalticket1) * 100);
			Double percentage = (earnedcount / totalticket1) * 100;
			ticketCountResponseDTO1.setPercentage(percentage);
			ticketCountResponseDTO1.setMonth("January");
			ticketCountResponseDTOList.add(ticketCountResponseDTO1);
		});

		return Library.getSuccessfulResponse(ticketCountResponseDTOList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

	// Dashboard for grievance inspecting & excise officer

	public GenericResponse getCount_percentageinspect(GrievanceRegRequestDTO requestDto,
			AuthenticationDTO authenticationDTO) {
		final Date finalDate;
		Integer tictotalcount = 0;
		String userid = (requestDto.getAssignToId());

		System.out.println("assign to id " + userid);

		// List<CreateTicketEntity> ticketStatusList1 =
		// createTicketRepository.getAllOfCurrentMonth();

		List<GrievanceTicketCountResponseDTO> ticketCountResponseDTOList = new ArrayList<>();

		List<GrievanceTicketCountResponseDTO> ticketCountResponseDTOList1 = new ArrayList<>();

		List<GrievanceregisterEntity> ticketStatusList = grievanceRepository.findAllByStatusOrderByModifiedDateDesc();

		List<GrievanceregisterEntity> ticketStatusList1 = grievanceRepository.findAllByStatusOrderByModifiedDateDesc();

		if (CollectionUtils.isEmpty(ticketStatusList)) {
			throw new RecordNotFoundException("No Record Found");
		}

		ticketStatusList.forEach(ticketStatus -> {
			if (Objects.isNull(ticketStatus.getId()) || StringUtils.isBlank(ticketStatus.getGrievancetcstatus())) {
				return;
			}
			GrievanceTicketCountResponseDTO ticketCountResponseDTO = new GrievanceTicketCountResponseDTO();
			ticketCountResponseDTO.setTotcount(grievanceRepository.getCountinspect(userid));
			int total = ticketCountResponseDTO.getTotcount();
			double totalticket = total;
			System.out.println("Totalticket::" + totalticket);
			ticketCountResponseDTO.setStatus(ticketStatus.getGrievancetcstatus());
			if (Objects.isNull(authenticationDTO) || Objects.isNull(authenticationDTO.getUserId())) {
				log.error("invalid authentication details : {}", authenticationDTO);
				throw new RecordNotFoundException("No Record Found");
			}
			// ticketCountResponseDTO.setCount(grievanceRepository.getCountByStatusAndCreatedByperinspect(ticketStatus.getId(),authenticationDTO.getUserId(),userid));
			ticketCountResponseDTO
					.setCount(grievanceRepository.getCountByStatusAndCreatedByperinspect(ticketStatus.getId(), userid));

			double earnedcount = (ticketCountResponseDTO.getCount());
			System.out.println("PERCENTAGE:::" + (earnedcount / totalticket) * 100);
			Double percentage = (earnedcount / totalticket) * 100;
			ticketCountResponseDTO.setPercentage(percentage);
			ticketCountResponseDTOList.add(ticketCountResponseDTO);
		});

		for (int i = 0; i < ticketCountResponseDTOList.size(); i++) {

			tictotalcount += ticketCountResponseDTOList.get(i).getCount();
		}

		Integer tot = tictotalcount;

		ticketStatusList1.forEach(ticketStatus -> {
			if (Objects.isNull(ticketStatus.getId()) || StringUtils.isBlank(ticketStatus.getGrievancetcstatus())) {
				return;
			}
			GrievanceTicketCountResponseDTO ticketCountResponseDTO1 = new GrievanceTicketCountResponseDTO();
			ticketCountResponseDTO1.setTotcount(tot);
			int total = ticketCountResponseDTO1.getTotcount();
			double totalticket = total;
			System.out.println("Totalticket::" + totalticket);
			ticketCountResponseDTO1.setStatus(ticketStatus.getGrievancetcstatus());
			if (Objects.isNull(authenticationDTO) || Objects.isNull(authenticationDTO.getUserId())) {
				log.error("invalid authentication details : {}", authenticationDTO);
				throw new RecordNotFoundException("No Record Found");
			}
			// ticketCountResponseDTO.setCount(grievanceRepository.getCountByStatusAndCreatedByperinspect(ticketStatus.getId(),authenticationDTO.getUserId(),userid));
			ticketCountResponseDTO1
					.setCount(grievanceRepository.getCountByStatusAndCreatedByperinspect(ticketStatus.getId(), userid));

			double earnedcount = (ticketCountResponseDTO1.getCount());
			System.out.println("PERCENTAGE:::" + (earnedcount / totalticket) * 100);
			Double percentage = (earnedcount / totalticket) * 100;
			ticketCountResponseDTO1.setPercentage(percentage);
			ticketCountResponseDTOList1.add(ticketCountResponseDTO1);
		});

		return Library.getSuccessfulResponse(ticketCountResponseDTOList1, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

	// Dashboard by Month inspecting officer & excise officer

	@Override
	public GenericResponse getbyMonthinspect(GrievanceRegRequestDTO requestDto, AuthenticationDTO authenticationDTO) {

		String userid = (requestDto.getAssignToId());

		LocalDate currentDate = LocalDate.now();
		LocalDate currentDateLastMonth = LocalDate.of(currentDate.getYear(), currentDate.getMonth().minus(0),
				currentDate.getDayOfMonth());

		LocalDate firstDayOfMonth = currentDateLastMonth.with(TemporalAdjusters.firstDayOfMonth());
		LocalDate lastDayOfMonth = currentDateLastMonth.with(TemporalAdjusters.lastDayOfMonth());

		final LocalDate finalDate1 = lastDayOfMonth;
		System.out.println("..." + finalDate1);
		final Date finalDate;
		try {
			finalDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(lastDayOfMonth + " " + "00:00:00");
		} catch (ParseException e) {
			log.error("error occurred while parsing date : {}", e.getMessage());
			throw new InvalidDataValidation("Invalid date parameter passed");
		}

		List<GrievanceTicketCountResponseDTO> ticketCountResponseDTOList = new ArrayList<>();

		List<GrievanceregisterEntity> ticketStatusList = grievanceRepository.findAllByStatusOrderByModifiedDateDesc();

		List<GrievanceregisterEntity> ticketStatusList1 = grievanceRepository.findAllByStatusOrderByModifiedDateDesc();

		List<GrievanceregisterEntity> ticketStatusListApril = grievanceRepository
				.findAllByStatusOrderByModifiedDateDesc();

		if (CollectionUtils.isEmpty(ticketStatusList1)) {
			throw new RecordNotFoundException("No Record Found");
		}

		if (CollectionUtils.isEmpty(ticketStatusList)) {
			throw new RecordNotFoundException("No Record Found");
		}

		ticketStatusList1.forEach(ticketStatus1 -> {
			if (Objects.isNull(ticketStatus1.getId()) || StringUtils.isBlank(ticketStatus1.getGrievancetcstatus())) {
				return;
			}
			GrievanceTicketCountResponseDTO ticketCountResponseDTO1 = new GrievanceTicketCountResponseDTO();
			ticketCountResponseDTO1.setTotcount(grievanceRepository.getCountfebinspectmonth(userid));
			int total1 = ticketCountResponseDTO1.getTotcount();
			double totalticket1 = total1;
			System.out.println("Totalticket::" + totalticket1);
			ticketCountResponseDTO1.setStatus(ticketStatus1.getGrievancetcstatus());
			if (Objects.isNull(authenticationDTO) || Objects.isNull(authenticationDTO.getUserId())) {
				log.error("invalid authentication details : {}", authenticationDTO);
				throw new RecordNotFoundException("No Record Found");
			}
			ticketCountResponseDTO1
					.setCount(grievanceRepository.getCountByStatusAndCreatedByinsfeb(ticketStatus1.getId(), userid));
			double earnedcount = (ticketCountResponseDTO1.getCount());
			System.out.println("PERCENTAGE:::" + (earnedcount / totalticket1) * 100);
			Double percentage = (earnedcount / totalticket1) * 100;
			ticketCountResponseDTO1.setPercentage(percentage);
			ticketCountResponseDTO1.setMonth("February");
			ticketCountResponseDTOList.add(ticketCountResponseDTO1);
		});

		ticketStatusList.forEach(ticketStatus -> {
			if (Objects.isNull(ticketStatus.getId()) || StringUtils.isBlank(ticketStatus.getGrievancetcstatus())) {
				return;
			}
			GrievanceTicketCountResponseDTO ticketCountResponseDTO = new GrievanceTicketCountResponseDTO();
			ticketCountResponseDTO.setTotcount(grievanceRepository.getCountinspectmonth(userid));
			int total = ticketCountResponseDTO.getTotcount();
			double totalticket = total;
			System.out.println("Totalticket::" + totalticket);
			ticketCountResponseDTO.setStatus(ticketStatus.getGrievancetcstatus());
			if (Objects.isNull(authenticationDTO) || Objects.isNull(authenticationDTO.getUserId())) {
				log.error("invalid authentication details : {}", authenticationDTO);
				throw new RecordNotFoundException("No Record Found");
			}

			ticketCountResponseDTO.setCount(
					grievanceRepository.getCountByStatusAndCreatedByinspectmonth(ticketStatus.getId(), userid));
			double earnedcount = (ticketCountResponseDTO.getCount());
			System.out.println("PERCENTAGE:::" + (earnedcount / totalticket) * 100);
			Double percentage = (earnedcount / totalticket) * 100;
			ticketCountResponseDTO.setMonth("March");
			ticketCountResponseDTO.setPercentage(percentage);
			ticketCountResponseDTOList.add(ticketCountResponseDTO);

		});

		ticketStatusListApril.forEach(ticketStatus2 -> {
			if (Objects.isNull(ticketStatus2.getId()) || StringUtils.isBlank(ticketStatus2.getGrievancetcstatus())) {
				return;
			}
			GrievanceTicketCountResponseDTO ticketCountResponseDTO2 = new GrievanceTicketCountResponseDTO();
			ticketCountResponseDTO2.setTotcount(grievanceRepository.getCountapinspectmonth(userid));
			int total1 = ticketCountResponseDTO2.getTotcount();
			double totalticket1 = total1;
			System.out.println("Totalticket::" + totalticket1);
			ticketCountResponseDTO2.setStatus(ticketStatus2.getGrievancetcstatus());
			if (Objects.isNull(authenticationDTO) || Objects.isNull(authenticationDTO.getUserId())) {
				log.error("invalid authentication details : {}", authenticationDTO);
				throw new RecordNotFoundException("No Record Found");
			}
			ticketCountResponseDTO2
					.setCount(grievanceRepository.getCountByStatusAndCreatedByinsap(ticketStatus2.getId(), userid));
			double earnedcount = (ticketCountResponseDTO2.getCount());
			System.out.println("PERCENTAGE:::" + (earnedcount / totalticket1) * 100);
			Double percentage = (earnedcount / totalticket1) * 100;
			ticketCountResponseDTO2.setPercentage(percentage);
			ticketCountResponseDTO2.setMonth("April");
			ticketCountResponseDTOList.add(ticketCountResponseDTO2);
		});

		return Library.getSuccessfulResponse(ticketCountResponseDTOList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

	@Override
	public GenericResponse getBycommonserach(GrievanceRegRequestDTO requestDTO) {
		// List<GrievanceregisterEntity> list =
		// grievanceRepository.getByGrievanceidAndUserid(requestDTO.getGrievanceId(),requestDTO.getUserid());
		List<GrievanceregisterEntity> list = grievanceRepository
				.getByGrievanceidAndTypeofuser(requestDTO.getGrievanceId(), requestDTO.getTypeofUser());
		// List<GrievanceregisterEntity> list =
		// grievanceRepository.getByGrievanceid(requestDTO.getGrievanceId());

		if (CollectionUtils.isEmpty(list)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<GrievanceRegResponseDTO> responseDto = list.stream().map(grievancemapper::convertEntityToResponseDTO)
				.collect(Collectors.toList());
		return Library.getSuccessfulResponse(responseDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse FlagLIstAPI(GrievanceRegRequestDTO requestDTO) {
		Boolean flg = requestDTO.isFlag();

		String asigntoid = requestDTO.getAssignToId();

		AuthenticationDTO authenticationDTO = SecurityUtils.findAuthenticationObject();
		if (Objects.isNull(authenticationDTO) || Objects.isNull(authenticationDTO.getUserId())) {
			log.error("authentication details missing");
			return Library.getFailResponseCode(ErrorCode.ACCESS_DENIED.getErrorCode(), ErrorMessages.ACCESS_DENIED);
		}
		// List<CreateTicketEntity> list =
		// createTicketRepository.getByAssignTo(authenticationDTO.getUserId());

		List<GrievanceregisterEntity> list = grievanceRepository.getByFlagAssignto(asigntoid, flg);
		if (CollectionUtils.isEmpty(list)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<GrievanceRegResponseDTO> responseDto = list.stream().map(grievancemapper::convertEntityToResponseDTO)
				.collect(Collectors.toList());
		return Library.getSuccessfulResponse(responseDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

	// Flag Update

	@Override
	@Transactional
	public GenericResponse UpdateFlag(GrievanceRegRequestDTO requestDTO) {
		Optional<GrievanceregisterEntity> optional = grievanceRepository.findById(requestDTO.getId());

		if (!optional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}

		GrievanceregisterEntity entity = optional.get();
		// Optional<TicketStatusEntity> ticketOptional =
		// ticketStatusrepository.findById(requestDTO.getTicketStatusId());
		// if (!ticketOptional.isPresent()) {
		// throw new InvalidDataValidation(
		// ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] {
		// TICKET_STATUS_ID }));
		// }

		entity.setFlag(requestDTO.isFlag());
		grievanceRepository.save(entity);
		// this.saveAuditHistory(entity, HelpDeskTicketAction.UPDATED,
		// entity.getModifiedDate(), entity.getNotes());
		return Library.getSuccessfulResponse(grievancemapper.convertEntityToResponseDTO(entity),
				ErrorCode.CREATED.getErrorCode(), ErrorMessages.RECORED_UPDATED);

	}

	@Override
	public GenericResponse sendOTP(String phoneNumber) {
		Optional<ApplicationConstantEntity> applicationConstantEntity = applicationConstantRepository
				.findByCodeIgnoreCase(Constant.SMS_SERVICE_ENABLED);
		if (!applicationConstantEntity.isPresent() || Boolean.FALSE.equals(applicationConstantEntity.get().getStatus())
				|| !Constant.TRUE.equalsIgnoreCase(applicationConstantEntity.get().getValue())) {
			log.info(ErrorMessages.SMS_SERVICE_NOT_ENABLED);
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ErrorMessages.SMS_SERVICE_NOT_ENABLED);
		}

		if (!commonUtil.isPhoneNumberValid(phoneNumber)) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { Constant.PHONE_NUMBER }));
		}
		phoneNumber = phoneNumber.trim();
		GrievanceOTPVerificationEntity otpVerification = grievanceOTPVerificationRepository
				.findByPhoneNumber(phoneNumber);

		Random rnd = new Random();
		int number = rnd.nextInt(999999);
		String otp = String.format("%06d", number);
		Optional<TemplateEntity> templateEntity = templateRepository
				.findByCode(TemplateCode.HELPDESK_OTP_GENERATION.toString());
		if (Objects.isNull(templateEntity)) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ErrorMessages.TEMPLATE_NOT_CONFIGURED);
		}
		String content = templateEntity.get().getContent();
		content = content.replace("<<OTP>>", otp);
		SMSDetails smsDetails = new SMSDetails();
		smsDetails.setEvent(Constant.HELPDESK);
		smsDetails.setSubEvent(templateEntity.get().getTemplateId());
		smsDetails.setLangCode(Locale.ENGLISH.getLanguage());
		smsDetails.setMessage(content);
		smsDetails.setMobileNumber(phoneNumber);
		smsDetails.setTemplateId(templateEntity.get().getTemplateId());
		ApiResponse response = commonDataController.processSMS(smsDetails);
		if (Objects.isNull(response) || Boolean.FALSE.equals(response.getSuccess())) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), response.getResponseMsg());
		}

		if (Objects.nonNull(otpVerification)) {
			otpVerification.setOtp(otp);
			otpVerification.setExpiryDateTime(10);
			otpVerification.setOtpCount(otpVerification.getOtpCount() + 1);
		} else {
			otpVerification = new GrievanceOTPVerificationEntity();
			otpVerification.setPhoneNumber(phoneNumber);
			otpVerification.setOtp(otp);
			otpVerification.setExpiryDateTime(10);
			otpVerification.setOtpCount(1);
		}
		grievanceOTPVerificationRepository.save(otpVerification);

		return Library.getSuccessfulResponse(null, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.OTP_SENT_SUCCESSFULLY);
	}

	@Override
	public GenericResponse verifyOTP(@Valid GrievanceOTPVerificationRequestDTO otpVerificationDTO) {
		otpVerificationDTO.setPhoneNumber(otpVerificationDTO.getPhoneNumber().trim());
		otpVerificationDTO.setOtp(otpVerificationDTO.getOtp().trim());
		GrievanceOTPVerificationEntity otpVerification = grievanceOTPVerificationRepository
				.findByPhoneNumber(otpVerificationDTO.getPhoneNumber());

		if (Objects.isNull(otpVerification) || Objects.isNull(otpVerification.getOtp())
				|| Objects.isNull(otpVerification.getOtpExpiryDateTime())) {
			return Library.getFailedfulResponse(null, ErrorCode.IN_VALID_OTP.getErrorCode(),
					ErrorMessages.IN_VALID_OTP);
		}

		if (otpVerification.getOtpExpiryDateTime().isBefore(LocalDateTime.now())) {
			return Library.getFailResponseCode(ErrorCode.EXPIRED_OTP_OR_TOKEN.getErrorCode(),
					ResponseMessageConstant.EXPIRED_OTP_OR_TOKEN.getMessage());
		}
		if (otpVerificationDTO.getOtp().equals(otpVerification.getOtp())) {
			otpVerification.setExpiryDateTime(0);
			otpVerification.setOtpCount(0);
			grievanceOTPVerificationRepository.save(otpVerification);
		} else {
			return Library.getFailResponseCode(ErrorCode.INVALID_OTP_OR_TOKEN.getErrorCode(),
					ResponseMessageConstant.INVALID_OTP_OR_TOKEN.getMessage());
		}
		return Library.getSuccessfulResponse(null, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.OTP_VERIFIED_SUCCESSFULLY);
	}

	@Scheduled(cron = "0 */30 * * * ?")
	@Override
	@Transactional
	public GenericResponse autoEscalate() {
		List<String> ticketStatusList = new ArrayList<>();
		ticketStatusList.add("Pending");
		List<GrievanceregisterEntity> entityList = grievanceRepository.findByFilter(ticketStatusList);
		if (CollectionUtils.isEmpty(entityList)) {
			log.info("No data found in grievance_register table");
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		entityList.forEach(entity -> {
			if (Objects.isNull(entity.getIssuedetails()) || Objects.isNull(entity.getSlaEntity())) {
				log.info("either issue details or sla details missing : {}", entity.getId());
				return;
			}
			log.info("No data found in grievance_register table=:: {}, {}", entity.getIssuedetails().getId(),
					entity.getSlaEntity().getId());
			List<GrievanceEscalationWorkflowEntity> workFlowEntityList = escalationWorkflowRepository
					.getBySlaIdAndIssueDetailId(entity.getIssuedetails().getId(), entity.getSlaEntity().getId());
			if (CollectionUtils.isEmpty(workFlowEntityList)) {
				log.info("escalation workflow not configured : {}", entity.getId());
				return;
			}

			if (Objects.isNull(workFlowEntityList.get(0).getAssignTo())) {
				log.info("assignTo is null in escalation workflow : {}", entity.getId());
				return;
			}
			UserEntity userEntity = workFlowEntityList.get(0).getAssignTo();
			if (Objects.nonNull(userEntity) && !CollectionUtils.isEmpty(userEntity.getRoles())) {
				entity.setAssigntoid(userEntity.getId().toString());
				entity.setAssigngroup(userEntity.getRoles().get(0).getRoleName());
				entity.setGrievancetcstatus(Constant.ESCALATED);

				log.info("grievance ticket assignee updated : {}", entity.getId());
			}

		});
		return Library.getSuccessfulResponse(null, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORD_PROCESSED_SUCCESSFULLY);
	}

	public GenericResponse ViewList(GrievanceRegRequestDTO requestDto) {
		String fromdate = requestDto.getFromDate();
		String todate = requestDto.getEndDate();

		final Date fromDate;
		final Date toDate;

		try {
			fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fromdate + " " + "00:00:00");
		} catch (ParseException e) {
			log.error("error occurred while parsing date : {}", e.getMessage());
			throw new InvalidDataValidation("Invalid date parameter passed");
		}
		try {
			toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(todate + " " + "00:00:00");
		} catch (ParseException e) {
			log.error("error occurred while parsing date : {}", e.getMessage());
			throw new InvalidDataValidation("Invalid date parameter passed");
		}

		List<GrievanceregisterEntity> DepList = grievanceRepository.getCreatedDate(fromDate, toDate);
		if (CollectionUtils.isEmpty(DepList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<GrievanceRegResponseDTO> depResponseList = DepList.stream()
				.map(grievancemapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(depResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

//	private void saveAuditHistory(GrievanceregisterEntity entity, HelpDeskTicketAction action, Date date, String comments ) {
//		AuthenticationDTO authenticationDTO = SecurityUtils.findAuthenticationObject();
//		ZoneId defaultZoneId = ZoneId.systemDefault();
//		LocalDateTime localDateTime = date.toInstant().atZone(defaultZoneId).toLocalDateTime();
//		Optional<UserEntity> userEntity = null;
//		if(authenticationDTO != null) {
//		 userEntity = userRepository.findById(authenticationDTO.getUserId());
//		}else
//		{
//		userEntity = null;
//		}
//		GrievanceTicketAuditEntity grievanceticketaudit = new GrievanceTicketAuditEntity(
//				Objects.nonNull(userEntity) ? userEntity.get() : null, action, entity.getRefertic_number(),
//				entity.getGrievancetcstatus(), localDateTime, comments, entity.getAssigntoid());	
//		grievanceTicketAuditRepository.save(grievanceticketaudit);
//		
//	}

	@Override
	public GenericResponse ListByAll(PaginationRequestDTO paginationDto) {
		Pageable pageable = null;
		Page<GrievanceregisterEntity> list = null;
		Integer hofficerId = null;
		String typeofuser = null;
		String grievancetcStatus = null;
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
			if (Objects.nonNull(paginationDto.getFilters().get("hofficerId"))
					&& !paginationDto.getFilters().get("hofficerId").toString().trim().isEmpty()) {
				try {
					// id = Long.valueOf(paginationDto.getFilters().get(ID).toString());
//					assignto = Long.valueOf(paginationDto.getFilters().get("assignto").toString());
					hofficerId = Integer.valueOf(paginationDto.getFilters().get("hofficerId").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing assignto_Id :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}
			if (Objects.nonNull(paginationDto.getFilters().get("typeofuser"))
					&& !paginationDto.getFilters().get("typeofuser").toString().trim().isEmpty()) {
				try {
					typeofuser = String.valueOf(paginationDto.getFilters().get("typeofuser").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing typeofuser :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}

		}

		if (Objects.nonNull(hofficerId) && Objects.nonNull(typeofuser)) {
			list = grievanceRepository.getByHofficerIdAndtypeofuser(hofficerId, typeofuser, pageable);
		} else if (Objects.isNull(hofficerId) && Objects.nonNull(typeofuser)) {
			list = grievanceRepository.getBytype(typeofuser, pageable);
		}

		else if (Objects.nonNull(hofficerId) && Objects.isNull(typeofuser)) {
			list = grievanceRepository.getByHofficerId(hofficerId, pageable);
		}

		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		Page<GrievanceRegResponseDTO> finalResponse = list.map(grievancemapper::convertEntityToResponseDTOList);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getCount_percentage_grievance_dashboardprocess(CreateTicketRequestDto requestDto,
			AuthenticationDTO authenticationDTO) {

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

		List<GrievanceDashboardCountDTO> grievanceticketrecoverypointlist = null;

		if (Objects.isNull(authenticationDTO) || Objects.isNull(authenticationDTO.getUserId())) {
			log.error("invalid authentication details : {}", authenticationDTO);
			throw new RecordNotFoundException("No Record Found");
		}

		if (authenticationDTO.getRoleCodes().stream().anyMatch(r -> Constant.HELPDESK_ADMIN.equals(r))) {
			grievanceticketrecoverypointlist = grievanceRepository.getCountByFromDataAndToDate(fromDate, toDate);

		} else {
			grievanceticketrecoverypointlist = grievanceRepository.getCountByFromDataAndToDate(fromDate, toDate);
		}
		return Library.getSuccessfulResponse(grievanceticketrecoverypointlist,
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getbyGrievanceDashboardByMonth(AuthenticationDTO authenticationDTO) {

		List<GrievanceDashboardByMonthCountDTO> grievanceticketrecoverypointlist = null;

		if (Objects.isNull(authenticationDTO) || Objects.isNull(authenticationDTO.getUserId())) {
			log.error("invalid authentication details : {}", authenticationDTO);
			throw new RecordNotFoundException("No Record Found");
		}

		if (authenticationDTO.getRoleCodes().stream().anyMatch(r -> Constant.HELPDESK_ADMIN.equals(r))) {
			grievanceticketrecoverypointlist = grievanceRepository.getGrievanceDashboardByMonthCount();

		} else {
			grievanceticketrecoverypointlist = grievanceRepository.getGrievanceDashboardByMonthCount();
		}

		return Library.getSuccessfulResponse(grievanceticketrecoverypointlist,
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse addGrievanceTicketStatus(GrievanceTicketStatusDTO requestDTO) {

		Optional<GrievanceTicketStatusEntity> ticketOptional = grievanceTicketStatusRespository
				.findByTicketstatusnameIgnoreCase(requestDTO.getTicketstatusname().toUpperCase());

		if (ticketOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { TICKETSTATUS_NAME }));
		}
		ticketOptional = grievanceTicketStatusRespository
				.findByTicketstatusCodeIgnoreCase(requestDTO.getTicketstatusCode());
		if (ticketOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { CODE }));
		}
		requestDTO.setId(null);
		GrievanceTicketStatusEntity tcEntity = commonUtil.modalMap(requestDTO, GrievanceTicketStatusEntity.class);
		grievanceTicketStatusRespository.save(tcEntity);
		return Library.getSuccessfulResponse(tcEntity, ErrorCode.CREATED.getErrorCode(), ErrorMessages.RECORED_CREATED);
	}

	@Override
	public GenericResponse getGrievanceAllActive() {

		List<GrievanceTicketStatusEntity> assetTypeList = grievanceTicketStatusRespository
				.findAllByStatusOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(assetTypeList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<GrievanceTicketStatusResponseDTO> assetTypeResponseList = assetTypeList.stream()
				.map(grievanceTicketStatusMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(assetTypeResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

}
