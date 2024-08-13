package com.oasys.helpdesk.service;

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
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.PaginationResponseDTO;
import com.oasys.helpdesk.dto.SiteIssueTypeDTO;
import com.oasys.helpdesk.dto.SiteIssueTypeResponseDTO;
import com.oasys.helpdesk.dto.SiteObservationDTO;
import com.oasys.helpdesk.dto.SiteVisitResponseDTO;
import com.oasys.helpdesk.entity.SiteIssueTypeEntity;
import com.oasys.helpdesk.entity.SiteObservationEntity;
import com.oasys.helpdesk.entity.SiteVisitEntity;
import com.oasys.helpdesk.mapper.SiteIssueTypeMapper;
import com.oasys.helpdesk.repository.SiteIssueTypeRepository;
import com.oasys.helpdesk.repository.SiteObservationRepository;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;

@Service

public class SiteIssueTypeServiceImpl  implements SiteIssueTypeService{
	
	@Autowired
	SiteIssueTypeRepository siteissuetyperepository;

	
	@Autowired
	private CommonDataController commonDataController;

	@Autowired
	private CommonUtil commonUtil;
	
	 @Autowired
	private EntityManager entityManager;
	 
	 @Autowired
	 private SiteIssueTypeMapper siteissuetypemapper;
	
	 
	 public GenericResponse add(SiteIssueTypeDTO requestDTO) {
			Optional<SiteIssueTypeEntity> entity = siteissuetyperepository
					.findByissuetypeIgnoreCase(requestDTO.getIssuetype());
			if (entity.isPresent()) {
				return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
						ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { "Site Issue Type " }));
			}
			requestDTO.setId(null);
			SiteIssueTypeEntity site = commonUtil.modalMap(requestDTO, SiteIssueTypeEntity.class);
			siteissuetyperepository.save(site);
			return Library.getSuccessfulResponse(site, ErrorCode.CREATED.getErrorCode(), ErrorMessages.RECORED_CREATED);
		}
		
	 @Override
		public GenericResponse getById(Long id) {
			Optional<SiteIssueTypeEntity> entitydetail = siteissuetyperepository.findById(id);
			List<SiteIssueTypeDTO> Entitylist = new ArrayList<SiteIssueTypeDTO>();
			try {
				SiteIssueTypeDTO entity = new SiteIssueTypeDTO();
				entity.setId(entitydetail.get().getId());
				entity.setActive(entitydetail.get().isActive());
				entity.setCreatedBy(entitydetail.get().getCreatedBy());
				entity.setModifiedBy(entitydetail.get().getModifiedBy());
				entity.setIssuetype(entitydetail.get().getIssuetype());
				Long userid = (long) entitydetail.get().getCreatedBy();
				String createdByUserName = commonDataController.getUserNameById(userid);
				String modifiedByUserName = commonDataController.getUserNameById(userid);
				entity.setCreatedByName(createdByUserName);
				entity.setModifiedByName(modifiedByUserName);
				entity.setCreatedDate(entitydetail.get().getCreatedDate().toString());
				entity.setModifiedDate(entitydetail.get().getModifiedDate().toString());
				Entitylist.add(entity);

			} catch (Exception e) {
				e.printStackTrace();
			}

			if (!entitydetail.isPresent()) {
				return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
						ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
			}
			return Library.getSuccessfulResponse(Entitylist, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
	}
	 @Override
		public GenericResponse getAll() {
			List<SiteIssueTypeEntity> EntityDetailsList = siteissuetyperepository.findAll();
			List<SiteIssueTypeDTO> Entitylist = new ArrayList<SiteIssueTypeDTO>();
			try {
				EntityDetailsList.stream().forEach(action -> {
					SiteIssueTypeDTO entity = new SiteIssueTypeDTO();
					entity.setId(action.getId());
					entity.setActive(action.isActive());
					entity.setCreatedBy(action.getCreatedBy());
					entity.setModifiedBy(action.getModifiedBy());
					entity.setIssuetype(action.getIssuetype());
					Long userid = (long) action.getCreatedBy();
					String createdByUserName = commonDataController.getUserNameById(userid);
					String modifiedByUserName = commonDataController.getUserNameById(userid);
					entity.setCreatedByName(createdByUserName);
					entity.setModifiedByName(modifiedByUserName);
					entity.setCreatedDate(action.getCreatedDate().toString());
					entity.setModifiedDate(action.getModifiedDate().toString());
					Entitylist.add(entity);
				});
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (CollectionUtils.isEmpty(Entitylist)) {
				return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
						ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
			}

			return Library.getSuccessfulResponse(Entitylist, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}
	 
	 @Override
		public GenericResponse updateSite(SiteIssueTypeDTO requestDTO) {
			if (Objects.isNull(requestDTO.getId())) {
				return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
						ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { "SITEISSUETYPEID" }));
			}

			Optional<SiteIssueTypeEntity> entityOptional = siteissuetyperepository.findById(requestDTO.getId());
			if (!entityOptional.isPresent()) {
				return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "SITEISSUETYPEID" }));
			}

			SiteIssueTypeEntity entity = entityOptional.get();
			Optional<SiteIssueTypeEntity> existingEntityOptional = siteissuetyperepository
					.findByissuetypeIgnoreCaseAndIdNot(requestDTO.getIssuetype(), entity.getId());

			if (existingEntityOptional.isPresent()) {
				return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
						ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { "SITEISSUETYPE" }));

			}
			entity.setIssuetype(requestDTO.getIssuetype());
			entity.setActive(requestDTO.isActive());
			siteissuetyperepository.save(entity);
			return Library.getSuccessfulResponse(entity, ErrorCode.CREATED.getErrorCode(), ErrorMessages.RECORED_UPDATED);
		}
	   @Override
		public GenericResponse getAllActive() {
			List<SiteIssueTypeEntity> List = siteissuetyperepository
					.findAllByIsActiveOrderByModifiedDateDesc(Boolean.TRUE);
			if (CollectionUtils.isEmpty(List)) {
				return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
						ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
			}

			return Library.getSuccessfulResponse(List, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}
	   @Override
		public GenericResponse getAlllist() {
			List<SiteIssueTypeEntity> DepList = siteissuetyperepository.findAllByOrderByModifiedDateDesc();
			if (CollectionUtils.isEmpty(DepList)) {
				return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
						ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
			}
			List<SiteIssueTypeResponseDTO> depResponseList = DepList.stream()
					.map(siteissuetypemapper::entityToResponseDTO).collect(Collectors.toList());
			return Library.getSuccessfulResponse(depResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}
	
	 		public GenericResponse getAllByRequestFilter(PaginationRequestDTO requestData) throws ParseException {
			PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
			Long count = this.getCountBySearchFields(requestData);
			//log.info("total count :: {}", count);
			if (count > 0) {
				List<SiteIssueTypeEntity> list = this.getRecordsByFilterDTO(requestData);
				if (CollectionUtils.isEmpty(list)) {
					throw new RecordNotFoundException("No Record Found");
				}
				//Long listcount = (long) list.size();
				List<SiteIssueTypeResponseDTO> dtoList = list.stream().map(siteissuetypemapper::entityToResponseDTO)
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

		public List<SiteIssueTypeEntity> getRecordsByFilterDTO(PaginationRequestDTO filterRequestDTO) throws ParseException {
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			CriteriaQuery<SiteIssueTypeEntity> cq = cb.createQuery(SiteIssueTypeEntity.class);
			Root<SiteIssueTypeEntity> from = cq.from(SiteIssueTypeEntity.class);
			List<Predicate> list = new ArrayList<>();
			TypedQuery<SiteIssueTypeEntity> typedQuery = null;
			addCriteria(cb, list, filterRequestDTO, from);
			cq.where(cb.and(list.toArray(new Predicate[list.size()])));
			cq.distinct(true);
			if (StringUtils.isBlank(filterRequestDTO.getSortField())) {
				filterRequestDTO.setSortField("MODIFIED_DATE");// double quotes instead of constant
			}
			if (StringUtils.isNotBlank(filterRequestDTO.getSortOrder())
					&& filterRequestDTO.getSortOrder().equalsIgnoreCase("ASC")) {
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

			List<SiteIssueTypeEntity> data = typedQuery.getResultList();
			if (CollectionUtils.isEmpty(data)) {
				throw new RecordNotFoundException("No Record Found");
			}
			return data;
		}

		private Long getCountBySearchFields(PaginationRequestDTO filterRequestDTO) throws ParseException {

			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<SiteIssueTypeEntity> from = cq.from(SiteIssueTypeEntity.class);
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
				Root<SiteIssueTypeEntity> from) throws ParseException {
			SiteIssueTypeEntity div = new SiteIssueTypeEntity();
			try {
				if (Objects.nonNull(filterRequestDTO.getFilters())) {

					if (Objects.nonNull(filterRequestDTO.getFilters().get("FROM_DATE"))
							&& !filterRequestDTO.getFilters().get("TO_DATE").toString().trim().isEmpty()) {

						Date fromDate = new SimpleDateFormat("DATE_FORMAT")
								.parse(filterRequestDTO.getFilters().get("FROM_DATE").toString() + " " + "START_TIME");
						Date toDate = new SimpleDateFormat("DATE_FORMAT")
								.parse(filterRequestDTO.getFilters().get("TO_DATE").toString() + " " + "END_TIME");
						list.add(cb.between(from.get("CREATED_DATE"), fromDate, toDate));

					}		

					if (Objects.nonNull(filterRequestDTO.getFilters().get("issuetype"))
							&& !filterRequestDTO.getFilters().get("issuetype").toString().trim().isEmpty()) {

						String issuetype = String.valueOf(filterRequestDTO.getFilters().get("issuetype").toString());
						list.add(cb.equal(from.get("issuetype"), issuetype));
					}
					if (Objects.nonNull(filterRequestDTO.getFilters().get("is_active"))
							&& !filterRequestDTO.getFilters().get("is_active").toString().trim().isEmpty()) {

							Long status = Long.valueOf(filterRequestDTO.getFilters().get("is_active").toString());
							list.add(cb.equal(from.get("isActive"), status));
								}


				}
			} catch (ParseException e) {
				throw new InvalidParameterException("Invalid filter value passed!");
			}
		}
		
	}


