package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.CATEGORY;
import static com.oasys.helpdesk.constant.Constant.CATEGORYID;
import static com.oasys.helpdesk.constant.Constant.CREATED_DATE;
import static com.oasys.helpdesk.constant.Constant.DATE_FORMAT;
import static com.oasys.helpdesk.constant.Constant.DESC;
import static com.oasys.helpdesk.constant.Constant.END_TIME;
import static com.oasys.helpdesk.constant.Constant.ID;
import static com.oasys.helpdesk.constant.Constant.ISSUEFROM;
import static com.oasys.helpdesk.constant.Constant.ISSUE_FROM_ID;
import static com.oasys.helpdesk.constant.Constant.KB_ID;
import static com.oasys.helpdesk.constant.Constant.LICENSE_NUMBER;
import static com.oasys.helpdesk.constant.Constant.LICENSE_TYPE_ID;
import static com.oasys.helpdesk.constant.Constant.MODIFIED_DATE;
import static com.oasys.helpdesk.constant.Constant.PRIORITY;
import static com.oasys.helpdesk.constant.Constant.PRIORITYID;
import static com.oasys.helpdesk.constant.Constant.SLA;
import static com.oasys.helpdesk.constant.Constant.SLA_ID;
import static com.oasys.helpdesk.constant.Constant.START_TIME;
import static com.oasys.helpdesk.constant.Constant.STATUS;
import static com.oasys.helpdesk.constant.Constant.SUBCATEGORY;
import static com.oasys.helpdesk.constant.Constant.SUB_CATEGORY_ID;
import static com.oasys.helpdesk.constant.Constant.TICKET_NUMBER;
import static com.oasys.helpdesk.constant.Constant.TICKET_STATUS;
import static com.oasys.helpdesk.constant.Constant.TICKET_STATUS_ID;
import static com.oasys.helpdesk.constant.Constant.TYPE;
import static com.oasys.helpdesk.constant.Constant.USER_ID;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import com.oasys.helpdesk.async.AsyncCommunicationExecution;
import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.DashboardCount;
import com.oasys.helpdesk.dto.DowntimeDTO;
import com.oasys.helpdesk.dto.DurationDTO;
import com.oasys.helpdesk.dto.InboundCallsTotalOperatinghrsDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO2;
import com.oasys.helpdesk.dto.PaginationResponseDTO;
import com.oasys.helpdesk.dto.Resolution24hrDTO;
import com.oasys.helpdesk.dto.ResolutionDTO;
import com.oasys.helpdesk.dto.ResolutionTicket24hrDTO;
import com.oasys.helpdesk.dto.SMSDetails;
import com.oasys.helpdesk.dto.SecuritymanagementDTO;
import com.oasys.helpdesk.dto.SecuritymanagementTicketsDTO;
import com.oasys.helpdesk.dto.TicketCountResponseDTO;
import com.oasys.helpdesk.dto.TicketcounRequest;
import com.oasys.helpdesk.dto.TollfreeDashboardDTO;
import com.oasys.helpdesk.entity.ApplicationConstantEntity;
import com.oasys.helpdesk.entity.CreateTicketEntity;
import com.oasys.helpdesk.entity.CreateTicketEntitypayment;
import com.oasys.helpdesk.entity.HelpdeskTicketAuditEntity;
import com.oasys.helpdesk.entity.IssueFromEntity;
import com.oasys.helpdesk.entity.KnowledgeBase;
import com.oasys.helpdesk.entity.RoleMaster;
import com.oasys.helpdesk.entity.TemplateEntity;
import com.oasys.helpdesk.entity.TicketStatusEntity;
import com.oasys.helpdesk.entity.UserEntity;
import com.oasys.helpdesk.entity.WorkflowEntity;
import com.oasys.helpdesk.enums.TemplateCode;
import com.oasys.helpdesk.mapper.CreateTicketMapper;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.repository.ApplicationConstantRepository;
import com.oasys.helpdesk.repository.CreateTicketRepository;
import com.oasys.helpdesk.repository.CreateTicketRepositorypaymet;
import com.oasys.helpdesk.repository.Districtcategorydashboard;
import com.oasys.helpdesk.repository.Districtdashboard;
import com.oasys.helpdesk.repository.Districtticketdashboard;
import com.oasys.helpdesk.repository.EmailRequestRepository;
import com.oasys.helpdesk.repository.EntitylicenseDTO;
import com.oasys.helpdesk.repository.EntitylicenseshopDTO;
import com.oasys.helpdesk.repository.HelpDeskTicketAuditRepository;
import com.oasys.helpdesk.repository.IssueFromRepository;
import com.oasys.helpdesk.repository.IssueFromRepositoryUpdate;
import com.oasys.helpdesk.repository.KnowledgeRepository;
import com.oasys.helpdesk.repository.RoleMasterRepository;
import com.oasys.helpdesk.repository.SubCategoryRepository;
import com.oasys.helpdesk.repository.TemplateRepository;
import com.oasys.helpdesk.repository.TicketStatusrepository;
import com.oasys.helpdesk.repository.Ticketdashboard;
import com.oasys.helpdesk.repository.UserRepository;
import com.oasys.helpdesk.repository.WorkflowRepository;
import com.oasys.helpdesk.request.CreateTicketDashboardDTO;
import com.oasys.helpdesk.request.CreateTicketRequestDto;
import com.oasys.helpdesk.request.CreateTicketRequestDto2;
import com.oasys.helpdesk.request.ResolutionDTOTickets;
import com.oasys.helpdesk.response.CreateTicketResponseDto;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.security.SecurityUtils;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.HelpDeskTicketAction;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.RandomUtil;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class CreateTicketServiceImpl implements CreateTicketService {
	private static final SingularAttribute SHOP_CODE = null;

	@Autowired
	CreateTicketRepository createTicketRepository;

	@Autowired
	CreateTicketRepositorypaymet createTicketRepositoryp;

	@Autowired
	CreateTicketMapper createTicketMapper;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private IssueFromRepository issueFromRepository;

	@Autowired
	private IssueFromRepositoryUpdate issueFromRepositoryupdate;

	@Autowired
	private SubCategoryRepository subCategoryRepository;

	@Autowired
	private TicketStatusrepository ticketStatusrepository;

	@Autowired
	private EmailRequestRepository emailRequestRepository;

	@Autowired
	private KnowledgeRepository knowledgeRepository;

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private WorkflowRepository workflowRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PaginationMapper paginationMapper;

	@Autowired
	private HelpDeskTicketAuditRepository helpDeskTicketAuditRepository;

	@Autowired
	private SendTemplateEmailService sendTemplateEmailService;

	private AsyncCommunicationExecution asyncCommunicationExecution;

	@Autowired
	private TemplateRepository templateRepository;

	@Autowired
	private CommonDataController commonDataController;

	@Autowired
	private ApplicationConstantRepository applicationConstantRepository;

	@Autowired
	private RoleMasterRepository rolemasterrepository;

	@Override
	public GenericResponse getAll(String type) {
		String typeq = "App";
		String typeWebApp = "Web App";
		List<String> types = new ArrayList<String>();
		types.add(typeq);
		types.add(typeWebApp);

		List<CreateTicketEntity> list = new ArrayList<CreateTicketEntity>();
		List<CreateTicketEntity> list1 = new ArrayList<CreateTicketEntity>();

		List<CreateTicketEntity> finalist = new ArrayList<CreateTicketEntity>();

		if (type.equalsIgnoreCase("All")) {
			list = createTicketRepository.findAllByOrderByModifiedDateDesc();
		}

		if (type.equalsIgnoreCase("SMS")) {
			Optional<IssueFromEntity> issueFromMasters = issueFromRepository.findByIssueFromIgnoreCase(type);

			if (!issueFromMasters.isPresent()) {
				return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { TYPE }));
			}
			IssueFromEntity entity = issueFromMasters.get();
			list = createTicketRepository.findAllByIssueFrom_idOrderByModifiedDateDesc(entity.getId());
			finalist.addAll(list);
		}

		if (type.equalsIgnoreCase("Email")) {
			Optional<IssueFromEntity> issueFromMasteremail = issueFromRepository.findByIssueFromIgnoreCase(type);

			if (!issueFromMasteremail.isPresent()) {
				return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { TYPE }));
			}
			IssueFromEntity entity = issueFromMasteremail.get();
			list = createTicketRepository.findAllByIssueFrom_idOrderByModifiedDateDesc(entity.getId());
			finalist.addAll(list);
		}

		if (type.equalsIgnoreCase("app")) {
			Optional<IssueFromEntity> issueFromMaster = issueFromRepository.findByIssueFromIgnoreCase(typeq);

			if (!issueFromMaster.isPresent()) {
				return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { TYPE }));
			}
			IssueFromEntity entity = issueFromMaster.get();
			list = createTicketRepository.findAllByIssueFrom_idOrderByModifiedDateDesc(entity.getId());

			Optional<IssueFromEntity> issueFromMaster1 = issueFromRepositoryupdate
					.findByIssueFromIgnoreCase(typeWebApp);

			if (!issueFromMaster1.isPresent()) {
				return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { TYPE }));
			}
			IssueFromEntity entity1 = issueFromMaster1.get();
			list1 = createTicketRepository.findAllByIssueFrom_idOrderByModifiedDateDesc(entity1.getId());
			finalist.addAll(list);
			finalist.addAll(list1);

		}
		if (CollectionUtils.isEmpty(finalist)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<CreateTicketResponseDto> responseDto = finalist.stream()
				.map(createTicketMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(responseDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getByStatus(Long id) {
		List<CreateTicketEntity> list = null;
		if (Objects.nonNull(id)) {
			list = createTicketRepository.findByStatus(id);
		}

		if (CollectionUtils.isEmpty(list)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<CreateTicketResponseDto> responseDto = list.stream().map(createTicketMapper::convertEntityToResponseDTO)
				.collect(Collectors.toList());
		return Library.getSuccessfulResponse(responseDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getById(Long id) {
		Optional<CreateTicketEntity> entity = createTicketRepository.findById(id);

		if (!entity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(createTicketMapper.convertEntityToResponseDTO(entity.get()),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getBylicencenumber(String licencenumber) {
		List<CreateTicketEntity> list = createTicketRepository.findLicenceNumber(licencenumber);
		if (CollectionUtils.isEmpty(list)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<CreateTicketResponseDto> responseDto = list.stream().map(createTicketMapper::convertEntityToResponseDTO)
				.collect(Collectors.toList());

		return Library.getSuccessfulResponse(responseDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getByAssignToId(CreateTicketDashboardDTO requestDTO) {
		AuthenticationDTO authenticationDTO = SecurityUtils.findAuthenticationObject();
		if (Objects.isNull(authenticationDTO) || Objects.isNull(authenticationDTO.getUserId())) {
			log.error("authentication details missing");
			return Library.getFailResponseCode(ErrorCode.ACCESS_DENIED.getErrorCode(), ErrorMessages.ACCESS_DENIED);
		}
		List<CreateTicketEntity> list = createTicketRepository.getByAssignTo(authenticationDTO.getUserId());

		if (CollectionUtils.isEmpty(list)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<CreateTicketResponseDto> responseDto = list.stream().map(createTicketMapper::convertEntityToResponseDTO)
				.collect(Collectors.toList());
		return Library.getSuccessfulResponse(responseDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getByAssignToIdfromdatetodate(CreateTicketRequestDto requestDTO) {
		AuthenticationDTO authenticationDTO = SecurityUtils.findAuthenticationObject();

		final Date fromDate;
		final Date toDate;
		try {
			fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(requestDTO.getFromDate() + " " + "00:00:00");
		} catch (ParseException e) {
			log.error("error occurred while parsing date : {}", e.getMessage());
			throw new InvalidDataValidation("Invalid date parameter passed");
		}
		try {
			toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(requestDTO.getToDate() + " " + "23:59:59");
		} catch (ParseException e) {
			log.error("error occurred while parsing date : {}", e.getMessage());
			throw new InvalidDataValidation("Invalid date parameter passed");
		}

		if (Objects.isNull(authenticationDTO) || Objects.isNull(authenticationDTO.getUserId())) {
			log.error("authentication details missing");
			return Library.getFailResponseCode(ErrorCode.ACCESS_DENIED.getErrorCode(), ErrorMessages.ACCESS_DENIED);
		}
		// List<CreateTicketEntity> list =
		// createTicketRepository.getByAssignToAndCreateddate(authenticationDTO.getUserId(),fromDate,toDate);

//		if (CollectionUtils.isEmpty(list)) {
//			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
//					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
//		}
//		List<CreateTicketResponseDto> responseDto = list.stream()
//				.map(createTicketMapper::convertEntityToResponseDTO).collect(Collectors.toList());
//		return Library.getSuccessfulResponse(responseDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
//				ErrorMessages.RECORED_FOUND);

		return null;
	}

	@Override
	public GenericResponse FlagLIstAPI(CreateTicketDashboardDTO requestDTO) {
		Boolean flg = requestDTO.isFlag();

		Long asigntoid = requestDTO.getAssignToId();

		AuthenticationDTO authenticationDTO = SecurityUtils.findAuthenticationObject();
		if (Objects.isNull(authenticationDTO) || Objects.isNull(authenticationDTO.getUserId())) {
			log.error("authentication details missing");
			return Library.getFailResponseCode(ErrorCode.ACCESS_DENIED.getErrorCode(), ErrorMessages.ACCESS_DENIED);
		}
		// List<CreateTicketEntity> list =
		// createTicketRepository.getByAssignTo(authenticationDTO.getUserId());

		List<CreateTicketEntity> list = createTicketRepository.getByFlagAssignto(asigntoid, flg);
		if (CollectionUtils.isEmpty(list)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<CreateTicketResponseDto> responseDto = list.stream().map(createTicketMapper::convertEntityToResponseDTO)
				.collect(Collectors.toList());
		return Library.getSuccessfulResponse(responseDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	// Flag Update

	@Override
	@Transactional
	public GenericResponse UpdateFlag(CreateTicketDashboardDTO requestDTO) {
		Optional<CreateTicketEntity> optional = createTicketRepository.findById(requestDTO.getId());

		if (!optional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}

		CreateTicketEntity entity = optional.get();
		Optional<TicketStatusEntity> ticketOptional = ticketStatusrepository.findById(requestDTO.getTicketStatusId());
		if (!ticketOptional.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { TICKET_STATUS_ID }));
		}

		entity.setFlag(requestDTO.isFlag());
		createTicketRepository.save(entity);
		this.saveAuditHistory(entity, HelpDeskTicketAction.UPDATED, entity.getModifiedDate(), requestDTO.getRemarks());
		return Library.getSuccessfulResponse(createTicketMapper.convertEntityToResponseDTO(entity),
				ErrorCode.CREATED.getErrorCode(), ErrorMessages.RECORED_UPDATED);

	}

	@Override
	public GenericResponse getAllByRequestFilter(PaginationRequestDTO requestData, AuthenticationDTO authenticationDTO)
			throws ParseException {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		Long count = this.getCountBySearchFields(requestData, authenticationDTO);
		log.info("total count :: {}", count);
		if (count > 0) {
			List<CreateTicketEntity> list = this.getRecordsByFilterDTO(requestData, authenticationDTO);
			if (CollectionUtils.isEmpty(list)) {
				throw new RecordNotFoundException("No Record Found");
			}
			List<CreateTicketResponseDto> dtoList = list.stream().map(createTicketMapper::convertEntityToResponseDTO)
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

	public List<CreateTicketEntity> getRecordsByFilterDTO(PaginationRequestDTO filterRequestDTO,
			AuthenticationDTO authenticationDTO) throws ParseException {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<CreateTicketEntity> cq = cb.createQuery(CreateTicketEntity.class);
		Root<CreateTicketEntity> from = cq.from(CreateTicketEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<CreateTicketEntity> typedQuery = null;
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

		List<CreateTicketEntity> data = typedQuery.getResultList();
		if (CollectionUtils.isEmpty(data)) {
			throw new RecordNotFoundException("No Record Found");
		}
		return data;
	}

	private Long getCountBySearchFields(PaginationRequestDTO filterRequestDTO, AuthenticationDTO authenticationDTO)
			throws ParseException {
		log.info("getting total count by search fields :: {}", filterRequestDTO.toString());
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<CreateTicketEntity> from = cq.from(CreateTicketEntity.class);
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

	private void addCriteria(CriteriaBuilder cb, List<Predicate> list, PaginationRequestDTO filterRequestDTO,
			Root<CreateTicketEntity> from, AuthenticationDTO authenticationDTO) throws ParseException {

		if (authenticationDTO.getRoleCodes() == null) {

		}
//		else if (authenticationDTO.getRoleCodes().stream().anyMatch(r -> Constant.HELPDESK_EXECUTIVE_ROLE.equals(r))) {
//			list.add(cb.equal(from.get(CREATED_BY), authenticationDTO.getUserId()));
//		}

//		
		else if (authenticationDTO.getRoleCodes().stream().anyMatch(r -> Constant.FIELD_ENGINEER_ROLE.equals(r))
				|| authenticationDTO.getRoleCodes().stream().anyMatch(r -> Constant.NODAL_OFFICER_ROLE.equals(r))) {
			list.add(cb.equal(from.get("assignTo"), authenticationDTO.getUserId()));
		}
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

				if (Objects.nonNull(filterRequestDTO.getFilters().get("fromDate"))
						&& !filterRequestDTO.getFilters().get("fromDate").toString().trim().isEmpty()
						&& Objects.nonNull(filterRequestDTO.getFilters().get("toDate"))
						&& !filterRequestDTO.getFilters().get("toDate").toString().trim().isEmpty()) {

					Date fromDate = new SimpleDateFormat(DATE_FORMAT)
							.parse(filterRequestDTO.getFilters().get("fromDate").toString() + " " + START_TIME);

					Date toDate = new SimpleDateFormat(DATE_FORMAT)
							.parse(filterRequestDTO.getFilters().get("toDate").toString() + " " + END_TIME);

					list.add(cb.between(from.get(CREATED_DATE), fromDate, toDate));

				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get(ID))
						&& !filterRequestDTO.getFilters().get(ID).toString().trim().isEmpty()) {

					Long id = Long.valueOf(filterRequestDTO.getFilters().get(ID).toString());
					list.add(cb.equal(from.get(ID), id));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get(TICKET_NUMBER))
						&& !filterRequestDTO.getFilters().get(TICKET_NUMBER).toString().trim().isEmpty()) {

					String ticketNumber = String.valueOf(filterRequestDTO.getFilters().get(TICKET_NUMBER).toString());
					list.add(cb.equal(from.get(TICKET_NUMBER), ticketNumber));

				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get(ISSUE_FROM_ID))
						&& !filterRequestDTO.getFilters().get(ISSUE_FROM_ID).toString().trim().isEmpty()) {
					Long issueFrom = Long.valueOf(filterRequestDTO.getFilters().get(ISSUE_FROM_ID).toString());
					list.add(cb.equal(from.get(ISSUEFROM), issueFrom));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get(CATEGORYID))
						&& !filterRequestDTO.getFilters().get(CATEGORYID).toString().trim().isEmpty()) {
					Long categoryId = Long.valueOf(filterRequestDTO.getFilters().get(CATEGORYID).toString());
					list.add(cb.equal(from.get(CATEGORY), categoryId));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get(SUB_CATEGORY_ID))
						&& !filterRequestDTO.getFilters().get(SUB_CATEGORY_ID).toString().trim().isEmpty()) {
					Long subcategoryId = Long.valueOf(filterRequestDTO.getFilters().get(SUB_CATEGORY_ID).toString());
					list.add(cb.equal(from.get(SUBCATEGORY), subcategoryId));
				}
				if (Objects.nonNull(filterRequestDTO.getFilters().get(LICENSE_TYPE_ID))
						&& !filterRequestDTO.getFilters().get(LICENSE_TYPE_ID).toString().trim().isEmpty()) {
					String licenseTypeId = String
							.valueOf(filterRequestDTO.getFilters().get(LICENSE_TYPE_ID).toString());
					list.add(cb.equal(from.get("licenceTypeId"), licenseTypeId));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get(LICENSE_NUMBER))
						&& !filterRequestDTO.getFilters().get(LICENSE_NUMBER).toString().trim().isEmpty()) {
					String licenseNumber = String.valueOf(filterRequestDTO.getFilters().get(LICENSE_NUMBER).toString());
					// list.add(cb.equal(from.get(LICENSE_NUMBER), licenseNumber));
					Expression<String> mainlic = from.get(LICENSE_NUMBER);
					Expression<String> mainModushopcode = from.get("shopCode");
					Predicate liceneNo = mainlic.in(filterRequestDTO.getFilters().get(LICENSE_NUMBER));
					Predicate shopcode = mainModushopcode.in(filterRequestDTO.getFilters().get(LICENSE_NUMBER));
					Predicate liceneNoOrunitcode = cb.or(liceneNo, shopcode);
					list.add(liceneNoOrunitcode);
				}
				if (Objects.nonNull(filterRequestDTO.getFilters().get(PRIORITYID))
						&& !filterRequestDTO.getFilters().get(PRIORITYID).toString().trim().isEmpty()) {
					Long priorityId = Long.valueOf(filterRequestDTO.getFilters().get(PRIORITYID).toString());
					list.add(cb.equal(from.get(PRIORITY), priorityId));
				}
				if (Objects.nonNull(filterRequestDTO.getFilters().get(TICKET_STATUS_ID))
						&& !filterRequestDTO.getFilters().get(TICKET_STATUS_ID).toString().trim().isEmpty()) {
					Long status = Long.valueOf(filterRequestDTO.getFilters().get(TICKET_STATUS_ID).toString());
					list.add(cb.equal(from.get(TICKET_STATUS), status));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get(SLA_ID))
						&& !filterRequestDTO.getFilters().get(SLA_ID).toString().trim().isEmpty()) {
					Long sla = Long.valueOf(filterRequestDTO.getFilters().get(SLA_ID).toString());
					list.add(cb.equal(from.get(SLA), sla));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("raisedBy"))
						&& !filterRequestDTO.getFilters().get("raisedBy").toString().trim().isEmpty()) {
					String raisedBy = String.valueOf(filterRequestDTO.getFilters().get("raisedBy").toString());
					list.add(cb.equal(from.get("raisedBy"), raisedBy));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("assignTo"))
						&& !filterRequestDTO.getFilters().get("assignTo").toString().trim().isEmpty()) {
					Long assignto = Long.valueOf(filterRequestDTO.getFilters().get("assignTo").toString());
					list.add(cb.equal(from.get("assignTo"), assignto));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("AppWepappId"))
						&& !filterRequestDTO.getFilters().get("AppWepappId").toString().trim().isEmpty()) {
					List<String> appwebappids = (List<String>) (filterRequestDTO.getFilters().get("AppWepappId"));
					boolean empty = appwebappids.isEmpty();
					if (empty == true) {
						System.out.println("The ArrayList is empty");
					} else {
						System.out.println("The ArrayList is not empty");
						Expression<String> mainModule = from.get("issueFrom");
						list.add(mainModule.in(appwebappids));
					}
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("statusarrayofId"))
						&& !filterRequestDTO.getFilters().get("AppWepappId").toString().trim().isEmpty()) {
					List<String> statusarrayofIds = (List<String>) (filterRequestDTO.getFilters()
							.get("statusarrayofId"));
					boolean empty = statusarrayofIds.isEmpty();
					if (empty == true) {
						System.out.println("The ArrayList is empty");
					} else {
						System.out.println("The ArrayList is not empty");
						Expression<String> mainModule = from.get("ticketStatus");
						list.add(mainModule.in(statusarrayofIds));
					}
				}

			}
		} catch (ParseException e) {
			throw new InvalidParameterException("No Record Found");
		}
	}

	@Override
	public GenericResponse getCount(String date, AuthenticationDTO authenticationDTO) {
		final Date finalDate;
		try {
			finalDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date + " " + "00:00:00");
		} catch (ParseException e) {
			log.error("error occurred while parsing date : {}", e.getMessage());
			throw new InvalidDataValidation("Invalid date parameter passed");
		}
		List<TicketCountResponseDTO> ticketCountResponseDTOList = new ArrayList<>();
		List<TicketStatusEntity> ticketStatusList = ticketStatusrepository.findAllByStatusOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(ticketStatusList)) {
			throw new RecordNotFoundException("No Record Found");
		}
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
			if (authenticationDTO.getRoleCodes().stream().anyMatch(r -> Constant.HELPDESK_ADMIN.equals(r))) {
				ticketCountResponseDTO.setCount(
						createTicketRepository.getCountByStatusAndCreatedDate(ticketStatus.getId(), finalDate));
			} else if (authenticationDTO.getRoleCodes().stream()
					.anyMatch(r -> Constant.HELPDESK_EXECUTIVE_ROLE.equals(r))) {
				ticketCountResponseDTO.setCount(createTicketRepository.getCountByStatusAndCreatedByAndCreatedDate(
						ticketStatus.getId(), authenticationDTO.getUserId(), finalDate));
			} else if (authenticationDTO.getRoleCodes().stream().anyMatch(r -> Constant.FIELD_ENGINEER_ROLE.equals(r))
					|| authenticationDTO.getRoleCodes().stream().anyMatch(r -> Constant.NODAL_OFFICER_ROLE.equals(r))) {
				ticketCountResponseDTO.setCount(createTicketRepository.getCountByStatusAndAssignToAndCreatedDate(
						ticketStatus.getId(), authenticationDTO.getUserId(), finalDate));
			}
			ticketCountResponseDTOList.add(ticketCountResponseDTO);
		});
		return Library.getSuccessfulResponse(ticketCountResponseDTOList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

	@Override
	public GenericResponse getCount1(String date, String todate, String issueFrom,
			AuthenticationDTO authenticationDTO) {
		final Date fromDate;
		final Date toDate;
		try {
			fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date + " " + "00:00:00");
		} catch (ParseException e) {
			log.error("error occurred while parsing date : {}", e.getMessage());
			throw new InvalidDataValidation("Invalid date parameter passed");
		}
		try {
			toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(todate + " " + "23:59:59");
		} catch (ParseException e) {
			log.error("error occurred while parsing date : {}", e.getMessage());
			throw new InvalidDataValidation("Invalid date parameter passed");
		}
		String issue = issueFrom;
		if (issue == null) {
			List<TicketCountResponseDTO> ticketCountResponseDTOList = new ArrayList<>();
			List<TicketStatusEntity> ticketStatusList = ticketStatusrepository.findAllByStatusOrderByModifiedDateDesc();
			if (CollectionUtils.isEmpty(ticketStatusList)) {
				throw new RecordNotFoundException("No Record Found");
			}
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
				if (authenticationDTO.getRoleCodes().stream().anyMatch(r -> Constant.HELPDESK_ADMIN.equals(r))) {
//					ticketCountResponseDTO.setCount(
//							createTicketRepository.getCountByStatusAndCreatedDate(ticketStatus.getId(), fromDate));
					ticketCountResponseDTO.setCount(createTicketRepository
							.getCountByStatusAndCreatedDateBetween(ticketStatus.getId(), fromDate, toDate));

				} else if (authenticationDTO.getRoleCodes().stream()
						.anyMatch(r -> Constant.HELPDESK_EXECUTIVE_ROLE.equals(r))) {
//					ticketCountResponseDTO.setCount(createTicketRepository.getCountByStatusAndCreatedByAndCreatedDate(
//							ticketStatus.getId(), authenticationDTO.getUserId(), fromDate));

					ticketCountResponseDTO
							.setCount(createTicketRepository.getCountByStatusAndCreatedByAndCreatedDateBetween(
									ticketStatus.getId(), authenticationDTO.getUserId(), fromDate, toDate));

				} else if (authenticationDTO.getRoleCodes().stream()
						.anyMatch(r -> Constant.FIELD_ENGINEER_ROLE.equals(r))
						|| authenticationDTO.getRoleCodes().stream()
								.anyMatch(r -> Constant.NODAL_OFFICER_ROLE.equals(r))
						|| authenticationDTO.getRoleCodes().stream()
								.anyMatch(r -> Constant.SOFTWARE_TEAM_ROLE.equals(r))
						|| authenticationDTO.getRoleCodes().stream()
								.anyMatch(r -> Constant.FIELD_TEAM_ROLE.equals(r))) {
					ticketCountResponseDTO
							.setCount(createTicketRepository.getCountByStatusAndAssignToAndCreatedDateBetween(
									ticketStatus.getId(), authenticationDTO.getUserId(), fromDate, toDate));

				}

				else if (authenticationDTO.getRoleCodes().stream()
						.anyMatch(r -> Constant.HD_SHIFT_SUPERVISOR.equals(r))) {
					ticketCountResponseDTO
							.setCount(createTicketRepository.getCountByStatusAndCreatedByAndCreatedDateBetween(
									ticketStatus.getId(), authenticationDTO.getUserId(), fromDate, toDate));

				}

				else if (authenticationDTO.getRoleCodes().stream().anyMatch(r -> Constant.MANAGER.equals(r))) {
					ticketCountResponseDTO
							.setCount(createTicketRepository.getCountByStatusAndCreatedByAndCreatedDateBetween(
									ticketStatus.getId(), authenticationDTO.getUserId(), fromDate, toDate));

				}

				else if (authenticationDTO.getRoleCodes().stream().anyMatch(r -> Constant.HD_MANAGER.equals(r))) {
					ticketCountResponseDTO
							.setCount(createTicketRepository.getCountByStatusAndCreatedByAndCreatedDateBetween(
									ticketStatus.getId(), authenticationDTO.getUserId(), fromDate, toDate));

				}

				ticketCountResponseDTOList.add(ticketCountResponseDTO);
			});

			return Library.getSuccessfulResponse(ticketCountResponseDTOList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		} else if (issue.equalsIgnoreCase("app")) {// if(issue.equalsIgnoreCase("app")) {
			List<TicketCountResponseDTO> ticketCountResponseDTOList = new ArrayList<>();
			List<TicketStatusEntity> ticketStatusList = ticketStatusrepository.findAllByStatusOrderByModifiedDateDesc();
			if (CollectionUtils.isEmpty(ticketStatusList)) {
				throw new RecordNotFoundException("No Record Found");
			}
			ticketStatusList.forEach(ticketStatus -> {
				Long app = (long) 4;
				Long webapp = (long) 5;
				Integer appcount = 0;
				Integer webappcount = 0;
				Integer total = 0;

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
				if (authenticationDTO.getRoleCodes().stream().anyMatch(r -> Constant.HELPDESK_ADMIN.equals(r))) {
					appcount = createTicketRepository.getCountByStatusAndCreatedDateBetweenAndIssueFrom(
							ticketStatus.getId(), fromDate, toDate, app);
					webappcount = createTicketRepository.getCountByStatusAndCreatedDateBetweenAndIssueFrom(
							ticketStatus.getId(), fromDate, toDate, webapp);
					total = appcount + webappcount;
					ticketCountResponseDTO.setCount(total);

				} else if (authenticationDTO.getRoleCodes().stream()
						.anyMatch(r -> Constant.HELPDESK_EXECUTIVE_ROLE.equals(r))) {
					appcount = createTicketRepository.getCountByStatusAndCreatedByAndCreatedDateBetweenAndIssueFrom(
							ticketStatus.getId(), authenticationDTO.getUserId(), fromDate, toDate, app);
					webappcount = createTicketRepository.getCountByStatusAndCreatedByAndCreatedDateBetweenAndIssueFrom(
							ticketStatus.getId(), authenticationDTO.getUserId(), fromDate, toDate, webapp);
					total = appcount + webappcount;
					ticketCountResponseDTO.setCount(total);

				}

				else if (authenticationDTO.getRoleCodes().stream().anyMatch(r -> Constant.FIELD_ENGINEER_ROLE.equals(r))
						|| authenticationDTO.getRoleCodes().stream()
								.anyMatch(r -> Constant.SOFTWARE_TEAM_ROLE.equals(r))
						|| authenticationDTO.getRoleCodes().stream().anyMatch(r -> Constant.FIELD_TEAM_ROLE.equals(r))
						|| authenticationDTO.getRoleCodes().stream()
								.anyMatch(r -> Constant.NODAL_OFFICER_ROLE.equals(r))) {
					appcount = createTicketRepository.getCountByStatusAndAssignToAndCreatedDateBetweenAndIssueFrom(
							ticketStatus.getId(), authenticationDTO.getUserId(), fromDate, toDate, app);
					webappcount = createTicketRepository.getCountByStatusAndAssignToAndCreatedDateBetweenAndIssueFrom(
							ticketStatus.getId(), authenticationDTO.getUserId(), fromDate, toDate, webapp);
					total = appcount + webappcount;
					ticketCountResponseDTO.setCount(total);

				}

				else if (authenticationDTO.getRoleCodes().stream()
						.anyMatch(r -> Constant.HD_SHIFT_SUPERVISOR.equals(r))) {
					appcount = createTicketRepository.getCountByStatusAndCreatedByAndCreatedDateBetweenAndIssueFrom(
							ticketStatus.getId(), authenticationDTO.getUserId(), fromDate, toDate, app);
					webappcount = createTicketRepository.getCountByStatusAndCreatedByAndCreatedDateBetweenAndIssueFrom(
							ticketStatus.getId(), authenticationDTO.getUserId(), fromDate, toDate, webapp);
					total = appcount + webappcount;
					ticketCountResponseDTO.setCount(total);

				}

				else if (authenticationDTO.getRoleCodes().stream().anyMatch(r -> Constant.MANAGER.equals(r))) {
					appcount = createTicketRepository.getCountByStatusAndCreatedByAndCreatedDateBetweenAndIssueFrom(
							ticketStatus.getId(), authenticationDTO.getUserId(), fromDate, toDate, app);
					webappcount = createTicketRepository.getCountByStatusAndCreatedByAndCreatedDateBetweenAndIssueFrom(
							ticketStatus.getId(), authenticationDTO.getUserId(), fromDate, toDate, webapp);
					total = appcount + webappcount;
					ticketCountResponseDTO.setCount(total);

				} else if (authenticationDTO.getRoleCodes().stream().anyMatch(r -> Constant.HD_MANAGER.equals(r))) {
					appcount = createTicketRepository.getCountByStatusAndCreatedByAndCreatedDateBetweenAndIssueFrom(
							ticketStatus.getId(), authenticationDTO.getUserId(), fromDate, toDate, app);
					webappcount = createTicketRepository.getCountByStatusAndCreatedByAndCreatedDateBetweenAndIssueFrom(
							ticketStatus.getId(), authenticationDTO.getUserId(), fromDate, toDate, webapp);
					total = appcount + webappcount;
					ticketCountResponseDTO.setCount(total);

				}

				ticketCountResponseDTOList.add(ticketCountResponseDTO);
			});
			return Library.getSuccessfulResponse(ticketCountResponseDTOList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}
		return null;
	}

	@Override
	@Transactional
	public GenericResponse add(CreateTicketRequestDto requestDTO) {
		Optional<CreateTicketEntity> optional = null;
		CreateTicketEntity entity = null;
		/*
		 * createTicketRepository.findByTicketNumberIgnoreCase(requestDTO.
		 * getTicketNumber().toUpperCase()); if (optional.isPresent()) { return
		 * Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
		 * ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] {
		 * TICKET_NUMBER })); }
		 */
		try {
			// String code = RandomUtil.generateTicketId();
			String code = RandomUtil.getinitialsequential();
			while (true) {
				optional = createTicketRepository.findByTicketNumberIgnoreCase(code);
				if (optional.isPresent()) {
					// code = RandomUtil.getRandomNumber();
					code = getsequential();

				} else {
					break;
				}
			}
			requestDTO.setTicketNumber(code);
			requestDTO.setId(null);
			requestDTO.setDuration("0");
			entity = createTicketMapper.convertRequestDTOToEntity(requestDTO, null);
			createTicketRepository.save(entity);
		}

		catch (Exception e) {
			throw new InvalidDataValidation("Heavy traffic please try later");
		}
		// CreateTicketResponseDto createTicketResponseDto =
		// createTicketMapper.convertEntityToResponseDTO(entity);
		String email = "-";
		if (email.equals(requestDTO.getEmail())) {
			log.info("Emailid is Empty::" + requestDTO.getEmail());
		} else {
			try {
				this.saveAuditHistory(entity, HelpDeskTicketAction.CREATED, entity.getCreatedDate(),
						requestDTO.getRemarks());
				// sendTemplateEmailService.sendAcknowledgeTemplate(createTicketResponseDto);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (StringUtils.isNotBlank(entity.getMobile())) {
			try {
				log.info(":::::MObile number:::::" + entity.getMobile());
//				this.sendTicketNotification(requestDTO.getTicketStatusId(), entity.getTicketNumber(),
//						entity.getMobile());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return Library.getSuccessfulResponse(requestDTO, ErrorCode.CREATED.getErrorCode(),
				ErrorMessages.RECORED_CREATED);

	}

	@Override
	@Transactional
	public GenericResponse update(CreateTicketDashboardDTO requestDTO) {
		Optional<CreateTicketEntity> optional = createTicketRepository.findById(requestDTO.getId());

		if (!optional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}

		CreateTicketEntity entity = optional.get();
		Optional<TicketStatusEntity> ticketOptional = ticketStatusrepository.findById(requestDTO.getTicketStatusId());
		if (!ticketOptional.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { TICKET_STATUS_ID }));
		}

		if (requestDTO.getAssignToId() != null) {
			Optional<UserEntity> uOptional = userRepository.findById(requestDTO.getAssignToId());
			if (!uOptional.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { USER_ID }));
			}
			entity.setAssignTo(uOptional.get());
		}

		if (requestDTO.getAssignGroupId() != null) {
			Optional<RoleMaster> uOptionalassign = rolemasterrepository.findById(requestDTO.getAssignGroupId());
			if (!uOptionalassign.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { USER_ID }));
			}
			entity.setAssignGroup(uOptionalassign.get());
		}

		if (requestDTO.getKnowledgeBaseId() != null) {
			Optional<KnowledgeBase> knOptional = knowledgeRepository.findById(requestDTO.getKnowledgeBaseId());
			if (!knOptional.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { KB_ID }));
			}

			entity.setKnowledgeBase(knOptional.get());
		}

		entity.setTicketStatus(ticketOptional.get());
		entity.setRemarks(requestDTO.getRemarks());
		entity.setImageUrl(requestDTO.getImageUrl());
		entity.setImageUuid(requestDTO.getImageUuid());
		entity.setUploadApplication(requestDTO.getUploadApplication());
		entity.setNotes(requestDTO.getNotes());
		entity.setApplicationUuid(requestDTO.getApplicationUuid());
		entity.setUserRemarks(requestDTO.getUserRemarks());
		String duration = optional.get().getDuration();
		entity.setCreatedBy(optional.get().getCreatedBy());
		try {
			if (duration != null) {
				if (optional.get().getTicketStatus().getTicketstatusname().equalsIgnoreCase("Closed")
						|| optional.get().getTicketStatus().getTicketstatusname().equalsIgnoreCase("Resolved")) {
					if (duration.equalsIgnoreCase("0")) {
						Optional<DurationDTO> time = createTicketRepository.getByTimestamp(requestDTO.getId());
						entity.setDuration(time.get().getTimestampDiffernce());
					}

				}

			}
		} catch (Exception e) {
			log.info("::Timestamp::Calc" + e);
		}

		createTicketRepository.save(entity);
		CreateTicketResponseDto createTicketResponseDto = null;

		try {
			this.saveAuditHistory(entity, HelpDeskTicketAction.UPDATED, entity.getModifiedDate(),
					requestDTO.getRemarks());
			if (StringUtils.isNotBlank(entity.getMobile())) {
				this.sendTicketNotification(requestDTO.getTicketStatusId(), entity.getTicketNumber(),
						entity.getMobile());
			}
			createTicketResponseDto = createTicketMapper.convertEntityToResponseDTO(entity);
			if (Constant.CLOSE_TICKET.equals(createTicketResponseDto.getTicketStatus())
					&& StringUtils.isNotBlank(createTicketResponseDto.getEmail())) {
				// currently we dont have any template for close ticket
			} else if (Constant.RESOLVE_TICKET.equals(createTicketResponseDto.getTicketStatus())
					&& StringUtils.isNotBlank(createTicketResponseDto.getEmail())) {
				sendTemplateEmailService.sendResolvingTemplate(createTicketResponseDto);
			} else if (StringUtils.isNotBlank(createTicketResponseDto.getEmail())
					&& StringUtils.isNotBlank(createTicketResponseDto.getAssignToName())) {
				sendTemplateEmailService.sendUpdatingTemplate(createTicketResponseDto);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception:" + e);
		}
		return Library.getSuccessfulResponse(createTicketResponseDto, ErrorCode.CREATED.getErrorCode(),
				ErrorMessages.RECORED_UPDATED);

	}

	@Override
	@Transactional
	public GenericResponse updatedesc(CreateTicketDashboardDTO requestDTO) {
		Optional<CreateTicketEntity> optional = createTicketRepository.findById(requestDTO.getId());

		if (!optional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}

		CreateTicketEntity entity = optional.get();
		Optional<TicketStatusEntity> ticketOptional = ticketStatusrepository.findById(requestDTO.getTicketStatusId());
		if (!ticketOptional.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { TICKET_STATUS_ID }));
		}

		entity.setTicketStatus(ticketOptional.get());
		entity.setUserRemarks(requestDTO.getUserRemarks());
		entity.setCreatedBy(optional.get().getCreatedBy());
		createTicketRepository.save(entity);
		String comments = requestDTO.getUserRemarks();
		this.saveAuditHistory(entity, HelpDeskTicketAction.UPDATED, entity.getModifiedDate(), comments);
		if (StringUtils.isNotBlank(entity.getMobile())) {
			// this.sendTicketNotification(requestDTO.getTicketStatusId(),
			// entity.getTicketNumber(), entity.getMobile());
		}
		CreateTicketResponseDto createTicketResponseDto = createTicketMapper.convertEntityToResponseDTO(entity);
		if (Constant.CLOSE_TICKET.equals(createTicketResponseDto.getTicketStatus())
				&& StringUtils.isNotBlank(createTicketResponseDto.getEmail())) {
			// currently we dont have any template for close ticket
		} else if (Constant.RESOLVE_TICKET.equals(createTicketResponseDto.getTicketStatus())
				&& StringUtils.isNotBlank(createTicketResponseDto.getEmail())) {
			// sendTemplateEmailService.sendResolvingTemplate(createTicketResponseDto);
		} else if (StringUtils.isNotBlank(createTicketResponseDto.getEmail())
				&& StringUtils.isNotBlank(createTicketResponseDto.getAssignToName())) {
			// sendTemplateEmailService.sendUpdatingTemplate(createTicketResponseDto);
		}

		return Library.getSuccessfulResponse(createTicketResponseDto, ErrorCode.CREATED.getErrorCode(),
				ErrorMessages.RECORED_UPDATED);

	}

	private void saveAuditHistory1(CreateTicketEntity entity, HelpDeskTicketAction action, Date date, String comments) {
		AuthenticationDTO authenticationDTO = SecurityUtils.findAuthenticationObject();
		ZoneId defaultZoneId = ZoneId.systemDefault();
		LocalDateTime localDateTime = date.toInstant().atZone(defaultZoneId).toLocalDateTime();
		Optional<UserEntity> userEntity = null;
		if (authenticationDTO != null) {
			userEntity = userRepository.findById(authenticationDTO.getUserId());
			UserEntity ue = new UserEntity();
			ue.setId(authenticationDTO.getUserId());

			HelpdeskTicketAuditEntity helpdeskTicketAuditEntity = new HelpdeskTicketAuditEntity();
			helpdeskTicketAuditEntity.setAction(action);
			helpdeskTicketAuditEntity.setActionPerformedDateTime(localDateTime);
			helpdeskTicketAuditEntity.setAssignTo(entity.getAssignTo());
			helpdeskTicketAuditEntity.setTicketNumber(entity.getTicketNumber());
			helpdeskTicketAuditEntity.setTicketStatus(entity.getTicketStatus());
			helpdeskTicketAuditEntity.setComments(comments);
			helpDeskTicketAuditRepository.save(helpdeskTicketAuditEntity);
		}
	}

	@Override
	@Transactional
	public GenericResponse updatefielder(CreateTicketDashboardDTO requestDTO) {
		Optional<CreateTicketEntity> optional = createTicketRepository.findById(requestDTO.getId());

		if (!optional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}

		// CreateTicketEntity entity = optional.get();
		CreateTicketEntity entity = createTicketMapper.convertRequestToEntityForUpdate(requestDTO, optional.get());

		String duration = optional.get().getDuration();
		try {
			if (duration != null) {
				if (optional.get().getTicketStatus().getTicketstatusname().equalsIgnoreCase("Closed")
						|| optional.get().getTicketStatus().getTicketstatusname().equalsIgnoreCase("Resolved")) {
					if (duration.equalsIgnoreCase("0")) {
						Optional<DurationDTO> time = createTicketRepository.getByTimestamp(requestDTO.getId());
						entity.setDuration(time.get().getTimestampDiffernce());
					}

				}

			}
		} catch (Exception e) {
			log.info("::Timestamp::Calc" + e);
		}
		entity.setCreatedBy(optional.get().getCreatedBy());
		createTicketRepository.save(entity);
		this.saveAuditHistory(entity, HelpDeskTicketAction.UPDATED, entity.getModifiedDate(), requestDTO.getRemarks());
		if (StringUtils.isNotBlank(entity.getMobile())) {

			this.sendTicketNotification(requestDTO.getTicketStatusId(), entity.getTicketNumber(), entity.getMobile());
		}
		return Library.getSuccessfulResponse(createTicketMapper.convertEntityToResponseDTO(entity),
				ErrorCode.CREATED.getErrorCode(), ErrorMessages.RECORED_UPDATED);

	}

