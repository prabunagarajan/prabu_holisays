package com.oasys.helpdesk.service;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.PaginationResponseDTO;
import com.oasys.helpdesk.dto.SiteActionTakenResponseDto;
import com.oasys.helpdesk.dto.SiteObservationDTO;
import com.oasys.helpdesk.dto.SiteObservationRequestDto;
import com.oasys.helpdesk.entity.SiteActionTakenEntity;
import com.oasys.helpdesk.entity.SiteIssueTypeEntity;
import com.oasys.helpdesk.entity.SiteObservationEntity;
import com.oasys.helpdesk.mapper.SiteObservationMapper;
import com.oasys.helpdesk.repository.SiteIssueTypeRepository;
import com.oasys.helpdesk.repository.SiteObservationRepository;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;

import lombok.extern.log4j.Log4j2;

import org.apache.commons.lang3.StringUtils;

@Service
@Log4j2
public class SiteObservationServiceImpl implements SiteObservation {

	@Autowired
	SiteObservationRepository siteobservationrepository;

	@Autowired
	private SiteIssueTypeRepository siteIssueTypeRepository;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private SiteObservationMapper siteObservationMapper;

	public GenericResponse add(SiteObservationRequestDto requestDTO) {
		if (siteobservationrepository != null) {
			Optional<SiteIssueTypeEntity> siteObservation = siteIssueTypeRepository
					.findById(requestDTO.getSiteIssueTypeId());

			if (!siteObservation.isPresent()) {
				return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage());
			}

			List<SiteObservationEntity> siteObservationEntity = siteobservationrepository
					.findBySiteIssueTypeIdAndObservation(requestDTO.getSiteIssueTypeId(),
							requestDTO.getSiteObservation());

			if (!siteObservationEntity.isEmpty()) {
				return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
						ResponseMessageConstant.RECORD_ALREADY_EXISTS.getMessage());
			}
			requestDTO.setId(null);
			SiteObservationEntity entity = siteObservationMapper.convertRequestDTOToEntity(requestDTO, null);

			siteobservationrepository.save(entity);

