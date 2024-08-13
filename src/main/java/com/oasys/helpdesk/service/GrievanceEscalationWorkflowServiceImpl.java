package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.CODE;
import static com.oasys.helpdesk.constant.Constant.ID;

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
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.GrievanceEscalationWorkflowRequestDTO;
import com.oasys.helpdesk.dto.GrievanceEscalationWorkflowResponseDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.PaginationResponseDTO;
import com.oasys.helpdesk.entity.GrievanceEscalationWorkflowEntity;
import com.oasys.helpdesk.mapper.GrievanceEscalationWorkflowMapper;
import com.oasys.helpdesk.repository.GrievanceEscalationWorkflowRepository;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.RandomUtil;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class GrievanceEscalationWorkflowServiceImpl implements GrievanceEscalationWorkflowService{

	@Autowired
	private GrievanceEscalationWorkflowMapper workflowMapper;
	
	@Autowired
	private GrievanceEscalationWorkflowRepository workflowRepository;
	
	public static final String GRIEVANCE_ESCALATION_WORKFLOW= "Grievance Escation Workflow";
	
	@Autowired
	private EntityManager entityManager;
	
	@Override
	public GenericResponse getAll() {
		List<GrievanceEscalationWorkflowEntity> workflowList = workflowRepository.findAllByOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(workflowList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<GrievanceEscalationWorkflowResponseDTO> workflowResponseList = workflowList.stream()
				.map(workflowMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(workflowResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	@Override
	@Transactional
	public GenericResponse save(GrievanceEscalationWorkflowRequestDTO requestDTO) {
		Optional<GrievanceEscalationWorkflowEntity> optional = workflowRepository.findByCodeIgnoreCase(requestDTO.getCode());
		if(optional.isPresent())
		{
				return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
						ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { CODE }));
			
		}
		
		requestDTO.setId(null);
		GrievanceEscalationWorkflowEntity entity = workflowMapper.convertRequestDTOToEntity(requestDTO,null);
		workflowRepository.save(entity);
		
		return Library.getSuccessfulResponse(workflowMapper.convertEntityToResponseDTO(entity), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_CREATED);
	}
	
	@Override
	public GenericResponse getById(Long id) {
		Optional<GrievanceEscalationWorkflowEntity> entity = workflowRepository.findById(id);
		if (!entity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(workflowMapper.convertEntityToResponseDTO(entity.get()), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	@Override
	@Transactional
	public GenericResponse update(GrievanceEscalationWorkflowRequestDTO requestDTO) {
		if(Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		
		Optional<GrievanceEscalationWorkflowEntity> optional = workflowRepository.findById(requestDTO.getId());
		if (!optional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		
		GrievanceEscalationWorkflowEntity entity = optional.get();
		GrievanceEscalationWorkflowEntity updatedEntity = workflowMapper.convertRequestDTOToEntity(requestDTO,entity);
		workflowRepository.save(updatedEntity);
		return Library.getSuccessfulResponse(workflowMapper.convertEntityToResponseDTO(entity), ErrorCode.CREATED.getErrorCode(),ErrorMessages.RECORED_UPDATED);
	
	}
	
	@Override
	public GenericResponse getCode() {
		MenuPrefix prefix = MenuPrefix.getType(GRIEVANCE_ESCALATION_WORKFLOW);
		String code = prefix.toString() + RandomUtil.getRandomNumber();
		while (true) {
			Optional<GrievanceEscalationWorkflowEntity> entity = workflowRepository.findByCodeIgnoreCase(code);
			if (entity.isPresent()) {
				code = prefix.toString() + RandomUtil.getRandomNumber();
			} else {
				break;
			}
		}
		return Library.getSuccessfulResponse(code, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	@Override
	public GenericResponse getAllActive() {
		List<GrievanceEscalationWorkflowEntity> list = workflowRepository.findByStatusOrderByModifiedDateDesc(Boolean.TRUE);
		if (CollectionUtils.isEmpty(list)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<GrievanceEscalationWorkflowResponseDTO> responseDto = list.stream()
				.map(workflowMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(responseDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	@Override
	public GenericResponse getAllByRequestFilter(PaginationRequestDTO requestData) throws ParseException {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		Long count = this.getCountBySearchFields(requestData);
		log.info("total count :: {}", count);
		if (count > 0) {
			List<GrievanceEscalationWorkflowEntity> list = this.getRecordsByFilterDTO(requestData);
			if (CollectionUtils.isEmpty(list)) {
				throw new RecordNotFoundException("No Record Found");
			}
			List<GrievanceEscalationWorkflowResponseDTO> dtoList = list.stream().map(workflowMapper::convertEntityToResponseDTO)
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
	
	public List<GrievanceEscalationWorkflowEntity> getRecordsByFilterDTO(PaginationRequestDTO filterRequestDTO) throws ParseException {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<GrievanceEscalationWorkflowEntity> cq = cb.createQuery(GrievanceEscalationWorkflowEntity.class);
		Root<GrievanceEscalationWorkflowEntity> from = cq.from(GrievanceEscalationWorkflowEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<GrievanceEscalationWorkflowEntity> typedQuery = null;
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

		List<GrievanceEscalationWorkflowEntity> data = typedQuery.getResultList();
		if (CollectionUtils.isEmpty(data)) {
			throw new RecordNotFoundException("No Record Found");
		}
		return data;
	}
	
	private Long getCountBySearchFields(PaginationRequestDTO filterRequestDTO) throws ParseException {
		log.info("getting total count by search fields :: {}", filterRequestDTO.toString());
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<GrievanceEscalationWorkflowEntity> from = cq.from(GrievanceEscalationWorkflowEntity.class);
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
			Root<GrievanceEscalationWorkflowEntity> from) throws ParseException {
		
		
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

				
				if (Objects.nonNull(filterRequestDTO.getFilters().get(Constant.PRIORITY))
						&& !filterRequestDTO.getFilters().get(Constant.PRIORITY).toString().trim().isEmpty()) {

					String priority = String.valueOf(filterRequestDTO.getFilters().get(Constant.PRIORITY).toString());
					list.add(cb.like(cb.upper(from.get(Constant.PRIORITY)), "%" + priority.toUpperCase() + "%"));

				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get(Constant.ID))
						&& !filterRequestDTO.getFilters().get(Constant.ID).toString().trim().isEmpty()) {
					Long id = Long.valueOf(filterRequestDTO.getFilters().get(Constant.ID).toString());
					list.add(cb.equal(from.get(Constant.ID), id));
				}
				
				if (Objects.nonNull(filterRequestDTO.getFilters().get(Constant.CATEGORYID))
						&& !filterRequestDTO.getFilters().get(Constant.CATEGORYID).toString().trim().isEmpty()) {

					Long categoryId = Long.valueOf(filterRequestDTO.getFilters().get(Constant.CATEGORYID).toString());
					list.add(cb.equal(from.get(Constant.CATEGORY),categoryId));
				}
				
				if (Objects.nonNull(filterRequestDTO.getFilters().get(Constant.STATUS))
						&& !filterRequestDTO.getFilters().get(Constant.STATUS).toString().trim().isEmpty()) {

					Boolean status = Boolean.valueOf(filterRequestDTO.getFilters().get(Constant.STATUS).toString());
					list.add(cb.equal(from.get(Constant.STATUS),status));
				}
				

			}
		} catch (ParseException e) {
			throw new InvalidParameterException("No Record Found");
		}
	}
	
	
	
	
	
	
	
	
}