//	Dashboard work

	@Override
	public GenericResponse getCount_percentage(AuthenticationDTO authenticationDTO) {
		final Date finalDate;
		Integer tictotalcount = 0;

		// List<CreateTicketEntity> ticketStatusList1 =
		// createTicketRepository.getAllOfCurrentMonth();

		List<TicketCountResponseDTO> ticketCountResponseDTOList = new ArrayList<>();
		List<TicketCountResponseDTO> ticketCountResponseDTOList1 = new ArrayList<>();
		List<TicketStatusEntity> ticketStatusList = ticketStatusrepository.findAllByStatusOrderByModifiedDateDesc();

		List<TicketStatusEntity> ticketStatusList1 = ticketStatusrepository.findAllByStatusOrderByModifiedDateDesc();

		if (CollectionUtils.isEmpty(ticketStatusList)) {
			throw new RecordNotFoundException("No Record Found");
		}

		ticketStatusList.forEach(ticketStatus -> {
			if (Objects.isNull(ticketStatus.getId()) || StringUtils.isBlank(ticketStatus.getTicketstatusname())) {
				return;
			}
			TicketCountResponseDTO ticketCountResponseDTO = new TicketCountResponseDTO();
			ticketCountResponseDTO.setTotcount(createTicketRepository.getCount());
			int total = ticketCountResponseDTO.getTotcount();
			double totalticket = total;
			System.out.println("Totalticket::" + totalticket);
			ticketCountResponseDTO.setStatus(ticketStatus.getTicketstatusname());
			if (Objects.isNull(authenticationDTO) || Objects.isNull(authenticationDTO.getUserId())) {
				log.error("invalid authentication details : {}", authenticationDTO);
				throw new RecordNotFoundException("No Record Found");
			}

			// ticketCountResponseDTO.setCount(createTicketRepository.getCountByStatusAndCreatedByper(ticketStatus.getId(),
			// authenticationDTO.getUserId()));

			if (authenticationDTO.getRoleCodes().stream().anyMatch(r -> Constant.HELPDESK_ADMIN.equals(r))) {
				ticketCountResponseDTO.setCount(createTicketRepository.getCountBySAdmin(ticketStatus.getId()));
			} else {
				ticketCountResponseDTO.setCount(createTicketRepository.getCountBySAdmin(ticketStatus.getId()));
			}

//			} else if (authenticationDTO.getRoleCodes().stream()
//					.anyMatch(r -> Constant.HELPDESK_EXECUTIVE_ROLE.equals(r))) {
//				ticketCountResponseDTO.setCount(createTicketRepository.getCountByStatusAndCreatedByper(ticketStatus.getId(), authenticationDTO.getUserId()));
//			}
//			
//			else if (authenticationDTO.getRoleCodes().stream()
//					.anyMatch(r -> Constant.HD_SHIFT_SUPERVISOR.equals(r))) {
//				ticketCountResponseDTO.setCount(createTicketRepository.getCountByStatusAndCreatedByper(ticketStatus.getId(), authenticationDTO.getUserId()));
//			}
//			
//			else if (authenticationDTO.getRoleCodes().stream()
//					.anyMatch(r -> Constant.MANAGER.equals(r))) {
//				ticketCountResponseDTO.setCount(createTicketRepository.getCountByStatusAndCreatedByper(ticketStatus.getId(), authenticationDTO.getUserId()));
//			}
//			
//			else if (authenticationDTO.getRoleCodes().stream()
//					.anyMatch(r -> Constant.HD_MANAGER.equals(r))) {
//				ticketCountResponseDTO.setCount(createTicketRepository.getCountByStatusAndCreatedByper(ticketStatus.getId(), authenticationDTO.getUserId()));
//			}

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
			if (Objects.isNull(ticketStatus1.getId()) || StringUtils.isBlank(ticketStatus1.getTicketstatusname())) {
				return;
			}
			TicketCountResponseDTO ticketCountResponseDTO1 = new TicketCountResponseDTO();
			ticketCountResponseDTO1.setTotcount(createTicketRepository.getCount());
			int total = ticketCountResponseDTO1.getTotcount();
			double totalticket = total;
			System.out.println("Totalticket::" + totalticket);
			ticketCountResponseDTO1.setStatus(ticketStatus1.getTicketstatusname());
			if (Objects.isNull(authenticationDTO) || Objects.isNull(authenticationDTO.getUserId())) {
				log.error("invalid authentication details : {}", authenticationDTO);
				throw new RecordNotFoundException("No Record Found");
			}

			// ticketCountResponseDTO.setCount(createTicketRepository.getCountByStatusAndCreatedByper(ticketStatus.getId(),
			// authenticationDTO.getUserId()));

			if (authenticationDTO.getRoleCodes().stream().anyMatch(r -> Constant.HELPDESK_ADMIN.equals(r))) {
				ticketCountResponseDTO1.setCount(createTicketRepository.getCountBySAdmin(ticketStatus1.getId()));

			} else {
				ticketCountResponseDTO1.setCount(createTicketRepository.getCountBySAdmin(ticketStatus1.getId()));
			}

//			else if (authenticationDTO.getRoleCodes().stream()
//					.anyMatch(r -> Constant.HELPDESK_EXECUTIVE_ROLE.equals(r))) {
//				ticketCountResponseDTO1.setCount(createTicketRepository.getCountByStatusAndCreatedByper(ticketStatus1.getId(), authenticationDTO.getUserId()));
//			}
//			
//			else if (authenticationDTO.getRoleCodes().stream()
//					.anyMatch(r -> Constant.HD_SHIFT_SUPERVISOR.equals(r))) {
//				ticketCountResponseDTO1.setCount(createTicketRepository.getCountByStatusAndCreatedByper(ticketStatus1.getId(), authenticationDTO.getUserId()));
//			}
//			
//			else if (authenticationDTO.getRoleCodes().stream()
//					.anyMatch(r -> Constant.MANAGER.equals(r))) {
//				ticketCountResponseDTO1.setCount(createTicketRepository.getCountByStatusAndCreatedByper(ticketStatus1.getId(), authenticationDTO.getUserId()));
//			}
//			
//			else if (authenticationDTO.getRoleCodes().stream()
//					.anyMatch(r -> Constant.HD_MANAGER.equals(r))) {
//				ticketCountResponseDTO1.setCount(createTicketRepository.getCountByStatusAndCreatedByper(ticketStatus1.getId(), authenticationDTO.getUserId()));
//			}

			double earnedcount = (ticketCountResponseDTO1.getCount());
			System.out.println("PERCENTAGE:::" + (earnedcount / totalticket) * 100);
			Double percentage = (earnedcount / totalticket) * 100;
			ticketCountResponseDTO1.setPercentage(percentage);
			ticketCountResponseDTOList1.add(ticketCountResponseDTO1);
		});

		return Library.getSuccessfulResponse(ticketCountResponseDTOList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
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

		List<TicketCountResponseDTO> ticketCountResponseDTOList = new ArrayList<>();

		List<TicketStatusEntity> ticketStatusList = ticketStatusrepository.findAllByStatusOrderByModifiedDateDesc();

		List<TicketStatusEntity> ticketStatusList1 = ticketStatusrepository.findAllByStatusOrderByModifiedDateDesc();

		List<TicketStatusEntity> ticketStatusListApril = ticketStatusrepository
				.findAllByStatusOrderByModifiedDateDesc();

		if (CollectionUtils.isEmpty(ticketStatusList1)) {
			throw new RecordNotFoundException("No Record Found");
		}

		if (CollectionUtils.isEmpty(ticketStatusList)) {
			throw new RecordNotFoundException("No Record Found");
		}

		ticketStatusList.forEach(ticketStatus -> {
			if (Objects.isNull(ticketStatus.getId()) || StringUtils.isBlank(ticketStatus.getTicketstatusname())) {
				return;
			}
			TicketCountResponseDTO ticketCountResponseDTO = new TicketCountResponseDTO();
			// ticketCountResponseDTO.setTotcount(createTicketRepository.getCount());
			// ticketCountResponseDTO.setTotcount(createTicketRepository.getCountover());
			ticketCountResponseDTO.setTotcount(createTicketRepository.getCountF());
			int total = ticketCountResponseDTO.getTotcount();
			double totalticket = total;
			System.out.println("Totalticket::" + totalticket);
			ticketCountResponseDTO.setStatus(ticketStatus.getTicketstatusname());
			if (Objects.isNull(authenticationDTO) || Objects.isNull(authenticationDTO.getUserId())) {
				log.error("invalid authentication details : {}", authenticationDTO);
				throw new RecordNotFoundException("No Record Found");
			}

			// ticketCountResponseDTO.setCount(createTicketRepository.getCountByStatusAndCreatedBy1(ticketStatus.getId(),
			// authenticationDTO.getUserId()));
			if (authenticationDTO.getRoleCodes().stream().anyMatch(r -> Constant.HELPDESK_ADMIN.equals(r))) {
				ticketCountResponseDTO
						.setCount(createTicketRepository.getCountByStatusAndHelpdesk(ticketStatus.getId()));
			}

			else {
				ticketCountResponseDTO
						.setCount(createTicketRepository.getCountByStatusAndHelpdesk(ticketStatus.getId()));
			}

//			else if (authenticationDTO.getRoleCodes().stream()
//					.anyMatch(r -> Constant.HELPDESK_EXECUTIVE_ROLE.equals(r))) {
//				ticketCountResponseDTO.setCount(createTicketRepository.getCountByStatusAndCreatedBy1(ticketStatus.getId(), authenticationDTO.getUserId()));
//			} 
//			else if (authenticationDTO.getRoleCodes().stream()
//					.anyMatch(r -> Constant.HD_MANAGER.equals(r))) {
//				ticketCountResponseDTO.setCount(createTicketRepository.getCountByStatusAndCreatedBy1(ticketStatus.getId(), authenticationDTO.getUserId()));
//			} 
//			else if (authenticationDTO.getRoleCodes().stream()
//					.anyMatch(r -> Constant.HD_SHIFT_SUPERVISOR.equals(r))) {
//				ticketCountResponseDTO.setCount(createTicketRepository.getCountByStatusAndCreatedBy1(ticketStatus.getId(), authenticationDTO.getUserId()));
//			} 
//			
//			else if (authenticationDTO.getRoleCodes().stream()
//					.anyMatch(r -> Constant.MANAGER.equals(r))) {
//				ticketCountResponseDTO.setCount(createTicketRepository.getCountByStatusAndCreatedBy1(ticketStatus.getId(), authenticationDTO.getUserId()));
//			} 

			double earnedcount = (ticketCountResponseDTO.getCount());
			System.out.println("PERCENTAGE:::" + (earnedcount / totalticket) * 100);
			Double percentage = (earnedcount / totalticket) * 100;
			ticketCountResponseDTO.setMonth("November");
			ticketCountResponseDTO.setPercentage(percentage);
			ticketCountResponseDTOList.add(ticketCountResponseDTO);

		});

		ticketStatusListApril.forEach(ticketStatus2 -> {
			if (Objects.isNull(ticketStatus2.getId()) || StringUtils.isBlank(ticketStatus2.getTicketstatusname())) {
				return;
			}
			TicketCountResponseDTO ticketCountResponseDTO2 = new TicketCountResponseDTO();
			ticketCountResponseDTO2.setTotcount(createTicketRepository.getCountapril());
			int total1 = ticketCountResponseDTO2.getTotcount();
			double totalticket1 = total1;
			System.out.println("Totalticket::" + totalticket1);
			ticketCountResponseDTO2.setStatus(ticketStatus2.getTicketstatusname());
			if (Objects.isNull(authenticationDTO) || Objects.isNull(authenticationDTO.getUserId())) {
				log.error("invalid authentication details : {}", authenticationDTO);
				throw new RecordNotFoundException("No Record Found");
			}
			ticketCountResponseDTO2.setCount(createTicketRepository
					.getCountByStatusAndCreatedByApril(ticketStatus2.getId(), authenticationDTO.getUserId()));

			if (authenticationDTO.getRoleCodes().stream().anyMatch(r -> Constant.HELPDESK_ADMIN.equals(r))) {
				ticketCountResponseDTO2
						.setCount(createTicketRepository.getCountByFebHelpdeskapril(ticketStatus2.getId()));
			}

			else {
				ticketCountResponseDTO2
						.setCount(createTicketRepository.getCountByFebHelpdeskapril(ticketStatus2.getId()));
			}

//			else if (authenticationDTO.getRoleCodes().stream()
//					.anyMatch(r -> Constant.HELPDESK_EXECUTIVE_ROLE.equals(r))) {
//				ticketCountResponseDTO2.setCount(createTicketRepository.getCountByStatusAndCreatedByApex(ticketStatus2.getId(), authenticationDTO.getUserId()));
//			} 
//			else if (authenticationDTO.getRoleCodes().stream()
//					.anyMatch(r -> Constant.HD_MANAGER.equals(r))) {
//				ticketCountResponseDTO2.setCount(createTicketRepository.getCountByStatusAndCreatedBy1(ticketStatus2.getId(), authenticationDTO.getUserId()));
//			} 
//			else if (authenticationDTO.getRoleCodes().stream()
//					.anyMatch(r -> Constant.HD_SHIFT_SUPERVISOR.equals(r))) {
//				ticketCountResponseDTO2.setCount(createTicketRepository.getCountByStatusAndCreatedBy1(ticketStatus2.getId(), authenticationDTO.getUserId()));
//			} 
//			
//			else if (authenticationDTO.getRoleCodes().stream()
//					.anyMatch(r -> Constant.MANAGER.equals(r))) {
//				ticketCountResponseDTO2.setCount(createTicketRepository.getCountByStatusAndCreatedBy1(ticketStatus2.getId(), authenticationDTO.getUserId()));
//			} 
//			

			double earnedcount = (ticketCountResponseDTO2.getCount());
			System.out.println("PERCENTAGE:::" + (earnedcount / totalticket1) * 100);
			Double percentage = (earnedcount / totalticket1) * 100;
			ticketCountResponseDTO2.setPercentage(percentage);
			ticketCountResponseDTO2.setMonth("December");
			ticketCountResponseDTOList.add(ticketCountResponseDTO2);
		});

		ticketStatusList1.forEach(ticketStatus1 -> {
			if (Objects.isNull(ticketStatus1.getId()) || StringUtils.isBlank(ticketStatus1.getTicketstatusname())) {
				return;
			}
			TicketCountResponseDTO ticketCountResponseDTO1 = new TicketCountResponseDTO();
			ticketCountResponseDTO1.setTotcount(createTicketRepository.getCountfeb());
			int total1 = ticketCountResponseDTO1.getTotcount();
			double totalticket1 = total1;
			System.out.println("Totalticket::" + totalticket1);
			ticketCountResponseDTO1.setStatus(ticketStatus1.getTicketstatusname());
			if (Objects.isNull(authenticationDTO) || Objects.isNull(authenticationDTO.getUserId())) {
				log.error("invalid authentication details : {}", authenticationDTO);
				throw new RecordNotFoundException("No Record Found");
			}
			ticketCountResponseDTO1.setCount(createTicketRepository.getCountByStatusAndCreatedBy(ticketStatus1.getId(),
					authenticationDTO.getUserId()));

			if (authenticationDTO.getRoleCodes().stream().anyMatch(r -> Constant.HELPDESK_ADMIN.equals(r))) {
				ticketCountResponseDTO1.setCount(createTicketRepository.getCountByFebHelpdesk(ticketStatus1.getId()));
			} else {
				ticketCountResponseDTO1.setCount(createTicketRepository.getCountByFebHelpdesk(ticketStatus1.getId()));
			}
//			else if (authenticationDTO.getRoleCodes().stream()
//					.anyMatch(r -> Constant.HELPDESK_EXECUTIVE_ROLE.equals(r))) {
//				ticketCountResponseDTO1.setCount(createTicketRepository.getCountByStatusAndCreatedBy(ticketStatus1.getId(), authenticationDTO.getUserId()));
//			} 
//			else if (authenticationDTO.getRoleCodes().stream()
//					.anyMatch(r -> Constant.HD_MANAGER.equals(r))) {
//				ticketCountResponseDTO1.setCount(createTicketRepository.getCountByStatusAndCreatedBy1(ticketStatus1.getId(), authenticationDTO.getUserId()));
//			} 
//			else if (authenticationDTO.getRoleCodes().stream()
//					.anyMatch(r -> Constant.HD_SHIFT_SUPERVISOR.equals(r))) {
//				ticketCountResponseDTO1.setCount(createTicketRepository.getCountByStatusAndCreatedBy1(ticketStatus1.getId(), authenticationDTO.getUserId()));
//			} 
//			
//			else if (authenticationDTO.getRoleCodes().stream()
//					.anyMatch(r -> Constant.MANAGER.equals(r))) {
//				ticketCountResponseDTO1.setCount(createTicketRepository.getCountByStatusAndCreatedBy1(ticketStatus1.getId(), authenticationDTO.getUserId()));
//			} 

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

	@Override
	public GenericResponse getByStatusName(String name, String licenseNumber) {
		List<CreateTicketEntity> list = null;
		if (Objects.nonNull(name) && name.equalsIgnoreCase("Open")) {
			list = createTicketRepository.findByTicketStatusOpen(licenseNumber.toUpperCase());
		}
		if (Objects.nonNull(name) && name.equalsIgnoreCase("Pending")) {
			list = createTicketRepository.findByTicketStatusPending(licenseNumber.toUpperCase());
		}
		if (Objects.nonNull(name) && name.equalsIgnoreCase("Closed")) {
			list = createTicketRepository.findByTicketStatusClosed(licenseNumber.toUpperCase());
		}
		if (Objects.nonNull(name) && name.equalsIgnoreCase("Reopen")) {
			list = createTicketRepository.findByTicketStatusReopen(licenseNumber.toUpperCase());
		}
		if (Objects.nonNull(name) && name.equalsIgnoreCase("Escalated")) {
			list = createTicketRepository.findByTicketStatusEscalated(licenseNumber.toUpperCase());
		}
		if (Objects.nonNull(name) && name.equalsIgnoreCase("Resolved")) {
			list = createTicketRepository.findByTicketStatusResolved(licenseNumber.toUpperCase());
		}

		if (Objects.nonNull(name) && name.equalsIgnoreCase("Change Request")) {
			list = createTicketRepository.findByTicketStatusChangeRequest(licenseNumber.toUpperCase());
		}

		if (CollectionUtils.isEmpty(list)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<CreateTicketResponseDto> responseDto = list.stream().map(createTicketMapper::convertEntityToResponseDTO)
				.collect(Collectors.toList());
		return Library.getSuccessfulResponse(responseDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	// @Scheduled(cron="0 0 0 * * ?")
	@Scheduled(cron = "0 */30 * * * ?")
	@Override
	@Transactional
	public GenericResponse updateAssigneeForSLAExpiredTicket() {
		List<String> ticketStatusList = new ArrayList<>();
		ticketStatusList.add("Open");
		ticketStatusList.add("Pending");
		List<Long> ticketStatusIds = ticketStatusrepository.findByTicketStatusNames(ticketStatusList);
		if (CollectionUtils.isEmpty(ticketStatusIds)) {
			log.info("ticket status data not found");
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<CreateTicketEntity> ticketEntityList = createTicketRepository.findByFilter(ticketStatusIds);
		if (CollectionUtils.isEmpty(ticketEntityList)) {
			log.info("No data found in ticket table");
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		ticketEntityList.forEach(entity -> {
			if (Objects.isNull(entity.getIssueMaster()) || Objects.isNull(entity.getSlaMaster())) {
				// log.info("either issue detail or sla detail missing : {}", entity.getId());
				return;
			}
			log.info("No data found in ticket table=:: {}, {}", entity.getIssueMaster().getId(),
					entity.getSlaMaster().getId());
			List<WorkflowEntity> workFlowEntityList = workflowRepository
					.getBySlaIdAndIssueDetailId(entity.getIssueMaster().getId(), entity.getSlaMaster().getId());
			if (CollectionUtils.isEmpty(workFlowEntityList)) {
				// log.info("workflow not configured : {}", entity.getId());
				return;
			}
			if (workFlowEntityList.size() > 1) {
				// log.info("multiple workflow configurations found : {}", entity.getId());
				return;
			}
			if (Objects.isNull(workFlowEntityList.get(0).getAssignTo())) {
				// log.info("assignTo is null in workflow : {}", entity.getId());
				return;
			}
//			Optional<UserEntity> userEntity = userRepository.findById(workFlowEntityList.get(0).getAssignTo().getId());
//			if (userEntity.isPresent() && !CollectionUtils.isEmpty(userEntity.get().getRoles()) && Objects.nonNull(workFlowEntityList.get(0).getAssignTo().getId())) {
//				entity.setAssignTo(userEntity.get());
//				entity.setAssignGroup(userEntity.get().getRoles().get(0));
//				createTicketRepository.save(entity);
//				log.info("ticket assignee updated : {}", entity.getId());
//				this.saveAuditHistory(entity, HelpDeskTicketAction.UPDATED, entity.getModifiedDate());
//			}

			UserEntity userEntity = workFlowEntityList.get(0).getAssignTo();
			if (Objects.nonNull(userEntity) && !CollectionUtils.isEmpty(userEntity.getRoles())) {
				entity.setAssignTo(userEntity);
				entity.setAssignGroup(userEntity.getRoles().get(0));

				Optional<TicketStatusEntity> ticketStatusEntity = ticketStatusrepository
						.findByTicketstatusnameIgnoreCase("Escalated");
				if (ticketStatusEntity.isPresent()) {
					entity.setTicketStatus(ticketStatusEntity.get());
				}

				createTicketRepository.save(entity);
				// log.info("ticket assignee updated : {}", entity.getId());
				// this.saveAuditHistory(entity, HelpDeskTicketAction.UPDATED,
				// entity.getModifiedDate(), Constant.AUTO_ESCALATED);
			}

		});
		return Library.getSuccessfulResponse(null, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORD_PROCESSED_SUCCESSFULLY);
	}

	public static void main(String[] args) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd ");
		Date date = new Date();
		System.out.println(formatter.format(date));

	}
//old code
//	@Override
//	public GenericResponse viewReport(PaginationRequestDTO paginationDto, AuthenticationDTO authenticationDTO)
//			throws ParseException {
//		Pageable pageable = null;
//		Page<CreateTicketEntity> list = null;
//		Long categoryId = null;
//		String issueDetails = null;
//		Long subCategoryId = null;
//		Long status = null;
//		Long issuefromid = null;
//		Long category = null;
//		Long subcategory = null;
//		String licencetype = null;
//		String licencenumber = null;
//		Long priorityid = null;
//
//		String ticketNumber = null;
//
//		String commonsearch = null;
//		ArrayList<Long> statusId = new ArrayList<Long>();
//
//		/*
//		 * if(StringUtils.isNotBlank(paginationDto.getSortField())) {
//		 * if(CATEGORYID.equals(paginationDto.getSortField())) {
//		 * paginationDto.setSortField(CATEGORY); }
//		 * if(SUB_CATEGORY_ID.equals(paginationDto.getSortField())) {
//		 * paginationDto.setSortField(SUBCATEGORY); }
//		 * if(ISSUE_DETAILS.equals(paginationDto.getSortField())) {
//		 * paginationDto.setSortField(ISSUED); } }
//		 */
//		if (paginationDto.getSortOrder().equalsIgnoreCase(ASC)) {
//			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
//					Sort.by(Direction.ASC, paginationDto.getSortField()));
//		} else {
//			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
//					Sort.by(Direction.DESC, paginationDto.getSortField()));
//		}
//		if (Objects.nonNull(paginationDto.getFilters())) {
//			if (Objects.nonNull(paginationDto.getFilters().get(STATUS))
//					&& !paginationDto.getFilters().get(STATUS).toString().trim().isEmpty()) {
//				status = Long.valueOf(paginationDto.getFilters().get(STATUS).toString());
//				Long obj1 = new Long(status);
//				Long obj2 = new Long(57);
////				if (obj1.equals(obj2)) {
////					statusId.add(status);
////					statusId.add((long) 59);
////					statusId.add((long) 61);
////				} else {
//				statusId.add(status);
//
////				}
//			}
//		}
//
////		if (Objects.nonNull(paginationDto.getFilters())) {
////			if (Objects.nonNull(paginationDto.getFilters().get("statusId"))
////					&& !paginationDto.getFilters().get("statusId").toString().trim().isEmpty()) {
////				statusId = (ArrayList<Long>) (paginationDto.getFilters().get("statusId"));
////				statusId.add((long) 59);
////				statusId.add((long) 61);
////			}
////		}
//
//		// newly added
//
//		if (Objects.nonNull(paginationDto.getFilters())) {
//			if (Objects.nonNull(paginationDto.getFilters().get("categoryId"))
//					&& !paginationDto.getFilters().get("categoryId").toString().trim().isEmpty()) {
//				categoryId = Long.valueOf(paginationDto.getFilters().get("categoryId").toString());
//			}
//		}
//
//		if (Objects.nonNull(paginationDto.getFilters())) {
//			if (Objects.nonNull(paginationDto.getFilters().get("subCategoryId"))
//					&& !paginationDto.getFilters().get("subCategoryId").toString().trim().isEmpty()) {
//				subCategoryId = Long.valueOf(paginationDto.getFilters().get("subCategoryId").toString());
//			}
//		}
//
//		if (Objects.nonNull(paginationDto.getFilters())) {
//			if (Objects.nonNull(paginationDto.getFilters().get("issueFromId"))
//					&& !paginationDto.getFilters().get("issueFromId").toString().trim().isEmpty()) {
//				issuefromid = Long.valueOf(paginationDto.getFilters().get("issueFromId").toString());
//			}
//		}
//
//		if (Objects.nonNull(paginationDto.getFilters())) {
//			if (Objects.nonNull(paginationDto.getFilters().get("licenseTypeId"))
//					&& !paginationDto.getFilters().get("licenseTypeId").toString().trim().isEmpty()) {
//				licencetype = String.valueOf(paginationDto.getFilters().get("licenseTypeId").toString());
//			}
//		}
//
//		if (Objects.nonNull(paginationDto.getFilters())) {
//			if (Objects.nonNull(paginationDto.getFilters().get("licencenumber"))
//					&& !paginationDto.getFilters().get("licencenumber").toString().trim().isEmpty()) {
//				licencenumber = String.valueOf(paginationDto.getFilters().get("licencenumber").toString());
//			}
//		}
//
//		if (Objects.nonNull(paginationDto.getFilters())) {
//			if (Objects.nonNull(paginationDto.getFilters().get("priorityId"))
//					&& !paginationDto.getFilters().get("priorityId").toString().trim().isEmpty()) {
//				priorityid = Long.valueOf(paginationDto.getFilters().get("priorityId").toString());
//			}
//		}
//
//		if (Objects.nonNull(paginationDto.getFilters())) {
//			if (Objects.nonNull(paginationDto.getFilters().get("commonSearch"))
//					&& !paginationDto.getFilters().get("commonSearch").toString().trim().isEmpty()) {
//				commonsearch = String.valueOf(paginationDto.getFilters().get("commonSearch").toString());
//			}
//		}
//
//		if (Objects.nonNull(paginationDto.getFilters())) {
//			if (Objects.nonNull(paginationDto.getFilters().get("ticketNumber"))
//					&& !paginationDto.getFilters().get("ticketNumber").toString().trim().isEmpty()) {
//				ticketNumber = String.valueOf(paginationDto.getFilters().get("ticketNumber").toString());
//			}
//		}
//
//		Date fromDate = new SimpleDateFormat(DATE_FORMAT)
//				.parse(paginationDto.getFilters().get("fromDate").toString() + " " + START_TIME);
//		Date toDate = new SimpleDateFormat(DATE_FORMAT)
//				.parse(paginationDto.getFilters().get("toDate").toString() + " " + END_TIME);
//		list = getByFilter(fromDate, toDate, status, categoryId, subCategoryId, issuefromid, licencetype, licencenumber,
//				priorityid, ticketNumber, commonsearch, statusId, pageable);
//		if (Objects.isNull(list)) {
//			list = createTicketRepository.getAll(pageable);
//		}
//		if (Objects.isNull(list) || list.isEmpty()) {
//			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
//					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
//		}
//		Page<CreateTicketResponseDto> finalResponse = list.map(createTicketMapper::convertEntityToResponseDTO);
//		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse),
//				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
//	}

//	private Page<CreateTicketEntity> getByFilter(Date fromDate, Date toDate, Long status, Long categoryId,
//			Long subCategoryId, Long issuefromid, String licencetype, String licencenumber, Long priorityid,
//			String ticketNumber, String commonsearch, List<Long> statusId, Pageable pageable) {
//		Page<CreateTicketEntity> list = null;
//
//		if (Objects.isNull(list) && Objects.nonNull(status) && Objects.nonNull(fromDate) && Objects.nonNull(toDate)
//				&& Objects.nonNull(categoryId) && Objects.isNull(subCategoryId) && Objects.nonNull(commonsearch)) {
//			list = createTicketRepository.getByCreatedDateStatusAndCategoryAll(fromDate, toDate, statusId, categoryId,
//					commonsearch, commonsearch, commonsearch, pageable);
//		}
//
//		if (Objects.isNull(list) && Objects.nonNull(status) && Objects.nonNull(fromDate) && Objects.nonNull(toDate)
//				&& Objects.nonNull(categoryId) && Objects.isNull(subCategoryId) && Objects.isNull(commonsearch)) {
//			list = createTicketRepository.getByCreatedDateAndStatusAndCategoryStage(fromDate, toDate, statusId,
//					categoryId, pageable);
//		}
//
//		if (Objects.isNull(list) && Objects.isNull(status) && Objects.nonNull(fromDate) && Objects.nonNull(toDate)
//				&& Objects.isNull(categoryId) && Objects.isNull(subCategoryId) && Objects.nonNull(commonsearch)) {
//			list = createTicketRepository.getByCreatedDateStatusAll(fromDate, toDate, commonsearch, commonsearch,
//					commonsearch, pageable);
//		}
//
//		if (Objects.isNull(list) && Objects.nonNull(status) && Objects.nonNull(fromDate) && Objects.nonNull(toDate)
//				&& Objects.nonNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.nonNull(issuefromid)
//				&& Objects.nonNull(licencetype) && Objects.nonNull(licencenumber) && Objects.nonNull(priorityid)
//				&& Objects.nonNull(ticketNumber)) {
//			list = createTicketRepository.getByCreatedDateStatusAll(fromDate, toDate, statusId, categoryId,
//					subCategoryId, issuefromid, licencetype, licencenumber, priorityid, ticketNumber, pageable);
//		}
//
//		if (Objects.isNull(list) && Objects.isNull(status) && Objects.nonNull(fromDate) && Objects.nonNull(toDate)
//				&& Objects.nonNull(categoryId) && Objects.isNull(subCategoryId) && Objects.isNull(issuefromid)
//				&& Objects.isNull(licencetype) && Objects.isNull(licencenumber) && Objects.isNull(priorityid)
//				&& Objects.isNull(ticketNumber)) {
//			list = createTicketRepository.getByCreatedDateAndCategory(fromDate, toDate, categoryId, pageable);
//		}
//
//		// if(Objects.isNull(list) && Objects.isNull(status) &&
//		// Objects.nonNull(fromDate) && Objects.nonNull(toDate) &&
//		// Objects.nonNull(categoryId) && Objects.isNull(subCategoryId) &&
//		// Objects.isNull(issuefromid) && Objects.isNull(licencetype) &&
//		// Objects.isNull(licencenumber)&& Objects.isNull(priorityid)&&
//		// Objects.isNull(ticketNumber)) {
//		// list =
//		// createTicketRepository.getByCreatedDateAndCategory(fromDate,toDate,categoryId,pageable);
//		// }
//
//		if (Objects.isNull(list) && Objects.isNull(status) && Objects.nonNull(fromDate) && Objects.nonNull(toDate)
//				&& Objects.isNull(categoryId) && Objects.nonNull(subCategoryId) && Objects.isNull(issuefromid)
//				&& Objects.isNull(licencetype) && Objects.isNull(licencenumber) && Objects.isNull(priorityid)
//				&& Objects.isNull(ticketNumber)) {
//			list = createTicketRepository.getByCreatedDateAndSubcategory(fromDate, toDate, subCategoryId, pageable);
//		}
//
//		if (Objects.isNull(list) && Objects.isNull(status) && Objects.nonNull(fromDate) && Objects.nonNull(toDate)
//				&& Objects.isNull(categoryId) && Objects.isNull(subCategoryId) && Objects.nonNull(issuefromid)
//				&& Objects.isNull(licencetype) && Objects.isNull(licencenumber) && Objects.isNull(priorityid)
//				&& Objects.isNull(ticketNumber)) {
//			list = createTicketRepository.getByCreatedDateAndIssuefromId(fromDate, toDate, issuefromid, pageable);
//		}
//
//		if (Objects.isNull(list) && Objects.isNull(status) && Objects.nonNull(fromDate) && Objects.nonNull(toDate)
//				&& Objects.isNull(categoryId) && Objects.isNull(subCategoryId) && Objects.isNull(issuefromid)
//				&& Objects.nonNull(licencetype) && Objects.isNull(licencenumber) && Objects.isNull(priorityid)
//				&& Objects.isNull(ticketNumber)) {
//			list = createTicketRepository.getByCreatedDateAndLicenceTypeId(fromDate, toDate, licencetype, pageable);
//		}
//
//		if (Objects.isNull(list) && Objects.isNull(status) && Objects.nonNull(fromDate) && Objects.nonNull(toDate)
//				&& Objects.isNull(categoryId) && Objects.isNull(subCategoryId) && Objects.isNull(issuefromid)
//				&& Objects.isNull(licencetype) && Objects.nonNull(licencenumber) && Objects.isNull(priorityid)
//				&& Objects.isNull(ticketNumber)) {
//			list = createTicketRepository.getByCreatedDateAndLicenceNumber(fromDate, toDate, licencenumber, pageable);
//		}
//
//		if (Objects.isNull(list) && Objects.isNull(status) && Objects.nonNull(fromDate) && Objects.nonNull(toDate)
//				&& Objects.isNull(categoryId) && Objects.isNull(subCategoryId) && Objects.isNull(issuefromid)
//				&& Objects.isNull(licencetype) && Objects.isNull(licencenumber) && Objects.nonNull(priorityid)
//				&& Objects.isNull(ticketNumber)) {
//			list = createTicketRepository.getByCreatedDateAndPriority(fromDate, toDate, priorityid, pageable);
//		}
//
//		if (Objects.isNull(list) && Objects.isNull(status) && Objects.nonNull(fromDate) && Objects.nonNull(toDate)
//				&& Objects.isNull(categoryId) && Objects.isNull(subCategoryId) && Objects.isNull(issuefromid)
//				&& Objects.isNull(licencetype) && Objects.isNull(licencenumber) && Objects.isNull(priorityid)
//				&& Objects.nonNull(ticketNumber)) {
//			list = createTicketRepository.getByCreatedDateAndTicketNumber(fromDate, toDate, ticketNumber, pageable);
//		}
//
//		if (Objects.isNull(list) && Objects.nonNull(status) && Objects.isNull(fromDate) && Objects.isNull(toDate)
//				&& Objects.isNull(categoryId)) {
//			list = createTicketRepository.getByStatus(statusId, pageable);
//		}
//
//		if (Objects.isNull(list) && Objects.nonNull(status) && Objects.nonNull(fromDate) && Objects.nonNull(toDate)
//				&& Objects.isNull(categoryId)) {
//			list = createTicketRepository.getByCreatedDateAndStatus(fromDate, toDate, statusId, pageable);
//		}
//
//		if (Objects.isNull(list) && Objects.isNull(status) && Objects.nonNull(fromDate) && Objects.nonNull(toDate)
//				&& Objects.isNull(categoryId)) {
//			list = createTicketRepository.getByCreatedDate(fromDate, toDate, pageable);
//		}
//
//		if (Objects.isNull(list) && Objects.nonNull(status) && Objects.nonNull(fromDate) && Objects.nonNull(toDate)
//				&& Objects.nonNull(categoryId)) {
//			list = createTicketRepository.getByCreatedDateAndStatusPageAndCategory(fromDate, toDate, statusId,
//					categoryId, pageable);
//		}
//
//		return list;
//	}
//old code end

	// new code
	@Override
	public GenericResponse viewReport(PaginationRequestDTO paginationDto, AuthenticationDTO authenticationDTO)
			throws ParseException {
		Pageable pageable = null;
		Page<CreateTicketEntity> list = null;
		Long categoryId = null;
		String issueDetails = null;
		Long subCategoryId = null;
		Long status = null;
		Long issuefromid = null;
		Long category = null;
		Long subcategory = null;
		String licencetype = null;
		String licencenumber = null;
		Long priorityid = null;
		String ticketNumber = null;
		String commonsearch = null;
		String entityTypeId = null;
		Long ticketStatus = null;
		Long issueDetailsId = null;
		String issueTypeSH = null;

		/*
		 * if(StringUtils.isNotBlank(paginationDto.getSortField())) {
		 * if(CATEGORYID.equals(paginationDto.getSortField())) {
		 * paginationDto.setSortField(CATEGORY); }
		 * if(SUB_CATEGORY_ID.equals(paginationDto.getSortField())) {
		 * paginationDto.setSortField(SUBCATEGORY); }
		 * if(ISSUE_DETAILS.equals(paginationDto.getSortField())) {
		 * paginationDto.setSortField(ISSUED); } }
		 */
		if (paginationDto.getSortOrder().equalsIgnoreCase(ASC)) {
			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
					Sort.by(Direction.ASC, paginationDto.getSortField()));
		} else {
			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
					Sort.by(Direction.DESC, paginationDto.getSortField()));
		}

		// newly added

		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("category"))
					&& !paginationDto.getFilters().get("category").toString().trim().isEmpty()) {
				category = Long.valueOf(paginationDto.getFilters().get("category").toString());
			}
		}

		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("ticketStatus"))
					&& !paginationDto.getFilters().get("ticketStatus").toString().trim().isEmpty()) {
				ticketStatus = Long.valueOf(paginationDto.getFilters().get("ticketStatus").toString());
			}
		}

		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("subCategoryId"))
					&& !paginationDto.getFilters().get("subCategoryId").toString().trim().isEmpty()) {
				subCategoryId = Long.valueOf(paginationDto.getFilters().get("subCategoryId").toString());
			}
		}

		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("issueFromId"))
					&& !paginationDto.getFilters().get("issueFromId").toString().trim().isEmpty()) {
				issuefromid = Long.valueOf(paginationDto.getFilters().get("issueFromId").toString());
			}
		}

		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("licenseTypeId"))
					&& !paginationDto.getFilters().get("licenseTypeId").toString().trim().isEmpty()) {
				licencetype = String.valueOf(paginationDto.getFilters().get("licenseTypeId").toString());
			}
		}

		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("licencenumber"))
					&& !paginationDto.getFilters().get("licencenumber").toString().trim().isEmpty()) {
				licencenumber = String.valueOf(paginationDto.getFilters().get("licencenumber").toString());
			}
		}

		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("priorityId"))
					&& !paginationDto.getFilters().get("priorityId").toString().trim().isEmpty()) {
				priorityid = Long.valueOf(paginationDto.getFilters().get("priorityId").toString());
			}
		}

		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("commonSearch"))
					&& !paginationDto.getFilters().get("commonSearch").toString().trim().isEmpty()) {
				commonsearch = String.valueOf(paginationDto.getFilters().get("commonSearch").toString());
			}
		}

		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("ticketNumber"))
					&& !paginationDto.getFilters().get("ticketNumber").toString().trim().isEmpty()) {
				ticketNumber = String.valueOf(paginationDto.getFilters().get("ticketNumber").toString());
			}
		}

		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("entityTypeId"))
					&& !paginationDto.getFilters().get("entityTypeId").toString().trim().isEmpty()) {
				entityTypeId = String.valueOf(paginationDto.getFilters().get("entityTypeId").toString());
			}
		}

		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("issueDetailsId"))
					&& !paginationDto.getFilters().get("issueDetailsId").toString().trim().isEmpty()) {
				issueDetailsId = Long.valueOf(paginationDto.getFilters().get("issueDetailsId").toString());
			}
		}

		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("issueTypeSH"))
					&& !paginationDto.getFilters().get("issueTypeSH").toString().trim().isEmpty()) {
				issueTypeSH = String.valueOf(paginationDto.getFilters().get("issueTypeSH").toString());
			}
		}

		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.isNull(paginationDto.getFilters().get("fromDate"))
					&& Objects.isNull(paginationDto.getFilters().get("toDate"))) {
				Date fromDate = null;
				Date toDate = null;
				list = getByFilter(fromDate, toDate, category, commonsearch, entityTypeId, ticketStatus, issueTypeSH,
						pageable);
			} else {
				Date fromDate = new SimpleDateFormat(DATE_FORMAT)
						.parse(paginationDto.getFilters().get("fromDate").toString() + " " + START_TIME);
				Date toDate = new SimpleDateFormat(DATE_FORMAT)
						.parse(paginationDto.getFilters().get("toDate").toString() + " " + END_TIME);
				list = getByFilter(fromDate, toDate, category, commonsearch, entityTypeId, ticketStatus, issueTypeSH,
						pageable);
			}
		}

		if (Objects.isNull(list)) {
			list = createTicketRepository.getAll(pageable);
		}
		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		Page<CreateTicketResponseDto> finalResponse = list.map(createTicketMapper::convertEntityToResponseDTO);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}

	private Page<CreateTicketEntity> getByFilter(Date fromDate, Date toDate, Long category, String commonsearch,
			String entityTypeId, Long ticketStatus, String issueTypeSH, Pageable pageable) {
		Page<CreateTicketEntity> list = null;

		list = createTicketRepository.findTickets(fromDate, toDate, category, commonsearch, entityTypeId, ticketStatus,
				issueTypeSH, pageable);

		return list;
	}

	@Override
	public GenericResponse getByTicketNumber(String ticketNumber) {
		Optional<CreateTicketEntity> entity = createTicketRepository.findByTicketNumberIgnoreCase(ticketNumber);

		if (!entity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(createTicketMapper.convertEntityToResponseDTO(entity.get()),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}

	private void saveAuditHistory(CreateTicketEntity entity, HelpDeskTicketAction action, Date date, String comments) {
		AuthenticationDTO authenticationDTO = SecurityUtils.findAuthenticationObject();
		ZoneId defaultZoneId = ZoneId.systemDefault();
		LocalDateTime localDateTime = date.toInstant().atZone(defaultZoneId).toLocalDateTime();
		Optional<UserEntity> userEntity = null;
		if (authenticationDTO != null) {
			userEntity = userRepository.findById(authenticationDTO.getUserId());
		} else {
			userEntity = null;
		}
		HelpdeskTicketAuditEntity helpdeskTicketAuditEntity = new HelpdeskTicketAuditEntity(
				Objects.nonNull(userEntity) ? userEntity.get() : null, action, entity.getTicketNumber(),
				entity.getTicketStatus(), localDateTime, comments, entity.getAssignTo());
		helpDeskTicketAuditRepository.save(helpdeskTicketAuditEntity);
	}

	@Override
	public GenericResponse getTicketByLicenseNumber(String licenseNumber) {
		List<CreateTicketEntity> entity = new ArrayList<CreateTicketEntity>();

		entity = createTicketRepository.findByLicenceNumberIgnoreCase(licenseNumber);

		if (CollectionUtils.isEmpty(entity)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}

		List<CreateTicketResponseDto> responseDto = entity.stream().map(createTicketMapper::convertEntityToResponseDTO)
				.collect(Collectors.toList());
		return Library.getSuccessfulResponse(responseDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	public void sendTicketNotification(Long ticketStatusId, String ticketNumber, String phoneNumber) {
		Optional<ApplicationConstantEntity> applicationConstantEntity = applicationConstantRepository
				.findByCodeIgnoreCase(Constant.SMS_SERVICE_ENABLED);
		if (!applicationConstantEntity.isPresent() || Boolean.FALSE.equals(applicationConstantEntity.get().getStatus())
				|| !Constant.TRUE.equalsIgnoreCase(applicationConstantEntity.get().getValue())) {
			log.info(ErrorMessages.SMS_SERVICE_NOT_ENABLED);
			return;
		}

		Optional<TemplateEntity> templateEntity = null;
		Optional<TicketStatusEntity> ticketStatusEntity = ticketStatusrepository.findById(ticketStatusId);
		if (!ticketStatusEntity.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { TICKET_STATUS_ID }));
		}
		if (ticketStatusEntity.get().getTicketstatusname().contentEquals("Open")
				|| ticketStatusEntity.get().getTicketstatusname().contentEquals("Assigned")) {
			templateEntity = templateRepository.findByCode(TemplateCode.TICKET_CREATION.toString());
		} else if (ticketStatusEntity.get().getTicketstatusname().contentEquals("Reopen")) {
			templateEntity = templateRepository.findByCode(TemplateCode.TICKET_REOPENED.toString());
		} else if (ticketStatusEntity.get().getTicketstatusname().contentEquals("Resolved")) {
			templateEntity = templateRepository.findByCode(TemplateCode.TICKET_RESOLVED.toString());
		} else if (ticketStatusEntity.get().getTicketstatusname().contentEquals("Closed")) {
			templateEntity = templateRepository.findByCode(TemplateCode.TICKET_CLOSED.toString());
		}
		if (Objects.isNull(templateEntity)) {
			log.error("Template not configured for ticket status id : {}, ", ticketStatusId);
		} else {
			String content = templateEntity.get().getContent();
			content = content.replace("<<TICKET_NUMBER>>", ticketNumber);
			SMSDetails smsDetails = new SMSDetails();
			smsDetails.setEvent(Constant.HELPDESK);
			smsDetails.setSubEvent(Constant.HELPDESK_UPDATE_TICKET);
			smsDetails.setLangCode(Locale.ENGLISH.getLanguage());
			smsDetails.setMessage(content);
			smsDetails.setMobileNumber(phoneNumber);
			smsDetails.setTemplateId(templateEntity.get().getTemplateId());
			commonDataController.processSMS(smsDetails);
		}

	}

	@Override
	public GenericResponse getTicketByPhnOrEmail(String search) {
		List<CreateTicketEntity> entity = new ArrayList<CreateTicketEntity>();

		entity = createTicketRepository.findByMobileOrEmailIgnoreCase(search.toUpperCase());

		if (CollectionUtils.isEmpty(entity)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}

		List<CreateTicketResponseDto> responseDto = entity.stream().map(createTicketMapper::convertEntityToResponseDTO)
				.collect(Collectors.toList());
		return Library.getSuccessfulResponse(responseDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getByStatusViaApp(String name, Long issueFromId) {
		List<CreateTicketEntity> list = null;
		List<CreateTicketEntity> listwepap = null;
		ArrayList<CreateTicketEntity> finallist = new ArrayList<CreateTicketEntity>();
		Long webappid = (long) 5;

		if (Objects.nonNull(name) && name.equalsIgnoreCase("Open")) {
			list = createTicketRepository.findByTicketOpen(issueFromId);
		}

		if (Objects.nonNull(name) && name.equalsIgnoreCase("Pending")) {
			list = createTicketRepository.findByTicketPending(issueFromId);
			listwepap = createTicketRepository.findByTicketPending(webappid);
			finallist.addAll(list);
			finallist.addAll(listwepap);
		}

		if (Objects.nonNull(name) && name.equalsIgnoreCase("Closed")) {
			list = createTicketRepository.findByTicketClosed(issueFromId);
			listwepap = createTicketRepository.findByTicketClosed(webappid);
			finallist.addAll(list);
			finallist.addAll(listwepap);
		}

		if (Objects.nonNull(name) && name.equalsIgnoreCase("Reopen")) {
			list = createTicketRepository.findByTicketReopen(issueFromId);
		}

		if (Objects.nonNull(name) && name.equalsIgnoreCase("Escalated")) {
			list = createTicketRepository.findByTicketEscalated(issueFromId);
		}

		if (Objects.nonNull(name) && name.equalsIgnoreCase("All")) {
			list = createTicketRepository.findAllByIssueFrom(issueFromId);
		}

		if (Objects.nonNull(name) && name.equalsIgnoreCase("Change Request")) {
			list = createTicketRepository.findByTicketChangeRequest(issueFromId);
		}

		if (CollectionUtils.isEmpty(finallist)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<CreateTicketResponseDto> responseDto = finallist.stream()
				.map(createTicketMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(responseDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getCountticketstatus(TicketcounRequest requestDto, AuthenticationDTO authenticationDTO) {
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

		List<TicketCountResponseDTO> ticketCountResponseDTOList = new ArrayList<>();
		List<TicketStatusEntity> ticketStatusList = ticketStatusrepository.findAllByStatusOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(ticketStatusList)) {
			throw new RecordNotFoundException("No Record Found");
		}
		ticketStatusList.forEach(ticketStatus -> {
			if (ticketStatus.getTicketstatusname().equalsIgnoreCase(requestDto.getTicketstatus())) {
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
				if (authenticationDTO.getRoleCodes().stream().anyMatch(r -> Constant.HELPDESK_ADMIN.equals(r))) {
//				ticketCountResponseDTO.setCount(
//						createTicketRepository.getCountByStatusAndCreatedDate(ticketStatus.getId(), fromDate));
					ticketCountResponseDTO.setCount(createTicketRepository
							.getCountByStatusAndCreatedDateBetween(ticketStatus.getId(), fromDate, toDate));

				} else if (authenticationDTO.getRoleCodes().stream()
						.anyMatch(r -> Constant.HELPDESK_EXECUTIVE_ROLE.equals(r))) {
//				ticketCountResponseDTO.setCount(createTicketRepository.getCountByStatusAndCreatedByAndCreatedDate(
//						ticketStatus.getId(), authenticationDTO.getUserId(), fromDate));

					ticketCountResponseDTO
							.setCount(createTicketRepository.getCountByStatusAndCreatedByAndCreatedDateBetween(
									ticketStatus.getId(), authenticationDTO.getUserId(), fromDate, toDate));

				} else if (authenticationDTO.getRoleCodes().stream()
						.anyMatch(r -> Constant.FIELD_ENGINEER_ROLE.equals(r))
						|| authenticationDTO.getRoleCodes().stream()
								.anyMatch(r -> Constant.NODAL_OFFICER_ROLE.equals(r))) {

					ticketCountResponseDTO
							.setCount(createTicketRepository.getCountByStatusAndAssignToAndCreatedDateBetween(
									ticketStatus.getId(), authenticationDTO.getUserId(), fromDate, toDate));

				}
				ticketCountResponseDTOList.add(ticketCountResponseDTO);
			}

		});
		return Library.getSuccessfulResponse(ticketCountResponseDTOList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

	@Override
	@Transactional
	public GenericResponse addapp(CreateTicketDashboardDTO requestDTO) {
		Optional<CreateTicketEntitypayment> optional = null;
		CreateTicketEntitypayment entity = null;
		/*
		 * createTicketRepository.findByTicketNumberIgnoreCase(requestDTO.
		 * getTicketNumber().toUpperCase()); if (optional.isPresent()) { return
		 * Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
		 * ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] {
		 * TICKET_NUMBER })); }
		 */

		// String code = RandomUtil.generateTicketId();
		try {
			String code = RandomUtil.getinitialsequential();
			while (true) {
				optional = createTicketRepositoryp.findByTicketNumberIgnoreCase(code);
				if (optional.isPresent()) {
					// code = RandomUtil.getRandomNumber();
					code = getsequential();
				} else {
					break;
				}
			}

			requestDTO.setTicketNumber(code);
			requestDTO.setId(null);
			requestDTO.setDuration("0");
			// requestDTO.setCreatedBy(null);
			entity = createTicketMapper.convertRequestDTOToEntityapp(requestDTO, null);
			Long l = new Long(0);
			int i = l.intValue();
			entity.setCreatedBy(l);

			createTicketRepositoryp.save(entity);
		} catch (Exception e) {
			throw new InvalidDataValidation("Heavy traffic please try later");
		}
		// this.saveAuditHistory(entity, HelpDeskTicketAction.CREATED,
		// entity.getCreatedDate(), requestDTO.getRemarks());
		// CreateTicketResponseDto createTicketResponseDto =
		// createTicketMapper.convertEntityToResponseDTOapp(entity);
//		if (StringUtils.isNotBlank(createTicketResponseDto.getEmail())) {
//			// sendTemplateEmailService.sendAcknowledgeTemplate(createTicketResponseDto);
//		}
//		if (StringUtils.isNotBlank(entity.getMobile())) {
//			// this.sendTicketNotification(requestDTO.getTicketStatusId(),
//			// entity.getTicketNumber(), entity.getMobile());
//		}
		return Library.getSuccessfulResponse(requestDTO, ErrorCode.CREATED.getErrorCode(),
				ErrorMessages.RECORED_CREATED);

	}

	@Override
	@Transactional
	public GenericResponse updateapp(CreateTicketDashboardDTO requestDTO) {
		Optional<CreateTicketEntitypayment> optional = createTicketRepositoryp.findById(requestDTO.getId());

		if (!optional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		CreateTicketEntitypayment entity = optional.get();
		try {

			requestDTO.setDuration("0");
			entity = createTicketMapper.convertRequestDTOToEntityappupdate(requestDTO, null);
			entity.setCreatedDate(optional.get().createdDate);
			Long l = new Long(0);
			int i = l.intValue();
			entity.setCreatedBy(l);
			entity.setTicketNumber(requestDTO.getTicketNumber());
			entity.setId(requestDTO.getId());
			createTicketRepositoryp.save(entity);
		} catch (Exception e) {
			throw new InvalidDataValidation("Heavy traffic please try later");
		}
		return Library.getSuccessfulResponse(requestDTO, ErrorCode.CREATED.getErrorCode(),
				ErrorMessages.RECORED_CREATED);
	}

	@Override
	public GenericResponse listStatusViaApp(List<CreateTicketDashboardDTO> requestDto) {
		List<CreateTicketEntity> finalList = new ArrayList<CreateTicketEntity>();
		List<String> lic = new ArrayList<String>();
		requestDto.forEach(ticreq -> {
			Date fromDate = null;
			Date toDate = null;
			try {
				fromDate = new SimpleDateFormat(DATE_FORMAT).parse(ticreq.getFromDate().toString() + " " + START_TIME);
				toDate = new SimpleDateFormat(DATE_FORMAT).parse(ticreq.getToDate().toString() + " " + END_TIME);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			List<CreateTicketEntity> list = null;

			if (Objects.nonNull(ticreq.getTicketstatusName()) && ticreq.getTicketstatusName().equalsIgnoreCase("Open")
					&& Objects.isNull(fromDate) && Objects.isNull(toDate)) {
				list = createTicketRepository.findByTicketOpen1(ticreq.getLicenseNumber(), ticreq.getLicenseNumber());
			}

			if (Objects.nonNull(ticreq.getTicketstatusName())
					&& ticreq.getTicketstatusName().equalsIgnoreCase("Assigned") && Objects.nonNull(fromDate)
					&& Objects.nonNull(toDate)) {
				list = createTicketRepository.findByTicketAssigned(ticreq.getLicenseNumber(), ticreq.getLicenseNumber(),
						fromDate, toDate);
			}

			if (Objects.nonNull(ticreq.getTicketstatusName()) && ticreq.getTicketstatusName().equalsIgnoreCase("Open")
					&& Objects.nonNull(fromDate) && Objects.nonNull(toDate)) {
				list = createTicketRepository.findByTicketOpenFTDate(ticreq.getLicenseNumber(),
						ticreq.getLicenseNumber(), fromDate, toDate);
			}

			if (Objects.nonNull(ticreq.getTicketstatusName())
					&& ticreq.getTicketstatusName().equalsIgnoreCase("Pending") && Objects.isNull(fromDate)
					&& Objects.isNull(toDate)) {
				list = createTicketRepository.findByTicketPending1(ticreq.getLicenseNumber(),
						ticreq.getLicenseNumber());
			}

			if (Objects.nonNull(ticreq.getTicketstatusName())
					&& ticreq.getTicketstatusName().equalsIgnoreCase("Pending") && Objects.nonNull(fromDate)
					&& Objects.nonNull(toDate)) {
				list = createTicketRepository.findByTicketPendingFTDate(ticreq.getLicenseNumber(),
						ticreq.getLicenseNumber(), fromDate, toDate);
			}

			if (Objects.nonNull(ticreq.getTicketstatusName()) && ticreq.getTicketstatusName().equalsIgnoreCase("Closed")
					&& Objects.isNull(fromDate) && Objects.isNull(toDate)) {
				list = createTicketRepository.findByTicketClosedReCAL(ticreq.getLicenseNumber(),
						ticreq.getLicenseNumber());
			}

			if (Objects.nonNull(ticreq.getTicketstatusName()) && ticreq.getTicketstatusName().equalsIgnoreCase("Closed")
					&& Objects.nonNull(fromDate) && Objects.nonNull(toDate)) {
				list = createTicketRepository.findByTicketClosedReCALFTDate(ticreq.getLicenseNumber(),
						ticreq.getLicenseNumber(), fromDate, toDate);
			}

			if (Objects.nonNull(ticreq.getTicketstatusName()) && ticreq.getTicketstatusName().equalsIgnoreCase("Reopen")
					&& Objects.isNull(fromDate) && Objects.isNull(toDate)) {
				list = createTicketRepository.findByTicketReopen1(ticreq.getLicenseNumber(), ticreq.getLicenseNumber());
			}

			if (Objects.nonNull(ticreq.getTicketstatusName()) && ticreq.getTicketstatusName().equalsIgnoreCase("Reopen")
					&& Objects.nonNull(fromDate) && Objects.nonNull(toDate)) {
				list = createTicketRepository.findByTicketReopenFTDate(ticreq.getLicenseNumber(),
						ticreq.getLicenseNumber(), fromDate, toDate);
			}

			if (Objects.nonNull(ticreq.getTicketstatusName())
					&& ticreq.getTicketstatusName().equalsIgnoreCase("Escalated")) {
				list = createTicketRepository.findByTicketEscalated1(ticreq.getLicenseNumber(),
						ticreq.getLicenseNumber());
			}

			if (Objects.nonNull(ticreq.getTicketstatusName()) && ticreq.getTicketstatusName().equalsIgnoreCase("All")
					&& Objects.isNull(fromDate) && Objects.isNull(toDate)) {
				list = createTicketRepository.findAllByIssueFrom1(ticreq.getLicenseNumber(), ticreq.getLicenseNumber());

			}

			if (Objects.nonNull(ticreq.getTicketstatusName()) && ticreq.getTicketstatusName().equalsIgnoreCase("All")
					&& Objects.nonNull(fromDate) && Objects.nonNull(toDate)) {
				list = createTicketRepository.findAllByIssueFromFTDate(ticreq.getLicenseNumber(),
						ticreq.getLicenseNumber(), fromDate, toDate);

			}

			finalList.addAll(list);
		});

		if (CollectionUtils.isEmpty(finalList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<CreateTicketResponseDto> responseDto = finalList.stream()
				.map(createTicketMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(responseDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

//	@Override
//	public GenericResponse getCountapp(List<CreateTicketRequestDto> requestDto) {
//		List<TicketCountResponseDTO> finallist = new ArrayList<>();
//		List<TicketCountResponseDTO> ticketCountResponseDTOList = new ArrayList<>();
//		//TicketCountResponseDTO ticketCountResponseDTO = new TicketCountResponseDTO();
//		requestDto.stream().forEach(listlc -> {
//			
//			final Date fromDate;
//			final Date toDate;
//			try {
//				fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(listlc.getFromDate() + " " + "00:00:00");
//			} catch (ParseException e) {
//				log.error("error occurred while parsing date : {}", e.getMessage());
//				throw new InvalidDataValidation("Invalid date parameter passed");
//			}
//			try {
//				toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(listlc.getToDate() + " " + "23:59:59");
//			} catch (ParseException e) {
//				log.error("error occurred while parsing date : {}", e.getMessage());
//				throw new InvalidDataValidation("Invalid date parameter passed");
//			}
//			
//			List<TicketStatusEntity> ticketStatusList = ticketStatusrepository.findAllByStatusOrderByModifiedDateDesc();
//			if (CollectionUtils.isEmpty(ticketStatusList)) {
//				throw new RecordNotFoundException("No Record Found");
//			}
//			
//			ticketStatusList.forEach(ticketStatus -> {
//				if (Objects.isNull(ticketStatus.getId()) || StringUtils.isBlank(ticketStatus.getTicketstatusname())) {
//					return;
//				}
//				TicketCountResponseDTO ticketCountResponseDTO = new TicketCountResponseDTO();
//				ticketCountResponseDTO.setStatus(ticketStatus.getTicketstatusname());
//				ticketCountResponseDTO.setCount(createTicketRepository.getCountByStatusAndCreatedDateBetweenAndLicenceNumber(
//								ticketStatus.getId(), fromDate, toDate, listlc.getLicenseNumber()));
//				// ticketCountResponseDTO.setCount(createTicketRepository.getCountByStatusAndCreatedDateBetweenAndLicenceNumber(fromDate,toDate,licenseNumber));
//
//				ticketCountResponseDTOList.add(ticketCountResponseDTO);
//				
//			});
//		
//		});
//	finallist.addAll(ticketCountResponseDTOList);
//		
//		return Library.getSuccessfulResponse(finallist, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
//				ErrorMessages.RECORED_FOUND);
//
//	}

//	@Override
//	public GenericResponse getCountapp(List<CreateTicketRequestDto> requestDto) {
//		List<TicketCountResponseDTO> finallist = new ArrayList<>();
//		List<TicketCountResponseDTO> ticketCountResponseDTOList = new ArrayList<>();
//		//TicketCountResponseDTO ticketCountResponseDTO = new TicketCountResponseDTO();
//		requestDto.stream().forEach(listlc -> {
//			final Date fromDate;
//			final Date toDate;
//			try {
//				fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(listlc.getFromDate() + " " + "00:00:00");
//			} catch (ParseException e) {
//				log.error("error occurred while parsing date : {}", e.getMessage());
//				throw new InvalidDataValidation("Invalid date parameter passed");
//			}
//			try {
//				toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(listlc.getToDate() + " " + "23:59:59");
//			} catch (ParseException e) {
//				log.error("error occurred while parsing date : {}", e.getMessage());
//				throw new InvalidDataValidation("Invalid date parameter passed");
//			}
//			
//			List<TicketStatusEntity> ticketStatusList = ticketStatusrepository.findAllByStatusOrderByModifiedDateDesc();
//			if (CollectionUtils.isEmpty(ticketStatusList)) {
//				throw new RecordNotFoundException("No Record Found");
//			}
//			
//			ticketStatusList.forEach(ticketStatus -> {
//				if (Objects.isNull(ticketStatus.getId()) || StringUtils.isBlank(ticketStatus.getTicketstatusname())) {
//					return;
//				}
//				TicketCountResponseDTO ticketCountResponseDTO = new TicketCountResponseDTO();
//				ticketCountResponseDTO.setStatus(ticketStatus.getTicketstatusname());
////				ticketCountResponseDTO.setCount(createTicketRepository.getCountByStatusAndCreatedDateBetweenAndLicenceNumber(
////								ticketStatus.getId(), fromDate, toDate, listlc.getLicenseNumber()));
//				
//			if(ticketCountResponseDTO.getCount()==0) {
//					
//				}
//				else {
//				int count=ticketCountResponseDTO.getCount();	
//				count++;
//				ticketCountResponseDTO.setCount(count);
//				}
//				
//				ticketCountResponseDTOList.add(ticketCountResponseDTO);
//				
//			});
//			//ticketCountResponseDTOList.add(ticketCountResponseDTO);
//		});
//		
//		finallist.addAll(ticketCountResponseDTOList);
//		
//		return Library.getSuccessfulResponse(finallist, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
//				ErrorMessages.RECORED_FOUND);
//
//	}

	@Override
	public GenericResponse getCountapp(CreateTicketRequestDto2 requestDto) {
		List<TicketCountResponseDTO> finallist = new ArrayList<>();
		List<TicketCountResponseDTO> ticketCountResponseDTOList = new ArrayList<>();
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

		List<TicketStatusEntity> ticketStatusList = ticketStatusrepository.findAllByStatusOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(ticketStatusList)) {
			throw new RecordNotFoundException("No Record Found");
		}

		ticketStatusList.forEach(ticketStatus -> {
			Long app = (long) 4;
			Long webapp = (long) 5;
			Integer appcount = 0;
			Integer webappcount = 0;
			Integer total = 0;

			if (Objects.isNull(ticketStatus.getId()) || StringUtils.isBlank(ticketStatus.getTicketstatusname())) {
				return;
			}
			TicketCountResponseDTO ticketCountResponseDTO = new TicketCountResponseDTO();
			ticketCountResponseDTO.setStatus(ticketStatus.getTicketstatusname());
			ArrayList<String> licnumber = requestDto.getLicenseNumber();
			appcount = createTicketRepository.getCountByStatusAndCreatedDateBetweenAndLicenceNumber(
					ticketStatus.getId(), fromDate, toDate, licnumber, licnumber);

//			 webappcount=createTicketRepository.getCountByStatusAndCreatedDateBetweenAndLicenceNumberAndIssueFrom(
//						ticketStatus.getId(), fromDate, toDate,licnumber,webapp);
			total = appcount + webappcount;
			ticketCountResponseDTO.setCount(total);
			ticketCountResponseDTOList.add(ticketCountResponseDTO);
		});

		finallist.addAll(ticketCountResponseDTOList);

		return Library.getSuccessfulResponse(finallist, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

	@Override
	public GenericResponse getAllByRequestFilterapp(PaginationRequestDTO2 requestData,
			AuthenticationDTO authenticationDTO) throws ParseException {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
//		Long count = this.getCountBySearchFieldsapp(requestData,authenticationDTO);
//		log.info("total count :: {}", count);
		// if (count > 0) {
		List<CreateTicketEntity> list = this.getRecordsByFilterDTOapp(requestData, authenticationDTO);
		if (CollectionUtils.isEmpty(list)) {
			throw new RecordNotFoundException("No Record Found");
		}
		List<CreateTicketResponseDto> dtoList = list.stream()
				.map(createTicketMapper::convertEntityToResponseDTOPaymentApp).collect(Collectors.toList());

		paginationResponseDTO.setContents(dtoList);
		paginationResponseDTO.setNumberOfElements(Objects.nonNull(dtoList.size()) ? dtoList.size() : null);
		paginationResponseDTO.setTotalElements((long) dtoList.size());
		return Library.getSuccessfulResponse(paginationResponseDTO, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
		// }
//		else {
//			throw new RecordNotFoundException("No Record Found");
//		}
	}

	public List<CreateTicketEntity> getRecordsByFilterDTOapp(PaginationRequestDTO2 filterRequestDTO,
			AuthenticationDTO authenticationDTO) throws ParseException {
		// List<CreateTicketEntity> data = new ArrayList<>();
		ArrayList<String> licnumber = filterRequestDTO.getLicenseNumber();
		String lic_no = null;
//	 for (String ap : licnumber) {
//	 lic_no = ap;
//	  filterRequestDTO.put(lic_no);
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<CreateTicketEntity> cq = cb.createQuery(CreateTicketEntity.class);
		Root<CreateTicketEntity> from = cq.from(CreateTicketEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<CreateTicketEntity> typedQuery = null;
		addCriteriaapp(cb, list, filterRequestDTO, from, authenticationDTO);
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
		List<CreateTicketEntity> data = typedQuery.getResultList();
		if (CollectionUtils.isEmpty(data)) {
			throw new RecordNotFoundException("No Record Found");
		}
		return data;
	}

	private Long getCountBySearchFieldsapp(PaginationRequestDTO2 filterRequestDTO, AuthenticationDTO authenticationDTO)
			throws ParseException {
		log.info("getting total count by search fields :: {}", filterRequestDTO.toString());
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<CreateTicketEntity> from = cq.from(CreateTicketEntity.class);
		cq.select(cb.count(from));
		List<Predicate> list = new ArrayList<>();
		addCriteriaapp(cb, list, filterRequestDTO, from, authenticationDTO);
		cq.where(cb.and(list.toArray(new Predicate[list.size()])));
		Long count = entityManager.createQuery(cq).getSingleResult();
		if (count > 0) {
			return count;
		} else {
			throw new RecordNotFoundException("No Record Found");
		}
	}

	private void addCriteriaapp(CriteriaBuilder cb, List<Predicate> list, PaginationRequestDTO2 filterRequestDTO,
			Root<CreateTicketEntity> from, AuthenticationDTO authenticationDTO) throws ParseException {

		if (authenticationDTO.getRoleCodes() == null) {

		}
//		else if (authenticationDTO.getRoleCodes().stream().anyMatch(r -> Constant.HELPDESK_EXECUTIVE_ROLE.equals(r))) {
//			list.add(cb.equal(from.get(CREATED_BY), authenticationDTO.getUserId()));
//		}

//		
		else if (authenticationDTO.getRoleCodes().stream().anyMatch(r -> Constant.FIELD_ENGINEER_ROLE.equals(r))
				|| authenticationDTO.getRoleCodes().stream().anyMatch(r -> Constant.NODAL_OFFICER_ROLE.equals(r))) {
			list.add(cb.equal(from.get("assignTo"), authenticationDTO.getUserId()));
		}

		if (Objects.nonNull(filterRequestDTO.getCreatedDate())
				&& !filterRequestDTO.getCreatedDate().toString().trim().isEmpty()) {

			Date fromDate = new SimpleDateFormat(DATE_FORMAT)
					.parse(filterRequestDTO.getCreatedDate().toString() + " " + START_TIME);
			Date toDate = new SimpleDateFormat(DATE_FORMAT)
					.parse(filterRequestDTO.getCreatedDate().toString() + " " + END_TIME);
			list.add(cb.between(from.get(CREATED_DATE), fromDate, toDate));

		}

		if (Objects.nonNull(filterRequestDTO.getFromDate())
				&& !filterRequestDTO.getFromDate().toString().trim().isEmpty()
				&& Objects.nonNull(filterRequestDTO.getToDate())
				&& !filterRequestDTO.getToDate().toString().trim().isEmpty()) {

			Date fromDate = new SimpleDateFormat(DATE_FORMAT)
					.parse(filterRequestDTO.getFromDate().toString() + " " + START_TIME);
			Date toDate = new SimpleDateFormat(DATE_FORMAT)
					.parse(filterRequestDTO.getToDate().toString() + " " + END_TIME);
			list.add(cb.between(from.get(CREATED_DATE), fromDate, toDate));

		}

		if (Objects.nonNull(filterRequestDTO.getId()) && !filterRequestDTO.getId().toString().trim().isEmpty()) {

			Long id = Long.valueOf(filterRequestDTO.getId().toString());
			list.add(cb.equal(from.get(ID), id));
		}

		if (Objects.nonNull(filterRequestDTO.getTicketNumber())
				&& !filterRequestDTO.getTicketNumber().toString().trim().isEmpty()) {

			String ticketNumber = String.valueOf(filterRequestDTO.getTicketNumber().toString());
			list.add(cb.equal(from.get(TICKET_NUMBER), ticketNumber));
		}

		if (Objects.nonNull(filterRequestDTO.getLicenseNumber())
				&& !filterRequestDTO.getLicenseNumber().toString().trim().isEmpty()) {
			Expression<String> mainlic = from.get(LICENSE_NUMBER);
			Expression<String> mainModushopcode = from.get("shopCode");
			Predicate liceneNo = mainlic.in(filterRequestDTO.getLicenseNumber());
			Predicate shopcode = mainModushopcode.in(filterRequestDTO.getLicenseNumber());
			Predicate liceneNoOrunitcode = cb.or(liceneNo, shopcode);
			list.add(liceneNoOrunitcode);

		}

		if (Objects.nonNull(filterRequestDTO.getIssueFromId())
				&& !filterRequestDTO.getIssueFromId().toString().trim().isEmpty()) {
			Long issueFrom = Long.valueOf(filterRequestDTO.getIssueFromId().toString());
			list.add(cb.equal(from.get(ISSUEFROM), issueFrom));
		}

		/*
		 * if (Objects.nonNull(filterRequestDTO.getIssueFromId()) &&
		 * !filterRequestDTO.getIssueFromId().toString().trim().isEmpty()) { Long
		 * issueFromid = Long.valueOf(filterRequestDTO.getIssueFromId().toString());
		 * list.add(cb.equal(from.get(ISSUE_FROM_ID), issueFromid)); }
		 */

		if (Objects.nonNull(filterRequestDTO.getCategoryId())
				&& !filterRequestDTO.getCategoryId().toString().trim().isEmpty()) {
			Long categoryId = Long.valueOf(filterRequestDTO.getCategoryId().toString());
			list.add(cb.equal(from.get(CATEGORY), categoryId));
		}

		if (Objects.nonNull(filterRequestDTO.getSubCategoryId())
				&& !filterRequestDTO.getSubCategoryId().toString().trim().isEmpty()) {
			Long subcategoryId = Long.valueOf(filterRequestDTO.getSubCategoryId().toString());
			list.add(cb.equal(from.get(SUBCATEGORY), subcategoryId));
		}
		if (Objects.nonNull(filterRequestDTO.getLicenceTypeId())
				&& !filterRequestDTO.getLicenceTypeId().toString().trim().isEmpty()) {
			String licenseTypeId = String.valueOf(filterRequestDTO.getLicenceTypeId().toString());
			list.add(cb.equal(from.get("licenceTypeId"), licenseTypeId));
		}

		if (Objects.nonNull(filterRequestDTO.getPriorityId())
				&& !filterRequestDTO.getPriorityId().toString().trim().isEmpty()) {
			Long priorityId = Long.valueOf(filterRequestDTO.getPriorityId().toString());
			list.add(cb.equal(from.get(PRIORITY), priorityId));
		}
		if (Objects.nonNull(filterRequestDTO.getTicketStatusId())
				&& !filterRequestDTO.getTicketStatusId().toString().trim().isEmpty()) {
			Long status = Long.valueOf(filterRequestDTO.getTicketStatusId().toString());
			list.add(cb.equal(from.get(TICKET_STATUS), status));
		}

		if (Objects.nonNull(filterRequestDTO.getSlaId()) && !filterRequestDTO.getSlaId().toString().trim().isEmpty()) {
			Long sla = Long.valueOf(filterRequestDTO.getSlaId().toString());
			list.add(cb.equal(from.get(SLA), sla));
		}

//				if (Objects.nonNull(filterRequestDTO.getas)
//						&& !filterRequestDTO.getFilters().get("assignTo").toString().trim().isEmpty()) {
//					Long assignto = Long.valueOf(filterRequestDTO.getFilters().get("assignTo").toString());
//					list.add(cb.equal(from.get("assignTo"), assignto));
//				}

	}

	@Override
	public GenericResponse getAllByRequestFilterfieldapp(PaginationRequestDTO paginationDto,
			AuthenticationDTO authenticationDTO) throws ParseException {

		Pageable pageable = null;
		Page<CreateTicketEntity> list = null;
		Long assignToId = null;
		Date fromDate = null;
		Date toDate = null;

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
			if (Objects.nonNull(paginationDto.getFilters().get("fromDate"))
					&& !paginationDto.getFilters().get("fromDate").toString().trim().isEmpty())
				try {
					fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.parse(paginationDto.getFilters().get("fromDate") + " " + "00:00:00");
				} catch (ParseException e) {
					log.error("error occurred while parsing date : {}", e.getMessage());
					throw new InvalidDataValidation("Invalid date parameter passed");
				}
		}
		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("toDate"))
					&& !paginationDto.getFilters().get("toDate").toString().trim().isEmpty())

				try {
					toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.parse(paginationDto.getFilters().get("toDate") + " " + "23:59:59");
				} catch (ParseException e) {
					log.error("error occurred while parsing date : {}", e.getMessage());
					throw new InvalidDataValidation("Invalid date parameter passed");
				}
		}

		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("assignToId"))
					&& !paginationDto.getFilters().get("assignToId").toString().trim().isEmpty()) {
				try {
					assignToId = Long.valueOf(paginationDto.getFilters().get("assignToId").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing assignToId :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}

		}

		if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && Objects.nonNull(assignToId)) {
			list = createTicketRepository.getByAssignToAndCreateddate(assignToId, fromDate, toDate, pageable);
		}

		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}

		Page<CreateTicketResponseDto> finalResponse = list.map(createTicketMapper::convertEntityToResponseDTO);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getmaindashboard(CreateTicketDashboardDTO requestDto) {
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

		Long categoryid = (long) 24;

		List<Ticketdashboard> ticketEntityList = createTicketRepository
				.getCountByStatusAndCreatedDateBetweenAndCategory(fromDate, toDate, categoryid);

		if (CollectionUtils.isEmpty(ticketEntityList)) {
			throw new RecordNotFoundException("No Record Found");
		}

		return Library.getSuccessfulResponse(ticketEntityList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

	@Override
	public GenericResponse getdistrictwiseticket(CreateTicketDashboardDTO requestDto) {
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
		Long categoryid = (long) 24;
		List<Districtdashboard> ticketEntityList = createTicketRepository
				.getCountByCreatedDateBetweenAndCategory(fromDate, toDate, categoryid);

		if (CollectionUtils.isEmpty(ticketEntityList)) {
			throw new RecordNotFoundException("No Record Found");
		}

		return Library.getSuccessfulResponse(ticketEntityList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

	@Override
	public GenericResponse getdistrictcategorywiseticket(String fromDate, String toDate, String district) {
		final Date fDate;
		final Date endDate;
		try {
			fDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fromDate + " " + "00:00:00");
		} catch (ParseException e) {
			log.error("error occurred while parsing date : {}", e.getMessage());
			throw new InvalidDataValidation("Invalid date parameter passed");
		}
		try {
			endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(toDate + " " + "23:59:59");
		} catch (ParseException e) {
			log.error("error occurred while parsing date : {}", e.getMessage());
			throw new InvalidDataValidation("Invalid date parameter passed");
		}
		// List<Districtcategorydashboard> ticketEntityList=null;
		// if(!district.isEmpty()) {

		Long categoryid = (long) 24;

		List<Districtcategorydashboard> ticketEntityList = createTicketRepository
				.getCountByDistrictAndCreatedDateBetweenAndCategory(district, fDate, endDate, categoryid);
		// }

		if (CollectionUtils.isEmpty(ticketEntityList)) {
			throw new RecordNotFoundException("No Record Found");
		}

		return Library.getSuccessfulResponse(ticketEntityList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

	// @Scheduled(cron="*/5 * * * * *")//5secs
	// @Scheduled(cron="*/15 * * * *") //15 mins
	@Transactional
	public GenericResponse maindashboardemail() {

		System.out.println("TESTINGEMAIL:::::::::" + "EMAILLLLLLLL");

		final Date fromDate;
		final Date toDate;
		try {
			fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-09-01" + " " + "00:00:00");
		} catch (ParseException e) {
			log.error("error occurred while parsing date : {}", e.getMessage());
			throw new InvalidDataValidation("Invalid date parameter passed");
		}
		try {
			toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-09-20" + " " + "23:59:59");
		} catch (ParseException e) {
			log.error("error occurred while parsing date : {}", e.getMessage());
			throw new InvalidDataValidation("Invalid date parameter passed");
		}

//    List<Ticketdashboard> ticketEntityList = createTicketRepository.getCountByStatusAndCreatedDateBetween(fromDate,toDate);
//    
//    ticketEntityList.stream().forEach(action -> {
//    	
//    });

		return null;

	}

	@Override
	public GenericResponse getdistrictwiseshopcodeticket(CreateTicketRequestDto requestDto) {
		final Date fromDate;
		final Date toDate;
//		try {
//			fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(requestDto.getFromDate() + " " + "00:00:00");
//		} catch (ParseException e) {
//			log.error("error occurred while parsing date : {}", e.getMessage());
//			throw new InvalidDataValidation("Invalid date parameter passed");
//		}
//		try {
//			toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(requestDto.getToDate() + " " + "23:59:59");
//		} catch (ParseException e) {
//			log.error("error occurred while parsing date : {}", e.getMessage());
//			throw new InvalidDataValidation("Invalid date parameter passed");
//		}
//		List<EntitylicenseshopDTO> ticketEntityList=null;	
		List<EntitylicenseshopDTO> ticketEntityList = createTicketRepository
				.getByDistrictCode(requestDto.getDistrictCode());

		if (CollectionUtils.isEmpty(ticketEntityList)) {
			throw new RecordNotFoundException("No Record Found");
		}

		return Library.getSuccessfulResponse(ticketEntityList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

	@Override
	public GenericResponse getdistrictwiseentityticket(CreateTicketRequestDto requestDto) {
		final Date fromDate;
		final Date toDate;
		String rolecode = requestDto.getRoleCode();

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
		Long incidentcategory = (long) 24;

		List<String> districtcodeid = requestDto.getDistrictIdcode();
		List<EntitylicenseDTO> ticketEntityList = null;
		if (rolecode.equalsIgnoreCase("ROLE_HELPDESK")) {

			ticketEntityList = createTicketRepository.getCountByCreatedDateBetweenAndCategoryall(fromDate, toDate,
					incidentcategory);
		} else {
			ticketEntityList = createTicketRepository.getCountByDistrictCodeAndCreatedDateBetweenAndCategory(
					districtcodeid, fromDate, toDate, incidentcategory);

		}

		if (CollectionUtils.isEmpty(ticketEntityList)) {
			throw new RecordNotFoundException("No Record Found");
		}

		return Library.getSuccessfulResponse(ticketEntityList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

	@Override
	public GenericResponse viewReportincident(PaginationRequestDTO paginationDto, AuthenticationDTO authenticationDTO)
			throws ParseException {
		Pageable pageable = null;
		Page<CreateTicketEntity> list = null;
		Long categoryId = null;
		String issueDetails = null;
		Long subCategoryId = null;
		Long status = null;
		Long issuefromid = null;
		Long category = null;
		Long subcategory = null;
		String licencetype = null;
		String licencenumber = null;
		Long priorityid = null;
		ArrayList<Long> statusId = new ArrayList<Long>();
		String ticketNumber = null;
		String categoryName = null;
		String commonsearch = null;
		String entityType = null;
		Long ticketStatus = null;
		/*
		 * if(StringUtils.isNotBlank(paginationDto.getSortField())) {
		 * if(CATEGORYID.equals(paginationDto.getSortField())) {
		 * paginationDto.setSortField(CATEGORY); }
		 * if(SUB_CATEGORY_ID.equals(paginationDto.getSortField())) {
		 * paginationDto.setSortField(SUBCATEGORY); }
		 * if(ISSUE_DETAILS.equals(paginationDto.getSortField())) {
		 * paginationDto.setSortField(ISSUED); } }
		 */
		if (paginationDto.getSortOrder().equalsIgnoreCase(ASC)) {
			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
					Sort.by(Direction.ASC, paginationDto.getSortField()));
		} else {
			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
					Sort.by(Direction.DESC, paginationDto.getSortField()));
		}
//		if (Objects.nonNull(paginationDto.getFilters())) {
//			if (Objects.nonNull(paginationDto.getFilters().get(STATUS))
//					&& !paginationDto.getFilters().get(STATUS).toString().trim().isEmpty()) {
//				status = Long.valueOf(paginationDto.getFilters().get(STATUS).toString());
//			}
//		}

		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get(STATUS))
					&& !paginationDto.getFilters().get(STATUS).toString().trim().isEmpty()) {
				status = Long.valueOf(paginationDto.getFilters().get(STATUS).toString());
				Long obj1 = new Long(status);
				Long obj2 = new Long(57);

				if (obj1.equals(obj2)) {
					statusId.add(status);
					statusId.add((long) 59);
					statusId.add((long) 61);
				} else {
					statusId.add(status);

				}
			}
		}

		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("ticketStatus"))
					&& !paginationDto.getFilters().get("ticketStatus").toString().trim().isEmpty()) {
				ticketStatus = Long.valueOf(paginationDto.getFilters().get("ticketStatus").toString());
//				Long obj1 = new Long(status);
//				Long obj2 = new Long(57);
//
//				if (obj1.equals(obj2)) {
//					statusId.add(status);
//					statusId.add((long) 59);
//					statusId.add((long) 61);
//				} else {
//					statusId.add(status);
//
//				}
			}
		}

		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("commonSearch"))
					&& !paginationDto.getFilters().get("commonSearch").toString().trim().isEmpty()) {
				commonsearch = String.valueOf(paginationDto.getFilters().get("commonSearch").toString());
			}
		}

		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("entityType"))
					&& !paginationDto.getFilters().get("entityType").toString().trim().isEmpty()) {
				entityType = String.valueOf(paginationDto.getFilters().get("entityType").toString());
			}
		}

		Long incidentcategory = (long) 24;

		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.isNull(paginationDto.getFilters().get("fromDate"))
					&& Objects.isNull(paginationDto.getFilters().get("toDate"))) {
				Date fromDate = null;
				Date toDate = null;
				list = getByFilterincident(fromDate, toDate, incidentcategory, commonsearch, ticketStatus, pageable,
						entityType);

			} else {
				Date fromDate = new SimpleDateFormat(DATE_FORMAT)
						.parse(paginationDto.getFilters().get("fromDate").toString() + " " + START_TIME);
				Date toDate = new SimpleDateFormat(DATE_FORMAT)
						.parse(paginationDto.getFilters().get("toDate").toString() + " " + END_TIME);
				list = getByFilterincident(fromDate, toDate, incidentcategory, commonsearch, ticketStatus, pageable,
						entityType);

			}
		}

		if (Objects.isNull(list)) {
			list = createTicketRepository.getAll(pageable);
		}
		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		Page<CreateTicketResponseDto> finalResponse = list.map(createTicketMapper::convertEntityToResponseDTOReports);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}

	private Page<CreateTicketEntity> getByFilterincident(Date fromDate, Date toDate, Long incidentcategory,
			String commonsearch, Long ticketStatus, Pageable pageable, String entityType) {
		Page<CreateTicketEntity> list = null;
		Long checkcategory = null;
		Long incidentcategoryqw = null;

//		 if(obj1.equals(obj2)) {
//
//		list = createTicketRepository.getByCreatedDateAndCategoryAll(fromDate, toDate, incidentcategory,
//				commonsearch, commonsearch, commonsearch, pageable);
//
//		// fromdate,todate,category,commonse
//		if (Objects.isNull(list) && Objects.nonNull(fromDate) && Objects.nonNull(toDate)
//				&& Objects.nonNull(incidentcategory) && Objects.nonNull(commonsearch) && Objects.isNull(status)
//				&& Objects.isNull(entityType)) {
//			list = createTicketRepository.getByCreatedDateAndCategoryAll(fromDate, toDate, incidentcategory,
//					commonsearch, commonsearch, commonsearch, pageable);
//		}
//
//		// fromd,tod,incidentcategory
//		if (Objects.isNull(list) && Objects.nonNull(fromDate) && Objects.nonNull(toDate)
//				&& Objects.nonNull(incidentcategory) && Objects.isNull(status) && Objects.isNull(commonsearch)
//				&& Objects.isNull(entityType)) {
//			list = createTicketRepository.getByCreatedDateAndCategory(fromDate, toDate, incidentcategory, pageable);
//		}
//
//		// fromd,tod,commonsearch,incidentcategory,statusId
//		if (Objects.isNull(list) && Objects.nonNull(fromDate) && Objects.nonNull(toDate)
//				&& Objects.nonNull(incidentcategory) && Objects.nonNull(statusId) && !statusId.isEmpty()
//				&& Objects.nonNull(commonsearch) && Objects.isNull(entityType)) {
//			list = createTicketRepository.getByCreatedDateAndCategoryAndStatusAll(fromDate, toDate, incidentcategory,
//					statusId, commonsearch, commonsearch, commonsearch, pageable);
//		}
//
//		// fromd,tod,incidentcategory,statusId
//		if (Objects.isNull(list) && Objects.nonNull(fromDate) && Objects.nonNull(toDate)
//				&& Objects.nonNull(incidentcategory) && Objects.nonNull(statusId) && !statusId.isEmpty()
//				&& Objects.isNull(commonsearch) && Objects.isNull(entityType)) {
//			list = createTicketRepository.getByCreatedDateAndCategoryAndStatusA(fromDate, toDate, incidentcategory,
//					statusId, pageable);
//		}
//
//		// fdate, to d
//		if (Objects.isNull(list) && Objects.nonNull(fromDate) && Objects.nonNull(toDate)
//				&& Objects.isNull(incidentcategory) && Objects.isNull(status) && Objects.isNull(commonsearch)
//				&& Objects.isNull(entityType)) {
//			list = createTicketRepository.getByCreatedDate(fromDate, toDate, pageable);
//		}
//
//		// fromDate, toDate ,statusId
//		if (Objects.isNull(list) && Objects.nonNull(fromDate) && Objects.nonNull(toDate)
//				&& Objects.isNull(incidentcategory) && Objects.nonNull(statusId) && !statusId.isEmpty()
//				&& Objects.isNull(commonsearch) && Objects.isNull(entityType)) {
//			list = createTicketRepository.getByCreatedDateAndStatus(fromDate, toDate, statusId, pageable);
//		}
//
//		// fromDate, toDate ,commonsearch
//		if (Objects.isNull(list) && Objects.nonNull(fromDate) && Objects.nonNull(toDate)
//				&& Objects.isNull(incidentcategory) && Objects.isNull(status) && Objects.nonNull(commonsearch)
//				&& Objects.isNull(entityType)) {
//			list = createTicketRepository.getByCreatedDateAll(fromDate, toDate, commonsearch, commonsearch,
//					commonsearch, pageable);
//		}
//
//		// fromDate, toDate ,entityType
//		if (Objects.isNull(list) && Objects.nonNull(fromDate) && Objects.nonNull(toDate)
//				&& Objects.isNull(incidentcategory) && Objects.isNull(status) && Objects.isNull(commonsearch)
//				&& Objects.nonNull(entityType)) {
//			list = createTicketRepository.getByCreatedDateAndEntityTypeId(fromDate, toDate, entityType, pageable);
//		}
//
//		// fromDate, toDate,incidentcategory ,entityType
//		if (Objects.isNull(list) && Objects.nonNull(fromDate) && Objects.nonNull(toDate)
//				&& Objects.nonNull(incidentcategory) && Objects.isNull(status) && Objects.isNull(commonsearch)
//				&& Objects.nonNull(entityType)) {
//			list = createTicketRepository.getByCreatedDateAndCategoryAndEntityTypeId(fromDate, toDate, incidentcategory,
//					entityType, pageable);
//		}
//
//		// fromDate, toDate,statusId ,commonsearch
//		if (Objects.isNull(list) && Objects.nonNull(fromDate) && Objects.nonNull(toDate)
//				&& Objects.isNull(incidentcategory) && Objects.isNull(status) && Objects.nonNull(commonsearch)
//				&& Objects.isNull(entityType)) {
//			list = createTicketRepository.getByCreatedDateAndStatusAndAll(fromDate, toDate, statusId, commonsearch,
//					commonsearch, commonsearch, pageable);
//		}
//
//		// fromDate, toDate,statusId ,entityType
//		if (Objects.isNull(list) && Objects.nonNull(fromDate) && Objects.nonNull(toDate)
//				&& Objects.isNull(incidentcategory) && Objects.isNull(status) && Objects.isNull(commonsearch)
//				&& Objects.nonNull(entityType)) {
//			list = createTicketRepository.getByCreatedDateAndStatusAndEntityType(fromDate, toDate, statusId, entityType,
//					pageable);
//		}
//
//		// fromDate, toDate,commonsearch ,entityType
//		if (Objects.isNull(list) && Objects.nonNull(fromDate) && Objects.nonNull(toDate)
//				&& Objects.isNull(incidentcategory) && Objects.isNull(status) && Objects.nonNull(commonsearch)
//				&& Objects.nonNull(entityType)) {
//			list = createTicketRepository.getByCreatedDateAndAllAndEntityType(fromDate, toDate, commonsearch,
//					commonsearch, commonsearch, entityType, pageable);
//		}
//
//		// fromDate, toDate,incidentcategory,statusId ,entityType
//		if (Objects.isNull(list) && Objects.nonNull(fromDate) && Objects.nonNull(toDate)
//				&& Objects.nonNull(incidentcategory) && Objects.nonNull(statusId) && !statusId.isEmpty()
//				&& Objects.isNull(commonsearch) && Objects.nonNull(entityType)) {
//			list = createTicketRepository.getByCreatedDateAndCategoryAndStatusAndEntityTypeId(fromDate, toDate,
//					incidentcategory, statusId, entityType, pageable);
//		}
//
//		// fromDate, toDate,incidentcategory,commonsearch,entityType
//		if (Objects.isNull(list) && Objects.nonNull(fromDate) && Objects.nonNull(toDate)
//				&& Objects.nonNull(incidentcategory) && Objects.isNull(status) && Objects.nonNull(commonsearch)
//				&& Objects.nonNull(entityType)) {
//			list = createTicketRepository.getByCreatedDateAndCategoryAndAllAndEntityTypeId(fromDate, toDate,
//					incidentcategory, commonsearch, commonsearch, commonsearch, entityType, pageable);
//		}
//
//		// fromDate, toDate,statusId,commonsearch ,entityType
//		if (Objects.isNull(list) && Objects.nonNull(fromDate) && Objects.nonNull(toDate)
//				&& Objects.isNull(incidentcategory) && Objects.nonNull(statusId) && !statusId.isEmpty()
//				&& Objects.nonNull(commonsearch) && Objects.nonNull(entityType)) {
//			list = createTicketRepository.getByCreatedDateAndStatusAndAllAndEntityTypeId(fromDate, toDate, statusId,
//					commonsearch, commonsearch, commonsearch, entityType, pageable);
//		}
//
//		// fromDate, toDate,incidentcategory,statusId,commonsearch ,entityType
//		if (Objects.isNull(list) && Objects.nonNull(fromDate) && Objects.nonNull(toDate)
//				&& Objects.nonNull(incidentcategory) && Objects.nonNull(statusId) && !statusId.isEmpty()
//				&& Objects.nonNull(commonsearch) && Objects.nonNull(entityType)) {
//			list = createTicketRepository.getByCreatedDateAndCategoryAndStatusAndAllAndEntityTypeId(fromDate, toDate,
//					incidentcategory, statusId, commonsearch, commonsearch, commonsearch, entityType, pageable);
//		}
//
//	 }

		list = createTicketRepository.findIncidentTickets(fromDate, toDate, incidentcategory, commonsearch, entityType,
				ticketStatus, pageable);

		return list;
	}

	public GenericResponse viewReportquery(PaginationRequestDTO paginationDto, AuthenticationDTO authenticationDTO)
			throws ParseException {
		Pageable pageable = null;
		Page<CreateTicketEntity> list = null;
		Long categoryId = null;
		String issueDetails = null;
		Long subCategoryId = null;
		Long status = null;
		Long issuefromid = null;
		Long category = null;
		Long subcategory = null;
		String licencetype = null;
		String licencenumber = null;
		Long priorityid = null;

		String ticketNumber = null;

		String commonsearch = null;
		String entityTypeId = null;

		Long ticketStatus = null;
		String issueType = null;

		ArrayList<Long> statusId = new ArrayList<Long>();

		if (paginationDto.getSortOrder().equalsIgnoreCase(ASC)) {
			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
					Sort.by(Direction.ASC, paginationDto.getSortField()));
		} else {
			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
					Sort.by(Direction.DESC, paginationDto.getSortField()));
		}

		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("ticketStatus"))
					&& !paginationDto.getFilters().get("ticketStatus").toString().trim().isEmpty()) {
				ticketStatus = Long.valueOf(paginationDto.getFilters().get("ticketStatus").toString());
			}
		}

		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("commonSearch"))
					&& !paginationDto.getFilters().get("commonSearch").toString().trim().isEmpty()) {
				commonsearch = String.valueOf(paginationDto.getFilters().get("commonSearch").toString());
			}
		}

		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("entityTypeId"))
					&& !paginationDto.getFilters().get("entityTypeId").toString().trim().isEmpty()) {
				entityTypeId = String.valueOf(paginationDto.getFilters().get("entityTypeId").toString());
			}
		}

		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("issueType"))
					&& !paginationDto.getFilters().get("issueType").toString().trim().isEmpty()) {
				issueType = String.valueOf(paginationDto.getFilters().get("issueType").toString());
			}
		}
		Long querycategory = (long) 25;
		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.isNull(paginationDto.getFilters().get("fromDate"))
					&& Objects.isNull(paginationDto.getFilters().get("toDate"))) {
				Date fromDate = null;
				Date toDate = null;
				list = getByFilterquery(fromDate, toDate, querycategory, commonsearch, entityTypeId, ticketStatus,
						issueType, pageable);
			} else {
				Date fromDate = new SimpleDateFormat(DATE_FORMAT)
						.parse(paginationDto.getFilters().get("fromDate").toString() + " " + START_TIME);
				Date toDate = new SimpleDateFormat(DATE_FORMAT)
						.parse(paginationDto.getFilters().get("toDate").toString() + " " + END_TIME);
				list = getByFilterquery(fromDate, toDate, querycategory, commonsearch, entityTypeId, ticketStatus,
						issueType, pageable);
			}
		}
		if (Objects.isNull(list)) {
			list = createTicketRepository.getAll(pageable);
		}
		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		Page<CreateTicketResponseDto> finalResponse = list.map(createTicketMapper::convertEntityToResponseDTOReports);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}

	private Page<CreateTicketEntity> getByFilterquery(Date fromDate, Date toDate, Long querycategory,
			String commonsearch, String entityTypeId, Long ticketStatus, String issueType, Pageable pageable) {
		Page<CreateTicketEntity> list = null;

		list = createTicketRepository.findTickets(fromDate, toDate, querycategory, commonsearch, entityTypeId,
				ticketStatus, issueType, pageable);

		return list;
	}

	@Override
	@Transactional
	public GenericResponse updateviewstausticket(CreateTicketDashboardDTO requestDTO) {
		Optional<CreateTicketEntity> optional = createTicketRepository.findById(requestDTO.getId());

		if (!optional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}

		Optional<CreateTicketEntity> viewstatus = createTicketRepository.findByViewStatusAndId(true,
				requestDTO.getId());

		if (!viewstatus.isPresent()) {

			CreateTicketEntity entity = optional.get();
			entity.setViewStatus(requestDTO.isViewStatus());
			entity.setModifiedDate(new Date());
			createTicketRepository.save(entity);
			return Library.getSuccessfulResponse("Success", ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_UPDATED);

		}

		else {
			return Library.getSuccessfulResponse("Failed", ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					"Status Already Updated");

		}

	}

	@Override
	public GenericResponse getdistrictdashboard(CreateTicketDashboardDTO requestDto) {
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
		List<String> districtidcode = requestDto.getDistrictIdcode();

		List<Districtticketdashboard> ticketEntityList = createTicketRepository
				.getCountByStatusAndCreatedDateBetweenAndDistrictCode(fromDate, toDate, districtidcode);

		if (CollectionUtils.isEmpty(ticketEntityList)) {
			throw new RecordNotFoundException("No Record Found");
		}

		return Library.getSuccessfulResponse(ticketEntityList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

	public GenericResponse getByStatusdistrict(String name, String districtcode) {
		List<CreateTicketEntity> list = null;
		ArrayList<CreateTicketEntity> finallist = new ArrayList<CreateTicketEntity>();

		if (Objects.nonNull(name) && name.equalsIgnoreCase("Open")) {
			list = createTicketRepository.findByTicketOpenAndDistrictCode(districtcode);
		}
		if (Objects.nonNull(name) && name.equalsIgnoreCase("Pending")) {
			list = createTicketRepository.findByTicketPendingAndDistrictCode(districtcode);
		}
		if (Objects.nonNull(name) && name.equalsIgnoreCase("Closed")) {
			list = createTicketRepository.findByTicketClosedAndDistrictCode(districtcode);

		}

		if (Objects.nonNull(name) && name.equalsIgnoreCase("All")) {
			list = createTicketRepository.findAllByDistrictCode(districtcode);
		}

		if (CollectionUtils.isEmpty(list)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<CreateTicketResponseDto> responseDto = list.stream().map(createTicketMapper::convertEntityToResponseDTO)
				.collect(Collectors.toList());
		return Library.getSuccessfulResponse(responseDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getdistrictwiseincident(CreateTicketRequestDto requestDto) {

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
		Long incidentcategory = (long) 24;
		List<CreateTicketEntity> list = createTicketRepository.findByCreatedDateBetweenAndCategoryAndDistrictCodeAll(
				fromDate, toDate, incidentcategory, requestDto.getDistrictCode());

		if (CollectionUtils.isEmpty(list)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<CreateTicketResponseDto> responseDto = list.stream().map(createTicketMapper::convertEntityToResponseDTO)
				.collect(Collectors.toList());

		return Library.getSuccessfulResponse(responseDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	public GenericResponse searchByFilter(PaginationRequestDTO paginationDto) {
		Pageable pageable = null;
		Page<CreateTicketEntity> list = null;
		Date fromDate = null;
		Date toDate = null;
		Long incidentcategory = (long) 24;

		String districtcode = null;

		String rolecode = null;

		String fdate = null;

		String tdate = null;

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

			if (Objects.nonNull(paginationDto.getFilters().get("fromDate"))
					&& !paginationDto.getFilters().get("fromDate").toString().trim().isEmpty()) {
				try {
					try {
						fdate = String.valueOf(paginationDto.getFilters().get("fromDate").toString());
						fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fdate + " " + "00:00:00");
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

		if (Objects.nonNull(paginationDto.getFilters())) {

			if (Objects.nonNull(paginationDto.getFilters().get("toDate"))
					&& !paginationDto.getFilters().get("toDate").toString().trim().isEmpty()) {
				try {

					try {
						tdate = String.valueOf(paginationDto.getFilters().get("toDate").toString());
						toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(tdate + " " + "23:59:59");
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

		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("districtCode"))
					&& !paginationDto.getFilters().get("districtCode").toString().trim().isEmpty()) {
				try {
					districtcode = String.valueOf(paginationDto.getFilters().get("districtCode").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing referticNumber :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}
		}
		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("roleCode"))
					&& !paginationDto.getFilters().get("roleCode").toString().trim().isEmpty()) {
				try {
					rolecode = String.valueOf(paginationDto.getFilters().get("roleCode").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing rolecode :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}
		}

		list = getByFilter(fromDate, toDate, incidentcategory, districtcode, rolecode, pageable);
		if (Objects.isNull(list)) {
			list = createTicketRepository.getAll(pageable);
		}
		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}

		Page<CreateTicketResponseDto> finalResponse = list.map(createTicketMapper::convertEntityToResponseDTO);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}

	private Page<CreateTicketEntity> getByFilter(Date fromDate, Date toDate, Long incidentcategory, String districtcode,
			String rolecode, Pageable pageable) {
		Page<CreateTicketEntity> list = null;
		if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && Objects.nonNull(incidentcategory)
				&& Objects.nonNull(districtcode) && Objects.isNull(rolecode)) {
			list = createTicketRepository.findByCreatedDateBetweenAndCategoryAndDistrictCode(fromDate, toDate,
					incidentcategory, districtcode, pageable);
		}

		if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && Objects.nonNull(incidentcategory)
				&& Objects.isNull(districtcode) && Objects.nonNull(rolecode)) {
			list = createTicketRepository.findByCreatedDateBetweenAndCategoryAll(fromDate, toDate, incidentcategory,
					pageable);
		}

		return list;
	}

//	@Override
//	public GenericResponse getdistrictwiseincident( CreateTicketRequestDto requestDto) {
//		
//		final Date fromDate;
//		final Date toDate;
//		try {
//			fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(requestDto.getFromDate() + " " + "00:00:00");
//		} catch (ParseException e) {
//			log.error("error occurred while parsing date : {}", e.getMessage());
//			throw new InvalidDataValidation("Invalid date parameter passed");
//		}
//		try {
//			toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(requestDto.getToDate() + " " + "23:59:59");
//		} catch (ParseException e) {
//			log.error("error occurred while parsing date : {}", e.getMessage());
//			throw new InvalidDataValidation("Invalid date parameter passed");
//		}
//		Long incidentcategory=(long) 24;
//		
//		List<CreateTicketEntity> list = createTicketRepository.findByCreatedDateBetweenAndCategoryAndDistrictCode(fromDate,toDate,incidentcategory,requestDto.getDistrictCode());
//		
//		if (CollectionUtils.isEmpty(list)) {
//			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
//					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
//		}
//		List<CreateTicketResponseDto> responseDto = list.stream()
//				.map(createTicketMapper::convertEntityToResponseDTO).collect(Collectors.toList());
//		
//		
//		return Library.getSuccessfulResponse(responseDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
//				ErrorMessages.RECORED_FOUND);
//	}
//	

	// @Cacheable("dataCache")
	public GenericResponse searchByFilterdistricthadle(PaginationRequestDTO paginationDto) {
		Pageable pageable = null;

		Page<CreateTicketEntity> list = null;

		Date fromDate = null;

		Date toDate = null;

		String districtcode = null;

		List<Long> ticketstatus = new ArrayList<>();

		String fdate = null;

		String tdate = null;

		String districtId = null;

		// Long categoryName = null;

		String commonsearch = null;

		String unitName = null;

		String entityTypeId = null;

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

			if (Objects.nonNull(paginationDto.getFilters().get("fromDate"))
					&& !paginationDto.getFilters().get("fromDate").toString().trim().isEmpty()) {
				try {
					try {
						fdate = String.valueOf(paginationDto.getFilters().get("fromDate").toString());
						fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fdate + " " + "00:00:00");
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

		if (Objects.nonNull(paginationDto.getFilters())) {

			if (Objects.nonNull(paginationDto.getFilters().get("toDate"))
					&& !paginationDto.getFilters().get("toDate").toString().trim().isEmpty()) {
				try {

					try {
						tdate = String.valueOf(paginationDto.getFilters().get("toDate").toString());
						toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(tdate + " " + "23:59:59");
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

		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("districtIdcode"))
					&& !paginationDto.getFilters().get("districtIdcode").toString().trim().isEmpty()) {
				try {
					districtId = (String) (paginationDto.getFilters().get("districtIdcode"));
				} catch (Exception e) {
					log.error("error occurred while parsing referticNumber :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}
		}
		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("ticketStatus"))
					&& !paginationDto.getFilters().get("ticketStatus").toString().trim().isEmpty()) {
				try {
					String ticketStatusValue = paginationDto.getFilters().get("ticketStatus").toString().trim();
					if ("Open".equalsIgnoreCase(ticketStatusValue)) {
						ticketstatus.add(ticketStatusrepository.findByTicketStatusNames1("Open"));
						ticketstatus.add(ticketStatusrepository.findByTicketStatusNames1("Reopen"));

					} else if ("Closed".equalsIgnoreCase(ticketStatusValue)) {
						ticketstatus.add(ticketStatusrepository.findByTicketStatusNames1("Closed"));
						ticketstatus.add(ticketStatusrepository.findByTicketStatusNames1("Resolved"));
						ticketstatus.add(ticketStatusrepository.findByTicketStatusNames1("Call Closer Reported"));
					} else if ("Pending".equalsIgnoreCase(ticketStatusValue)) {
						ticketstatus.add(ticketStatusrepository.findByTicketStatusNames1("Pending"));
						ticketstatus.add(ticketStatusrepository.findByTicketStatusNames1("Assigned"));
						ticketstatus.add(ticketStatusrepository.findByTicketStatusNames1("Escalated"));
						ticketstatus.add(ticketStatusrepository.findByTicketStatusNames1("Change Request"));
						ticketstatus.add(ticketStatusrepository.findByTicketStatusNames1("Need Clarification"));
						ticketstatus
								.add(ticketStatusrepository.findByTicketStatusNames1("Need Clarification from Dept."));
						ticketstatus
								.add(ticketStatusrepository.findByTicketStatusNames1("Escalate to Service Provider"));
						ticketstatus.add(
								ticketStatusrepository.findByTicketStatusNames1("Escalate to Security Management"));
						ticketstatus.add(ticketStatusrepository.findByTicketStatusNames1("Recovery Time Objective"));
						ticketstatus.add(ticketStatusrepository.findByTicketStatusNames1("Recovery Point Objective"));
					} else if ("All".equalsIgnoreCase(ticketStatusValue)) {
						ticketstatus.add(ticketStatusrepository.findByTicketStatusNames1("Open"));
						ticketstatus.add(ticketStatusrepository.findByTicketStatusNames1("Reopen"));
						ticketstatus.add(ticketStatusrepository.findByTicketStatusNames1("Closed"));
						ticketstatus.add(ticketStatusrepository.findByTicketStatusNames1("Resolved"));
						ticketstatus.add(ticketStatusrepository.findByTicketStatusNames1("Call Closer Reported"));
						ticketstatus.add(ticketStatusrepository.findByTicketStatusNames1("Pending"));
						ticketstatus.add(ticketStatusrepository.findByTicketStatusNames1("Assigned"));
						ticketstatus.add(ticketStatusrepository.findByTicketStatusNames1("Escalated"));
						ticketstatus.add(ticketStatusrepository.findByTicketStatusNames1("Change Request"));
						ticketstatus.add(ticketStatusrepository.findByTicketStatusNames1("Need Clarification"));
						ticketstatus
								.add(ticketStatusrepository.findByTicketStatusNames1("Need Clarification from Dept."));
						ticketstatus
								.add(ticketStatusrepository.findByTicketStatusNames1("Escalate to Service Provider"));
						ticketstatus.add(
								ticketStatusrepository.findByTicketStatusNames1("Escalate to Security Management"));
						ticketstatus.add(ticketStatusrepository.findByTicketStatusNames1("Recovery Time Objective"));
						ticketstatus.add(ticketStatusrepository.findByTicketStatusNames1("Recovery Point Objective"));
					}
				} catch (Exception e) {
					log.error("Error occurred while parsing ticketStatus :: {}", e);
					return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
							ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "ticketStatus" }));
				}
			} else {
				return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
						ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { "ticketStatus" }));
			}
		}

//		if (Objects.nonNull(paginationDto.getFilters())) {
//			if (Objects.nonNull(paginationDto.getFilters().get("category"))
//					&& !paginationDto.getFilters().get("category").toString().trim().isEmpty()) {
//				try {
//					categoryName = Long.valueOf(paginationDto.getFilters().get("category").toString());
//				} catch (Exception e) {
//					log.error("error occurred while parsing rolecode :: {}", e);
//					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
//							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
//				}
//			}
//		}

		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("commonSearch"))
					&& !paginationDto.getFilters().get("commonSearch").toString().trim().isEmpty()) {
				commonsearch = String.valueOf(paginationDto.getFilters().get("commonSearch").toString());
			}
		}

		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("unitName"))
					&& !paginationDto.getFilters().get("unitName").toString().trim().isEmpty()) {
				unitName = String.valueOf(paginationDto.getFilters().get("unitName").toString());
			}
		}

		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("entityTypeId"))
					&& !paginationDto.getFilters().get("entityTypeId").toString().trim().isEmpty()) {
				unitName = String.valueOf(paginationDto.getFilters().get("entityTypeId").toString());
			}
		}

		list = getByFilterdistricthandle(fromDate, toDate, districtId, ticketstatus, commonsearch, unitName,
				entityTypeId, pageable);

		if (Objects.isNull(list)) {
			list = createTicketRepository.getAll(pageable);
		}
		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}

		Page<CreateTicketResponseDto> finalResponse = list.map(createTicketMapper::convertEntityToResponseDTO);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}

	private Page<CreateTicketEntity> getByFilterdistricthandle(Date fromDate, Date toDate, String districtId,
			List<Long> ticketstatus, String commonsearch, String unitName, String entityTypeId, Pageable pageable) {
		Page<CreateTicketEntity> list = null;
//		Long incidentcategory = (long) 24;

		list = createTicketRepository.searchdistrictlist(fromDate, toDate, districtId, ticketstatus, commonsearch,
				unitName, pageable);
//		if (Objects.isNull(categoryName)) {
//
////			if (Objects.nonNull(ticketstatus) && ticketstatus.equalsIgnoreCase("Open") && Objects.nonNull(fromDate)
////					&& Objects.nonNull(toDate)) {
//				list = createTicketRepository.findByTicketOpenAndDistrictCode(districtId, fromDate, toDate, pageable);
////			}
//
//			if (Objects.nonNull(ticketstatus) && ticketstatus.equalsIgnoreCase("Open") && Objects.nonNull(fromDate)
//					&& Objects.nonNull(toDate)) {
//				list = createTicketRepository.findByTicketOpen(fromDate, toDate, pageable);
//			}
//
//			if (Objects.nonNull(ticketstatus) && ticketstatus.equalsIgnoreCase("Open") && Objects.isNull(districtId)
//					&& Objects.isNull(fromDate) && Objects.isNull(toDate)) {
//				list = createTicketRepository.findByTicketOpenGet(pageable);
//			}
//
//			if (Objects.nonNull(ticketstatus) && ticketstatus.equalsIgnoreCase("Open") && districtId.isEmpty()
//					&& Objects.nonNull(fromDate) && Objects.nonNull(toDate) && Objects.isNull(commonsearch)) {
//				list = createTicketRepository.findByTicketOpen(fromDate, toDate, pageable);
//			} else if (!districtId.isEmpty() && ticketstatus.equalsIgnoreCase("Open")) {
//				list = createTicketRepository.findByTicketOpenAndDistrictCode(districtId, fromDate, toDate, pageable);
//			}
//
//			if (Objects.isNull(ticketstatus) && Objects.isNull(districtId) && Objects.nonNull(fromDate)
//					&& Objects.nonNull(toDate) && Objects.isNull(commonsearch)) {
//				list = createTicketRepository.findByTicketDate(fromDate, toDate, pageable);
//			}
//
//			if (Objects.isNull(ticketstatus) && Objects.nonNull(districtId) && Objects.nonNull(fromDate)
//					&& Objects.nonNull(toDate) && Objects.isNull(commonsearch)) {
//				list = createTicketRepository.findByTicketDateAndDistrictCode(districtId, fromDate, toDate, pageable);
//			}
//
//			if (Objects.isNull(ticketstatus) && Objects.nonNull(districtId) && Objects.nonNull(fromDate)
//					&& Objects.nonNull(toDate) && Objects.nonNull(commonsearch)) {
//				list = createTicketRepository.findByTicketDistrictCommon(districtId, fromDate, toDate, commonsearch,
//						commonsearch, commonsearch, pageable);
//			}
//
//			if (Objects.isNull(ticketstatus) && Objects.nonNull(districtId) && Objects.nonNull(fromDate)
//					&& Objects.nonNull(toDate) && Objects.nonNull(commonsearch)) {
//				list = createTicketRepository.findByTicketCommon(fromDate, toDate, commonsearch, commonsearch,
//						commonsearch, pageable);
//			}
//
//			if (Objects.isNull(ticketstatus) && Objects.nonNull(districtId) && Objects.nonNull(fromDate)
//					&& Objects.nonNull(toDate) && Objects.nonNull(commonsearch) && Objects.nonNull(unitName)) {
//				list = createTicketRepository.findByTicketDistrictCommonUnitName(districtId, fromDate, toDate,
//						commonsearch, commonsearch, commonsearch, unitName, pageable);
//			}
//
//			if (Objects.isNull(ticketstatus) && Objects.nonNull(districtId) && Objects.nonNull(fromDate)
//					&& Objects.nonNull(toDate) && Objects.isNull(commonsearch) && Objects.nonNull(unitName)) {
//				list = createTicketRepository.findByTicketDistrictUnitName(districtId, fromDate, toDate, unitName,
//						pageable);
//			}
//
//			if (Objects.isNull(ticketstatus) && Objects.isNull(districtId) && Objects.nonNull(fromDate)
//					&& Objects.nonNull(toDate) && Objects.isNull(commonsearch) && Objects.nonNull(unitName)) {
//				list = createTicketRepository.findByTicketUnitName(fromDate, toDate, unitName, pageable);
//			}
//
//			if (Objects.nonNull(ticketstatus) && ticketstatus.equalsIgnoreCase("Pending") && districtId.isEmpty()
//					&& Objects.nonNull(fromDate) && Objects.nonNull(toDate) && Objects.isNull(commonsearch)) {
//				list = createTicketRepository.findByTicketPending(fromDate, toDate, pageable);
//			} else if (!districtId.isEmpty() && ticketstatus.equalsIgnoreCase("Pending")) {
//
//				list = createTicketRepository.findByTicketPendingAndDistrictCode(districtId, fromDate, toDate,
//						pageable);
//
//			}
//
//			if (Objects.nonNull(ticketstatus) && ticketstatus.equalsIgnoreCase("Pending") && Objects.isNull(districtId)
//					&& Objects.isNull(fromDate) && Objects.isNull(toDate)) {
//				list = createTicketRepository.findByTicketPendingGet(pageable);
//			}
//
////			if (Objects.nonNull(ticketstatus) && ticketstatus.equalsIgnoreCase("Closed") && Objects.nonNull(fromDate)
////					&& Objects.nonNull(toDate)) {
////				list = createTicketRepository.findByTicketClosedAndDistrictCode(districtId, fromDate, toDate, pageable);
////
////			}
//
//			if (Objects.nonNull(ticketstatus) && ticketstatus.equalsIgnoreCase("Closed") && Objects.nonNull(fromDate)
//					&& Objects.nonNull(toDate) && districtId.isEmpty() && Objects.isNull(commonsearch)) {
//				list = createTicketRepository.findByTicketClosed(fromDate, toDate, pageable);
//
//			}
//
//			else if (!districtId.isEmpty() && ticketstatus.equalsIgnoreCase("Closed")) {
//				list = createTicketRepository.findByTicketClosedAndDistrictCode(districtId, fromDate, toDate, pageable);
//			}
//
//			if (Objects.nonNull(ticketstatus) && ticketstatus.equalsIgnoreCase("Closed") && Objects.isNull(districtId)
//					&& Objects.isNull(fromDate) && Objects.isNull(toDate)) {
//				list = createTicketRepository.findByTicketClosedGet(pageable);
//
//			}
//
//			if (Objects.nonNull(ticketstatus) && Objects.nonNull(districtId) && Objects.nonNull(fromDate)
//					&& Objects.nonNull(toDate) && Objects.nonNull(commonsearch) && Objects.nonNull(unitName)) {
//				list = createTicketRepository.findByTicketDistrictCommonUnitName(districtId, fromDate, toDate,
//						commonsearch, commonsearch, commonsearch, unitName, pageable);
//			}
//
//			// code Changed
////			if (Objects.nonNull(ticketstatus) && ticketstatus.equalsIgnoreCase("All") && districtId.isEmpty()
////					&& Objects.nonNull(fromDate) && Objects.nonNull(toDate)) {
////				list = createTicketRepository.findAllByFromDateAndToDateAndPageable(fromDate, toDate, pageable);
////			} else {
////				list = createTicketRepository.findAllByDistrictCode(districtId, fromDate, toDate, pageable);
////			}
//
////			if (Objects.nonNull(ticketstatus) && ticketstatus.equalsIgnoreCase("All") && Objects.nonNull(fromDate)
////					&& Objects.nonNull(toDate)) {
////				list = createTicketRepository.findAllByDistrictCode(districtId, fromDate, toDate, pageable);
////			}
//
//			if (Objects.nonNull(ticketstatus) && ticketstatus.equalsIgnoreCase("All") && Objects.nonNull(fromDate)
//					&& Objects.nonNull(toDate) && districtId.isEmpty() && Objects.isNull(commonsearch)) {
//				list = createTicketRepository.findAllBy(fromDate, toDate, pageable);
//			} else if (!districtId.isEmpty() && ticketstatus.equalsIgnoreCase("All")) {
//				list = createTicketRepository.findAllByDistrictCode(districtId, fromDate, toDate, pageable);
//			}
//
//			if (Objects.nonNull(ticketstatus) && ticketstatus.equalsIgnoreCase("All") && Objects.isNull(districtId)
//					&& Objects.isNull(fromDate) && Objects.isNull(toDate)) {
//				list = createTicketRepository.findAllByGet(pageable);
//			}
//
//			if (Objects.nonNull(ticketstatus) && ticketstatus.equalsIgnoreCase("All") && districtId.isEmpty()
//					&& Objects.nonNull(commonsearch)) {
//				list = createTicketRepository.findAllByCommon(fromDate, toDate, commonsearch, commonsearch,
//						commonsearch, pageable);
//			}
//
//			if (Objects.nonNull(ticketstatus) && ticketstatus.equalsIgnoreCase("All") && !districtId.isEmpty()
//					&& Objects.nonNull(commonsearch)) {
//				list = createTicketRepository.findAllByCommonDistricode(fromDate, toDate, commonsearch, commonsearch,
//						commonsearch, districtId, pageable);
//			}
//
//			if (Objects.nonNull(ticketstatus) && ticketstatus.equalsIgnoreCase("Open") && !districtId.isEmpty()
//					&& Objects.nonNull(commonsearch)) {
//				list = createTicketRepository.findByTicketDateAndDistrictCodeAndcommonserach(districtId, fromDate,
//						toDate, commonsearch, commonsearch, commonsearch, pageable);
//			}
//
//			if (Objects.nonNull(ticketstatus) && ticketstatus.equalsIgnoreCase("Open") && !districtId.isEmpty()
//					&& Objects.isNull(commonsearch)) {
//				list = createTicketRepository.findByTicketDateAndDistrictCodelist(districtId, fromDate, toDate,
//						pageable);
//			}
//
//			if (Objects.nonNull(ticketstatus) && ticketstatus.equalsIgnoreCase("Open") && districtId.isEmpty()
//					&& Objects.nonNull(commonsearch)) {
//				list = createTicketRepository.findAllByTicketCommonSearch(fromDate, toDate, commonsearch, commonsearch,
//						commonsearch, pageable);
//			}
//			// closed
//			if (Objects.nonNull(ticketstatus) && ticketstatus.equalsIgnoreCase("Closed") && !districtId.isEmpty()
//					&& Objects.nonNull(commonsearch)) {
//				list = createTicketRepository.findByTicketDateAndDistrictCodeAndcommonserachClosed(districtId, fromDate,
//						toDate, commonsearch, commonsearch, commonsearch, pageable);
//			}
//
//			if (Objects.nonNull(ticketstatus) && ticketstatus.equalsIgnoreCase("Closed") && !districtId.isEmpty()
//					&& Objects.isNull(commonsearch)) {
//				list = createTicketRepository.findByTicketDateAndDistrictCodelistClosed(districtId, fromDate, toDate,
//						pageable);
//			}
//
//			if (Objects.nonNull(ticketstatus) && ticketstatus.equalsIgnoreCase("Closed") && districtId.isEmpty()
//					&& Objects.nonNull(commonsearch)) {
//				list = createTicketRepository.findAllByTicketCommonSearchClosed(fromDate, toDate, commonsearch,
//						commonsearch, commonsearch, pageable);
//			}
//			// pending
//			if (Objects.nonNull(ticketstatus) && ticketstatus.equalsIgnoreCase("Pending") && !districtId.isEmpty()
//					&& Objects.nonNull(commonsearch)) {
//				list = createTicketRepository.findByTicketDateAndDistrictCodeAndcommonserachPending(districtId,
//						fromDate, toDate, commonsearch, commonsearch, commonsearch, pageable);
//			}
//
//			if (Objects.nonNull(ticketstatus) && ticketstatus.equalsIgnoreCase("Pending") && !districtId.isEmpty()
//					&& Objects.isNull(commonsearch)) {
//				list = createTicketRepository.findByTicketDateAndDistrictCodelistPending(districtId, fromDate, toDate,
//						pageable);
//			}
//
//			if (Objects.nonNull(ticketstatus) && ticketstatus.equalsIgnoreCase("Pending") && districtId.isEmpty()
//					&& Objects.nonNull(commonsearch)) {
//				list = createTicketRepository.findAllByTicketCommonSearchPending(fromDate, toDate, commonsearch,
//						commonsearch, commonsearch, pageable);
//			}
//
//		} else {
//
//			if (Objects.nonNull(ticketstatus) && ticketstatus.equalsIgnoreCase("Open") && Objects.isNull(districtId)
//					&& Objects.isNull(commonsearch)) {
//				list = createTicketRepository.findByTicketOpenAndIncidentcategory(fromDate, toDate, incidentcategory,
//						pageable);
//			}
//
//			if (Objects.nonNull(ticketstatus) && ticketstatus.equalsIgnoreCase("Pending") && Objects.isNull(districtId)
//					&& Objects.isNull(commonsearch)) {
//				list = createTicketRepository.findByTicketPendingAndIncidentcategory(fromDate, toDate, incidentcategory,
//						pageable);
//			}
//
//			if (Objects.nonNull(ticketstatus) && ticketstatus.equalsIgnoreCase("Closed") && Objects.isNull(districtId)
//					&& Objects.isNull(commonsearch)) {
//				list = createTicketRepository.findByTicketClosedAndIncident(fromDate, toDate, incidentcategory,
//						pageable);
//
//			}
//
//			if (Objects.nonNull(ticketstatus) && ticketstatus.equalsIgnoreCase("All") && Objects.isNull(districtId)
//					&& Objects.isNull(commonsearch)) {
//				list = createTicketRepository.findAllByIncident(fromDate, toDate, incidentcategory, pageable);
//			}
//
//			if (Objects.nonNull(ticketstatus) && ticketstatus.equalsIgnoreCase("Open") && Objects.isNull(districtId)
//					&& Objects.nonNull(commonsearch)) {
//				list = createTicketRepository.findByTicketOpenAndIncidentAndLCTNSPC(fromDate, toDate, incidentcategory,
//						commonsearch, commonsearch, commonsearch, pageable);
//			}
//
//			if (Objects.nonNull(ticketstatus) && ticketstatus.equalsIgnoreCase("Pending") && Objects.isNull(districtId)
//					&& Objects.nonNull(commonsearch)) {
//				list = createTicketRepository.findByTicketPendingAndIncidentAndTNCNSPCO(fromDate, toDate,
//						incidentcategory, commonsearch, commonsearch, commonsearch, pageable);
//			}
//
//			if (Objects.nonNull(ticketstatus) && ticketstatus.equalsIgnoreCase("Closed") && Objects.isNull(districtId)
//					&& Objects.nonNull(commonsearch)) {
//				list = createTicketRepository.findByTicketClosedAndIncidentAndTNCNSPCO(fromDate, toDate,
//						incidentcategory, commonsearch, commonsearch, commonsearch, pageable);
//
//			}
//
//			if (Objects.nonNull(ticketstatus) && ticketstatus.equalsIgnoreCase("All") && Objects.isNull(districtId)
//					&& Objects.isNull(commonsearch)) {
//				list = createTicketRepository.findAllByIncidentAndLNSCTN(fromDate, toDate, incidentcategory,
//						commonsearch, commonsearch, commonsearch, pageable);
//			}
//
//		}
		return list;
	}

	@Override
	public GenericResponse getCount_percentageincident(AuthenticationDTO authenticationDTO) {
		final Date finalDate;
		Integer tictotalcount = 0;
		List<TicketCountResponseDTO> ticketCountResponseDTOList = new ArrayList<>();
		List<TicketCountResponseDTO> ticketCountResponseDTOList1 = new ArrayList<>();
		List<TicketStatusEntity> ticketStatusList = ticketStatusrepository.findAllByStatusOrderByModifiedDateDesc();
		List<TicketStatusEntity> ticketStatusList1 = ticketStatusrepository.findAllByStatusOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(ticketStatusList)) {
			throw new RecordNotFoundException("No Record Found");
		}
		Long incidentcategory = (long) 24;
		ticketStatusList.forEach(ticketStatus -> {
			if (Objects.isNull(ticketStatus.getId()) || StringUtils.isBlank(ticketStatus.getTicketstatusname())) {
				return;
			}
			TicketCountResponseDTO ticketCountResponseDTO = new TicketCountResponseDTO();
			ticketCountResponseDTO.setTotcount(createTicketRepository.getCountincident(incidentcategory));
			int total = ticketCountResponseDTO.getTotcount();
			double totalticket = total;
			System.out.println("Totalticket::" + totalticket);
			ticketCountResponseDTO.setStatus(ticketStatus.getTicketstatusname());
			if (Objects.isNull(authenticationDTO) || Objects.isNull(authenticationDTO.getUserId())) {
				log.error("invalid authentication details : {}", authenticationDTO);
				throw new RecordNotFoundException("No Record Found");
			}

//			if (authenticationDTO.getRoleCodes().stream()
//					.anyMatch(r -> Constant.DEPARTMENT.equals(r))) {
//				ticketCountResponseDTO.setCount(createTicketRepository.getCountByStatusAndCreatedByperIncident(ticketStatus.getId(), authenticationDTO.getUserId(),incidentcategory));
//			}

			ticketCountResponseDTO.setCount(
					createTicketRepository.getCountByStatusAndperIncident(ticketStatus.getId(), incidentcategory));
			// ticketCountResponseDTO.setCount(createTicketRepository.getCountByStatusAndCreatedByper(ticketStatus.getId(),
			// authenticationDTO.getUserId()));
//         	if (authenticationDTO.getRoleCodes().stream().anyMatch(r -> Constant.HELPDESK_ADMIN.equals(r))) {
//				ticketCountResponseDTO.setCount(createTicketRepository.getCountBySAdminIncident(ticketStatus.getId(),incidentcategory));
//				
//			} else if (authenticationDTO.getRoleCodes().stream()
//					.anyMatch(r -> Constant.HELPDESK_EXECUTIVE_ROLE.equals(r))) {
//				ticketCountResponseDTO.setCount(createTicketRepository.getCountByStatusAndCreatedByperIncident(ticketStatus.getId(), authenticationDTO.getUserId(),incidentcategory));
//			}
			double earnedcount = (ticketCountResponseDTO.getCount());
			System.out.println("PERCENTAGE:::" + (earnedcount / totalticket) * 100);
			Double percentage = (earnedcount / totalticket) * 100;
			ticketCountResponseDTO.setPercentage(percentage);
			ticketCountResponseDTOList.add(ticketCountResponseDTO);
		});

