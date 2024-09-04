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
import com.devar.cabs.entity.DriverDetailsEntity;
import com.devar.cabs.exception.InvalidDataValidation;
import com.devar.cabs.repository.DriverDetailsRepository;
import com.devar.cabs.requestDTO.DriverDetailsRequestDTO;
import com.devar.cabs.requestDTO.PaginationRequestDTO;
import com.devar.cabs.requestDTO.PaginationResponseDTO;
import com.devar.cabs.service.DriverDetailsService;
import com.devar.cabs.utility.GenericResponse;
import com.devar.cabs.utility.Library;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class DriverDetailsServiceImpl implements DriverDetailsService {

	@Autowired
	DriverDetailsRepository driverDetailsRepository;

	@Autowired
	private EntityManager entityManager;

	public GenericResponse add(DriverDetailsRequestDTO driverDetailsRequestDTO) {
		Optional<DriverDetailsEntity> driveraadhar = driverDetailsRepository
				.findByAadharNumber(driverDetailsRequestDTO.getAadharNumber());
		if (driveraadhar.isPresent()) {
//			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
//					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { "AADHAR" }));
			return Library.getFailResponseCode(ErrorCode.ALREADY_EXIST.getErrorCode(),
					"This Aadhar Number is Already Exist");
		} else {
			DriverDetailsEntity driverDetailsEntity = new DriverDetailsEntity();
			driverDetailsEntity.setAadharNumber(driverDetailsRequestDTO.getAadharNumber());
			driverDetailsEntity.setCounty(driverDetailsRequestDTO.getCounty());
			driverDetailsEntity.setDistrict(driverDetailsRequestDTO.getDistrict());
			driverDetailsEntity.setDoorNumber(driverDetailsRequestDTO.getDoorNumber());
			driverDetailsEntity.setDrivingLicenseNumber(driverDetailsRequestDTO.getDrivingLicenseNumber());
			driverDetailsEntity.setIsPermanentDriver(driverDetailsRequestDTO.getIsPermanentDriver());
			driverDetailsEntity.setMobileNumber(driverDetailsRequestDTO.getMobileNumber());
			driverDetailsEntity.setName(driverDetailsRequestDTO.getName());
			driverDetailsEntity.setState(driverDetailsRequestDTO.getState());
			driverDetailsEntity.setStatus(driverDetailsRequestDTO.getStatus());
			driverDetailsEntity.setStreet(driverDetailsRequestDTO.getStreet());
			driverDetailsEntity.setVillageOrCity(driverDetailsRequestDTO.getVillageOrCity());
			log.info("====================="+driverDetailsEntity);
			return Library.getSuccessfulResponse(driverDetailsRepository.save(driverDetailsEntity),
					ErrorCode.CREATED.getErrorCode(), ErrorMessages.RECORED_CREATED);
		}
	}

	@Override
	public GenericResponse update(DriverDetailsRequestDTO driverDetailsRequestDTO) {
		if (Objects.isNull(driverDetailsRequestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { "ID" }));
		}
		Optional<DriverDetailsEntity> entityOptional = driverDetailsRepository
				.findByAadharNotInId(driverDetailsRequestDTO.getAadharNumber(), driverDetailsRequestDTO.getId());
		if (entityOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { "NAME" }));
		}
		entityOptional = driverDetailsRepository.findById(driverDetailsRequestDTO.getId());
		if (!entityOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "ID" }));
		}
		DriverDetailsEntity entity = entityOptional.get();
		entity.setAadharNumber(driverDetailsRequestDTO.getAadharNumber());
		entity.setCounty(driverDetailsRequestDTO.getCounty());
		entity.setDistrict(driverDetailsRequestDTO.getDistrict());
		entity.setDoorNumber(driverDetailsRequestDTO.getDoorNumber());
		entity.setDrivingLicenseNumber(driverDetailsRequestDTO.getDrivingLicenseNumber());
		entity.setIsPermanentDriver(driverDetailsRequestDTO.getIsPermanentDriver());
		entity.setMobileNumber(driverDetailsRequestDTO.getMobileNumber());
		entity.setName(driverDetailsRequestDTO.getName());
		entity.setState(driverDetailsRequestDTO.getState());
		entity.setStatus(driverDetailsRequestDTO.getStatus());
		entity.setStreet(driverDetailsRequestDTO.getStreet());
		entity.setVillageOrCity(driverDetailsRequestDTO.getVillageOrCity());
		driverDetailsRepository.save(entity);
		return Library.getSuccessfulResponse(entity, ErrorCode.CREATED.getErrorCode(), ErrorMessages.RECORED_UPDATED);
	}

	@Override
	public GenericResponse getById(Long id) {
		Optional<DriverDetailsEntity> driverDetails = driverDetailsRepository.findById(id);
		if (!driverDetails.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		}
		return Library.getSuccessfulResponse(driverDetails, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getAll() {
		List<DriverDetailsEntity> DepList = driverDetailsRepository.findAllByOrderByIdDesc();
		if (CollectionUtils.isEmpty(DepList)) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
			}
//		List<SiteVisitResponseDTO> depResponseList = DepList.stream().map(sitevisitmapper::entityToResponseDTO)
//				.collect(Collectors.toList());
		return Library.getSuccessfulResponse(DepList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	public GenericResponse getsubPagesearchNewByFilter(PaginationRequestDTO requestData) {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		List<DriverDetailsEntity> list = this.getSubRecordsByFilterDTO1(requestData);
		List<DriverDetailsEntity> list1 = this.getSubRecordsByFilterDTO2(requestData);
		if (CollectionUtils.isEmpty(list) && CollectionUtils.isEmpty(list1)) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
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

	public List<DriverDetailsEntity> getSubRecordsByFilterDTO1(PaginationRequestDTO filterRequestDTO) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<DriverDetailsEntity> cq = cb.createQuery(DriverDetailsEntity.class);
		Root<DriverDetailsEntity> from = cq.from(DriverDetailsEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<DriverDetailsEntity> typedQuery = null;
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

	public List<DriverDetailsEntity> getSubRecordsByFilterDTO2(PaginationRequestDTO filterRequestDTO) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<DriverDetailsEntity> cq = cb.createQuery(DriverDetailsEntity.class);
		Root<DriverDetailsEntity> from = cq.from(DriverDetailsEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<DriverDetailsEntity> typedQuery1 = null;
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
			Root<DriverDetailsEntity> from) {

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

			String licencetype = (filterRequestDTO.getFilters().get("licenseType").toString());
			list.add(cb.equal(from.get("licenseType"), licencetype));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("name"))
				&& !filterRequestDTO.getFilters().get("name").toString().trim().isEmpty()) {

			String district = (filterRequestDTO.getFilters().get("name").toString());
			list.add(cb.equal(from.get("name"), district));
		}
		if (Objects.nonNull(filterRequestDTO.getFilters().get("mobileNumber"))
				&& !filterRequestDTO.getFilters().get("mobileNumber").toString().trim().isEmpty()) {

			String district = (filterRequestDTO.getFilters().get("mobileNumber").toString());
			list.add(cb.equal(from.get("mobileNumber"), district));
		}
		if (Objects.nonNull(filterRequestDTO.getFilters().get("drivingLicenseNumber"))
				&& !filterRequestDTO.getFilters().get("drivingLicenseNumber").toString().trim().isEmpty()) {

			String district = (filterRequestDTO.getFilters().get("drivingLicenseNumber").toString());
			list.add(cb.equal(from.get("drivingLicenseNumber"), district));
		}
		if (Objects.nonNull(filterRequestDTO.getFilters().get("aadharNumber"))
				&& !filterRequestDTO.getFilters().get("aadharNumber").toString().trim().isEmpty()) {

			String district = (filterRequestDTO.getFilters().get("aadharNumber").toString());
			list.add(cb.equal(from.get("aadharNumber"), district));
		}
		if (Objects.nonNull(filterRequestDTO.getFilters().get("district"))
				&& !filterRequestDTO.getFilters().get("district").toString().trim().isEmpty()) {

			String district = (filterRequestDTO.getFilters().get("district").toString());
			list.add(cb.equal(from.get("district"), district));
		}
		if (Objects.nonNull(filterRequestDTO.getFilters().get("status"))) {
		    Boolean status = Boolean.valueOf(filterRequestDTO.getFilters().get("status").toString());
		    list.add(cb.equal(from.get("status"), status));
		}
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
	
	public GenericResponse getAllActive() {
		List<DriverDetailsEntity> activeDriverDetails = driverDetailsRepository.findByStatusOrderByModifiedDateDesc(Boolean.TRUE);
		if (CollectionUtils.isEmpty(activeDriverDetails)) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		}
//		List<DriverDetailsEntity> actionTakenResponseDtos = activeDriverDetails.stream()
//				.map(actionTakenMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(activeDriverDetails, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	public void importExcelData(MultipartFile file) {
	    try (InputStream inputStream = file.getInputStream();
	         Workbook workbook = new XSSFWorkbook(inputStream)) {

	        Sheet sheet = workbook.getSheetAt(0);
	        Iterator<Row> rows = sheet.iterator();

	        // Skip header row
	        if (rows.hasNext()) rows.next();

	        while (rows.hasNext()) {
	            Row row = rows.next();
	            DriverDetailsEntity driverDetails = new DriverDetailsEntity();

	            driverDetails.setAadharNumber(getStringCellValue(row.getCell(0)));
	            driverDetails.setCounty(getStringCellValue(row.getCell(1)));
	            driverDetails.setDistrict(getStringCellValue(row.getCell(2)));
	            driverDetails.setDoorNumber(getStringCellValue(row.getCell(3)));
	            driverDetails.setDrivingLicenseNumber(getStringCellValue(row.getCell(4)));
	            driverDetails.setIsPermanentDriver(getBooleanCellValue(row.getCell(5)));
	            driverDetails.setMobileNumber(getStringCellValue(row.getCell(6)));
	            driverDetails.setName(getStringCellValue(row.getCell(7)));
	            driverDetails.setState(getStringCellValue(row.getCell(8)));
	            driverDetails.setStatus(getBooleanCellValue(row.getCell(9)));
	            driverDetails.setStreet(getStringCellValue(row.getCell(10)));
	            driverDetails.setVillageOrCity(getStringCellValue(row.getCell(11)));

	            driverDetails.setCreatedBy(getNumericCellValue(row.getCell(12)));
	            driverDetails.setModifiedBy(getNumericCellValue(row.getCell(14)));

	            // Handle date values if present
	            // Uncomment and adjust if your file contains dates
	            // driverDetails.setCreatedDate(getDateCellValue(row.getCell(13)));
	            // driverDetails.setModifiedDate(getDateCellValue(row.getCell(15)));

	            driverDetailsRepository.save(driverDetails);
	        }
	    } catch (IOException e) {
	        throw new RuntimeException("Failed to import data from Excel file", e);
	    }
	}

	private String getStringCellValue(Cell cell) {
	    return cell == null ? "" : cell.toString();
	}

	private boolean getBooleanCellValue(Cell cell) {
	    if (cell == null) return false;
	    switch (cell.getCellType()) {
	        case BOOLEAN:
	            return cell.getBooleanCellValue();
	        case STRING:
	            return Boolean.parseBoolean(cell.getStringCellValue());
	        default:
	            return false;
	    }
	}

	private long getNumericCellValue(Cell cell) {
	    if (cell == null) return 0;
	    switch (cell.getCellType()) {
	        case NUMERIC:
	            return (long) cell.getNumericCellValue();
	        case STRING:
	            try {
	                return Long.parseLong(cell.getStringCellValue());
	            } catch (NumberFormatException e) {
	                return 0;
	            }
	        default:
	            return 0;
	    }
	}

	// Uncomment and adjust if your file contains dates
	// private LocalDateTime getDateCellValue(Cell cell) {
//	     if (cell == null || cell.getCellType() != CellType.NUMERIC) return null;
//	     return cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	// }
}