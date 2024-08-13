package com.oasys.helpdesk.service;

import java.util.Optional;

import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.query.criteria.internal.expression.function.CurrentDateFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.auditing.CurrentDateTimeProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.EntityDetailsDTO;
import com.oasys.helpdesk.dto.EntityDetailsResponseDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.PaginationResponseDTO;
import com.oasys.helpdesk.entity.Accessories;
import com.oasys.helpdesk.entity.EntityDetails;
import com.oasys.helpdesk.mapper.AccessoriesMapper;
import com.oasys.helpdesk.mapper.EntityDetailMapper;
import com.oasys.helpdesk.mapper.SiteVisitMapper;
import com.oasys.helpdesk.repository.EntityDetailsRepository;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import org.springframework.util.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
//import java.util.Objects;
import java.util.Date;
import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

//import freemarker.template.utility.CollectionUtils;

import java.util.List;
import java.util.Objects;

//import antlr.collections.List;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service

public class EntityServiceImpl implements EntityService {

	@Autowired
	EntityDetailsRepository entitydetailsrepository;

	@Autowired
	EntityDetailMapper entitydetailmapper;
	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private AccessoriesMapper accessoriesMapper;

	@Autowired
	private CommonDataController commonDataController;
	@Autowired
	private EntityManager entityManager;
	@Autowired
	private EntityDetailMapper entitydetailMapper;

