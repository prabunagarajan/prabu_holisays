package com.oasys.posasset.service.impl;


import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.CATEGORY;
import static com.oasys.helpdesk.constant.Constant.CATEGORYID;
import static com.oasys.helpdesk.constant.Constant.CODE;
import static com.oasys.helpdesk.constant.Constant.CREATED_BY;
import static com.oasys.helpdesk.constant.Constant.CREATED_DATE;
import static com.oasys.helpdesk.constant.Constant.DATE_FORMAT;
import static com.oasys.helpdesk.constant.Constant.DEPARTMENT_NAME;
import static com.oasys.helpdesk.constant.Constant.END_TIME;
import static com.oasys.helpdesk.constant.Constant.ID;
import static com.oasys.helpdesk.constant.Constant.ISSUEFROM;
import static com.oasys.helpdesk.constant.Constant.ISSUE_FROM_ID;
import static com.oasys.helpdesk.constant.Constant.LICENSE_NUMBER;
import static com.oasys.helpdesk.constant.Constant.LICENSE_TYPE_ID;
import static com.oasys.helpdesk.constant.Constant.MODIFIED_DATE;
import static com.oasys.helpdesk.constant.Constant.PRIORITY;
import static com.oasys.helpdesk.constant.Constant.PRIORITYID;
import static com.oasys.helpdesk.constant.Constant.SLA;
import static com.oasys.helpdesk.constant.Constant.SLA_ID;
import static com.oasys.helpdesk.constant.Constant.START_TIME;
import static com.oasys.helpdesk.constant.Constant.STATUS;
import static com.oasys.helpdesk.constant.Constant.SUBCATEGORY;
import static com.oasys.helpdesk.constant.Constant.SUBSOL_NAME;
import static com.oasys.helpdesk.constant.Constant.SUB_CATEGORY_ID;
import static com.oasys.helpdesk.constant.Constant.TICKETSTATUS_NAME;
import static com.oasys.helpdesk.constant.Constant.TICKET_NUMBER;
import static com.oasys.helpdesk.constant.Constant.TICKET_STATUS;
import static com.oasys.helpdesk.constant.Constant.TICKET_STATUS_ID;

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

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.PaginationResponseDTO;
import com.oasys.helpdesk.dto.SubsolutionRequestDTO;
import com.oasys.helpdesk.dto.SubsolutionResponseDTO;
import com.oasys.helpdesk.dto.TicketstausRequestDTO;
import com.oasys.helpdesk.dto.TicketstausResponseDTO;
import com.oasys.helpdesk.entity.Category;
import com.oasys.helpdesk.entity.CreateTicketEntity;
import com.oasys.helpdesk.entity.IssueDetails;
import com.oasys.helpdesk.entity.SubCategory;
import com.oasys.helpdesk.entity.SubsolutionEntity;
import com.oasys.helpdesk.entity.TicketStatusEntity;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.mapper.SubsolutionMapper;
import com.oasys.helpdesk.mapper.TicketstatusMapper;
import com.oasys.helpdesk.repository.CategoryRepository;
import com.oasys.helpdesk.repository.SubCategoryRepository;
import com.oasys.helpdesk.repository.SubsolutionRepository;
import com.oasys.helpdesk.repository.TicketStatusrepository;
import com.oasys.helpdesk.response.CreateTicketResponseDto;
import com.oasys.helpdesk.response.IsssueDetresdto;
import com.oasys.helpdesk.response.PRResponseDTO;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.RandomUtil;
import com.oasys.posasset.entity.SIMEntity;
import com.oasys.posasset.entity.SIMProviderDetEntity;
import com.oasys.posasset.mapper.SIMMapper;
import com.oasys.posasset.mapper.SIMProviderMapper;
import com.oasys.posasset.repository.SIMProviderRepository;
import com.oasys.posasset.repository.SIMRepository;
import com.oasys.posasset.request.SIMProviderRequestDTO;
import com.oasys.posasset.request.SIMRequestDTO;
import com.oasys.posasset.response.SIMProviderResponseDTO;
import com.oasys.posasset.response.SIMResponseDTO;
import com.oasys.posasset.service.SIMService;
import com.oasys.posasset.service.SIMproviderService;

import lombok.extern.log4j.Log4j2;
@Service
@Log4j2
public class SIMServiceImpl implements SIMService {
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private SIMRepository simRepository;
	
	
	@Autowired
	private PaginationMapper paginationMapper;
	
	
	@Autowired
	private SIMProviderRepository simproviderRepository;
	
	
	@Autowired
	private SIMMapper simmapper;
	
	@Autowired
	private EntityManager entityManager;
	
