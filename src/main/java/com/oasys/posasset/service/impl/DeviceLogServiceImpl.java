package com.oasys.posasset.service.impl;


import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.CREATED_BY;
import static com.oasys.helpdesk.constant.Constant.CREATED_DATE;
import static com.oasys.helpdesk.constant.Constant.DATE_FORMAT;
import static com.oasys.helpdesk.constant.Constant.END_TIME;
import static com.oasys.helpdesk.constant.Constant.ID;
import static com.oasys.helpdesk.constant.Constant.MODIFIED_DATE;
import static com.oasys.helpdesk.constant.Constant.START_TIME;

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

import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.PaginationResponseDTO;

import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.posasset.entity.DeviceEntity;
import com.oasys.posasset.entity.DeviceLogEntity;
import com.oasys.posasset.entity.DevicestatusEntity;
import com.oasys.posasset.mapper.DeviceLogMapper;
import com.oasys.posasset.mapper.DeviceMapper;
import com.oasys.posasset.mapper.DeviceStatusMapper;
import com.oasys.posasset.repository.DeviceRepository;
import com.oasys.posasset.repository.DeviceStatusRepository;
import com.oasys.posasset.repository.DevicelogRepository;
import com.oasys.posasset.request.DeviceRequestDTO;
import com.oasys.posasset.response.DeviceLogResponseDTO;
import com.oasys.posasset.response.DeviceResponseDTO;
import com.oasys.posasset.response.DevicestatusResponseDTO;
import com.oasys.posasset.service.DeviceLogService;
import com.oasys.posasset.service.DeviceService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class DeviceLogServiceImpl implements DeviceLogService {
	
	@Autowired
	DeviceLogMapper devicelogMapper;
	
	@Autowired
	DeviceStatusMapper devicestatusmapper;
	

	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	DeviceStatusRepository devicestatusrepository;
	
	

	@Override
	public GenericResponse getAllByRequestFilter(PaginationRequestDTO requestData, AuthenticationDTO authenticationDTO)
			throws ParseException {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		Long count = this.getCountBySearchFields(requestData, authenticationDTO);
		log.info("total count :: {}", count);
		if (count > 0) {
			List<DeviceLogEntity> list = this.getRecordsByFilterDTO(requestData, authenticationDTO);
			if (CollectionUtils.isEmpty(list)) {
				throw new RecordNotFoundException("No Record Found");
			}
			List<DeviceLogResponseDTO> dtoList = list.stream().map(devicelogMapper::convertEntityToResponseDTO)
					.collect(Collectors.toList());

			paginationResponseDTO.setContents(dtoList);
			paginationResponseDTO.setNumberOfElements(Objects.nonNull(count) ? count.intValue() : null);
			paginationResponseDTO.setTotalElements(count);
			return Library.getSuccessfulResponse(paginationResponseDTO, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException("No Record Found");
		}
	}

	
	private Long getCountBySearchFields(PaginationRequestDTO filterRequestDTO, AuthenticationDTO authenticationDTO)
			throws ParseException {
		log.info("getting total count by search fields :: {}", filterRequestDTO.toString());
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<DeviceLogEntity> from = cq.from(DeviceLogEntity.class);
		cq.select(cb.count(from));
		List<Predicate> list = new ArrayList<>();
		addCriteria(cb, list, filterRequestDTO, from, authenticationDTO);
		cq.where(cb.and(list.toArray(new Predicate[list.size()])));
		Long count = entityManager.createQuery(cq).getSingleResult();
		if (count > 0) {
			return count;
		} else {
			throw new RecordNotFoundException("No Record Found");
		}
	}

	
	public List<DeviceLogEntity> getRecordsByFilterDTO(PaginationRequestDTO filterRequestDTO,
			AuthenticationDTO authenticationDTO) throws ParseException {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<DeviceLogEntity> cq = cb.createQuery(DeviceLogEntity.class);
		Root<DeviceLogEntity> from = cq.from(DeviceLogEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<DeviceLogEntity> typedQuery = null;
		addCriteria(cb, list, filterRequestDTO, from, authenticationDTO);
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

		List<DeviceLogEntity> data = typedQuery.getResultList();
		if (CollectionUtils.isEmpty(data)) {
			throw new RecordNotFoundException("No Record Found");
		}
		return data;
	}
	
	
	
	private void addCriteria(CriteriaBuilder cb, List<Predicate> list, PaginationRequestDTO filterRequestDTO,
			Root<DeviceLogEntity> from, AuthenticationDTO authenticationDTO) throws ParseException {
		if(authenticationDTO.getUserName().equalsIgnoreCase("admin")) {	
		}
		else{
			list.add(cb.equal(from.get(CREATED_BY), authenticationDTO.getUserId()));	
		}
		
		try {
			if (Objects.nonNull(filterRequestDTO.getFilters())) {
				log.info("filters ::" + filterRequestDTO.getFilters());

				if (Objects.nonNull(filterRequestDTO.getFilters().get(CREATED_DATE))
						&& !filterRequestDTO.getFilters().get(CREATED_DATE).toString().trim().isEmpty()) {

					Date fromDate = new SimpleDateFormat(DATE_FORMAT)
							.parse(filterRequestDTO.getFilters().get(CREATED_DATE).toString() + " " + START_TIME);
					Date toDate = new SimpleDateFormat(DATE_FORMAT)
							.parse(filterRequestDTO.getFilters().get(CREATED_DATE).toString() + " " + END_TIME);
					list.add(cb.between(from.get(CREATED_DATE), fromDate, toDate));

				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get(ID))
						&& !filterRequestDTO.getFilters().get(ID).toString().trim().isEmpty()) {

					Long id = Long.valueOf(filterRequestDTO.getFilters().get(ID).toString());
					list.add(cb.equal(from.get(ID), id));
				}

				
				if (Objects.nonNull(filterRequestDTO.getFilters().get("status"))
						&& !filterRequestDTO.getFilters().get("status").toString().trim().isEmpty()) {

					String status = String.valueOf(filterRequestDTO.getFilters().get("status").toString());
					list.add(cb.equal(from.get("status"), status));
				}
				
				if (Objects.nonNull(filterRequestDTO.getFilters().get("deviceId"))
						&& !filterRequestDTO.getFilters().get("deviceId").toString().trim().isEmpty()) {

					String deviceid = String.valueOf(filterRequestDTO.getFilters().get("deviceId").toString());
					list.add(cb.equal(from.get("deviceId"), deviceid));
				}
				
				if (Objects.nonNull(filterRequestDTO.getFilters().get("shopCode"))
						&& !filterRequestDTO.getFilters().get("shopCode").toString().trim().isEmpty()) {

					String shopcode = String.valueOf(filterRequestDTO.getFilters().get("shopCode").toString());
					list.add(cb.equal(from.get("shopCode"), shopcode));
				}

			}
		} catch (ParseException e) {
			throw new InvalidParameterException("No Record Found");
		}
	}
	
	
	
	
	@Override
	public GenericResponse getAll() {
		List<DevicestatusEntity> list = devicestatusrepository.findAll();

		if (CollectionUtils.isEmpty(list)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<DevicestatusResponseDTO> responseDto = list.stream().map(devicestatusmapper::convertEntityToResponseDTO)
				.collect(Collectors.toList());
		return Library.getSuccessfulResponse(responseDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	
}
