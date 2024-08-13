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

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.PaginationResponseDTO;
import com.oasys.helpdesk.dto.SiteActionTakenResponseDto;
import com.oasys.helpdesk.dto.SiteVisitResponseDTO;
import com.oasys.helpdesk.entity.SiteActionTakenEntity;
import com.oasys.helpdesk.entity.SiteObservationEntity;
import com.oasys.helpdesk.mapper.SiteActionTakenMapper;
import com.oasys.helpdesk.repository.SiteActionTakenRepository;
import com.oasys.helpdesk.repository.SiteObservationRepository;
import com.oasys.helpdesk.request.SiteActionTakenRequestDto;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import javax.persistence.EntityManager;

@Service
public class SiteActionTakenServiceImpl implements SiteActionTakenService {

	@Autowired
	SiteActionTakenRepository siteActionTakenRepository;

	@Autowired
	SiteActionTakenMapper siteActionTakenMapper;

	@Autowired
	private SiteObservationRepository siteObservationRepository;

	@Autowired
	CommonDataController commonDataController;

	@Autowired
	EntityManager entityManager;

	public GenericResponse createSiteActionTaken(SiteActionTakenRequestDto requestDto) {

		if (siteActionTakenRepository != null) {
			Optional<SiteObservationEntity> siteActionTakenEntity = siteObservationRepository
					.findById(requestDto.getObservationId());
			if (!siteActionTakenEntity.isPresent()) {
				return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage());
			}

//			Optional<SiteActionTakenEntity> siteActionTaken = siteActionTakenRepository
//					.findByObservationIdAndActionTaken(requestDto.getObservationId(),
//							requestDto.getSiteActionTaken());
			
			List<SiteActionTakenEntity> siteActionTaken = siteActionTakenRepository
					.findByObservationIdAndActionTaken(requestDto.getObservationId(),
							requestDto.getSiteActionTaken());
			if (!siteActionTaken.isEmpty()) {
				return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
						ResponseMessageConstant.RECORD_ALREADY_EXISTS.getMessage());
			}
			requestDto.setId(null);
			SiteActionTakenEntity entity = siteActionTakenMapper.convertRequestDTOToEntity(requestDto, null);

			siteActionTakenRepository.save(entity);

