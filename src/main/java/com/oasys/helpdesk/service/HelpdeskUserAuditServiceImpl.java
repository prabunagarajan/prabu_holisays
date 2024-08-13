package com.oasys.helpdesk.service;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.dto.HelpdeskUserAutditResponseDTO;
import com.oasys.helpdesk.dto.KeyValueResponseDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.PaginationResponseDTO;
import com.oasys.helpdesk.entity.HelpdeskUserAuditEntity;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.HelpdeskUserAuditAction;
import com.oasys.helpdesk.utility.Library;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class HelpdeskUserAuditServiceImpl implements HelpdeskUserAuditService{
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private CommonDataController commonDataController;

	@Override
	public GenericResponse getAllByRequestFilter(PaginationRequestDTO requestData) {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		Long count = this.getCountBySearchFields(requestData);
		log.info("total count :: {}", count);
		if (count > 0) {
			List<HelpdeskUserAuditEntity> list = this.getRecordsByFilterDTO(requestData);
			if (CollectionUtils.isEmpty(list)) {
				throw new RecordNotFoundException("No Record Found");
			}
			List<HelpdeskUserAutditResponseDTO> dtoList = list.stream().map(this::convertEntityToResponse)
					.collect(Collectors.toList());
			
			paginationResponseDTO.setContents(dtoList);
			paginationResponseDTO.setNumberOfElements(Objects.nonNull(count) ? count.intValue() : null);
			paginationResponseDTO.setTotalElements(count);
			return Library.getSuccessfulResponse(paginationResponseDTO,
					ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
		}
		else {
			throw new RecordNotFoundException("No Record Found");
		}
	}
	
	public List<HelpdeskUserAuditEntity> getRecordsByFilterDTO(PaginationRequestDTO filterRequestDTO) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<HelpdeskUserAuditEntity> cq = cb.createQuery(HelpdeskUserAuditEntity.class);
		Root<HelpdeskUserAuditEntity> from = cq.from(HelpdeskUserAuditEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<HelpdeskUserAuditEntity> typedQuery = null;
		addCriteria(cb, list, filterRequestDTO, from);
		cq.where(cb.and(list.toArray(new Predicate[list.size()])));
		cq.distinct(true);
		if (StringUtils.isBlank(filterRequestDTO.getSortField())) {
			filterRequestDTO.setSortField(Constant.MODIFIED_DATE);
		}
		if (StringUtils.isNotBlank(filterRequestDTO.getSortOrder())
				&& filterRequestDTO.getSortOrder().equalsIgnoreCase(Constant.ASC)) {
			cq.orderBy(cb.asc(from.get(filterRequestDTO.getSortField())));
			
		} else {
			cq.orderBy(cb.desc(from.get(filterRequestDTO.getSortField())));

		}
		if (Objects.nonNull(filterRequestDTO.getPaginationSize()) && Objects.nonNull(filterRequestDTO.getPaginationSize())) {
			typedQuery = entityManager.createQuery(cq)
					.setFirstResult((filterRequestDTO.getPageNo() * filterRequestDTO.getPaginationSize()))
					.setMaxResults(filterRequestDTO.getPaginationSize());
		} else {
			typedQuery = entityManager.createQuery(cq);
		}

		List<HelpdeskUserAuditEntity> data = typedQuery.getResultList();
		if (CollectionUtils.isEmpty(data)) {
			throw new RecordNotFoundException("No Record Found");
		}
		return data;
	}
	
	private Long getCountBySearchFields(PaginationRequestDTO filterRequestDTO)  {
		log.info("getting total count by search fields :: {}", filterRequestDTO.toString());
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<HelpdeskUserAuditEntity> from = cq.from(HelpdeskUserAuditEntity.class);
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
			Root<HelpdeskUserAuditEntity> from)  {
		try {
			if (Objects.nonNull(filterRequestDTO.getFilters())) {
				log.info("filters ::" + filterRequestDTO.getFilters());

				if (Objects.nonNull(filterRequestDTO.getFilters().get(Constant.CREATED_DATE))
						&& !filterRequestDTO.getFilters().get(Constant.CREATED_DATE).toString().trim().isEmpty()) {

					Date fromDate = new SimpleDateFormat(Constant.DATE_FORMAT)
							.parse(filterRequestDTO.getFilters().get(Constant.CREATED_DATE).toString() + " " + Constant.START_TIME);
					Date toDate = new SimpleDateFormat(Constant.DATE_FORMAT)
							.parse(filterRequestDTO.getFilters().get(Constant.CREATED_DATE).toString() + " " + Constant.END_TIME);
					list.add(cb.between(from.get(Constant.CREATED_DATE), fromDate, toDate));

				}
				
				if (Objects.nonNull(filterRequestDTO.getFilters().get(Constant.ACTION))
						&& !filterRequestDTO.getFilters().get(Constant.ACTION).toString().trim().isEmpty()) {

					String action = String.valueOf(filterRequestDTO.getFilters().get(Constant.ACTION).toString());
					list.add(cb.equal(from.get(Constant.ACTION), HelpdeskUserAuditAction.valueOf(action)));
				}
				if (Objects.nonNull(filterRequestDTO.getFilters().get(Constant.EMPLOYMENT_STATUS))
						&& !filterRequestDTO.getFilters().get(Constant.EMPLOYMENT_STATUS).toString().trim().isEmpty()) {

					String employmentStatus = String.valueOf(filterRequestDTO.getFilters().get(Constant.EMPLOYMENT_STATUS).toString());
					list.add(cb.equal(from.get(Constant.EMPLOYMENT_STATUS), HelpdeskUserAuditAction.valueOf(employmentStatus)));
				}
				if (Objects.nonNull(filterRequestDTO.getFilters().get(Constant.USER_ID))
						&& !filterRequestDTO.getFilters().get(Constant.USER_ID).toString().trim().isEmpty()) {
					Long userId = Long.valueOf(filterRequestDTO.getFilters().get(Constant.USER_ID).toString());
					list.add(cb.equal(from.get(Constant.ACTION_PERFORMED_ON), userId));
				}
				

			}
		} catch (ParseException e) {
			throw new InvalidParameterException("No Record Found");
		}
	}
	private HelpdeskUserAutditResponseDTO convertEntityToResponse(HelpdeskUserAuditEntity entity) {
		HelpdeskUserAutditResponseDTO response = commonUtil.modalMap(entity, HelpdeskUserAutditResponseDTO.class);
		if(Objects.nonNull(entity.getActionPerformedBy())) {
			response.setActionPerformedByUsername(commonDataController.getUserNameById(entity.getActionPerformedBy().getId()));
			response.setActionPerformedByRole(!CollectionUtils.isEmpty(entity.getActionPerformedBy().getRoles())
					? entity.getActionPerformedBy().getRoles().get(0).getRoleName()
					: null);
			response.setActionPerformedByEmployeeId(entity.getActionPerformedBy().getEmployeeId());
			String designationMasterResponse = commonDataController.getMasterDropDownValueByKey(
					Constant.DESIGNATION_DROPDOWN_KEY, entity.getActionPerformedBy().getDesignationCode());
			if (StringUtils.isNotBlank(designationMasterResponse)) {
				response.setDesignationValue(designationMasterResponse);
				response.setDesignationCode( entity.getActionPerformedBy().getDesignationCode());
			}
		}
		if(Objects.nonNull(entity.getActionPerformedOn())) {
			response.setActionPerformedOnUsername(commonDataController.getUserNameById(entity.getActionPerformedOn().getId()));
			response.setActionPerformedOnRole(!CollectionUtils.isEmpty(entity.getActionPerformedOn().getRoles())
					? entity.getActionPerformedOn().getRoles().get(0).getRoleName()
					: null);
			response.setActionPerformedOnEmployeeId(entity.getActionPerformedOn().getEmployeeId());
		}
		
		
		if(Objects.nonNull(entity.getModifiedDate())) {
			response.setActionPerformedDateTime(entity.getModifiedDate().toString());
		}
		return response;
		
	}
	
	@Override
	public GenericResponse getAllActions() {
		List<KeyValueResponseDTO> responseList = new ArrayList<>();
		for (HelpdeskUserAuditAction status : HelpdeskUserAuditAction.values()) {
			KeyValueResponseDTO response = new KeyValueResponseDTO();
			response.setKey(status);
			response.setValue(status.getType());
			responseList.add(response);
		}
		return Library.getSuccessfulResponse(responseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
}
