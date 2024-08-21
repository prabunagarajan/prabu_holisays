package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.CREATED_DATE;
import static com.oasys.helpdesk.constant.Constant.DATE_FORMAT;
import static com.oasys.helpdesk.constant.Constant.END_TIME;
import static com.oasys.helpdesk.constant.Constant.MODIFIED_DATE;
import static com.oasys.helpdesk.constant.Constant.START_TIME;
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
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.DistrictResponseDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.PaginationResponseDTO;
import com.oasys.helpdesk.dto.SiteObservationDTO;
import com.oasys.helpdesk.dto.SiteVisitDTO;
import com.oasys.helpdesk.dto.SiteVisitResponseDTO;
import com.oasys.helpdesk.dto.SiteVisitUserResponseDTO;
import com.oasys.helpdesk.entity.Accessories;
import com.oasys.helpdesk.entity.AssetBrandEntity;
import com.oasys.helpdesk.entity.AssetListEntity;
import com.oasys.helpdesk.entity.AssetTypeEntity;
import com.oasys.helpdesk.entity.DeviceHardwareEntity;
import com.oasys.helpdesk.entity.DistrictEntity;
import com.oasys.helpdesk.entity.EntityDetails;
import com.oasys.helpdesk.entity.POSAssetRequestEntity;
import com.oasys.helpdesk.entity.SiteActionTakenEntity;
import com.oasys.helpdesk.entity.SiteIssueTypeEntity;
import com.oasys.helpdesk.entity.SiteObservationEntity;
import com.oasys.helpdesk.entity.SiteVisitEntity;
import com.oasys.helpdesk.entity.SiteVisitStatusEntity;
import com.oasys.helpdesk.entity.SupplierEntity;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.mapper.SiteVisitMapper;
import com.oasys.helpdesk.repository.EntityDetailsRepository;
import com.oasys.helpdesk.repository.SiteActionTakenRepository;
import com.oasys.helpdesk.repository.SiteIssueTypeRepository;
import com.oasys.helpdesk.repository.SiteObservationRepository;
import com.oasys.helpdesk.repository.SiteVisitRepository;
import com.oasys.helpdesk.repository.SiteVisitStatusRepository;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.POSAssetRequestStatus;

import lombok.extern.log4j.Log4j2;
@Log4j2
@Service
public class SiteVisitServiceImpl  implements SiteVisitService{

	
	@Autowired
	SiteVisitRepository sitevisitrepository;
	
	@Autowired
	EntityDetailsRepository entityDetailsRepository;
	
	@Autowired
	SiteObservationRepository siteObservationRepository;
	
	@Autowired
	SiteActionTakenRepository siteActionTakenRepository;
	
	@Autowired
	SiteVisitStatusRepository siteVisitStatusRepository;
	
	@Autowired
	SiteIssueTypeRepository siteIssueTypeRepository;
	
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private CommonDataController commonDataController;
	
	@Autowired
	private PaginationMapper paginationMapper;

	@Autowired
	private SiteVisitMapper sitevisitmapper;
	