			return Library.getSuccessfulResponse(entity, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_CREATED);

		} else {
			throw new RecordNotFoundException();
		}

	}

	@Override
	public GenericResponse getById(Long id) {
		Optional<SiteObservationEntity> siteObservationEntity = siteobservationrepository.findById(id);
		if (!siteObservationEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(
				siteObservationMapper.convertEntityToResponseDTO(siteObservationEntity.get()),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getAll() {

		List<SiteObservationEntity> siteObservationList = siteobservationrepository.findAllByOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(siteObservationList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<SiteObservationDTO> siteObservationResponseList = siteObservationList.stream()
				.map(siteObservationMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(siteObservationResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override

	public GenericResponse updateSite(SiteObservationDTO requestDTO) {

		boolean activeStatus = requestDTO.isActive();
		log.info("activeStatus :{}", activeStatus);

		// Validate the requestDTO ID
		if (Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { "ID" }));
		}

		// Retrieve SiteObservationEntity by ID
		Optional<SiteObservationEntity> entityOptional = siteobservationrepository.findById(requestDTO.getId());
		if (!entityOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "OBSERVATION ID" }));
		}

		// Retrieve SiteIssueTypeEntity by ID
		if (Objects.isNull(requestDTO.getSiteIssueTypeId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "SITE ISSUE TYPE ID" }));
		}

		Optional<SiteIssueTypeEntity> siteIssueTypeEntityOptional = siteIssueTypeRepository
				.findById(requestDTO.getSiteIssueTypeId());
		if (!siteIssueTypeEntityOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "SITE ISSUE TYPE ID" }));
		}

		SiteObservationEntity entity = entityOptional.get();
		SiteIssueTypeEntity siteIssueTypeEntity = siteIssueTypeEntityOptional.get();

		// Check for duplicate observation ignoring case
		Optional<SiteObservationEntity> existingEntityOptional = siteobservationrepository
				.findByObservationIgnoreCaseAndIdNot(requestDTO.getObservation(), entity.getId());

		if (existingEntityOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { "SITE OBSERVATION" }));
		}

		List<SiteObservationEntity> siteObservationList = siteobservationrepository.findBySiteIssueTypeIdAndObservation1(
				requestDTO.getSiteIssueTypeId(), requestDTO.getObservation(), activeStatus);
		if (!siteObservationList.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.RECORD_ALREADY_EXISTS.getMessage());
		}

		entity.setObservation(requestDTO.getObservation());
		entity.setIssueType(siteIssueTypeEntity);
		entity.setActive(requestDTO.isActive());

		siteobservationrepository.save(entity);

		return Library.getSuccessfulResponse(entity, ErrorCode.CREATED.getErrorCode(), ErrorMessages.RECORED_UPDATED);
	}

	@Override
	public GenericResponse getAllActive() {
		List<SiteObservationEntity> List = siteobservationrepository
				.findAllByIsActiveOrderByModifiedDateDesc(Boolean.TRUE);
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

		if (count > 0) {
			List<SiteObservationEntity> list = this.getRecordsByFilterDTO(requestData);
			if (CollectionUtils.isEmpty(list)) {
				throw new RecordNotFoundException("No Record Found");
			}

			// Long listcount = (long) list.size();

			List<SiteObservationDTO> siteObservationResponseList = list.stream()
					.map(siteObservationMapper::convertEntityToResponseDTO).collect(Collectors.toList());

			paginationResponseDTO.setContents(siteObservationResponseList);
			paginationResponseDTO.setNumberOfElements(Objects.nonNull(count) ? count.intValue() : null);
			paginationResponseDTO.setTotalElements(count);
			return Library.getSuccessfulResponse(paginationResponseDTO, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException("No Record Found");
		}
	}

	public List<SiteObservationEntity> getRecordsByFilterDTO(PaginationRequestDTO filterRequestDTO)
			throws ParseException {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<SiteObservationEntity> cq = cb.createQuery(SiteObservationEntity.class);
		Root<SiteObservationEntity> from = cq.from(SiteObservationEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<SiteObservationEntity> typedQuery = null;
		addCriteria(cb, list, filterRequestDTO, from);
		cq.where(cb.and(list.toArray(new Predicate[list.size()])));
		cq.distinct(true);
		if (StringUtils.isBlank(filterRequestDTO.getSortField())) {
			filterRequestDTO.setSortField("MODIFIED_DATE");// double quotes instead of constant
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

		List<SiteObservationEntity> data = typedQuery.getResultList();
		if (CollectionUtils.isEmpty(data)) {
			throw new RecordNotFoundException("No Record Found");
		}
		return data;
	}

	private Long getCountBySearchFields(PaginationRequestDTO filterRequestDTO) throws ParseException {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<SiteObservationEntity> from = cq.from(SiteObservationEntity.class);
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
			Root<SiteObservationEntity> from) throws ParseException {
		SiteObservationEntity div = new SiteObservationEntity();
		try {
			if (Objects.nonNull(filterRequestDTO.getFilters())) {

				if (Objects.nonNull(filterRequestDTO.getFilters().get("FROM_DATE"))
						&& !filterRequestDTO.getFilters().get("TO_DATE").toString().trim().isEmpty()) {

					Date fromDate = new SimpleDateFormat("DATE_FORMAT")
							.parse(filterRequestDTO.getFilters().get("FROM_DATE").toString() + " " + "START_TIME");
					Date toDate = new SimpleDateFormat("DATE_FORMAT")
							.parse(filterRequestDTO.getFilters().get("TO_DATE").toString() + " " + "END_TIME");
					list.add(cb.between(from.get("CREATED_DATE"), fromDate, toDate));

				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("observation"))
						&& !filterRequestDTO.getFilters().get("observation").toString().trim().isEmpty()) {

					String observation = String.valueOf(filterRequestDTO.getFilters().get("observation").toString());
					list.add(cb.equal(from.get("observation"), observation));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("isssuetype_id"))
						&& !filterRequestDTO.getFilters().get("isssuetype_id").toString().trim().isEmpty()) {

					Long issueType = Long.valueOf(filterRequestDTO.getFilters().get("isssuetype_id").toString());
					list.add(cb.equal(from.get("issueType"), issueType));
				}

//				if (Objects.nonNull(filterRequestDTO.getFilters().get("status"))
//						&& !filterRequestDTO.getFilters().get("status").toString().trim().isEmpty()) {
//
//					String status = String
//							.valueOf(filterRequestDTO.getFilters().get("status").toString());
//					list.add(cb.equal(from.get("status"), status));
//				}

			}
		} catch (ParseException e) {
			throw new InvalidParameterException("Invalid filter value passed!");
		}
	}

	public GenericResponse getSiteObservationByIssueTypeId(Long issuetypeid) {

		List<SiteObservationEntity> SiteObservationList = siteobservationrepository
				.getSiteObservationByIssueTypeId(issuetypeid);
		if (SiteObservationList == null || SiteObservationList.size() == 0) {
			throw new RecordNotFoundException("No record found");
		}
		if (SiteObservationList.size() > 0) {
			List<SiteObservationDTO> actionTakenResponseList = SiteObservationList.stream()
					.map(siteObservationMapper::convertEntityToResponseDTO).collect(Collectors.toList());
			return Library.getSuccessfulResponse(actionTakenResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);

		} else {
			throw new RecordNotFoundException();
		}
	}

}