//		for(int i=0;i<ticketCountResponseDTOList.size();i++) {
//			tictotalcount+=ticketCountResponseDTOList.get(i).getCount();
//		}
//		
//		System.out.println("TOTALCOUNT:::" + tictotalcount);
//		Integer tot=tictotalcount;
//		ticketStatusList1.forEach(ticketStatus1 -> {
//			if (Objects.isNull(ticketStatus1.getId()) || StringUtils.isBlank(ticketStatus1.getTicketstatusname())) {
//				return;
//			}
//			TicketCountResponseDTO ticketCountResponseDTO1 = new TicketCountResponseDTO();
//			
//			ticketCountResponseDTO1.setTotcount(tot);
//			int total=ticketCountResponseDTO1.getTotcount();
//			double totalticket=total;
//		   System.out.println("Totalticket::" + totalticket);
//			ticketCountResponseDTO1.setStatus(ticketStatus1.getTicketstatusname());
//			if(Objects.isNull(authenticationDTO) || Objects.isNull(authenticationDTO.getUserId())) {
//				log.error("invalid authentication details : {}", authenticationDTO);
//				throw new RecordNotFoundException("No Record Found");
//			}
//			
//			
//			ticketCountResponseDTO1.setCount(createTicketRepository.getCountByStatusAndCreatedByper(ticketStatus1.getId(), authenticationDTO.getUserId(),incidentcategory));
//
//	
////			if (authenticationDTO.getRoleCodes().stream().anyMatch(r -> Constant.HELPDESK_ADMIN.equals(r))) {
////				ticketCountResponseDTO1.setCount(createTicketRepository.getCountBySAdmin(ticketStatus1.getId()));
////				
////			} else if (authenticationDTO.getRoleCodes().stream()
////					.anyMatch(r -> Constant.HELPDESK_EXECUTIVE_ROLE.equals(r))) {
////				ticketCountResponseDTO1.setCount(createTicketRepository.getCountByStatusAndCreatedByper(ticketStatus1.getId(), authenticationDTO.getUserId()));
////			}
//			
//			
//			double earnedcount = (ticketCountResponseDTO1.getCount());
//            System.out.println("PERCENTAGE:::" + (earnedcount / totalticket) * 100);
//            Double percentage = (earnedcount / totalticket) * 100;
//            ticketCountResponseDTO1.setPercentage(percentage);
//            ticketCountResponseDTOList1.add(ticketCountResponseDTO1);
//		});

		return Library.getSuccessfulResponse(ticketCountResponseDTOList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

	@Override
	public GenericResponse getbyMonthincident(AuthenticationDTO authenticationDTO) {
		Long incidentcategory = (long) 24;
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

		List<TicketCountResponseDTO> ticketCountResponseDTOList = new ArrayList<>();

		List<TicketStatusEntity> ticketStatusList = ticketStatusrepository.findAllByStatusOrderByModifiedDateDesc();

		List<TicketStatusEntity> ticketStatusList1 = ticketStatusrepository.findAllByStatusOrderByModifiedDateDesc();

		List<TicketStatusEntity> ticketStatusListApril = ticketStatusrepository
				.findAllByStatusOrderByModifiedDateDesc();

		if (CollectionUtils.isEmpty(ticketStatusList1)) {
			throw new RecordNotFoundException("No Record Found");
		}

		if (CollectionUtils.isEmpty(ticketStatusList)) {
			throw new RecordNotFoundException("No Record Found");
		}

		ticketStatusList1.forEach(ticketStatus1 -> {
			if (Objects.isNull(ticketStatus1.getId()) || StringUtils.isBlank(ticketStatus1.getTicketstatusname())) {
				return;
			}
			TicketCountResponseDTO ticketCountResponseDTO1 = new TicketCountResponseDTO();
			ticketCountResponseDTO1.setTotcount(createTicketRepository.getCountsep(incidentcategory));
			int total1 = ticketCountResponseDTO1.getTotcount();
			double totalticket1 = total1;
			System.out.println("Totalticket::" + totalticket1);
			ticketCountResponseDTO1.setStatus(ticketStatus1.getTicketstatusname());
			if (Objects.isNull(authenticationDTO) || Objects.isNull(authenticationDTO.getUserId())) {
				log.error("invalid authentication details : {}", authenticationDTO);
				throw new RecordNotFoundException("No Record Found");
			}
			ticketCountResponseDTO1.setCount(
					createTicketRepository.getCountByStatusAndCategory(ticketStatus1.getId(), incidentcategory));

//			if (authenticationDTO.getRoleCodes().stream().anyMatch(r -> Constant.HELPDESK_ADMIN.equals(r))) {
//				ticketCountResponseDTO1.setCount(
//						createTicketRepository.getCountByFebHelpdeskCategory(ticketStatus1.getId(),incidentcategory));
//			} else if (authenticationDTO.getRoleCodes().stream()
//					.anyMatch(r -> Constant.HELPDESK_EXECUTIVE_ROLE.equals(r))) {
//				ticketCountResponseDTO1.setCount(createTicketRepository.getCountByStatusAndCreatedByCategory(ticketStatus1.getId(), authenticationDTO.getUserId(),incidentcategory));
//			} 

			double earnedcount = (ticketCountResponseDTO1.getCount());
			System.out.println("PERCENTAGE:::" + (earnedcount / totalticket1) * 100);
			Double percentage = (earnedcount / totalticket1) * 100;
			ticketCountResponseDTO1.setPercentage(percentage);
			ticketCountResponseDTO1.setMonth("July");
			ticketCountResponseDTOList.add(ticketCountResponseDTO1);
		});

		ticketStatusList.forEach(ticketStatus -> {
			if (Objects.isNull(ticketStatus.getId()) || StringUtils.isBlank(ticketStatus.getTicketstatusname())) {
				return;
			}
			TicketCountResponseDTO ticketCountResponseDTO = new TicketCountResponseDTO();
			ticketCountResponseDTO.setTotcount(createTicketRepository.getCountoct(incidentcategory));
			int total = ticketCountResponseDTO.getTotcount();
			double totalticket = total;
			System.out.println("Totalticket::" + totalticket);
			ticketCountResponseDTO.setStatus(ticketStatus.getTicketstatusname());
			if (Objects.isNull(authenticationDTO) || Objects.isNull(authenticationDTO.getUserId())) {
				log.error("invalid authentication details : {}", authenticationDTO);
				throw new RecordNotFoundException("No Record Found");
			}

			ticketCountResponseDTO.setCount(
					createTicketRepository.getCountByStatusAndOctCate(ticketStatus.getId(), incidentcategory));
//			if (authenticationDTO.getRoleCodes().stream().anyMatch(r -> Constant.HELPDESK_ADMIN.equals(r))) {
//				ticketCountResponseDTO.setCount(
//						createTicketRepository.getCountByStatusAndHelpdeskCate(ticketStatus.getId(),incidentcategory));
//			} else if (authenticationDTO.getRoleCodes().stream()
//					.anyMatch(r -> Constant.HELPDESK_EXECUTIVE_ROLE.equals(r))) {
//				ticketCountResponseDTO.setCount(createTicketRepository.getCountByStatusAndCreatedByOctCate(ticketStatus.getId(), authenticationDTO.getUserId(),incidentcategory));
//			} 

			double earnedcount = (ticketCountResponseDTO.getCount());
			System.out.println("PERCENTAGE:::" + (earnedcount / totalticket) * 100);
			Double percentage = (earnedcount / totalticket) * 100;
			ticketCountResponseDTO.setMonth("August");
			ticketCountResponseDTO.setPercentage(percentage);
			ticketCountResponseDTOList.add(ticketCountResponseDTO);

		});

		ticketStatusListApril.forEach(ticketStatus2 -> {
			if (Objects.isNull(ticketStatus2.getId()) || StringUtils.isBlank(ticketStatus2.getTicketstatusname())) {
				return;
			}
			TicketCountResponseDTO ticketCountResponseDTO2 = new TicketCountResponseDTO();
			ticketCountResponseDTO2.setTotcount(createTicketRepository.getCountNov(incidentcategory));
			int total1 = ticketCountResponseDTO2.getTotcount();
			double totalticket1 = total1;
			System.out.println("Totalticket::" + totalticket1);
			ticketCountResponseDTO2.setStatus(ticketStatus2.getTicketstatusname());
			if (Objects.isNull(authenticationDTO) || Objects.isNull(authenticationDTO.getUserId())) {
				log.error("invalid authentication details : {}", authenticationDTO);
				throw new RecordNotFoundException("No Record Found");
			}
			// ticketCountResponseDTO2.setCount(createTicketRepository.getCountByStatusAndCreatedByNovCate(ticketStatus2.getId(),
			// authenticationDTO.getUserId(),incidentcategory));
			ticketCountResponseDTO2.setCount(
					createTicketRepository.getCountByStatusAndNovCate(ticketStatus2.getId(), incidentcategory));
//			if (authenticationDTO.getRoleCodes().stream().anyMatch(r -> Constant.HELPDESK_ADMIN.equals(r))) {
//				ticketCountResponseDTO2.setCount(
//						createTicketRepository.getCountByStatusAndHelpdeskCateNov(ticketStatus2.getId(),incidentcategory));
//			} else if (authenticationDTO.getRoleCodes().stream()
//					.anyMatch(r -> Constant.HELPDESK_EXECUTIVE_ROLE.equals(r))) {
//				ticketCountResponseDTO2.setCount(createTicketRepository.getCountByStatusAndCreatedByNovCate(ticketStatus2.getId(), authenticationDTO.getUserId(),incidentcategory));
//			} 
//			

			double earnedcount = (ticketCountResponseDTO2.getCount());
			System.out.println("PERCENTAGE:::" + (earnedcount / totalticket1) * 100);
			Double percentage = (earnedcount / totalticket1) * 100;
			ticketCountResponseDTO2.setPercentage(percentage);
			ticketCountResponseDTO2.setMonth("September");
			ticketCountResponseDTOList.add(ticketCountResponseDTO2);
		});

		return Library.getSuccessfulResponse(ticketCountResponseDTOList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

	@Override
	public GenericResponse getResolutionofticketssla12hrs(CreateTicketDashboardDTO requestDto) {

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

		List<ResolutionDTO> ticketresolutionList = null;

		List<ResolutionDTOTickets> finallist = new ArrayList<>();

		ticketresolutionList = createTicketRepository.getCountByCreatedDateBetweenPos(fromDate, toDate);

		try {

			for (int i = 0; i < ticketresolutionList.size(); i++) {
				ResolutionDTOTickets obj = new ResolutionDTOTickets();

				obj.setAbove12HoursTickets(ticketresolutionList.get(i).getAbove12HoursTickets());

				obj.setTotalRaisedTicketsINYEAR2023(ticketresolutionList.get(i).getTotalRaisedTicketsINYEAR2023());

				obj.setBelow12HoursTickets(ticketresolutionList.get(i).getBelow12HoursTickets());

				Integer totaltickets_raised = obj.getTotalRaisedTicketsINYEAR2023();

				Integer slabelow12hours = obj.getBelow12HoursTickets();

				Integer slaabove12hours = obj.getAbove12HoursTickets();

				float resolnpercentage = ((totaltickets_raised - slaabove12hours) / (totaltickets_raised * 1.0f)) * 100;

				obj.setResolutionPercentage(resolnpercentage);

				obj.setMonth(ticketresolutionList.get(i).getMonth());
				finallist.add(obj);
			}
		} catch (Exception e) {

		}

		if (CollectionUtils.isEmpty(finallist)) {
			throw new RecordNotFoundException("No Record Found");
		}

		return Library.getSuccessfulResponse(finallist, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

	public GenericResponse getResolutionofticketssla24hrs(CreateTicketDashboardDTO requestDto) {
		List<Resolution24hrDTO> resolutionlist = null;
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

		List<Resolution24hrDTO> ticketresolutionList = null;

		List<ResolutionTicket24hrDTO> finallist = new ArrayList<>();

		ticketresolutionList = createTicketRepository.getCountByCreatedDateBetweenPos24(fromDate, toDate);

		try {
			for (int i = 0; i < ticketresolutionList.size(); i++) {

				ResolutionTicket24hrDTO obj = new ResolutionTicket24hrDTO();
				obj.setAbove24HoursTickets(ticketresolutionList.get(i).getAbove24HoursTickets());
				obj.setTotalRaisedTicketsINYEAR2023(ticketresolutionList.get(i).getTotalRaisedTicketsINYEAR2023());
				obj.setBelow24HoursTickets(ticketresolutionList.get(i).getBelow24HoursTickets());
				Integer totaltickets_raised = obj.getTotalRaisedTicketsINYEAR2023();
				// Integer slaabove12hours = obj.getBelow12HoursTickets();
				Integer slaabove24hours = obj.getAbove24HoursTickets();
				float resolnpercentage = ((totaltickets_raised - slaabove24hours) / (totaltickets_raised * 1.0f)) * 100;
				obj.setResolutionPercentage(resolnpercentage);
				obj.setMonth(ticketresolutionList.get(i).getMonth());
				finallist.add(obj);
			}
		} catch (Exception e) {

		}
		if (CollectionUtils.isEmpty(finallist)) {
			throw new RecordNotFoundException("No Record Found");
		}

		return Library.getSuccessfulResponse(finallist, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

	@Override
	public GenericResponse getTollFreeSummary(CreateTicketDashboardDTO requestDto) {
		String fromDateStr = requestDto.getFromDate(); // Get start date from request DTO
		String toDateStr = requestDto.getToDate(); // Get end date from request DTO

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Date format for parsing

		Date fromDate;
		Date toDate;
		try {
			fromDate = sdf.parse(fromDateStr); // Parse start date
			toDate = sdf.parse(toDateStr); // Parse end date
		} catch (ParseException e) {
			log.error("Error occurred while parsing date: {}", e.getMessage());
			throw new InvalidDataValidation("Invalid date parameter passed"); // Handle parsing error
		}

		// Calculate total operating hours
		long durationMillis = toDate.getTime() - fromDate.getTime(); // Calculate duration in milliseconds
		long totalOperatingHours = durationMillis / (1000 * 60 * 60); // Convert milliseconds to hours

		try {
			// Fetch downtime data from repository
			List<InboundCallsTotalOperatinghrsDTO> downtimeList = createTicketRepository.getDowntimeHrs(fromDate,
					toDate);
			List<DowntimeDTO> downList = new ArrayList<>(); // List to hold downtime data
			List<TollfreeDashboardDTO> tollFreeDashboardList = new ArrayList<>(); // List to hold final dashboard data

			// Process each downtime record
			downtimeList.forEach(downtime -> {
				DowntimeDTO downtimeDTO = new DowntimeDTO();
				String hoursStr = downtime.getDown_Time().replace("hours", "").trim(); // Extract hours from downtime
																						// string
				int usedHrs = Integer.parseInt(hoursStr); // Convert to integer
				downtimeDTO.setHours(usedHrs); // Set hours in DTO
				downList.add(downtimeDTO); // Add to list
			});

			TollfreeDashboardDTO tollfreeDashboard = new TollfreeDashboardDTO();
			if (downList.isEmpty()) {
				DowntimeDTO downtimeDTO = new DowntimeDTO();
				downtimeDTO.setHours(0); // Set default value if no data
				downList.add(downtimeDTO);
			}

			// Calculate total downtime
			int totalDownTime = downList.stream().filter(Objects::nonNull).mapToInt(DowntimeDTO::getHours).sum();
			// Calculate total uptime
			int totalUptime = (int) totalOperatingHours - totalDownTime;
			// Calculate uptime percentage
			float uptimePercentage = ((float) totalUptime / totalOperatingHours) * 100;

			// Set values in dashboard DTO
			tollfreeDashboard.setTotalOpreatingHrs((int) totalOperatingHours);
			tollfreeDashboard.setTotalDownTimeHrs(totalDownTime);
			tollfreeDashboard.setTotalUptimeHrs(totalUptime);
			tollfreeDashboard.setUptimePercentage(uptimePercentage);

			// Add dashboard DTO to list
			tollFreeDashboardList.add(tollfreeDashboard);
			// Return successful response with data
			return Library.getSuccessfulResponse(tollFreeDashboardList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		} catch (RecordNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e); // Handle record not found
																						// exception
		}
	}

	@Override
	public GenericResponse getsecuritymanagements(CreateTicketDashboardDTO requestDto) {

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
		List<SecuritymanagementDTO> ticketresscuritymanagementlist = null;

		List<SecuritymanagementTicketsDTO> finallist = new ArrayList<>();

		ticketresscuritymanagementlist = createTicketRepository
				.getCountByCreatedDateBetweensecuritymanagementPos(fromDate, toDate);

		try {
			for (int i = 0; i < ticketresscuritymanagementlist.size(); i++) {
				SecuritymanagementTicketsDTO obj = new SecuritymanagementTicketsDTO();

				obj.setTotal_Tickets_Raised(ticketresscuritymanagementlist.get(i).getTotal_Tickets_Raised());

				obj.setTickets_Closed_Inprogress_Within_SLA(
						ticketresscuritymanagementlist.get(i).getTickets_Closed_Inprogress_Within_SLA());

				obj.setTickets_Breached_SLA(ticketresscuritymanagementlist.get(i).getTickets_Breached_SLA());

				Integer totalTickets_Raised = obj.getTotal_Tickets_Raised();

				Integer ticketsClosedInprogresswithinSLA = obj.getTickets_Closed_Inprogress_Within_SLA();

				Integer ticketsBreachedSLA = obj.getTickets_Breached_SLA();

				float resolnpercentage = ((totalTickets_Raised - ticketsBreachedSLA) / (totalTickets_Raised * 1.0f))
						* 100;

				obj.setResolution_Percentage(resolnpercentage);
				finallist.add(obj);
			}

		} catch (Exception e) {

		}

		if (CollectionUtils.isEmpty(finallist)) {
			throw new RecordNotFoundException("No Record Found");
		}

		return Library.getSuccessfulResponse(finallist, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

	@Override
	public GenericResponse getAllByRequestFilterlicensenr(PaginationRequestDTO paginationDto) throws ParseException {

		Pageable pageable = null;
		Page<CreateTicketEntity> list = null;
		Long assignToId = null;
		Date fromDate = null;
		Date toDate = null;

		String ticketstatusname = null;

		List<String> licenseNo = null;

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
			if (Objects.nonNull(paginationDto.getFilters().get("fromDate"))
					&& !paginationDto.getFilters().get("fromDate").toString().trim().isEmpty())
				try {
					fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.parse(paginationDto.getFilters().get("fromDate") + " " + "00:00:00");
				} catch (ParseException e) {
					log.error("error occurred while parsing date : {}", e.getMessage());
					throw new InvalidDataValidation("Invalid date parameter passed");
				}
		}
		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("toDate"))
					&& !paginationDto.getFilters().get("toDate").toString().trim().isEmpty())

				try {
					toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.parse(paginationDto.getFilters().get("toDate") + " " + "23:59:59");
				} catch (ParseException e) {
					log.error("error occurred while parsing date : {}", e.getMessage());
					throw new InvalidDataValidation("Invalid date parameter passed");
				}
		}

		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("ticketstatusName"))
					&& !paginationDto.getFilters().get("ticketstatusName").toString().trim().isEmpty())
				ticketstatusname = String.valueOf(paginationDto.getFilters().get("ticketstatusName").toString());
		}

		if (Objects.nonNull(paginationDto.getFilters().get("licenseNumber"))
				&& !paginationDto.getFilters().get("licenseNumber").toString().trim().isEmpty()) {
			licenseNo = (List<String>) (paginationDto.getFilters().get("licenseNumber"));

		}

		if (Objects.nonNull(ticketstatusname) && ticketstatusname.equalsIgnoreCase("Open") && Objects.isNull(fromDate)
				&& Objects.isNull(toDate)) {
			list = createTicketRepository.findByTicketOpenNew(licenseNo, licenseNo, pageable);
		}

		if (Objects.nonNull(ticketstatusname) && ticketstatusname.equalsIgnoreCase("Assigned")
				&& Objects.nonNull(fromDate) && Objects.nonNull(toDate)) {
			list = createTicketRepository.findByTicketAssignedNew(licenseNo, licenseNo, fromDate, toDate, pageable);
		}

		if (Objects.nonNull(ticketstatusname) && ticketstatusname.equalsIgnoreCase("Open") && Objects.nonNull(fromDate)
				&& Objects.nonNull(toDate)) {
			list = createTicketRepository.findByTicketOpenFTDateNew(licenseNo, licenseNo, fromDate, toDate, pageable);
		}

		if (Objects.nonNull(ticketstatusname) && ticketstatusname.equalsIgnoreCase("Pending")
				&& Objects.isNull(fromDate) && Objects.isNull(toDate)) {
			list = createTicketRepository.findByTicketPendingNew(licenseNo, licenseNo, pageable);
		}

		if (Objects.nonNull(ticketstatusname) && ticketstatusname.equalsIgnoreCase("Pending")
				&& Objects.nonNull(fromDate) && Objects.nonNull(toDate)) {
			list = createTicketRepository.findByTicketPendingFTDateNew(licenseNo, licenseNo, fromDate, toDate,
					pageable);
		}

		if (Objects.nonNull(ticketstatusname) && ticketstatusname.equalsIgnoreCase("Closed") && Objects.isNull(fromDate)
				&& Objects.isNull(toDate)) {
			list = createTicketRepository.findByTicketClosedReCALNew(licenseNo, licenseNo, pageable);
		}

		if (Objects.nonNull(ticketstatusname) && ticketstatusname.equalsIgnoreCase("Closed")
				&& Objects.nonNull(fromDate) && Objects.nonNull(toDate)) {
			list = createTicketRepository.findByTicketClosedReCALFTDateNew(licenseNo, licenseNo, fromDate, toDate,
					pageable);
		}

		if (Objects.nonNull(ticketstatusname) && ticketstatusname.equalsIgnoreCase("Reopen") && Objects.isNull(fromDate)
				&& Objects.isNull(toDate)) {
			list = createTicketRepository.findByTicketReopenNew(licenseNo, licenseNo, pageable);
		}

		if (Objects.nonNull(ticketstatusname) && ticketstatusname.equalsIgnoreCase("Reopen")
				&& Objects.nonNull(fromDate) && Objects.nonNull(toDate)) {
			list = createTicketRepository.findByTicketReopenFTDateNew(licenseNo, licenseNo, fromDate, toDate, pageable);
		}

		if (Objects.nonNull(ticketstatusname) && ticketstatusname.equalsIgnoreCase("Escalated")) {
			list = createTicketRepository.findByTicketEscalatedNew(licenseNo, licenseNo, pageable);
		}

		if (Objects.nonNull(ticketstatusname) && ticketstatusname.equalsIgnoreCase("All") && Objects.isNull(fromDate)
				&& Objects.isNull(toDate)) {
			list = createTicketRepository.findAllByIssueFromNew(licenseNo, licenseNo, pageable);

		}

		if (Objects.nonNull(ticketstatusname) && ticketstatusname.equalsIgnoreCase("All") && Objects.nonNull(fromDate)
				&& Objects.nonNull(toDate)) {
			list = createTicketRepository.findAllByIssueFromFTDateNew(licenseNo, fromDate, toDate, pageable);

		}

		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}

		Page<CreateTicketResponseDto> finalResponse = list.map(createTicketMapper::convertEntityToResponseDTO);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getAllByRequestFilterPaymentAppSearchNew(PaginationRequestDTO2 requestData)
			throws ParseException {

		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		Long count = this.getCountBySearchfilterappnew(requestData);
		log.info("total count :: {}", count);
		if (count > 0) {
			List<CreateTicketEntity> list = this.getRecordsByFilterDTOAppNew(requestData);
			if (CollectionUtils.isEmpty(list)) {
				throw new RecordNotFoundException("No Record Found");
			}
			List<CreateTicketResponseDto> dtoList = list.stream()
					.map(createTicketMapper::convertEntityToResponseDTOPaymentApp).collect(Collectors.toList());

			paginationResponseDTO.setContents(dtoList);
			paginationResponseDTO.setNumberOfElements(Objects.nonNull(dtoList.size()) ? dtoList.size() : null);
			paginationResponseDTO.setTotalElements(count);
			return Library.getSuccessfulResponse(paginationResponseDTO, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException("No Record Found");
		}

	}

	public List<CreateTicketEntity> getRecordsByFilterDTOAppNew(PaginationRequestDTO2 filterRequestDTO)
			throws ParseException {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<CreateTicketEntity> cq = cb.createQuery(CreateTicketEntity.class);
		Root<CreateTicketEntity> from = cq.from(CreateTicketEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<CreateTicketEntity> typedQuery = null;
		addCriteriaAppNew(cb, list, filterRequestDTO, from);
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
		List<CreateTicketEntity> data = typedQuery.getResultList();
		if (CollectionUtils.isEmpty(data)) {
			throw new RecordNotFoundException("No Record Found");
		}
		return data;
	}

	private void addCriteriaAppNew(CriteriaBuilder cb, List<Predicate> list, PaginationRequestDTO2 filterRequestDTO,
			Root<CreateTicketEntity> from) throws ParseException {
		try {
			if (Objects.nonNull(filterRequestDTO.getFromDate())
					&& !filterRequestDTO.getFromDate().toString().trim().isEmpty()
					&& Objects.nonNull(filterRequestDTO.getToDate())
					&& !filterRequestDTO.getToDate().toString().trim().isEmpty()) {

				Date fromDate = new SimpleDateFormat(DATE_FORMAT)
						.parse(filterRequestDTO.getFromDate().toString() + " " + START_TIME);
				Date toDate = new SimpleDateFormat(DATE_FORMAT)
						.parse(filterRequestDTO.getToDate().toString() + " " + END_TIME);
				list.add(cb.between(from.get(CREATED_DATE), fromDate, toDate));

			}

			if (Objects.nonNull(filterRequestDTO.getId()) && !filterRequestDTO.getId().toString().trim().isEmpty()) {

				Long id = Long.valueOf(filterRequestDTO.getId().toString());
				list.add(cb.equal(from.get(ID), id));
			}

			if (Objects.nonNull(filterRequestDTO.getTicketNumber())
					&& !filterRequestDTO.getTicketNumber().toString().trim().isEmpty()) {

				String ticketNumber = String.valueOf(filterRequestDTO.getTicketNumber().toString());
				list.add(cb.equal(from.get(TICKET_NUMBER), ticketNumber));
			}

			if (Objects.nonNull(filterRequestDTO.getLicenseNumber())
					&& !filterRequestDTO.getLicenseNumber().toString().trim().isEmpty()) {
//			Expression<String> mainlic = from.get(LICENSE_NUMBER);
//			Expression<String> mainModushopcode = from.get("shopCode");
//			Predicate liceneNo = mainlic.in(filterRequestDTO.getLicenseNumber());
//			Predicate shopcode = mainModushopcode.in(filterRequestDTO.getLicenseNumber());
//			Predicate liceneNoOrunitcode = cb.or(liceneNo, shopcode);

				List<String> licenseNo = (List<String>) (filterRequestDTO.getLicenseNumber());
				if (CollectionUtils.isEmpty(licenseNo)) {
				} else {
					Expression<String> mainModule = from.get(LICENSE_NUMBER);
					list.add(mainModule.in(licenseNo));
				}

			}
//		if (Objects.nonNull(filterRequestDTO.getShopCode())
//				&& !filterRequestDTO.getShopCode().toString().trim().isEmpty()) {
//
//			List<String> shopCode = (List<String>) (filterRequestDTO.getShopCode());
//			if (CollectionUtils.isEmpty(shopCode)) {
//			} else {
//				Expression<String> mainModule= from.get(SHOP_CODE);
//				list.add(mainModule.in(shopCode));
//			}
//		}

			if (Objects.nonNull(filterRequestDTO.getShopCode())
					&& !filterRequestDTO.getShopCode().toString().trim().isEmpty()) {

				List<String> shopCode = (List<String>) (filterRequestDTO.getShopCode());
				if (!shopCode.isEmpty()) {
					Expression<String> mainModule = from.get("shopCode");
					list.add(mainModule.in(shopCode));
				}
			}

			if (Objects.nonNull(filterRequestDTO.getIssueFromId())
					&& !filterRequestDTO.getIssueFromId().toString().trim().isEmpty()) {
				Long issueFrom = Long.valueOf(filterRequestDTO.getIssueFromId().toString());
				list.add(cb.equal(from.get(ISSUEFROM), issueFrom));
			}

			if (Objects.nonNull(filterRequestDTO.getCategoryId())
					&& !filterRequestDTO.getCategoryId().toString().trim().isEmpty()) {
				Long categoryId = Long.valueOf(filterRequestDTO.getCategoryId().toString());
				list.add(cb.equal(from.get(CATEGORY), categoryId));
			}

			if (Objects.nonNull(filterRequestDTO.getSubCategoryId())
					&& !filterRequestDTO.getSubCategoryId().toString().trim().isEmpty()) {
				Long subcategoryId = Long.valueOf(filterRequestDTO.getSubCategoryId().toString());
				list.add(cb.equal(from.get(SUBCATEGORY), subcategoryId));
			}
			if (Objects.nonNull(filterRequestDTO.getLicenceTypeId())
					&& !filterRequestDTO.getLicenceTypeId().toString().trim().isEmpty()) {
				String licenseTypeId = String.valueOf(filterRequestDTO.getLicenceTypeId().toString());
				list.add(cb.equal(from.get("licenceTypeId"), licenseTypeId));
			}

			if (Objects.nonNull(filterRequestDTO.getTicketstatusName())
					&& !filterRequestDTO.getTicketstatusName().toString().trim().isEmpty()) {
				String ticketstatusName = String.valueOf(filterRequestDTO.getTicketstatusName().toString());

				if (ticketstatusName.equalsIgnoreCase("All")) {

				} else {

					Optional<TicketStatusEntity> tickectStatusObj = ticketStatusrepository
							.findByTicketstatusnameIgnoreCase(ticketstatusName);

					list.add(cb.equal(from.get("ticketStatus"), tickectStatusObj.get().getId()));

				}

			}

			if (Objects.nonNull(filterRequestDTO.getPriorityId())
					&& !filterRequestDTO.getPriorityId().toString().trim().isEmpty()) {
				Long priorityId = Long.valueOf(filterRequestDTO.getPriorityId().toString());
				list.add(cb.equal(from.get(PRIORITY), priorityId));
			}
			if (Objects.nonNull(filterRequestDTO.getTicketStatusId())
					&& !filterRequestDTO.getTicketStatusId().toString().trim().isEmpty()) {
				Long status = Long.valueOf(filterRequestDTO.getTicketStatusId().toString());
				list.add(cb.equal(from.get(TICKET_STATUS), status));
			}

			if (Objects.nonNull(filterRequestDTO.getSlaId())
					&& !filterRequestDTO.getSlaId().toString().trim().isEmpty()) {
				Long sla = Long.valueOf(filterRequestDTO.getSlaId().toString());
				list.add(cb.equal(from.get(SLA), sla));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public GenericResponse getrecoverytimeobjective(CreateTicketDashboardDTO requestDto) {

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
		List<SecuritymanagementDTO> ticketrecoverytimetlist = null;

		List<SecuritymanagementTicketsDTO> finallist = new ArrayList<>();

//		Optional<TicketStatusEntity> tickectStatusObj = ticketStatusrepository
//				.findByTicketstatusnameIgnoreCase("Recovery Time Objective");

		String RTO = "RTO-123456";

		ticketrecoverytimetlist = createTicketRepository.getCountByCreatedDateBetweenrecoverytimeobjectivePos(fromDate,
				toDate, RTO);

		try {
			for (int i = 0; i < ticketrecoverytimetlist.size(); i++) {
				SecuritymanagementTicketsDTO obj = new SecuritymanagementTicketsDTO();

				obj.setTotal_Tickets_Raised(ticketrecoverytimetlist.get(i).getTotal_Tickets_Raised());

				obj.setTickets_Closed_Inprogress_Within_SLA(
						ticketrecoverytimetlist.get(i).getTickets_Closed_Inprogress_Within_SLA());

				obj.setTickets_Breached_SLA(ticketrecoverytimetlist.get(i).getTickets_Breached_SLA());

				Integer totalTickets_Raised = obj.getTotal_Tickets_Raised();

				Integer ticketsClosedInprogresswithinSLA = obj.getTickets_Closed_Inprogress_Within_SLA();

				Integer ticketsBreachedSLA = obj.getTickets_Breached_SLA();

				float resolnpercentage = ((totalTickets_Raised - ticketsBreachedSLA) / (totalTickets_Raised * 1.0f))
						* 100;

				obj.setResolution_Percentage(resolnpercentage);
				finallist.add(obj);
			}

		} catch (Exception e) {

		}

		if (CollectionUtils.isEmpty(finallist)) {
			throw new RecordNotFoundException("No Record Found");
		}

		return Library.getSuccessfulResponse(finallist, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

	@Override
	public GenericResponse getrecoverypointobjective(CreateTicketDashboardDTO requestDto) {

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
		List<SecuritymanagementDTO> ticketrecoverypointlist = null;

		List<SecuritymanagementTicketsDTO> finallist = new ArrayList<>();

//		Optional<TicketStatusEntity> tickectStatusObj = ticketStatusrepository
//				.findByTicketstatusnameIgnoreCase("Recovery Point Objective");

		String RPO = "RPO-123456";

		ticketrecoverypointlist = createTicketRepository.getCountByCreatedDateBetweenrecoverypointobjectivePos(fromDate,
				toDate, RPO);

		try {
			for (int i = 0; i < ticketrecoverypointlist.size(); i++) {
				SecuritymanagementTicketsDTO obj = new SecuritymanagementTicketsDTO();

				obj.setTotal_Tickets_Raised(ticketrecoverypointlist.get(i).getTotal_Tickets_Raised());

				obj.setTickets_Closed_Inprogress_Within_SLA(
						ticketrecoverypointlist.get(i).getTickets_Closed_Inprogress_Within_SLA());

				obj.setTickets_Breached_SLA(ticketrecoverypointlist.get(i).getTickets_Breached_SLA());

				Integer totalTickets_Raised = obj.getTotal_Tickets_Raised();

				Integer ticketsClosedInprogresswithinSLA = obj.getTickets_Closed_Inprogress_Within_SLA();

				Integer ticketsBreachedSLA = obj.getTickets_Breached_SLA();

				float resolnpercentage = ((totalTickets_Raised - ticketsBreachedSLA) / (totalTickets_Raised * 1.0f))
						* 100;

				obj.setResolution_Percentage(resolnpercentage);
				finallist.add(obj);
			}

		} catch (Exception e) {

		}

		if (CollectionUtils.isEmpty(finallist)) {
			throw new RecordNotFoundException("No Record Found");
		}

		return Library.getSuccessfulResponse(finallist, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

	private Long getCountBySearchfilterappnew(PaginationRequestDTO2 filterRequestDTO) throws ParseException {
		log.info("getting total count by search fields :: {}", filterRequestDTO.toString());
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<CreateTicketEntity> from = cq.from(CreateTicketEntity.class);
		cq.select(cb.count(from));
		List<Predicate> list = new ArrayList<>();
		addCriteriaAppNew(cb, list, filterRequestDTO, from);
		cq.where(cb.and(list.toArray(new Predicate[list.size()])));
		Long count = entityManager.createQuery(cq).getSingleResult();
		if (count > 0) {
			return count;
		} else {
			throw new RecordNotFoundException("No Record Found");
		}
	}

	@Override
	public GenericResponse getCount_percentage_dashboardprocess(CreateTicketDashboardDTO requestDto,
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

		List<DashboardCount> ticketrecoverypointlist = null;

		if (Objects.isNull(authenticationDTO) || Objects.isNull(authenticationDTO.getUserId())) {
			log.error("invalid authentication details : {}", authenticationDTO);
			throw new RecordNotFoundException("No Record Found");
		}

		if (authenticationDTO.getRoleCodes().stream().anyMatch(r -> Constant.HELPDESK_ADMIN.equals(r))) {
			ticketrecoverypointlist = createTicketRepository.getCountByFromDataAndToDate(fromDate, toDate);

		} else {
			ticketrecoverypointlist = createTicketRepository.getCountByFromDataAndToDate(fromDate, toDate);
		}
		return Library.getSuccessfulResponse(ticketrecoverypointlist, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

	@Override
	public GenericResponse getbyticketDashboardByMonth(AuthenticationDTO authenticationDTO) {

		List<DashboardCount> ticketrecoverypointlist = null;

		if (Objects.isNull(authenticationDTO) || Objects.isNull(authenticationDTO.getUserId())) {
			log.error("invalid authentication details : {}", authenticationDTO);
			throw new RecordNotFoundException("No Record Found");
		}

		if (authenticationDTO.getRoleCodes().stream().anyMatch(r -> Constant.HELPDESK_ADMIN.equals(r))) {
			ticketrecoverypointlist = createTicketRepository.getByCountTicketDashboardByMonth();

		} else {
			ticketrecoverypointlist = createTicketRepository.getByCountTicketDashboardByMonth();
		}

		return Library.getSuccessfulResponse(ticketrecoverypointlist, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	public String getsequential() {
		int lastTicketNumber = getLastTicketNumber();
		int nextTicketNumber = lastTicketNumber + 1;

		return String.format("%07d", nextTicketNumber);
	}

	public int getLastTicketNumber() {
		try {
			Optional<CreateTicketEntity> ticno = createTicketRepository.getByTicketNumber();

			if (ticno.isPresent() && ticno.get().getTicketNumber() != null) {
				Integer ticketnum = Integer.parseInt(ticno.get().getTicketNumber());
				return ticketnum;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public GenericResponse getAllByfilter(PaginationRequestDTO paginationDto) throws ParseException {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		StringBuilder sb = new StringBuilder();
		StringBuilder total = new StringBuilder();

		String query = "select (t.id ),(t.ticket_number),(t.call_disconnect),(t.required_field),\r\n"
				+ "(hdtc.category_name),(hdtsc.sub_category_name),(t.category_id),(t.subcategory_id),\r\n"
				+ "(ifd.issue_from),(t.issue_from_id),(id.issue_name),(t.issue_details_id),\r\n"
				+ "(pm.priority),(t.priority_id ),(t.licence_number),(sm.sla),\r\n" + "(t.sla_id),(u.username),\r\n"
				+ "(t.modified_by),(t.created_date),(t.modified_date),(t.remarks),\r\n"
				+ "(tsh.ticketstatusname),(t.ticket_status_id), (t.entity_type_id),(t.licence_type_id) ,\r\n"
				+ "(t.district) ,(t.unit_name) ,(t.license_status),(t.shop_code),\r\n"
				+ "(t.shop_name),(t.assign_to),(u.middle_name),TIMESTAMPDIFF(HOUR ,t.created_date , Now()),\r\n"
				+ "(ap.actual_problem),(at2.action_taken),(t.mobile),(t.alternative_mobile_number),(u.first_name),(t.duration),(t.is_active)\r\n"
				+ "from ticket t \r\n" + "LEFT join issue_from ifd on ifd.id=t.issue_from_id  \r\n"
				+ "LEFT join ticket_status_help tsh on tsh.id=t.ticket_status_id \r\n"
				+ "LEFT join help_desk_ticket_category hdtc on hdtc.id=t.category_id \r\n"
				+ "LEFT join help_desk_ticket_sub_category hdtsc on hdtsc.id=t.subcategory_id \r\n"
				+ "LEFT join issue_details id on id.id =t.issue_details_id\r\n"
				+ "LEFT join user u on u.id=t.assign_to\r\n"
				+ "LEFT join priority_master pm on pm.id=t.priority_id \r\n"
				+ "LEFT join actual_problem ap on ap.id=t.actual_problem_id \r\n"
				+ "LEFT join action_taken at2 on at2.id=t.action_taken_id\r\n"
				+ "LEFT join sla_master sm on sm.id=t.sla_id";

		List<String> list = generateSearchCustomQuery(paginationDto);
		total.append(query);
		total.append(list.get(0));
		sb.append(query);
		sb.append(list.get(1));
		log.info("::all ticket download query:::" + sb.toString());
		@SuppressWarnings("unchecked")
		List<Object[]> objList = entityManager.createNativeQuery(sb.toString()).getResultList();
		try {
			List<CreateTicketResponseDto> finalist = objList.stream().map(val -> {

				CreateTicketResponseDto obj = new CreateTicketResponseDto();
				obj.setId(Objects.nonNull(val[0]) ? Long.valueOf(val[0].toString()) : null);
				obj.setTicketNumber(Objects.nonNull(val[1]) ? val[1].toString() : null);
				obj.setCallDisconnect(Objects.nonNull(val[2]) ? val[2].toString() : null);
				obj.setRequiredField(Objects.nonNull(val[3]) ? val[3].toString() : null);
				obj.setCategoryName(Objects.nonNull(val[4]) ? val[4].toString() : null);
				obj.setSubCategoryName(Objects.nonNull(val[5]) ? val[5].toString() : null);
				obj.setCategoryId(Objects.nonNull(val[6]) ? Long.valueOf(val[6].toString()) : null);
				obj.setSubCategoryId(Objects.nonNull(val[7]) ? Long.valueOf(val[7].toString()) : null);
				obj.setIssueFrom(Objects.nonNull(val[8]) ? val[8].toString() : null);
				obj.setIssueFromId(Objects.nonNull(val[9]) ? Long.valueOf(val[9].toString()) : null);
				obj.setIssueDetails(Objects.nonNull(val[10]) ? val[10].toString() : null);
				obj.setIssueDetailsId(Objects.nonNull(val[11]) ? Long.valueOf(val[11].toString()) : null);
				obj.setPriorityName(Objects.nonNull(val[12]) ? val[12].toString() : null);
				obj.setPriorityId(Objects.nonNull(val[13]) ? Long.valueOf(val[13].toString()) : null);
				obj.setLicenceNumber(Objects.nonNull(val[14]) ? val[14].toString() : null);
				obj.setSla(Objects.nonNull(val[15]) ? Long.valueOf(val[15].toString()) : null);
				obj.setSlaId(Objects.nonNull(val[16]) ? Long.valueOf(val[16].toString()) : null);
				obj.setCreatedBy(Objects.nonNull(val[17]) ? val[17].toString() : null);
				obj.setModifiedBy(Objects.nonNull(val[18]) ? val[18].toString() : null);
				obj.setCreatedDate(Objects.nonNull(val[19]) ? val[19].toString() : null);
				obj.setModifiedDate(Objects.nonNull(val[20]) ? val[20].toString() : null);
				obj.setRemarks(Objects.nonNull(val[21]) ? val[21].toString() : null);
				obj.setTicketStatus(Objects.nonNull(val[22]) ? val[22].toString() : null);
				obj.setTicketStatusId(Objects.nonNull(val[23]) ? Long.valueOf(val[23].toString()) : null);
				obj.setEntityTypeId(Objects.nonNull(val[24]) ? val[24].toString() : null);
				obj.setLicenseTypeId(Objects.nonNull(val[25]) ? val[25].toString() : null);
				obj.setDistrict(Objects.nonNull(val[26]) ? val[26].toString() : null);
				obj.setUnitName(Objects.nonNull(val[27]) ? val[27].toString() : null);
				obj.setLicenseStatus(Objects.nonNull(val[28]) ? val[28].toString() : null);
				obj.setShopCode(Objects.nonNull(val[29]) ? val[29].toString() : null);
				obj.setShopName(Objects.nonNull(val[30]) ? val[30].toString() : null);
				obj.setAssignToId(Objects.nonNull(val[31]) ? Long.valueOf(val[31].toString()) : null);
				obj.setAssignToName(Objects.nonNull(val[32]) ? val[32].toString() : null);
//	 		obj.setDurationInHours(Objects.nonNull(val[33]) ? val[33].toString(): null);
//	 		log.info(obj.getDurationInHours());
				obj.setActualProplemName(Objects.nonNull(val[34]) ? val[34].toString() : null);
				obj.setActionTakenName(Objects.nonNull(val[35]) ? val[35].toString() : null);
				obj.setMobile(Objects.nonNull(val[36]) ? val[36].toString() : null);
				obj.setAlternativemobileNumber(Objects.nonNull(val[37]) ? val[37].toString() : null);
				obj.setFirstName(Objects.nonNull(val[38]) ? val[38].toString() : null);
				obj.setDuration(Objects.nonNull(val[39]) ? val[39].toString() : null);
				obj.setActive(Objects.nonNull(val[40]) ? Boolean.valueOf(val[40].toString()) : null);

				if (obj.getTicketStatus().equalsIgnoreCase("Closed")
						|| obj.getTicketStatus().equalsIgnoreCase("Resolved")) {
					String duration = obj.getDuration();
					log.info(duration + ":::Duration");
					if (Objects.nonNull(duration)) {
						Long timestampduration = Long.valueOf(obj.getDuration());
						long hours = TimeUnit.SECONDS.toHours(timestampduration);
						long minutes = TimeUnit.SECONDS.toMinutes(timestampduration) % 60;
						obj.setDurationInHours(hours + "." + minutes + " hours");
						// log.info(obj.getDurationInHours() + ":::!nonnull check ticket status closed
						// duration hav evalue ");
					}

					else if (Objects.isNull(duration)) {
						Long id = obj.getId();
						Optional<DurationDTO> time = createTicketRepository.getByTimeHRMins(id);
						obj.setDurationInHours(time.get().getHoursMins() + " hours");
						// log.info(obj.getDurationInHours() + ":::is null check ticket status closed
						// but duration null");
					}

				} else {
					Long id = obj.getId();
					Optional<DurationDTO> time = createTicketRepository.getByTimeHRMins(id);
					obj.setDurationInHours(time.get().getHoursMins() + " hours");
					// log.info(obj.getDurationInHours() + ":::other ticket status");

				}

				return obj;

			}).collect(Collectors.toList());
			// log.info(finalist + ":::Final List");
			paginationResponseDTO.setContents(finalist);
			paginationResponseDTO.setNumberOfElements(Objects.nonNull(finalist) ? finalist.size() : null);
			paginationResponseDTO
					.setTotalElements((long) entityManager.createNativeQuery(total.toString()).getResultList().size());
		} catch (NumberFormatException e) {
			// Handle the exception or log an error message
			e.printStackTrace();
			log.info("::::ALL Ticket download::::" + e);
		}
		return Library.getSuccessfulResponse(paginationResponseDTO, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	public List<String> generateSearchCustomQuery(PaginationRequestDTO paginationDto) {

		List<String> list = new ArrayList<>();

		String response = "";

		if (Objects.nonNull(paginationDto.getFilters().get("fromDate"))
				&& !paginationDto.getFilters().get("fromDate").toString().trim().isEmpty()
				&& Objects.nonNull(paginationDto.getFilters().get("toDate"))
				&& !paginationDto.getFilters().get("toDate").toString().trim().isEmpty()) {
			try {

				response = "where date(t.created_date) BETWEEN '"
						+ paginationDto.getFilters().get("fromDate").toString() + "' AND '"
						+ paginationDto.getFilters().get("toDate").toString() + "'";

			} catch (Exception e) {

			}
		}

		list.add(response);

		if (!response.trim().isEmpty()) {

			response += " limit " + paginationDto.getPageNo() * paginationDto.getPaginationSize() + ","
					+ paginationDto.getPaginationSize();

		}

		list.add(response);

		return list;

	}

}