			return Library.getSuccessfulResponse(entity, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_CREATED);

		} else {
			throw new RecordNotFoundException();
		}
	}

	public GenericResponse getById(Long id) {

		Optional<SiteActionTakenEntity> siteActionTakenEntity = siteActionTakenRepository.findById(id);
		if (!siteActionTakenEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(
				siteActionTakenMapper.convertEntityToResponseDTO(siteActionTakenEntity.get()),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}

	public GenericResponse getAllSiteActionTaken(AuthenticationDTO authenticationDTO) {

		List<SiteActionTakenEntity> SiteActionTakenList = siteActionTakenRepository.findAllByOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(SiteActionTakenList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<SiteActionTakenResponseDto> siteActionTakenResponseList = SiteActionTakenList.stream()
				.map(siteActionTakenMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(siteActionTakenResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	public GenericResponse getSiteActionTakenByObservationId(Long observationid) {

		List<SiteActionTakenEntity> SiteActionTakenList = siteActionTakenRepository
				.getSiteActionTakenByObservationId(observationid);
		if (SiteActionTakenList == null || SiteActionTakenList.size() == 0) {
			throw new RecordNotFoundException("No record found");
		}
		if (SiteActionTakenList.size() > 0) {
			List<SiteActionTakenResponseDto> actionTakenResponseList = SiteActionTakenList.stream()
					.map(siteActionTakenMapper::convertEntityToResponseDTO).collect(Collectors.toList());
			return Library.getSuccessfulResponse(actionTakenResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);

		} else {
			throw new RecordNotFoundException();
		}
	}

	@Override
	public GenericResponse editSiteActionTaken(SiteActionTakenRequestDto requestDTO) {
		if (Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "ID" }));
		}

		Optional<SiteActionTakenEntity> entityOptional = siteActionTakenRepository.findById(requestDTO.getId());
		if (!entityOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "SITE ACTION TAKEN ID" }));
		}

		Optional<SiteObservationEntity> entityOptional1 = siteObservationRepository
				.findById(requestDTO.getObservationId());
		if (!entityOptional1.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "OBSERVATION ID" }));
		}

		SiteActionTakenEntity entity = entityOptional.get();
		Optional<SiteActionTakenEntity> existingEntityOptional = siteActionTakenRepository
				.findBySiteActionTakenIgnoreCaseAndIdNot(requestDTO.getSiteActionTaken(), entity.getId());

		if (existingEntityOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { "SITE ACTION TAKEN" }));
		}
		
		List<SiteActionTakenEntity> siteActionTaken = siteActionTakenRepository
				.findByObservationIdAndActionTaken1(requestDTO.getObservationId(),
						requestDTO.getSiteActionTaken(),requestDTO.isActive());
		if (!siteActionTaken.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.RECORD_ALREADY_EXISTS.getMessage());
		}

		entity.setSiteActionTaken(requestDTO.getSiteActionTaken());
		entity.setObservation(siteObservationRepository.findById(requestDTO.getObservationId()).orElse(null));
		entity.setActive(requestDTO.isActive());
		siteActionTakenRepository.save(entity);
		return Library.getSuccessfulResponse(entity, ErrorCode.CREATED.getErrorCode(), ErrorMessages.RECORED_UPDATED);
	
	}


	public GenericResponse getAllByPassFilter(PaginationRequestDTO requestData) throws ParseException {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		Long count = this.getCountBySearchFields(requestData);

		if (count > 0) {
			List<SiteActionTakenEntity> list = this.getRecordsByFilterDTO(requestData);
			if (CollectionUtils.isEmpty(list)) {
				throw new RecordNotFoundException("No Record Found");
			}
			List<SiteActionTakenResponseDto> dtoList = list.stream().map(siteActionTakenMapper::convertEntityToResponseDTO)
					.collect(Collectors.toList());
			
			paginationResponseDTO.setContents(dtoList);
			paginationResponseDTO.setNumberOfElements(Objects.nonNull(count) ? count.intValue() : null);
			paginationResponseDTO.setTotalElements(count);
			return Library.getSuccessfulResponse(paginationResponseDTO,
					ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
//			Long listcount = (long) list.size();
//
//			paginationResponseDTO.setContents(list);
//			paginationResponseDTO.setNumberOfElements(Objects.nonNull(listcount) ? listcount.intValue() : null);
//			paginationResponseDTO.setTotalElements(count);
//			return Library.getSuccessfulResponse(paginationResponseDTO, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
//					ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException("No Record Found");
		}
	}

	public List<SiteActionTakenEntity> getRecordsByFilterDTO(PaginationRequestDTO filterRequestDTO)
			throws ParseException {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<SiteActionTakenEntity> cq = cb.createQuery(SiteActionTakenEntity.class);
		Root<SiteActionTakenEntity> from = cq.from(SiteActionTakenEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<SiteActionTakenEntity> typedQuery = null;
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

		List<SiteActionTakenEntity> data = typedQuery.getResultList();
		if (CollectionUtils.isEmpty(data)) {
			throw new RecordNotFoundException("No Record Found");
		}
		return data;
	}

	private Long getCountBySearchFields(PaginationRequestDTO filterRequestDTO) throws ParseException {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<SiteActionTakenEntity> from = cq.from(SiteActionTakenEntity.class);
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
			Root<SiteActionTakenEntity> from) throws ParseException {
		SiteActionTakenEntity div = new SiteActionTakenEntity();
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

				if (Objects.nonNull(filterRequestDTO.getFilters().get("actionTaken"))
						&& !filterRequestDTO.getFilters().get("actionTaken").toString().trim().isEmpty()) {

					String actionTaken = String.valueOf(filterRequestDTO.getFilters().get("actionTaken").toString());
					list.add(cb.equal(from.get("siteActionTaken"), actionTaken));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("CreatedByName"))
						&& !filterRequestDTO.getFilters().get("CreatedByName").toString().trim().isEmpty()) {
					String CreatedByName = String
							.valueOf(filterRequestDTO.getFilters().get("CreatedByName").toString());
					list.add(cb.equal(from.get("CreatedByName"), CreatedByName));
				}
			}
		} catch (ParseException e) {
			throw new InvalidParameterException("Invalid filter value passed!");
		}
	}

}
