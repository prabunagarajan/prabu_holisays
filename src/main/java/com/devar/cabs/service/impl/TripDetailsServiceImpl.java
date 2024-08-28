package com.devar.cabs.service.impl;

import static com.devar.cabs.common.Constant.ASC;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.devar.cabs.common.ErrorCode;
import com.devar.cabs.common.ErrorMessages;
import com.devar.cabs.common.ResponseMessageConstant;
import com.devar.cabs.entity.TripDetailsEntity;
import com.devar.cabs.exception.InvalidDataValidation;
import com.devar.cabs.repository.TripDetailsRepository;
import com.devar.cabs.requestDTO.PaginationRequestDTO;
import com.devar.cabs.requestDTO.PaginationResponseDTO;
import com.devar.cabs.requestDTO.TripDetailsRequestDTO;
import com.devar.cabs.service.TripDetailsService;
import com.devar.cabs.utility.GenericResponse;
import com.devar.cabs.utility.Library;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class TripDetailsServiceImpl implements TripDetailsService {

	@Autowired
	TripDetailsRepository tripDetailsRepository;

	@Autowired
	private EntityManager entityManager;

	public GenericResponse add(TripDetailsRequestDTO requestDTO) {

		TripDetailsEntity entity = new TripDetailsEntity();
		entity.setVehicleNumber(requestDTO.getVehicleNumber());
		entity.setDate(requestDTO.getDate());
		entity.setCustomerName(requestDTO.getCustomerName());
		entity.setCustomerMobileNumber(requestDTO.getCustomerMobileNumber());
		entity.setDriverName(requestDTO.getDriverName());
		entity.setStartingKM(requestDTO.getStartingKM());
		entity.setClosingKM(requestDTO.getClosingKM());
		entity.setUsedKM(requestDTO.getClosingKM() - requestDTO.getStartingKM());
		entity.setStartingTime(requestDTO.getStartingTime());
		entity.setClosingTime(requestDTO.getClosingTime());
		entity.setTotalTime(requestDTO.getTotalTime());
		entity.setAcOrNonAc(requestDTO.getAcOrNonAc());
		entity.setAcStartingKM(requestDTO.getAcStartingKM());
		entity.setAcClosingKM(requestDTO.getAcClosingKM());
		entity.setUsedAcKM(requestDTO.getAcClosingKM() - requestDTO.getAcStartingKM());
		entity.setAcNote(requestDTO.getAcNote());
		entity.setVisitingPlace(requestDTO.getVisitingPlace());
		entity.setAdvanceType(requestDTO.getAdvanceType());
		entity.setAdvanceAmount(requestDTO.getAdvanceAmount());
		entity.setDayRent(requestDTO.getDayRent());
		entity.setToll(requestDTO.getToll());
		entity.setTotalRent(requestDTO.getTotalRent());
		entity.setDiesel(requestDTO.getDiesel());
		entity.setDriverPayment(requestDTO.getDriverPayment());
		entity.setPermitAmount(requestDTO.getPermitAmount());
		entity.setPaymentType(requestDTO.getPaymentType());
		entity.setReceivedAmount(requestDTO.getReceivedAmount());
		entity.setPendingAmount(
				requestDTO.getTotalRent() - (requestDTO.getAdvanceAmount() + requestDTO.getReceivedAmount()));
		entity.setBalanceAmount(
				requestDTO.getTotalRent() - (requestDTO.getAdvanceAmount() + requestDTO.getReceivedAmount()));
		entity.setSubmittedBy(requestDTO.getSubmittedBy());
		entity.setStatus(requestDTO.getStatus());

		log.info("=====================" + entity);
		return Library.getSuccessfulResponse(tripDetailsRepository.save(entity), ErrorCode.CREATED.getErrorCode(),
				ErrorMessages.RECORED_CREATED);
	}

	public GenericResponse update(TripDetailsRequestDTO requestDTO) {
		// Check if the ID is null
		if (Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { "ID" }));
		}

		// Retrieve the existing entity from the repository
		Optional<TripDetailsEntity> entityOptional = tripDetailsRepository.findById(requestDTO.getId());
		if (!entityOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "ID" }));
		}

		// Update the existing entity with the data from the requestDTO
		TripDetailsEntity entity = entityOptional.get(); // Use the existing entity

		entity.setVehicleNumber(requestDTO.getVehicleNumber());
		entity.setDate(requestDTO.getDate());
		entity.setCustomerName(requestDTO.getCustomerName());
		entity.setCustomerMobileNumber(requestDTO.getCustomerMobileNumber());
		entity.setDriverName(requestDTO.getDriverName());
		entity.setStartingKM(requestDTO.getStartingKM());
		entity.setClosingKM(requestDTO.getClosingKM());
		entity.setUsedKM(requestDTO.getClosingKM() - requestDTO.getStartingKM());
		entity.setStartingTime(requestDTO.getStartingTime());
		entity.setClosingTime(requestDTO.getClosingTime());
		entity.setTotalTime(requestDTO.getTotalTime());
		entity.setAcOrNonAc(requestDTO.getAcOrNonAc());
		entity.setAcStartingKM(requestDTO.getAcStartingKM());
		entity.setAcClosingKM(requestDTO.getAcClosingKM());
		entity.setUsedAcKM(requestDTO.getAcClosingKM() - requestDTO.getAcStartingKM());
		entity.setAcNote(requestDTO.getAcNote());
		entity.setVisitingPlace(requestDTO.getVisitingPlace());
		entity.setAdvanceType(requestDTO.getAdvanceType());
		entity.setAdvanceAmount(requestDTO.getAdvanceAmount());
		entity.setDayRent(requestDTO.getDayRent());
		entity.setToll(requestDTO.getToll());
		entity.setTotalRent(requestDTO.getTotalRent());
		entity.setDiesel(requestDTO.getDiesel());
		entity.setDriverPayment(requestDTO.getDriverPayment());
		entity.setPermitAmount(requestDTO.getPermitAmount());
		entity.setPaymentType(requestDTO.getPaymentType());
		entity.setReceivedAmount(requestDTO.getReceivedAmount());

		// Calculate pending and balance amounts
		int calculatedPendingAmount = requestDTO.getTotalRent()
				- (requestDTO.getAdvanceAmount() + requestDTO.getReceivedAmount());
		entity.setPendingAmount(calculatedPendingAmount);
		entity.setBalanceAmount(calculatedPendingAmount); // Assuming balance is the same as pending amount

		entity.setSubmittedBy(requestDTO.getSubmittedBy());
		entity.setStatus(requestDTO.getStatus());

		// Log and save the updated entity
		log.info("=====================" + entity);
		tripDetailsRepository.save(entity);

		return Library.getSuccessfulResponse(entity, ErrorCode.CREATED.getErrorCode(), ErrorMessages.RECORED_UPDATED);
	}

	public GenericResponse getById(Long id) {
		Optional<TripDetailsEntity> driverDetails = tripDetailsRepository.findById(id);
		if (!driverDetails.isPresent()) {

			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(driverDetails, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getAll() {
		List<TripDetailsEntity> DepList = tripDetailsRepository.findAllByOrderByIdDesc();
		if (CollectionUtils.isEmpty(DepList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(), ErrorMessages.NO_RECORD_FOUND);
		}
		return Library.getSuccessfulResponse(DepList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	public GenericResponse getsubPagesearchNewByFilter(PaginationRequestDTO requestData) {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		List<TripDetailsEntity> list = this.getSubRecordsByFilterDTO1(requestData);
		List<TripDetailsEntity> list1 = this.getSubRecordsByFilterDTO2(requestData);
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

	public List<TripDetailsEntity> getSubRecordsByFilterDTO1(PaginationRequestDTO filterRequestDTO) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<TripDetailsEntity> cq = cb.createQuery(TripDetailsEntity.class);
		Root<TripDetailsEntity> from = cq.from(TripDetailsEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<TripDetailsEntity> typedQuery = null;
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

	public List<TripDetailsEntity> getSubRecordsByFilterDTO2(PaginationRequestDTO filterRequestDTO) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<TripDetailsEntity> cq = cb.createQuery(TripDetailsEntity.class);
		Root<TripDetailsEntity> from = cq.from(TripDetailsEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<TripDetailsEntity> typedQuery1 = null;
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
			Root<TripDetailsEntity> from) {

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

		if (Objects.nonNull(filterRequestDTO.getFilters().get("vehicleNumber"))
				&& !filterRequestDTO.getFilters().get("vehicleNumber").toString().trim().isEmpty()) {

			String licencetype = (filterRequestDTO.getFilters().get("vehicleNumber").toString());
			list.add(cb.equal(from.get("vehicleNumber"), licencetype));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("customerName"))
				&& !filterRequestDTO.getFilters().get("customerName").toString().trim().isEmpty()) {

			String district = (filterRequestDTO.getFilters().get("customerName").toString());
			list.add(cb.equal(from.get("customerName"), district));
		}
		if (Objects.nonNull(filterRequestDTO.getFilters().get("customerMobileNumber"))
				&& !filterRequestDTO.getFilters().get("customerMobileNumber").toString().trim().isEmpty()) {

			String district = (filterRequestDTO.getFilters().get("customerMobileNumber").toString());
			list.add(cb.equal(from.get("customerMobileNumber"), district));
		}
		if (Objects.nonNull(filterRequestDTO.getFilters().get("driverName"))
				&& !filterRequestDTO.getFilters().get("driverName").toString().trim().isEmpty()) {

			String district = (filterRequestDTO.getFilters().get("driverName").toString());
			list.add(cb.equal(from.get("driverName"), district));
		}
		if (Objects.nonNull(filterRequestDTO.getFilters().get("submittedBy"))
				&& !filterRequestDTO.getFilters().get("submittedBy").toString().trim().isEmpty()) {

			String district = (filterRequestDTO.getFilters().get("submittedBy").toString());
			list.add(cb.equal(from.get("submittedBy"), district));
		}
		if (Objects.nonNull(filterRequestDTO.getFilters().get("paymentType"))
				&& !filterRequestDTO.getFilters().get("paymentType").toString().trim().isEmpty()) {

			String district = (filterRequestDTO.getFilters().get("paymentType").toString());
			list.add(cb.equal(from.get("paymentType"), district));
		}
		if (Objects.nonNull(filterRequestDTO.getFilters().get("acOrNonAc"))
				&& !filterRequestDTO.getFilters().get("acOrNonAc").toString().trim().isEmpty()) {

			String district = (filterRequestDTO.getFilters().get("acOrNonAc").toString());
			list.add(cb.equal(from.get("acOrNonAc"), district));
		}
		if (Objects.nonNull(filterRequestDTO.getFilters().get("status"))
				&& !filterRequestDTO.getFilters().get("status").toString().trim().isEmpty()) {

			String district = (filterRequestDTO.getFilters().get("status").toString());
			list.add(cb.equal(from.get("status"), district));
		}
//		if (Objects.nonNull(filterRequestDTO.getFilters().get("status"))) {
//			Boolean status = Boolean.valueOf(filterRequestDTO.getFilters().get("status").toString());
//			list.add(cb.equal(from.get("status"), status));
//		}
//		if (Objects.nonNull(filterRequestDTO.getFilters().get("status"))) {
//		    boolean status = filterRequestDTO.getFilters().get("status");
//		    list.add(cb.equal(from.get("status"), status));
//		}

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

	}

	@Override
	public GenericResponse getPendingList() {
		List<TripDetailsEntity> DepList = tripDetailsRepository.getPendingList();
		if (CollectionUtils.isEmpty(DepList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(), ErrorMessages.NO_RECORD_FOUND);
		}
		return Library.getSuccessfulResponse(DepList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	public GenericResponse getLastRecordByVehicleNumber(String vehicleNumber) {
		Optional<TripDetailsEntity> lastRecord = tripDetailsRepository.getLastRecordByVehicleNumber(vehicleNumber);
		if (!lastRecord.isPresent()) {

			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(lastRecord, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
}
