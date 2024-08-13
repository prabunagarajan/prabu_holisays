package com.oasys.helpdesk.service;

import static com.oasys.posasset.constant.Constant.DESC;

import java.util.ArrayList;
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

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.PaginationResponseDTO;
import com.oasys.helpdesk.dto.SupplierDTO;
import com.oasys.helpdesk.dto.SupplierResponseDTO;
import com.oasys.helpdesk.entity.SupplierEntity;
import com.oasys.helpdesk.mapper.SupplierMapper;
import com.oasys.helpdesk.mapper.WorkflowNotifyMapper;
import com.oasys.helpdesk.repository.SupplierRepository;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class SupplierServiceImpl {

	@Autowired
	private SupplierRepository supplierRepository;

	@Autowired
	WorkflowNotifyMapper workflowmapper;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	SupplierMapper suppliermapper;

	public GenericResponse getsubPagesearchNewByFilterstock(PaginationRequestDTO requestData) {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		List<SupplierEntity> list = this.getSubRecordsByFilterDTO1(requestData);
		List<SupplierEntity> list1 = this.getSubRecordsByFilterDTO2(requestData);

		if (CollectionUtils.isEmpty(list) && CollectionUtils.isEmpty(list1)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(), "No Record Found");
		}
		if (!list.isEmpty()) {
			List<SupplierResponseDTO> dtoList = list.stream().map(suppliermapper::convertEntityToResponseDTO)
					.collect(Collectors.toList());
			paginationResponseDTO.setContents(dtoList);
		}
		Long count1 = (long) list1.size();
		paginationResponseDTO.setNumberOfElements(Objects.nonNull(list.size()) ? list.size() : null);
		paginationResponseDTO.setTotalElements(count1);
		return Library.getSuccessfulResponse(paginationResponseDTO, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	public List<SupplierEntity> getSubRecordsByFilterDTO1(PaginationRequestDTO filterRequestDTO) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<SupplierEntity> cq = cb.createQuery(SupplierEntity.class);
		Root<SupplierEntity> from = cq.from(SupplierEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<SupplierEntity> typedQuery = null;
		addSubCriteria(cb, list, filterRequestDTO, from);
		cq.where(cb.and(list.toArray(new Predicate[list.size()])));
		cq.distinct(true);
		if (StringUtils.isBlank(filterRequestDTO.getSortField())) {
			filterRequestDTO.setSortField("createdDate");
		}
		if (StringUtils.isNotBlank(filterRequestDTO.getSortOrder())
				&& filterRequestDTO.getSortOrder().equalsIgnoreCase(DESC)) {
			cq.orderBy(cb.desc(from.get(filterRequestDTO.getSortField())));

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

	public List<SupplierEntity> getSubRecordsByFilterDTO2(PaginationRequestDTO filterRequestDTO) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<SupplierEntity> cq = cb.createQuery(SupplierEntity.class);
		Root<SupplierEntity> from = cq.from(SupplierEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<SupplierEntity> typedQuery = null;
		addSubCriteria(cb, list, filterRequestDTO, from);
		cq.where(cb.and(list.toArray(new Predicate[list.size()])));
		cq.distinct(true);
		if (StringUtils.isBlank(filterRequestDTO.getSortField())) {
			filterRequestDTO.setSortField("createdDate");
		}
		if (StringUtils.isNotBlank(filterRequestDTO.getSortOrder())
				&& filterRequestDTO.getSortOrder().equalsIgnoreCase(DESC)) {
			cq.orderBy(cb.desc(from.get(filterRequestDTO.getSortField())));

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

	private void addSubCriteria(CriteriaBuilder cb, List<Predicate> list, PaginationRequestDTO filterRequestDTO,
			Root<SupplierEntity> from) {

		if (Objects.nonNull(filterRequestDTO.getFilters().get("supplier"))
				&& !filterRequestDTO.getFilters().get("supplier").toString().trim().isEmpty()) {

			String supplier = String.valueOf(filterRequestDTO.getFilters().get("supplier").toString());
			list.add(cb.equal(from.get("supplier"), supplier));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("supplierName"))
				&& !filterRequestDTO.getFilters().get("supplierName").toString().trim().isEmpty()) {

			String supplierName = String.valueOf(filterRequestDTO.getFilters().get("supplierName").toString());
			list.add(cb.equal(from.get("supplierName"), supplierName));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("mobileNumber"))
				&& !filterRequestDTO.getFilters().get("mobileNumber").toString().trim().isEmpty()) {

			String mobileNumber = String.valueOf(filterRequestDTO.getFilters().get("mobileNumber").toString());
			list.add(cb.equal(from.get("mobileNumber"), mobileNumber));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("emailId"))
				&& !filterRequestDTO.getFilters().get("emailId").toString().trim().isEmpty()) {

			String emailId = String.valueOf(filterRequestDTO.getFilters().get("emailId").toString());
			list.add(cb.equal(from.get("emailId"), emailId));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("address"))
				&& !filterRequestDTO.getFilters().get("address").toString().trim().isEmpty()) {

			String address = String.valueOf(filterRequestDTO.getFilters().get("address").toString());
			list.add(cb.equal(from.get("address"), address));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("createdBy"))
				&& !filterRequestDTO.getFilters().get("createdBy").toString().trim().isEmpty()) {

			Long createdBy = Long.valueOf(filterRequestDTO.getFilters().get("createdBy").toString());
			list.add(cb.equal(from.get("createdBy"), createdBy));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("active"))
				&& !filterRequestDTO.getFilters().get("active").toString().trim().isEmpty()) {

			Boolean emailId = Boolean.valueOf(filterRequestDTO.getFilters().get("active").toString());
			list.add(cb.equal(from.get("active"), emailId));
		}

	}

	public GenericResponse save(SupplierDTO supplierDTO) {
		try {
			Optional<SupplierEntity> deviceReturnDetails = supplierRepository.findByMobileNumberAndEmailIdAndSupplier(
					supplierDTO.getMobileNumber(), supplierDTO.getEmailId(), supplierDTO.getSupplier());
			if (!deviceReturnDetails.isPresent()) {
				SupplierEntity supplierEntity = commonUtil.modalMap(supplierDTO, SupplierEntity.class);
				supplierEntity = supplierRepository.save(supplierEntity);
				return Library.getSuccessfulResponse(supplierEntity, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
						ErrorMessages.RECORED_CREATED);
			} else {
				return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), "Record already exist");
			}
		} catch (Exception e) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), e.getMessage());
		}
	}

	public GenericResponse getById(Long id) {
		Optional<SupplierEntity> supplierEntity = supplierRepository.findById(id);
		if (supplierEntity.isPresent()) {
			return Library.getSuccessfulResponse(suppliermapper.convertEntityToResponseDTO(supplierEntity.get()),
					ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
		} else {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		}
	}

	public GenericResponse update(SupplierDTO supplierDTO) {
		try {
			Optional<SupplierEntity> deviceReturnDetails = supplierRepository.findById(supplierDTO.getId());
			if (!deviceReturnDetails.isPresent()) {
				return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), "Record not exist");
			} else {
				SupplierEntity supplierEntity = commonUtil.modalMap(supplierDTO, SupplierEntity.class);
				supplierEntity.setCreatedBy(supplierDTO.getCreatedBy());
				supplierEntity = supplierRepository.save(supplierEntity);
				return Library.getSuccessfulResponse(supplierEntity, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
						ErrorMessages.RECORED_UPDATED);
			}
		} catch (Exception e) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), e.getMessage());
		}
	}

	/**
	 * @return
	 * Get All Active List From supplier_master
	 */
	public GenericResponse getAllActive() {
		List<SupplierEntity> List = supplierRepository.findAllByIsActiveOrderByModifiedDateDesc(Boolean.TRUE);
		if (CollectionUtils.isEmpty(List)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(List, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

}