	@Override
	public GenericResponse addsim(SIMRequestDTO requestDTO)	{

		Optional<SIMEntity> ticketOptional=simRepository.findByImisIgnoreCase(requestDTO.getImis().toUpperCase());

		if (ticketOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { "Imis" }));
		}
		
		requestDTO.setId(null);
		SIMEntity tcEntity1 = commonUtil.modalMap(requestDTO, SIMEntity.class);
		tcEntity1.setNumber(requestDTO.getNumber());
		tcEntity1.setSerialnumber(requestDTO.getSerialnumber());
		tcEntity1.setStatus(requestDTO.getStatus());
		tcEntity1.setImis(requestDTO.getImis());
		tcEntity1.setSimprovidername(requestDTO.getProviderName());
		tcEntity1.setSiname(requestDTO.getSiName());
		SIMProviderDetEntity simprovider=simproviderRepository.getById(requestDTO.getSimproviderdetId());
		tcEntity1.setSimproviderdetId(simprovider);
		tcEntity1.getSimproviderdetId().getId();
		simRepository.save(tcEntity1);
		return Library.getSuccessfulResponse(tcEntity1, ErrorCode.CREATED.getErrorCode(),ErrorMessages.RECORED_CREATED);
	}
	

	@Override
	public GenericResponse getAll() {
		List<SIMEntity> DepList = simRepository.findAllByOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(DepList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		
		List<SIMResponseDTO> depResponseList = DepList.stream()
				.map(simmapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(depResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	
	
	@Override
	public GenericResponse getById(Long id) {
		Optional<SIMEntity> depTypeEntity = simRepository.findById(id);
		if (!depTypeEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(simmapper.convertEntityToResponseDTO(depTypeEntity.get()), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	
	@Override
	public GenericResponse updatesim(SIMRequestDTO requestDTO)	{
		if(Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
	
		Optional<SIMEntity> DeptOptional = simRepository
				.findByImisIgnoreCaseNotInId(requestDTO.getImis(), requestDTO.getId());
		
		if (DeptOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { DEPARTMENT_NAME }));
		}
		DeptOptional = simRepository.findById(requestDTO.getId());
		if (!DeptOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		SIMEntity deptEntity = DeptOptional.get();
		deptEntity.setImis(requestDTO.getImis());
		deptEntity.setStatus(requestDTO.getStatus());
		simRepository.save(deptEntity);
		return Library.getSuccessfulResponse(deptEntity, ErrorCode.CREATED.getErrorCode(),ErrorMessages.RECORED_UPDATED);
	}
	
	
	@Override
	public GenericResponse getAllActive() {
		List<SIMEntity> assetTypeList = simRepository.findAllByStatusOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(assetTypeList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<SIMResponseDTO> assetTypeResponseList = assetTypeList.stream()
				.map(simmapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(assetTypeResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	
	
	@Override
	public GenericResponse getAllByRequestFilter(PaginationRequestDTO requestData, AuthenticationDTO authenticationDTO) throws ParseException {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		Long count = this.getCountBySearchFields(requestData,authenticationDTO);
		log.info("total count :: {}", count);
		if (count > 0) {
			List<SIMEntity> list = this.getRecordsByFilterDTO(requestData,authenticationDTO);
			if (CollectionUtils.isEmpty(list)) {
				throw new RecordNotFoundException("No Record Found");
			}
			List<SIMResponseDTO> dtoList = list.stream().map(simmapper::convertEntityToResponseDTO)
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
	
	
	private Long getCountBySearchFields(PaginationRequestDTO filterRequestDTO, AuthenticationDTO authenticationDTO) throws ParseException {
		log.info("getting total count by search fields :: {}", filterRequestDTO.toString());
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<SIMEntity> from = cq.from(SIMEntity.class);
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
	
	public List<SIMEntity> getRecordsByFilterDTO(PaginationRequestDTO filterRequestDTO, AuthenticationDTO authenticationDTO) throws ParseException {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<SIMEntity> cq = cb.createQuery(SIMEntity.class);
		Root<SIMEntity> from = cq.from(SIMEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<SIMEntity> typedQuery = null;
		addCriteria(cb, list, filterRequestDTO, from,authenticationDTO);
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
		if (Objects.nonNull(filterRequestDTO.getPaginationSize()) && Objects.nonNull(filterRequestDTO.getPaginationSize())) {
			typedQuery = entityManager.createQuery(cq)
					.setFirstResult((filterRequestDTO.getPageNo() * filterRequestDTO.getPaginationSize()))
					.setMaxResults(filterRequestDTO.getPaginationSize());
		} else {
			typedQuery = entityManager.createQuery(cq);
		}

		List<SIMEntity> data = typedQuery.getResultList();
		if (CollectionUtils.isEmpty(data)) {
			throw new RecordNotFoundException("No Record Found");
		}
		return data;
	}
	
	
	private void addCriteria(CriteriaBuilder cb, List<Predicate> list, PaginationRequestDTO filterRequestDTO,
			Root<SIMEntity> from, AuthenticationDTO authenticationDTO) throws ParseException {
		list.add(cb.equal(from.get(CREATED_BY), authenticationDTO.getUserId()));
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

				if (Objects.nonNull(filterRequestDTO.getFilters().get("number"))
						&& !filterRequestDTO.getFilters().get("number").toString().trim().isEmpty()) {

					String number = String.valueOf(filterRequestDTO.getFilters().get("number").toString());
					list.add(cb.equal(from.get("number"), number));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("serialnumber"))
						&& !filterRequestDTO.getFilters().get("serialnumber").toString().trim().isEmpty()) {

					String serialnumber = String.valueOf(filterRequestDTO.getFilters().get("serialnumber").toString());
					list.add(cb.equal(from.get("serialnumber"), serialnumber));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("imis"))
						&& !filterRequestDTO.getFilters().get("imis").toString().trim().isEmpty()) {

					String imis = String.valueOf(filterRequestDTO.getFilters().get("imis").toString());
					list.add(cb.equal(from.get("imis"), imis));
				}
				
				
				
				if (Objects.nonNull(filterRequestDTO.getFilters().get("simproviderdetId"))
						&& !filterRequestDTO.getFilters().get("simproviderdetId").toString().trim().isEmpty()) {
					Long issueFrom = Long.valueOf(filterRequestDTO.getFilters().get("simproviderdetId").toString());
					list.add(cb.equal(from.get("simproviderdetId"), issueFrom));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("status"))
						&& !filterRequestDTO.getFilters().get("status").toString().trim().isEmpty()) {
					Boolean issueFrom = Boolean.valueOf(filterRequestDTO.getFilters().get("status").toString());
					list.add(cb.equal(from.get("status"), issueFrom));
				}

			}
		} catch (ParseException e) {
			throw new InvalidParameterException("No Record Found");
		}
	}
	
}
