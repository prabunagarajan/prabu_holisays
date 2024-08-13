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
import com.oasys.helpdesk.dto.HelpdeskTicketAuditResponseDTO;
import com.oasys.helpdesk.dto.KeyValueResponseDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.PaginationResponseDTO;
import com.oasys.helpdesk.entity.HelpdeskTicketAuditEntity;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.HelpDeskTicketAction;
import com.oasys.helpdesk.utility.Library;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class HelpdeskTicketAuditServiceImpl implements HelpdeskTicketAuditService{
	
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private CommonDataController commonDataController;

	@Override
	public GenericResponse getAllByRequestFilter(PaginationRequestDTO requestData) throws ParseException {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		Long count = this.getCountBySearchFields(requestData);
		log.info("total count :: {}", count);
		if (count > 0) {
			List<HelpdeskTicketAuditEntity> list = this.getRecordsByFilterDTO(requestData);
			if (CollectionUtils.isEmpty(list)) {
				throw new RecordNotFoundException("No Record Found");
			}
			List<HelpdeskTicketAuditResponseDTO> dtoList = list.stream().map(this::convertEntityToResponse)
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
	
	public List<HelpdeskTicketAuditEntity> getRecordsByFilterDTO(PaginationRequestDTO filterRequestDTO) throws ParseException {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<HelpdeskTicketAuditEntity> cq = cb.createQuery(HelpdeskTicketAuditEntity.class);
		Root<HelpdeskTicketAuditEntity> from = cq.from(HelpdeskTicketAuditEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<HelpdeskTicketAuditEntity> typedQuery = null;
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

		List<HelpdeskTicketAuditEntity> data = typedQuery.getResultList();
		if (CollectionUtils.isEmpty(data)) {
			throw new RecordNotFoundException("No Record Found");
		}
		return data;
	}
	
	private Long getCountBySearchFields(PaginationRequestDTO filterRequestDTO) throws ParseException {
		log.info("getting total count by search fields :: {}", filterRequestDTO.toString());
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<HelpdeskTicketAuditEntity> from = cq.from(HelpdeskTicketAuditEntity.class);
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
			Root<HelpdeskTicketAuditEntity> from) throws ParseException {
		
		
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

				
				if (Objects.nonNull(filterRequestDTO.getFilters().get(Constant.TICKET_NUMBER))
						&& !filterRequestDTO.getFilters().get(Constant.TICKET_NUMBER).toString().trim().isEmpty()) {

					String ticketNumber = String.valueOf(filterRequestDTO.getFilters().get(Constant.TICKET_NUMBER).toString());
					list.add(cb.equal(from.get(Constant.TICKET_NUMBER), ticketNumber));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get(Constant.TICKET_STATUS_ID))
						&& !filterRequestDTO.getFilters().get(Constant.TICKET_STATUS_ID).toString().trim().isEmpty()) {
					Long status = Long.valueOf(filterRequestDTO.getFilters().get(Constant.TICKET_STATUS_ID).toString());
					list.add(cb.equal(from.get(Constant.TICKET_STATUS), status));
				}
				
				if (Objects.nonNull(filterRequestDTO.getFilters().get(Constant.ACTION))
						&& !filterRequestDTO.getFilters().get(Constant.ACTION).toString().trim().isEmpty()) {

					String deviceStatus = String.valueOf(filterRequestDTO.getFilters().get(Constant.ACTION).toString());
					list.add(cb.equal(from.get(Constant.ACTION), HelpDeskTicketAction.valueOf(deviceStatus)));
				}
				

			}
		} catch (ParseException e) {
			throw new InvalidParameterException("No Record Found");
		}
	}
	private HelpdeskTicketAuditResponseDTO convertEntityToResponse(HelpdeskTicketAuditEntity entity) {
		HelpdeskTicketAuditResponseDTO response = commonUtil.modalMap(entity, HelpdeskTicketAuditResponseDTO.class);
		if(Objects.nonNull(entity.getActionPerformedBy())) {
			response.setActionPerformedByUser(commonDataController.getUserNameById(entity.getActionPerformedBy().getId()));
			response.setActionPerformedByRole(!CollectionUtils.isEmpty(entity.getActionPerformedBy().getRoles())
					? entity.getActionPerformedBy().getRoles().get(0).getRoleName()
					: null);
			String designationMasterResponse = commonDataController.getMasterDropDownValueByKey(
					Constant.DESIGNATION_DROPDOWN_KEY, entity.getActionPerformedBy().getDesignationCode());
			if (StringUtils.isNotBlank(designationMasterResponse)) {
				response.setDesignationValue(designationMasterResponse);
				response.setDesignationCode( entity.getActionPerformedBy().getDesignationCode());
			}
		}
		
		if(Objects.nonNull(entity.getTicketStatus())) {
			response.setTicketStatus(entity.getTicketStatus().getTicketstatusname());
		}
		
		if (Objects.nonNull(entity.getAssignTo())) {
			response.setAssignTo(commonDataController.getUserNameById(entity.getAssignTo().getId()));
		}
		
		if(Objects.nonNull(entity.getModifiedDate())) {
			response.setActionPerformedDateTime(entity.getModifiedDate().toString());
		}
		return response;
		
	}
	
	@Override
	public GenericResponse getAllActions() {
		List<KeyValueResponseDTO> responseList = new ArrayList<>();
		for (HelpDeskTicketAction status : HelpDeskTicketAction.values()) {
			KeyValueResponseDTO response = new KeyValueResponseDTO();
			response.setKey(status);
			response.setValue(status.getType());
			responseList.add(response);
		}
		return Library.getSuccessfulResponse(responseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
}