	@Autowired
	private EntityManager entityManager;
	
	
	public GenericResponse add(SiteVisitDTO requestDTO) {
		
		SiteVisitEntity entity = commonUtil.modalMap(requestDTO, SiteVisitEntity.class);
		
		Optional<EntityDetails> assetTypeEntity = entityDetailsRepository.findById(requestDTO.getEntityTypeId());
		if (!assetTypeEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Entity type" }));
		}

		Optional<SiteObservationEntity> assetnameEntity = siteObservationRepository.findById(requestDTO.getSiteobservationId());
		if (!assetnameEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Site observation" }));
		}

		Optional<SiteActionTakenEntity> assetbrandentity = siteActionTakenRepository.findById(requestDTO.getSiteactionTakenId());
		if (!assetbrandentity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Site actiontaken" }));
		}

		Optional<SiteVisitStatusEntity> supplierentity = siteVisitStatusRepository.findById(requestDTO.getSiteVisitstausId());

		if (!supplierentity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Site visitstatus" }));
		}
		Optional<SiteIssueTypeEntity> siteissueentity = siteIssueTypeRepository.findById(requestDTO.getSiteIssueTypeId());

		if (!siteissueentity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Site Issuetype" }));
		}
		String ticketNumber = requestDTO.getTicketNumber().trim();

		Optional<SiteVisitEntity> assetlistOptional = null;

		if (Objects.nonNull(requestDTO.getTicketNumber()) && ticketNumber.isEmpty()) {
			
			entity.setEntityType(assetTypeEntity.get());
			entity.setSiteObservation(assetnameEntity.get());
			entity.setSiteActionTaken(assetbrandentity.get());
			entity.setSiteVisitStatus(supplierentity.get());
			entity.setSiteIssueType(siteissueentity.get());
			entity.setShopCode(requestDTO.getShopCode());
			entity.setEntityName(requestDTO.getEntityName());
			entity.setLicenseName(requestDTO.getLicenseName());
			entity.setLicenseType(requestDTO.getLicenseType());
			entity.setAddress(requestDTO.getAddress());
			entity.setSalespersonName(requestDTO.getSalespersonName());
			entity.setContactNo(requestDTO.getContactNo());
			entity.setPendingReason(requestDTO.getPendingReason());
			entity.setLatitude(requestDTO.getLatitude());
			entity.setLongitude(requestDTO.getLongitude());
			entity.setDistrict(requestDTO.getDistrict());
			entity.setImage1(requestDTO.getImage1());
			entity.setImage2(requestDTO.getImage2());
			entity.setUuid1(requestDTO.getUuid1());
			entity.setUuid2(requestDTO.getUuid2());
			entity.setActive(requestDTO.isActive());
			entity.setTicketNumber(requestDTO.getTicketNumber());
			entity.setFinalStatus(requestDTO.getFinalStatus());
			entity = sitevisitrepository.save(entity);

		} else {

			assetlistOptional = sitevisitrepository.getByTicketNumber(ticketNumber);

			if (assetlistOptional.isPresent()) {
				throw new InvalidDataValidation("Record Already Exist");
			} else {
				entity.setEntityType(assetTypeEntity.get());
				entity.setSiteObservation(assetnameEntity.get());
				entity.setSiteActionTaken(assetbrandentity.get());
				entity.setSiteVisitStatus(supplierentity.get());
				entity.setSiteIssueType(siteissueentity.get());
				entity.setShopCode(requestDTO.getShopCode());
				entity.setEntityName(requestDTO.getEntityName());
				entity.setLicenseName(requestDTO.getLicenseName());
				entity.setLicenseType(requestDTO.getLicenseType());
				entity.setAddress(requestDTO.getAddress());
				entity.setSalespersonName(requestDTO.getSalespersonName());
				entity.setContactNo(requestDTO.getContactNo());
				entity.setPendingReason(requestDTO.getPendingReason());
				entity.setLatitude(requestDTO.getLatitude());
				entity.setLongitude(requestDTO.getLongitude());
				entity.setDistrict(requestDTO.getDistrict());
				entity.setImage1(requestDTO.getImage1());
				entity.setImage2(requestDTO.getImage2());
				entity.setUuid1(requestDTO.getUuid1());
				entity.setUuid2(requestDTO.getUuid2());
				entity.setActive(requestDTO.isActive());
				entity.setTicketNumber(requestDTO.getTicketNumber());
				entity.setFinalStatus(requestDTO.getFinalStatus());
				entity = sitevisitrepository.save(entity);

			}
		}
		return Library.getSuccessfulResponse("", ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_CREATED);
	}
	

	
	@Override
	public GenericResponse getById(Long id) {
		Optional<SiteVisitEntity> depTypeEntity = sitevisitrepository.findById(id);
		if (!depTypeEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(sitevisitmapper.entityToResponseDTO(depTypeEntity.get()), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	
	@Override
	public GenericResponse updateSite(SiteVisitDTO requestDTO) {
		if (Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { "SITEVISITID" }));
		}

		Optional<SiteVisitEntity> entityOptional = sitevisitrepository.findById(requestDTO.getId());
		if (!entityOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "SITEVISITID" }));
		}

	
		SiteVisitEntity entity = entityOptional.get();
		Optional<SiteVisitEntity> existingEntityOptional = sitevisitrepository
				.findByshopCodeIgnoreCaseAndIdNot(requestDTO.getShopCode(), entity.getId());

		if (existingEntityOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { "SHOPCODE" }));

		}
		Optional<EntityDetails> assetTypeEntity = entityDetailsRepository.findById(requestDTO.getEntityTypeId());
		if (!assetTypeEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Entity type" }));
		}

		Optional<SiteObservationEntity> assetnameEntity = siteObservationRepository.findById(requestDTO.getSiteobservationId());
		if (!assetnameEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Site observation" }));
		}

		Optional<SiteActionTakenEntity> assetbrandentity = siteActionTakenRepository.findById(requestDTO.getSiteactionTakenId());
		if (!assetbrandentity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Site actiontaken" }));
		}

		Optional<SiteVisitStatusEntity> supplierentity = siteVisitStatusRepository.findById(requestDTO.getSiteVisitstausId());

		if (!supplierentity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Site visitstatus" }));
		}
		Optional<SiteIssueTypeEntity> siteissueentity = siteIssueTypeRepository.findById(requestDTO.getSiteIssueTypeId());

		if (!siteissueentity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Site Issuetype" }));
		}

	
		entity.setActive(requestDTO.isActive());
		entity.setEntityType(assetTypeEntity.get());
		entity.setSiteObservation(assetnameEntity.get());
		entity.setSiteActionTaken(assetbrandentity.get());
		entity.setSiteVisitStatus(supplierentity.get());
		entity.setSiteIssueType(siteissueentity.get());
		entity.setShopCode(requestDTO.getShopCode());
		entity.setEntityName(requestDTO.getEntityName());
		//entity.setDeviceId(requestDTO.getDeviceId());
		entity.setLicenseName(requestDTO.getLicenseName());
		entity.setLicenseType(requestDTO.getLicenseType());
		entity.setAddress(requestDTO.getAddress());
		entity.setSalespersonName(requestDTO.getSalespersonName());
		entity.setContactNo(requestDTO.getContactNo());
		entity.setPendingReason(requestDTO.getPendingReason());
		entity.setLatitude(requestDTO.getLatitude());
		entity.setLongitude(requestDTO.getLatitude());
		entity.setDistrict(requestDTO.getDistrict());
		entity.setImage1(requestDTO.getImage1());
		entity.setImage2(requestDTO.getImage2());
		entity.setUuid1(requestDTO.getUuid1());
		entity.setUuid2(requestDTO.getUuid2());
		entity.setTicketNumber(requestDTO.getTicketNumber());
		entity.setFinalStatus(requestDTO.getFinalStatus());
		sitevisitrepository.save(entity);
		return Library.getSuccessfulResponse(entity, ErrorCode.CREATED.getErrorCode(), ErrorMessages.RECORED_UPDATED);
	}
	

	public GenericResponse getAllByRequestFilter(PaginationRequestDTO requestData) throws ParseException {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		Long count = this.getCountBySearchFields(requestData);
		log.info("total count :: {}", count);
		if (count > 0) {
			List<SiteVisitEntity> list = this.getRecordsByFilterDTO(requestData);
			if (CollectionUtils.isEmpty(list)) {
				throw new RecordNotFoundException("No Record Found");
			}
			List<SiteVisitResponseDTO> dtoList = list.stream().map(sitevisitmapper::entityToResponseDTO)
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
	
	public List<SiteVisitEntity> getRecordsByFilterDTO(PaginationRequestDTO filterRequestDTO) throws ParseException {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<SiteVisitEntity> cq = cb.createQuery(SiteVisitEntity.class);
		Root<SiteVisitEntity> from = cq.from(SiteVisitEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<SiteVisitEntity> typedQuery = null;
		addCriteria(cb, list, filterRequestDTO, from);
		cq.where(cb.and(list.toArray(new Predicate[list.size()])));
		cq.distinct(true);
		if (StringUtils.isBlank(filterRequestDTO.getSortField())) {
			filterRequestDTO.setSortField("MODIFIED_DATE");
		}
		if (StringUtils.isNotBlank(filterRequestDTO.getSortOrder())
				&& filterRequestDTO.getSortOrder().equalsIgnoreCase("ASC")) {
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

		List<SiteVisitEntity> data = typedQuery.getResultList();
		if (CollectionUtils.isEmpty(data)) {
			throw new RecordNotFoundException("No Record Found");
		}
		return data;
	}
	
	private Long getCountBySearchFields(PaginationRequestDTO filterRequestDTO) throws ParseException {
		log.info("getting total count by search fields :: {}", filterRequestDTO.toString());
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<SiteVisitEntity> from = cq.from(SiteVisitEntity.class);
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
			Root<SiteVisitEntity> from) throws ParseException {

		try {
			if (Objects.nonNull(filterRequestDTO.getFilters())) {
				log.info("filters ::" + filterRequestDTO.getFilters());

				if (Objects.nonNull(filterRequestDTO.getFilters().get("FROM_DATE"))
						&& !filterRequestDTO.getFilters().get("TO_DATE").toString().trim().isEmpty()) {

					Date fromDate = new SimpleDateFormat(DATE_FORMAT)
							.parse(filterRequestDTO.getFilters().get("FROM_DATE").toString() + " " + START_TIME);
					Date toDate = new SimpleDateFormat(DATE_FORMAT)
							.parse(filterRequestDTO.getFilters().get("TO_DATE").toString() + " " + END_TIME);
					list.add(cb.between(from.get(CREATED_DATE), fromDate, toDate));

				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("is_active"))
						&& !filterRequestDTO.getFilters().get("is_active").toString().trim().isEmpty()) {

					Long status = Long.valueOf(filterRequestDTO.getFilters().get("is_active").toString());
					list.add(cb.equal(from.get("isActive"), status));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("entity_type"))
						&& !filterRequestDTO.getFilters().get("entity_type").toString().trim().isEmpty()) {

					String entityType = String.valueOf(filterRequestDTO.getFilters().get("entity_type").toString());
					list.add(cb.equal(from.get("entityType"), entityType));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("shop_code"))
						&& !filterRequestDTO.getFilters().get("shop_code").toString().trim().isEmpty()) {

					String shopcode = String.valueOf(filterRequestDTO.getFilters().get("shop_code").toString());
					list.add(cb.equal(from.get("shopCode"), shopcode));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("entity_name"))
						&& !filterRequestDTO.getFilters().get("entity_name").toString().trim().isEmpty()) {

					String entityName = String.valueOf(filterRequestDTO.getFilters().get("entity_name").toString());
					list.add(cb.equal(from.get("entityName"), entityName));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("device_id"))
						&& !filterRequestDTO.getFilters().get("device_id").toString().trim().isEmpty()) {

					String deviceid = String.valueOf(filterRequestDTO.getFilters().get("device_id").toString());
					list.add(cb.equal(from.get("deviceId"), deviceid));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("license_name"))
						&& !filterRequestDTO.getFilters().get("license_name").toString().trim().isEmpty()) {

					String licenseName = String.valueOf(filterRequestDTO.getFilters().get("license_name").toString());
					list.add(cb.equal(from.get("licenseName"), licenseName));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("license_type"))
						&& !filterRequestDTO.getFilters().get("license_type").toString().trim().isEmpty()) {

					String licensetype = String.valueOf(filterRequestDTO.getFilters().get("license_type").toString());
					list.add(cb.equal(from.get("licenseType"), licensetype));
				}
				if (Objects.nonNull(filterRequestDTO.getFilters().get("address"))
						&& !filterRequestDTO.getFilters().get("address").toString().trim().isEmpty()) {

					String address = String.valueOf(filterRequestDTO.getFilters().get("address").toString());
					list.add(cb.equal(from.get("Address"), address));
				}
				if (Objects.nonNull(filterRequestDTO.getFilters().get("salesperson_name"))
						&& !filterRequestDTO.getFilters().get("salesperson_name").toString().trim().isEmpty()) {

					String salespersonname = String
							.valueOf(filterRequestDTO.getFilters().get("salesperson_name").toString());
					list.add(cb.equal(from.get("salespersonName"), salespersonname));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("contact_no"))
						&& !filterRequestDTO.getFilters().get("contact_no").toString().trim().isEmpty()) {

					String entityno = String.valueOf(filterRequestDTO.getFilters().get("contact_no").toString());
					list.add(cb.equal(from.get("contactNo"), entityno));
				}
				if (Objects.nonNull(filterRequestDTO.getFilters().get("observation_site"))
						&& !filterRequestDTO.getFilters().get("observation_site").toString().trim().isEmpty()) {

					String observationsite = String
							.valueOf(filterRequestDTO.getFilters().get("observation_site").toString());
					list.add(cb.equal(from.get("observationSite"), observationsite));
				}
				if (Objects.nonNull(filterRequestDTO.getFilters().get("action_taken"))
						&& !filterRequestDTO.getFilters().get("action_taken").toString().trim().isEmpty()) {

					String actiontaken = String.valueOf(filterRequestDTO.getFilters().get("action_taken").toString());
					list.add(cb.equal(from.get("actionTaken"), actiontaken));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("final_status"))
						&& !filterRequestDTO.getFilters().get("final_status").toString().trim().isEmpty()) {

					String finalstatus = String.valueOf(filterRequestDTO.getFilters().get("final_status").toString());
					list.add(cb.equal(from.get("finalStatus"), finalstatus));
				}
				if (Objects.nonNull(filterRequestDTO.getFilters().get("pending_reason"))
						&& !filterRequestDTO.getFilters().get("pending_reason").toString().trim().isEmpty()) {

					String pendingreason = String
							.valueOf(filterRequestDTO.getFilters().get("pending_reason").toString());
					list.add(cb.equal(from.get("pendingReason"), pendingreason));
				}
				if (Objects.nonNull(filterRequestDTO.getFilters().get("latitude"))
						&& !filterRequestDTO.getFilters().get("latitude").toString().trim().isEmpty()) {

					String latitude = String.valueOf(filterRequestDTO.getFilters().get("latitude").toString());
					list.add(cb.equal(from.get("Latitude"), latitude));
				}
				if (Objects.nonNull(filterRequestDTO.getFilters().get("longitude"))
						&& !filterRequestDTO.getFilters().get("longitude").toString().trim().isEmpty()) {

					String longitude = String.valueOf(filterRequestDTO.getFilters().get("longitude").toString());
					list.add(cb.equal(from.get("Longitude"), longitude));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("image1"))
						&& !filterRequestDTO.getFilters().get("image1").toString().trim().isEmpty()) {

					String image = String.valueOf(filterRequestDTO.getFilters().get("image1").toString());
					list.add(cb.equal(from.get("Image1"), image));
				}
				if (Objects.nonNull(filterRequestDTO.getFilters().get("image2"))
						&& !filterRequestDTO.getFilters().get("image2").toString().trim().isEmpty()) {

					String image1 = String.valueOf(filterRequestDTO.getFilters().get("image2").toString());
					list.add(cb.equal(from.get("Image2"), image1));
				}
				if (Objects.nonNull(filterRequestDTO.getFilters().get("uuid1"))
						&& !filterRequestDTO.getFilters().get("uuid1").toString().trim().isEmpty()) {

					String uuid1 = String.valueOf(filterRequestDTO.getFilters().get("uuid1").toString());
					list.add(cb.equal(from.get("Uuid1"), uuid1));
				}
				if (Objects.nonNull(filterRequestDTO.getFilters().get("uuid2"))
						&& !filterRequestDTO.getFilters().get("uuid2").toString().trim().isEmpty()) {

					String uuid2 = String.valueOf(filterRequestDTO.getFilters().get("uuid2").toString());
					list.add(cb.equal(from.get("Uuid2"), uuid2));
				}
				if (Objects.nonNull(filterRequestDTO.getFilters().get("entity_type_id"))
						&& !filterRequestDTO.getFilters().get("entity_type_id").toString().trim().isEmpty()) {

					String entity = String.valueOf(filterRequestDTO.getFilters().get("entity_type_id").toString());
					list.add(cb.equal(from.get("entityTypeId"), entity));
				}
				if (Objects.nonNull(filterRequestDTO.getFilters().get("site_obesrvation_id"))
						&& !filterRequestDTO.getFilters().get("site_obesrvation_id").toString().trim().isEmpty()) {

					String entitysite = String
							.valueOf(filterRequestDTO.getFilters().get("site_obesrvation_id").toString());
					list.add(cb.equal(from.get("siteobservationId"), entitysite));
				}
				if (Objects.nonNull(filterRequestDTO.getFilters().get("siteaction_taken_id"))
						&& !filterRequestDTO.getFilters().get("siteaction_taken_id").toString().trim().isEmpty()) {

					String entitysiteobs = String
							.valueOf(filterRequestDTO.getFilters().get("siteaction_taken_id").toString());
					list.add(cb.equal(from.get("siteactionTakenId"), entitysiteobs));
				}
				if (Objects.nonNull(filterRequestDTO.getFilters().get("site_visitstaus_id"))
						&& !filterRequestDTO.getFilters().get("site_visitstaus_id").toString().trim().isEmpty()) {

					String entitysitevisit = String
							.valueOf(filterRequestDTO.getFilters().get("site_visitstaus_id").toString());
					list.add(cb.equal(from.get("siteVisitstausId"), entitysitevisit));
				}
				if (Objects.nonNull(filterRequestDTO.getFilters().get("site_issuetype_id"))
						&& !filterRequestDTO.getFilters().get("site_issuetype_id").toString().trim().isEmpty()) {

					String entitysiteissue = String
							.valueOf(filterRequestDTO.getFilters().get("site_issuetype_id").toString());
					list.add(cb.equal(from.get("siteIssueTypeId"), entitysiteissue));
				}
				if (Objects.nonNull(filterRequestDTO.getFilters().get("district"))
						&& !filterRequestDTO.getFilters().get("district").toString().trim().isEmpty()) {

					String district = String
							.valueOf(filterRequestDTO.getFilters().get("district").toString());
					list.add(cb.equal(from.get("District"), district));
				}
				if (Objects.nonNull(filterRequestDTO.getFilters().get("ticket_number"))
						&& !filterRequestDTO.getFilters().get("ticket_number").toString().trim().isEmpty()) {

					String ticketnumber = String
							.valueOf(filterRequestDTO.getFilters().get("ticket_number").toString());
					list.add(cb.equal(from.get("ticketNumber"), ticketnumber));
				}
				if (Objects.nonNull(filterRequestDTO.getFilters().get("final_status"))
						&& !filterRequestDTO.getFilters().get("final_status").toString().trim().isEmpty()) {

					String finalstatus = String
							.valueOf(filterRequestDTO.getFilters().get("final_status").toString());
					list.add(cb.equal(from.get("finalStatus"), finalstatus));
				}
			}
		} catch (ParseException e) {
			throw new InvalidParameterException("No Record Found");
		}
	}
	
	

	@Override
	public GenericResponse getAll() {
		List<SiteVisitEntity> DepList = sitevisitrepository.findAllByOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(DepList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<SiteVisitResponseDTO> depResponseList = DepList.stream().map(sitevisitmapper::entityToResponseDTO)
				.collect(Collectors.toList());
		return Library.getSuccessfulResponse(depResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}



	public GenericResponse getsitevisit(SiteVisitDTO requestDto) {
		final Date fromDate;
		final Date toDate;

		try {
			fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(requestDto.getFromDate() + " " + "00:00:00");
		} catch (ParseException e) {
			log.error("error occurred while parsing date : {}", e.getMessage());
			throw new InvalidDataValidation("Invalid date parameter passed");
		}
		try {
			toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(requestDto.getToDate() + " " + "23:59:59");
		} catch (ParseException e) {
			log.error("error occurred while parsing date : {}", e.getMessage());
			throw new InvalidDataValidation("Invalid date parameter passed");
		}

		Long userid = requestDto.getUserId();

		List<SiteVisitEntity> sitevisitList = sitevisitrepository.getCountByStatusAndCreatedDateandToDate(fromDate,
				toDate, userid);

		if (CollectionUtils.isEmpty(sitevisitList)) {
			throw new RecordNotFoundException("No records found");
		}
		List<SiteVisitUserResponseDTO> sitevisit = sitevisitList.stream().map(sitevisitmapper::entityToUserResponseDTO)
				.collect(Collectors.toList());

		return Library.getSuccessfulResponse(sitevisit, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

}

