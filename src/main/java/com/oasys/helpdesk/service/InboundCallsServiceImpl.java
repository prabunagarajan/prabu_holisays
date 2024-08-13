package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.CREATED_DATE;
import static com.oasys.helpdesk.constant.Constant.DATE_FORMAT;
import static com.oasys.helpdesk.constant.Constant.END_TIME;
import static com.oasys.helpdesk.constant.Constant.FROM_DATE;
import static com.oasys.helpdesk.constant.Constant.ID;
import static com.oasys.helpdesk.constant.Constant.MODIFIED_DATE;
import static com.oasys.helpdesk.constant.Constant.START_TIME;
import static com.oasys.helpdesk.constant.Constant.TO_DATE;
import com.oasys.helpdesk.constant.Constant;
import java.security.InvalidParameterException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.InboundCallsDTO;
import com.oasys.helpdesk.dto.InboundCallsSummaryDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.PaginationResponseDTO;
import com.oasys.helpdesk.entity.InboundCallsEntity;
import com.oasys.helpdesk.mapper.InboundCallsMapper;
import com.oasys.helpdesk.repository.InboundCallsRepository;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;

import io.jsonwebtoken.lang.Collections;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class InboundCallsServiceImpl implements InboundCallsService {

	@Autowired
	InboundCallsRepository inboundCallsRepository;
	@Autowired
	private CommonUtil commonUtil;
	@Autowired
	private InboundCallsMapper inboundcallsmapmapper;

	@Autowired
	private EntityManager entityManager;

	@Override
	public GenericResponse add(InboundCallsDTO requestDTO) {
		// Check for overlapping time intervals on the same date
		if (isTimeIntervalOverlapping(requestDTO)) {
			// return Library.getFailedfulResponse(ErrorCode.BAD_REQUEST.getErrorCode(),
			// "Time interval overlaps with an existing entry.");
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS
							.getMessage(new Object[] { "Time interval " + requestDTO.getStartTime() + " to " + requestDTO.getEndTime() + " overlaps with an existing entry."
				            }));
		}

		InboundCallsEntity entity = commonUtil.modalMap(requestDTO, InboundCallsEntity.class);
		if ("24:00".equals(requestDTO.getEndTime())) {
			entity.setEndTime("23:59");
		}
		entity.setTotalCallsReceived(requestDTO.getTotalCallsReceived());
		entity.setTotalCallsAttended(requestDTO.getTotalCallsAttended());
		entity.setTotalCallsAbondoned(requestDTO.getTotalCallsAbondoned());

		float callsReceived = requestDTO.getTotalCallsReceived();
		float callsAbandoned = requestDTO.getTotalCallsAbondoned();
		float attendedPercentage = ((callsReceived - callsAbandoned) / callsReceived) * 100.0f;

		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		float roundedPercentage = Float.parseFloat(decimalFormat.format(attendedPercentage));
		entity.setCallsAttendedPercentage(roundedPercentage);

		entity = inboundCallsRepository.save(entity);
		return Library.getSuccessfulResponse(entity, ErrorCode.CREATED.getErrorCode(), ErrorMessages.RECORED_CREATED);
	}

	private boolean isTimeIntervalOverlapping(InboundCallsDTO requestDTO) {
		LocalDate date = LocalDate.now(); // Use the current date
		List<InboundCallsEntity> existingEntries = inboundCallsRepository.findByCreatedDate(date);

		String newStartTime = requestDTO.getStartTime().replace('.', ':');
		String newEndTime = requestDTO.getEndTime().replace('.', ':');

		if ("24:00".equals(newEndTime)) {
			newEndTime = "23:59";
		}

		for (InboundCallsEntity existingEntry : existingEntries) {
			Date existingEntryDate = existingEntry.getCreatedDate();
			LocalDate existingLocalDate = existingEntryDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

			if (!existingLocalDate.isEqual(date)) {
				continue; // Skip entries on different dates
			}

			String existingStartTime = existingEntry.getStartTime().replace('.', ':');
			String existingEndTime = existingEntry.getEndTime().replace('.', ':');

			// Use a custom format for parsing times with minutes represented as decimals
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

			LocalTime newStartTimeLocal = LocalTime.parse(newStartTime, formatter);
			LocalTime newEndTimeLocal = LocalTime.parse(newEndTime, formatter);
			LocalTime existingStartTimeLocal = LocalTime.parse(existingStartTime, formatter);
			LocalTime existingEndTimeLocal = LocalTime.parse(existingEndTime, formatter);

			// Check for overlapping time intervals on the same date
			if ((newStartTimeLocal.isBefore(existingStartTimeLocal) && newEndTimeLocal.isBefore(existingEndTimeLocal)
					&& !newEndTimeLocal.isBefore(existingStartTimeLocal))
					|| (newStartTimeLocal.isAfter(existingStartTimeLocal)

							&& newEndTimeLocal.isAfter(existingEndTimeLocal)
							&& !newStartTimeLocal.isAfter(existingEndTimeLocal))

					|| (newStartTimeLocal.equals(existingStartTimeLocal)
							&& newEndTimeLocal.equals(existingEndTimeLocal))

					|| (newStartTimeLocal.isAfter(existingStartTimeLocal)
							&& newEndTimeLocal.isBefore(existingEndTimeLocal))

					|| (newStartTimeLocal.isBefore(existingStartTimeLocal)
							&& newEndTimeLocal.isAfter(existingEndTimeLocal))

					|| (newStartTimeLocal.equals(existingStartTimeLocal)
							&& (newEndTimeLocal.isAfter(existingEndTimeLocal)

									|| newEndTimeLocal.isBefore(existingEndTimeLocal)))
					|| (newStartTimeLocal.isAfter(existingStartTimeLocal)

							|| newStartTimeLocal.isBefore(existingStartTimeLocal))
							&& newEndTimeLocal.equals(existingEndTimeLocal)) {
				return true; // Overlapping time intervals
			}

		}

		return false; // No overlapping time intervals found
	}
	

	@Override
	public GenericResponse getById(Long id) {
		Optional<InboundCallsEntity> inbountEntity = inboundCallsRepository.findById(id);
		if (!inbountEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(inboundcallsmapmapper.convertEntityToResponseDTO(inbountEntity.get()),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse updateInboundCalls(InboundCallsDTO requestDTO) {

		if (Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { "ID" }));
		}
		Optional<InboundCallsEntity> inboundCallsEntityOptional = inboundCallsRepository.findById(requestDTO.getId());

		InboundCallsEntity inboundCallsEntity = null;

		if (inboundCallsEntityOptional.isPresent()) {
			inboundCallsEntity = inboundCallsEntityOptional.get();
			inboundCallsEntity.setStartTime(requestDTO.getStartTime());
			inboundCallsEntity.setEndTime(requestDTO.getEndTime());
			inboundCallsEntity.setTotalCallsReceived(requestDTO.getTotalCallsReceived());
			inboundCallsEntity.setTotalCallsAttended(requestDTO.getTotalCallsAttended());
			inboundCallsEntity.setTotalCallsAbondoned(requestDTO.getTotalCallsAbondoned());
			inboundCallsEntity.setCallsAttendedPercentage(requestDTO.getCallsAttendedPercentage());
			float callsReceived = requestDTO.getTotalCallsReceived();
			float callsAbandoned = requestDTO.getTotalCallsAbondoned();
			float attendedPercentage = ((callsReceived - callsAbandoned) / callsReceived) * 100.0f;
			DecimalFormat decimalFormat = new DecimalFormat("#.##");
			float roundedPercentage = Float.parseFloat(decimalFormat.format(attendedPercentage));
			inboundCallsEntity.setCallsAttendedPercentage(roundedPercentage);
			inboundCallsRepository.save(inboundCallsEntity);
		}

		else {
			throw new InvalidDataValidation("Invalid ID");
		}
		return Library.getSuccessfulResponse(inboundCallsEntity, ErrorCode.CREATED.getErrorCode(),
				ErrorMessages.RECORED_UPDATED);
	}

	@Override
	public GenericResponse getAllByRequestFilter(PaginationRequestDTO requestData) throws ParseException {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		Long count = this.getCountBySearchFields(requestData);

		if (count > 0) {
			List<InboundCallsEntity> list = this.getRecordsByFilterDTO(requestData);
			if (CollectionUtils.isEmpty(list)) {
				throw new RecordNotFoundException("No Record Found");
			}
			Long listcount = (long) list.size();

			paginationResponseDTO.setContents(list);
			paginationResponseDTO.setNumberOfElements(Objects.nonNull(listcount) ? listcount.intValue() : null);
			paginationResponseDTO.setTotalElements(count);
			return Library.getSuccessfulResponse(paginationResponseDTO, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException("No Record Found");
		}
	}

	private Long getCountBySearchFields(PaginationRequestDTO filterRequestDTO) throws ParseException {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<InboundCallsEntity> from = cq.from(InboundCallsEntity.class);
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

	public List<InboundCallsEntity> getRecordsByFilterDTO(PaginationRequestDTO filterRequestDTO) throws ParseException {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();

		CriteriaQuery<InboundCallsEntity> cq = cb.createQuery(InboundCallsEntity.class);
		Root<InboundCallsEntity> from = cq.from(InboundCallsEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<InboundCallsEntity> typedQuery = null;
		addCriteria(cb, list, filterRequestDTO, from);
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

		List<InboundCallsEntity> data = typedQuery.getResultList();
		if (CollectionUtils.isEmpty(data)) {
			throw new RecordNotFoundException("No Record Found");
		}
		return data;
	}

	private void addCriteria(CriteriaBuilder cb, List<Predicate> list, PaginationRequestDTO filterRequestDTO,
			Root<InboundCallsEntity> from) throws ParseException {
		try {
			if (Objects.nonNull(filterRequestDTO.getFilters())) {

				if (Objects.nonNull(filterRequestDTO.getFilters().get(FROM_DATE))
						&& !filterRequestDTO.getFilters().get(TO_DATE).toString().trim().isEmpty()) {

					Date fromDate = new SimpleDateFormat(DATE_FORMAT)
							.parse(filterRequestDTO.getFilters().get(FROM_DATE).toString() + " " + START_TIME);
					Date toDate = new SimpleDateFormat(DATE_FORMAT)
							.parse(filterRequestDTO.getFilters().get(TO_DATE).toString() + " " + END_TIME);
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

				if (Objects.nonNull(filterRequestDTO.getFilters().get("totalCallsReceived"))
						&& !filterRequestDTO.getFilters().get("totalCallsReceived").toString().trim().isEmpty()) {

					Long totalCallsReceived = Long
							.valueOf(filterRequestDTO.getFilters().get("totalCallsReceived").toString());
					list.add(cb.equal(from.get("totalCallsReceived"), totalCallsReceived));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("totalCallsAttended"))
						&& !filterRequestDTO.getFilters().get("totalCallsAttended").toString().trim().isEmpty()) {

					Long totalCallsAttended = Long
							.valueOf(filterRequestDTO.getFilters().get("totalCallsAttended").toString());
					list.add(cb.equal(from.get("totalCallsAttended"), totalCallsAttended));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("totalCallsAbondoned"))
						&& !filterRequestDTO.getFilters().get("totalCallsAbondoned").toString().trim().isEmpty()) {

					Long totalCallsAttended = Long
							.valueOf(filterRequestDTO.getFilters().get("totalCallsAbondoned").toString());
					list.add(cb.equal(from.get("totalCallsAbondoned"), totalCallsAttended));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("callsAttendedPercentage"))
						&& !filterRequestDTO.getFilters().get("callsAttendedPercentage").toString().trim().isEmpty()) {

					Long totalCallsAttended = Long
							.valueOf(filterRequestDTO.getFilters().get("callsAttendedPercentage").toString());
					list.add(cb.equal(from.get("callsAttendedPercentage"), totalCallsAttended));
				}
			}
		} catch (ParseException e) {
			throw new InvalidParameterException("Invalid filter value passed!");
		}
	}

	public GenericResponse getTotalCallsSummaryCount(InboundCallsDTO requestDTO) {

		String fromDateStr = requestDTO.getFromDate(); // Get start date from request DTO
		String toDateStr = requestDTO.getToDate(); // Get end date from request DTO

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

		try {
			List<InboundCallsSummaryDTO> entitysummary = inboundCallsRepository.getTotalCallsSummaryCount(fromDate,
					toDate);
			if (entitysummary == null || entitysummary.isEmpty()) {
				return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(), "No record found");
			} else {
				return Library.getSuccessfulResponse(entitysummary, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
						ErrorMessages.RECORED_FOUND);
			}
		} catch (RecordNotFoundException e) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(), "No record found");
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"An error occurred while retrieving InboundTotalCalls count..", e);
		}
	}

}
