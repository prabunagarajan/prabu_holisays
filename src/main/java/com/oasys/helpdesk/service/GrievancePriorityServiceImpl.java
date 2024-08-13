package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.CODE;
import static com.oasys.helpdesk.constant.Constant.DESC;
import static com.oasys.helpdesk.constant.Constant.GRIEVANCE_CATEGORY_PRIORITY;
import static com.oasys.helpdesk.constant.Constant.ID;
import static com.oasys.helpdesk.constant.Constant.MODIFIED_DATE;
import static com.oasys.helpdesk.constant.Constant.STATUS;

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
import com.oasys.helpdesk.entity.GrievanceCategoryEntity;
import com.oasys.helpdesk.entity.GrievancePriorityEntity;
import com.oasys.helpdesk.mapper.GrievancePriorityMapper;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.repository.GrievanceCategoryRepository;
import com.oasys.helpdesk.repository.GrievancePriorityRepository;
import com.oasys.helpdesk.request.GrievancePriorityRequestDTO;
import com.oasys.helpdesk.response.GrievancePriorityResponseDTO;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.RandomUtil;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class GrievancePriorityServiceImpl implements GrievancePriorityService{

	@Autowired
	private GrievancePriorityRepository grievancePriorityRepository;
	
	
	@Autowired
	private GrievanceCategoryRepository grievanceCategoryRepository;
	
	@Autowired
	private GrievancePriorityMapper grievancePriorityMapper;
	
	@Autowired
	private PaginationMapper paginationMapper;
	
	public static final String GRIEVANCE_CATEGORY = "categoryName";
	
	@Autowired
	private EntityManager entityManager;
	
	@Override
	public GenericResponse createPriority(GrievancePriorityRequestDTO priorityRequestDTO)
	{
		Optional<GrievancePriorityEntity> priorityOptional = null;
				
		priorityOptional =	grievancePriorityRepository.findByCodeIgnoreCase(priorityRequestDTO.getCode());
		if (priorityOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { CODE }));
		}
		
		List<GrievancePriorityEntity> existingEntityList  = grievancePriorityRepository.getPriorityByCategoryId(priorityRequestDTO.getCategoryId());
		if(!CollectionUtils.isEmpty(existingEntityList))
		{
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ErrorMessages.PRIORITY_ALREADY_EXIST);
		}
		