	@Override
	public GenericResponse updateEntity(EntityDetailsDTO requestDTO) {
		if (Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { "ENTITYID" }));
		}

		Optional<EntityDetails> entityOptional = entitydetailsrepository.findById(requestDTO.getId());
		if (!entityOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "ENTITYID" }));
		}

		EntityDetails entity = entityOptional.get();

		Optional<EntityDetails> existingEntityOptional = entitydetailsrepository
				.findByEntityNameIgnoreCaseAndIdNot(requestDTO.getEntityName(), entity.getId());
		if (existingEntityOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { "ENTITY_NAME" }));

		}

		Optional<EntityDetails> existingEntityOptional1 = entitydetailsrepository
				.findByEntityCodeIgnoreCaseAndIdNot(requestDTO.getEntityCode(), entity.getId());
		if (existingEntityOptional1.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { "ENTITY_CODE" }));

		}

		entity.setEntityName(requestDTO.getEntityName());
		entity.setEntityCode(requestDTO.getEntityCode());
		entity.setEntityOrOthers(requestDTO.getEntityOrOthers());
		entity.setActive(requestDTO.isActive());
		entitydetailsrepository.save(entity);
		return Library.getSuccessfulResponse(entity, ErrorCode.CREATED.getErrorCode(), ErrorMessages.RECORED_UPDATED);
	}

	@Override
	public GenericResponse addEntityDetail(EntityDetailsDTO requestDTO) {

		if (requestDTO.getEntityName().isEmpty()) {

			throw new InvalidDataValidation("Mandatory parameter not passed :ENTITY_NAME");

		}

		if (requestDTO.getEntityCode().isEmpty()) {

			throw new InvalidDataValidation("Mandatory parameter not passed :ENTITY_CODE");
		}

		if (requestDTO.getEntityOrOthers().isEmpty()) {

			throw new InvalidDataValidation("Mandatory parameter not passed :ENTITY_OR_OTHERS");
		}

		Optional<EntityDetails> entityOptional = entitydetailsrepository
				.findByEntityNameIgnoreCase(requestDTO.getEntityName().toUpperCase());
		if (entityOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { "ENTITY_NAME" }));
		}
		entityOptional = entitydetailsrepository.findByEntityCodeIgnoreCase(requestDTO.getEntityCode());
		if (entityOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { "ENTITY_CODE" }));
		}
		requestDTO.setId(null);
		EntityDetails entityDetails = commonUtil.modalMap(requestDTO, EntityDetails.class);
		entitydetailsrepository.save(entityDetails);
		return Library.getSuccessfulResponse(entityDetails, ErrorCode.CREATED.getErrorCode(),
				ErrorMessages.RECORED_CREATED);
	}

	@Override
	public GenericResponse getById(Long id) {
		Optional<EntityDetails> entitydetail = entitydetailsrepository.findById(id);
		List<EntityDetailsDTO> Entitylist = new ArrayList<EntityDetailsDTO>();
		try {
			EntityDetailsDTO entity = new EntityDetailsDTO();
			entity.setId(entitydetail.get().getId());
			entity.setActive(entitydetail.get().isActive());
			entity.setCreatedBy(entitydetail.get().getCreatedBy());
			entity.setModifiedBy(entitydetail.get().getModifiedBy());
			entity.setEntityCode(entitydetail.get().getEntityCode());
			entity.setEntityOrOthers(entitydetail.get().getEntityOrOthers());
			entity.setEntityName(entitydetail.get().getEntityName());
			Long userid = (long) entitydetail.get().getCreatedBy();
			String createdByUserName = commonDataController.getUserNameById(userid);
			String modifiedByUserName = commonDataController.getUserNameById(userid);
			entity.setCreatedByName(createdByUserName);
			entity.setModifiedByName(modifiedByUserName);
			entity.setCreatedDate(entitydetail.get().getCreatedDate().toString());
			entity.setModifiedDate(entitydetail.get().getModifiedDate().toString());

			Entitylist.add(entity);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!entitydetail.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(Entitylist, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

	@Override
	public GenericResponse getAll() {
		List<EntityDetails> EntityDetailsList = entitydetailsrepository.findAll();
		List<EntityDetailsDTO> Entitylist = new ArrayList<EntityDetailsDTO>();
		try {
			EntityDetailsList.stream().forEach(action -> {
				EntityDetailsDTO entity = new EntityDetailsDTO();
				entity.setId(action.getId());
				entity.setActive(action.isActive());
				entity.setCreatedBy(action.getCreatedBy());
				entity.setModifiedBy(action.getModifiedBy());
				entity.setEntityCode(action.getEntityCode());
				entity.setEntityOrOthers(action.getEntityOrOthers());
				entity.setEntityName(action.getEntityName());
				Long userid = (long) action.getCreatedBy();
				String createdByUserName = commonDataController.getUserNameById(userid);
				String modifiedByUserName = commonDataController.getUserNameById(userid);
				entity.setCreatedByName(createdByUserName);
				entity.setModifiedByName(modifiedByUserName);
				entity.setCreatedDate(action.getCreatedDate().toString());
				entity.setModifiedDate(action.getModifiedDate().toString());

				Entitylist.add(entity);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (CollectionUtils.isEmpty(Entitylist)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}

		return Library.getSuccessfulResponse(Entitylist, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getEntityName(String entity_name) {
		List<EntityDetails> EntityDetailsList = entitydetailsrepository.getEntityName(entity_name);

		if (CollectionUtils.isEmpty(EntityDetailsList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}

		return Library.getSuccessfulResponse(EntityDetailsList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getStatus(Boolean is_active) {
		List<EntityDetails> EntityDetailsList = entitydetailsrepository.getStatus(is_active);
		if (CollectionUtils.isEmpty(EntityDetailsList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}

		return Library.getSuccessfulResponse(EntityDetailsList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getIsactiveTrue(Boolean pass) {
		List<EntityDetails> EntityDetailsList = entitydetailsrepository.getIsactiveTrue(pass);

		if (CollectionUtils.isEmpty(EntityDetailsList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}

		return Library.getSuccessfulResponse(EntityDetailsList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getAllActive() {
		List<EntityDetails> List = entitydetailsrepository.findAllByIsActiveOrderByModifiedDateDesc(Boolean.TRUE);
		if (CollectionUtils.isEmpty(List)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}

		return Library.getSuccessfulResponse(List, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	public GenericResponse getAllByRequestFilter(PaginationRequestDTO requestData) throws ParseException {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		Long count = this.getCountBySearchFields(requestData);
		log.info("total count :: {}", count);
		if (count > 0) {
			List<EntityDetails> list = this.getRecordsByFilterDTO(requestData);
			if (CollectionUtils.isEmpty(list)) {
				throw new RecordNotFoundException("No Record Found");
			}
			List<EntityDetailsResponseDTO> dtoList = list.stream().map(entitydetailMapper::entityToResponseDTO)
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

	public List<EntityDetails> getRecordsByFilterDTO(PaginationRequestDTO filterRequestDTO) throws ParseException {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<EntityDetails> cq = cb.createQuery(EntityDetails.class);
		Root<EntityDetails> from = cq.from(EntityDetails.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<EntityDetails> typedQuery = null;
		addCriteria(cb, list, filterRequestDTO, from);
		cq.where(cb.and(list.toArray(new Predicate[list.size()])));
		cq.distinct(true);
		if (StringUtils.isBlank(filterRequestDTO.getSortField())) {
			filterRequestDTO.setSortField("MODIFIED_DATE");
		}
		if (StringUtils.isNotBlank(filterRequestDTO.getSortOrder())
				&& filterRequestDTO.getSortOrder().equalsIgnoreCase("ASC")) {
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

		List<EntityDetails> data = typedQuery.getResultList();
		if (CollectionUtils.isEmpty(data)) {
			throw new RecordNotFoundException("No Record Found");
		}
		return data;
	}

	private Long getCountBySearchFields(PaginationRequestDTO filterRequestDTO) throws ParseException {
		log.info("getting total count by search fields :: {}", filterRequestDTO.toString());
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<EntityDetails> from = cq.from(EntityDetails.class);
		cq.select(cb.count(from));
		List<Predicate> list = new ArrayList<>();
		addCriteria(cb, list, filterRequestDTO, from);
		cq.where(cb.and(list.toArray(new Predicate[list.size()])));
		Long count = entityManager.createQuery(cq).getSingleResult();
		if (count > 0) {
			return count;
		} else {
			throw new RecordNotFoundException("No Record Found");
		}
	}

	private void addCriteria(CriteriaBuilder cb, List<Predicate> list, PaginationRequestDTO filterRequestDTO,
			Root<EntityDetails> from) throws ParseException {

		try {
			if (Objects.nonNull(filterRequestDTO.getFilters())) {
				log.info("filters ::" + filterRequestDTO.getFilters());

				if (Objects.nonNull(filterRequestDTO.getFilters().get("FROM_DATE"))
						&& !filterRequestDTO.getFilters().get("TO_DATE").toString().trim().isEmpty()) {

					Date fromDate = new SimpleDateFormat("DATE_FORMAT")
							.parse(filterRequestDTO.getFilters().get("FROM_DATE").toString() + " " + "START_TIME");
					Date toDate = new SimpleDateFormat("DATE_FORMAT")
							.parse(filterRequestDTO.getFilters().get("TO_DATE").toString() + " " + "END_TIME");
					list.add(cb.between(from.get("CREATED_DATE"), fromDate, toDate));

				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("is_active"))
						&& !filterRequestDTO.getFilters().get("is_active").toString().trim().isEmpty()) {

					Long status = Long.valueOf(filterRequestDTO.getFilters().get("is_active").toString());
					list.add(cb.equal(from.get("isActive"), status));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("entity_name"))
						&& !filterRequestDTO.getFilters().get("entity_name").toString().trim().isEmpty()) {

					String entityName = String.valueOf(filterRequestDTO.getFilters().get("entity_name").toString());
					list.add(cb.equal(from.get("entityName"), entityName));
				}

			}
		} catch (ParseException e) {
			throw new InvalidParameterException("No Record Found");
		}
	}

}
