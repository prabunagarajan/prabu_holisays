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
import com.devar.cabs.entity.VehicleDetailsEntity;
import com.devar.cabs.exception.InvalidDataValidation;
import com.devar.cabs.repository.VehicleDetailsRepository;
import com.devar.cabs.requestDTO.PaginationRequestDTO;
import com.devar.cabs.requestDTO.PaginationResponseDTO;
import com.devar.cabs.requestDTO.VehicleDetailsRequestDTO;
import com.devar.cabs.requestDTO.VehicleNextDateDTO;
import com.devar.cabs.service.VehicleDetailsService;
import com.devar.cabs.utility.GenericResponse;
import com.devar.cabs.utility.Library;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class VehicleDetailsServiceImpl implements VehicleDetailsService{

	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	VehicleDetailsRepository vehicleDetailsRepository;
	
	public GenericResponse add(VehicleDetailsRequestDTO vehicleDetailsRequestDTO) {
		Optional<VehicleDetailsEntity> vehiclenum = vehicleDetailsRepository
				.findByVehicleNumber(vehicleDetailsRequestDTO.getVehicleNumber());
		if (vehiclenum.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { "Vehicle Number" }));
		} else {
			VehicleDetailsEntity vehicleDetailsEntity = new VehicleDetailsEntity();
			vehicleDetailsEntity.setVehicleNumber(vehicleDetailsRequestDTO.getVehicleNumber());
			vehicleDetailsEntity.setVehicleName(vehicleDetailsRequestDTO.getVehicleName());
			vehicleDetailsEntity.setVehicleColor(vehicleDetailsRequestDTO.getVehicleColor());
			vehicleDetailsEntity.setStatus(vehicleDetailsRequestDTO.getStatus());
			vehicleDetailsEntity.setRemarks(vehicleDetailsRequestDTO.getRemarks());
			vehicleDetailsEntity.setInsuranceDate(vehicleDetailsRequestDTO.getInsuranceDate());
			vehicleDetailsEntity.setFcDate(vehicleDetailsRequestDTO.getFcDate());
			vehicleDetailsEntity.setTaxDate(vehicleDetailsRequestDTO.getTaxDate());
			vehicleDetailsEntity.setPolutionDate(vehicleDetailsRequestDTO.getPolutionDate());

			return Library.getSuccessfulResponse(vehicleDetailsRepository.save(vehicleDetailsEntity),
					ErrorCode.CREATED.getErrorCode(), ErrorMessages.RECORED_CREATED);
		}
	}
	
	@Override
	public GenericResponse update(VehicleDetailsRequestDTO vehicleDetailsRequestDTO) {
		if (Objects.isNull(vehicleDetailsRequestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { "ID" }));
		}
		Optional<VehicleDetailsEntity> entityOptional = vehicleDetailsRepository
				.findByVehicleNumberNotInId(vehicleDetailsRequestDTO.getVehicleNumber(), vehicleDetailsRequestDTO.getId());
		if (entityOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { "Vehicle Number" }));
		}
		entityOptional = vehicleDetailsRepository.findById(vehicleDetailsRequestDTO.getId());
		if (!entityOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "ID" }));
		}
		VehicleDetailsEntity vehicleDetailsEntity = entityOptional.get();
		vehicleDetailsEntity.setVehicleNumber(vehicleDetailsRequestDTO.getVehicleNumber());
		vehicleDetailsEntity.setVehicleName(vehicleDetailsRequestDTO.getVehicleName());
		vehicleDetailsEntity.setVehicleColor(vehicleDetailsRequestDTO.getVehicleColor());
		vehicleDetailsEntity.setStatus(vehicleDetailsRequestDTO.getStatus());
		vehicleDetailsEntity.setRemarks(vehicleDetailsRequestDTO.getRemarks());
		vehicleDetailsEntity.setInsuranceDate(vehicleDetailsRequestDTO.getInsuranceDate());
		vehicleDetailsEntity.setFcDate(vehicleDetailsRequestDTO.getFcDate());
		vehicleDetailsEntity.setTaxDate(vehicleDetailsRequestDTO.getTaxDate());
		vehicleDetailsEntity.setModifiedDate(new Date());
		vehicleDetailsEntity.setPolutionDate(vehicleDetailsRequestDTO.getPolutionDate());
		vehicleDetailsRepository.save(vehicleDetailsEntity);
		return Library.getSuccessfulResponse(vehicleDetailsEntity, ErrorCode.CREATED.getErrorCode(), ErrorMessages.RECORED_UPDATED);
	}

	@Override
	public GenericResponse getById(Long id) {
		Optional<VehicleDetailsEntity> driverDetails = vehicleDetailsRepository.findById(id);
		if (!driverDetails.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(driverDetails, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getAll() {
		List<VehicleDetailsEntity> DepList = vehicleDetailsRepository.findAllByOrderByIdDesc();
		if (CollectionUtils.isEmpty(DepList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(), ErrorMessages.NO_RECORD_FOUND);
		}
//		List<SiteVisitResponseDTO> depResponseList = DepList.stream().map(sitevisitmapper::entityToResponseDTO)
//				.collect(Collectors.toList());
		return Library.getSuccessfulResponse(DepList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	public GenericResponse getsubPagesearchNewByFilter(PaginationRequestDTO requestData) {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		List<VehicleDetailsEntity> list = this.getSubRecordsByFilterDTO1(requestData);
		List<VehicleDetailsEntity> list1 = this.getSubRecordsByFilterDTO2(requestData);
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

	public List<VehicleDetailsEntity> getSubRecordsByFilterDTO1(PaginationRequestDTO filterRequestDTO) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<VehicleDetailsEntity> cq = cb.createQuery(VehicleDetailsEntity.class);
		Root<VehicleDetailsEntity> from = cq.from(VehicleDetailsEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<VehicleDetailsEntity> typedQuery = null;
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

	public List<VehicleDetailsEntity> getSubRecordsByFilterDTO2(PaginationRequestDTO filterRequestDTO) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<VehicleDetailsEntity> cq = cb.createQuery(VehicleDetailsEntity.class);
		Root<VehicleDetailsEntity> from = cq.from(VehicleDetailsEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<VehicleDetailsEntity> typedQuery1 = null;
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
			Root<VehicleDetailsEntity> from) {

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

		if (Objects.nonNull(filterRequestDTO.getFilters().get("vehicleName"))
				&& !filterRequestDTO.getFilters().get("vehicleName").toString().trim().isEmpty()) {

			String district = (filterRequestDTO.getFilters().get("vehicleName").toString());
			list.add(cb.equal(from.get("vehicleName"), district));
		}
		if (Objects.nonNull(filterRequestDTO.getFilters().get("vehicleColor"))
				&& !filterRequestDTO.getFilters().get("vehicleColor").toString().trim().isEmpty()) {

			String district = (filterRequestDTO.getFilters().get("vehicleColor").toString());
			list.add(cb.equal(from.get("vehicleColor"), district));
		}
		

		if (Objects.nonNull(filterRequestDTO.getFilters().get("modifiedBy"))
				&& !filterRequestDTO.getFilters().get("modifiedBy").toString().trim().isEmpty()) {

			String modifiedBy = (filterRequestDTO.getFilters().get("modifiedBy").toString());
			list.add(cb.equal(from.get("modifiedBy"), modifiedBy));
		}
		if (Objects.nonNull(filterRequestDTO.getFilters().get("status"))) {
		    Boolean status = Boolean.valueOf(filterRequestDTO.getFilters().get("status").toString());
		    list.add(cb.equal(from.get("status"), status));
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
	
	public GenericResponse getAllActive() {
		List<VehicleDetailsEntity> activeDriverDetails = vehicleDetailsRepository.findByStatusOrderByModifiedDateDesc(Boolean.TRUE);
		if (CollectionUtils.isEmpty(activeDriverDetails)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(activeDriverDetails, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	

	public GenericResponse getNextDate() {
		List<VehicleNextDateDTO> DepList = vehicleDetailsRepository.getNextDate();
		if (CollectionUtils.isEmpty(DepList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(), ErrorMessages.NO_RECORD_FOUND);
		}
		return Library.getSuccessfulResponse(DepList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
}