//		priorityOptional = grievancePriorityRepository.getById(priorityRequestDTO.getId());
//		if(priorityOptional.isPresent())
//		{
//			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
//					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { CATEGORY_ID }));
//		}
		priorityRequestDTO.setId(null);
		GrievancePriorityEntity grievancepriority = new GrievancePriorityEntity();

		if(priorityRequestDTO.getCategoryId() !=null) {
			Optional<GrievanceCategoryEntity> grievancecategory =grievanceCategoryRepository.findById(priorityRequestDTO.getCategoryId());
			if (!grievancecategory.isPresent()) {
				return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "GRIEVANCE_CATEGORY_ID" }));
			}
		
			grievancepriority.setCategory(grievancecategory.get());
		}
		grievancepriority.setPriority(priorityRequestDTO.getPriority());
		grievancepriority.setCode(priorityRequestDTO.getCode());
		grievancepriority.setStatus(priorityRequestDTO.getStatus());
		grievancepriority.setTypeofUser(priorityRequestDTO.getTypeofUser());
		
		grievancePriorityRepository.save(grievancepriority);

		return Library.getSuccessfulResponse(grievancepriority, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_CREATED);
	} 
	
	@Override
	public GenericResponse updatePriority(GrievancePriorityRequestDTO requestDTO){
		if (Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		List<GrievancePriorityEntity> grievancePriorityList = grievancePriorityRepository
				.getPriorityByCategoryIdNotInId(requestDTO.getCategoryId(), requestDTO.getId());
		if (!CollectionUtils.isEmpty(grievancePriorityList)) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ErrorMessages.PRIORITY_ALREADY_EXIST);
		}
		Optional<GrievancePriorityEntity> grievancePriorityOptional = grievancePriorityRepository
				.findById(requestDTO.getId());
		if (!grievancePriorityOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
			GrievancePriorityEntity grievancePriority = grievancePriorityOptional.get();
			grievancePriority.setPriority(requestDTO.getPriority());
			
			
			Optional<GrievanceCategoryEntity> grievancecategory =grievanceCategoryRepository.findById(requestDTO.getCategoryId());
			if (!grievancecategory.isPresent()) {
				return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "GRIEVANCE_CATEGORY_ID" }));
			}
			grievancePriority.setCategory(grievancecategory.get());
			grievancePriority.setStatus(requestDTO.getStatus());
			grievancePriority.setTypeofUser(requestDTO.getTypeofUser());
			grievancePriorityRepository.save(grievancePriority);
			return Library.getSuccessfulResponse(grievancePriority, ErrorCode.CREATED.getErrorCode(),
					ErrorMessages.RECORED_UPDATED);
	}

	
	@Override
	public GenericResponse getById(Long Id) {
		Optional<GrievancePriorityEntity> GrievancepriorityEntity = grievancePriorityRepository.findById(Id);
		if (!GrievancepriorityEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(grievancePriorityMapper.convertEntityToResponseDTO(GrievancepriorityEntity.get()),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}
	
	
	@Override
	public GenericResponse getAll() {
		List<GrievancePriorityEntity> grievancePriority = grievancePriorityRepository.findAllByOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(grievancePriority)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<GrievancePriorityResponseDTO> ResponseData = grievancePriority.stream()
				.map(grievancePriorityMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(ResponseData, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	@Override
	public GenericResponse getAllActive() {
		List<GrievancePriorityEntity> configList = grievancePriorityRepository.findAllByStatusOrderByModifiedDateDesc(Boolean.TRUE);
		if (CollectionUtils.isEmpty(configList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<GrievancePriorityResponseDTO> ResponseData = configList.stream()
				.map(grievancePriorityMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(ResponseData, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	
	@Override
	public GenericResponse getCode() {
		MenuPrefix prefix = MenuPrefix.getType(GRIEVANCE_CATEGORY_PRIORITY);
		String code = prefix.toString() + RandomUtil.getRandomNumber();
		while (true) {
			Optional<GrievancePriorityEntity> grievancePriority = grievancePriorityRepository.findByCodeIgnoreCase(code);
			if (grievancePriority.isPresent()) {
				code = prefix.toString() + RandomUtil.getRandomNumber();
			} else {
				break;
			}
		}
		return Library.getSuccessfulResponse(code, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	
	
	@Override
	public GenericResponse searchByPriority(PaginationRequestDTO paginationDto){
		Pageable pageable = null;
		Page<GrievancePriorityEntity> list = null;
		Long categoryId = null;
		String priority = null;
		Boolean status = null;

		if (StringUtils.isBlank(paginationDto.getSortField())) {
			paginationDto.setSortField(MODIFIED_DATE);
		}
		if (StringUtils.isBlank(paginationDto.getSortOrder())) {
			paginationDto.setSortOrder(DESC);
		}
		if (paginationDto.getSortOrder().equalsIgnoreCase(ASC)) {
			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
					Sort.by(Direction.ASC, paginationDto.getSortField()));
		} else {
			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
					Sort.by(Direction.DESC, paginationDto.getSortField()));
		}
		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("categoryId"))
					&& !paginationDto.getFilters().get("categoryId").toString().trim().isEmpty()) {
				try {
					// id = Long.valueOf(paginationDto.getFilters().get(ID).toString());
					categoryId = Long.valueOf(paginationDto.getFilters().get("categoryId").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing categoryName :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}
			if (Objects.nonNull(paginationDto.getFilters().get("priority"))
					&& !paginationDto.getFilters().get("priority").toString().trim().isEmpty()) {
				try {
					priority = String.valueOf(paginationDto.getFilters().get("priority").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing priority :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}
			if (Objects.nonNull(paginationDto.getFilters().get(STATUS))
					&& !paginationDto.getFilters().get(STATUS).toString().trim().isEmpty()) {
				try {
					status = Boolean.valueOf(paginationDto.getFilters().get(STATUS).toString());
				} catch (Exception e) {
					log.error("error occurred while parsing status :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}

		}

//		GrievanceCategoryEntity categoryNameO = new GrievanceCategoryEntity();
//		categoryNameO.setId(categoryId);

		if (Objects.nonNull(categoryId) && Objects.nonNull(priority) && Objects.nonNull(status)) {
			list = grievancePriorityRepository.getByCategoryNameOPriorityAndStatus(categoryId, priority,status, pageable);
		}
		else if (Objects.nonNull(categoryId) && Objects.nonNull(priority) && Objects.isNull(status)) {
			list = grievancePriorityRepository.getByCategoryNameOAndPriority(categoryId, priority, pageable);
		}
		else if (Objects.isNull(categoryId) && Objects.nonNull(priority) && Objects.isNull(status)) {
			list = grievancePriorityRepository.getByPriority(priority, pageable);
		}
		else if (Objects.isNull(categoryId) && Objects.isNull(priority) && Objects.nonNull(status)) {
			list = grievancePriorityRepository.getByStatus(status, pageable);
		}
		else if (Objects.nonNull(categoryId) && Objects.isNull(priority)) {
			list = grievancePriorityRepository.getByCategoryNameO(categoryId, pageable);
		}


		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		Page<GrievancePriorityResponseDTO> finalResponse = list.map(grievancePriorityMapper::convertEntityToResponseDTO);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}
	
	@Override
	public GenericResponse getByCategory(Long category){

		List<GrievancePriorityEntity> grievancecategory = grievancePriorityRepository.findAllByCategory(category);
		if (CollectionUtils.isEmpty(grievancecategory)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<GrievancePriorityResponseDTO> ResponseList = grievancecategory.stream()
				.map(grievancePriorityMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(ResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}
	
	@Override
	public GenericResponse getAllByRequestFilter(PaginationRequestDTO requestData) throws ParseException {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		Long count = this.getCountBySearchFields(requestData);
		log.info("total count :: {}", count);
		if (count > 0) {
			List<GrievancePriorityEntity> list = this.getRecordsByFilterDTO(requestData);
			if (CollectionUtils.isEmpty(list)) {
				throw new RecordNotFoundException("No Record Found");
			}
			List<GrievancePriorityResponseDTO> dtoList = list.stream().map(grievancePriorityMapper::convertEntityToResponseDTO)
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
	
	public List<GrievancePriorityEntity> getRecordsByFilterDTO(PaginationRequestDTO filterRequestDTO) throws ParseException {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<GrievancePriorityEntity> cq = cb.createQuery(GrievancePriorityEntity.class);
		Root<GrievancePriorityEntity> from = cq.from(GrievancePriorityEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<GrievancePriorityEntity> typedQuery = null;
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

		List<GrievancePriorityEntity> data = typedQuery.getResultList();
		if (CollectionUtils.isEmpty(data)) {
			throw new RecordNotFoundException("No Record Found");
		}
		return data;
	}
	
	private Long getCountBySearchFields(PaginationRequestDTO filterRequestDTO) throws ParseException {
		log.info("getting total count by search fields :: {}", filterRequestDTO.toString());
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<GrievancePriorityEntity> from = cq.from(GrievancePriorityEntity.class);
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
			Root<GrievancePriorityEntity> from) throws ParseException {
		
		
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
				
				if (Objects.nonNull(filterRequestDTO.getFilters().get("typeofUser"))
						&& !filterRequestDTO.getFilters().get("typeofUser").toString().trim().isEmpty()) {

					String typeofUser = String.valueOf(filterRequestDTO.getFilters().get("typeofUser").toString());
					list.add(cb.equal(from.get("typeofUser"), typeofUser));
				}
				if (Objects.nonNull(filterRequestDTO.getFilters().get("priority"))
						&& !filterRequestDTO.getFilters().get("priority").toString().trim().isEmpty()) {

					String priority = String.valueOf(filterRequestDTO.getFilters().get("priority").toString());
					list.add(cb.equal(from.get("priority"), priority));
				}
			}
		} catch (ParseException e) {
			throw new InvalidParameterException("No Record Found");
		}
	}
	
	
	
	
	
	
	
	
	
	
	
}
