package com.oasys.posasset.service.impl;

import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.CREATED_DATE;
import static com.oasys.helpdesk.constant.Constant.DATE_FORMAT;
import static com.oasys.helpdesk.constant.Constant.END_TIME;
import static com.oasys.helpdesk.constant.Constant.MODIFIED_DATE;
import static com.oasys.helpdesk.constant.Constant.START_TIME;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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
import com.oasys.helpdesk.dto.KeyValueResponseDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.PaginationResponseDTO;
import com.oasys.helpdesk.entity.POSAssetRequestEntity;
import com.oasys.helpdesk.entity.UserEntity;
import com.oasys.helpdesk.repository.UserRepository;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.security.SecurityUtils;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.POSAssetRequestStatus;
import com.oasys.posasset.dto.POSAssetApprovalRequestDTO;
import com.oasys.posasset.dto.POSAssetRequestDTO;
import com.oasys.posasset.dto.POSAssetResponseDTO;
import com.oasys.posasset.mapper.POSAssetRequestMapper;
import com.oasys.posasset.repository.POSAssetRequestRepository;
import com.oasys.posasset.service.POSAssetRequestService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class POSAssetRequestServiceImpl implements POSAssetRequestService {
	
	@Autowired
	private POSAssetRequestRepository posAssetRequestRepository;
	
	@Autowired
	private POSAssetRequestMapper posAssetRequestMapper;
	
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional
	public GenericResponse save(POSAssetRequestDTO requestDTO) {
		POSAssetRequestEntity entity = posAssetRequestMapper.requestDTOToEntity(requestDTO);
		posAssetRequestRepository.save(entity);
		return Library.getSuccessfulResponse(posAssetRequestMapper.entityToResponseDTO(entity), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_CREATED);
	}
	
	@Override
	public GenericResponse getById(Long id) {
		Optional<POSAssetRequestEntity> entity = posAssetRequestRepository.findById(id);
		if (!entity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(posAssetRequestMapper.entityToResponseDTO(entity.get()), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	public GenericResponse getAllByRequestFilter(PaginationRequestDTO requestData) throws ParseException {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		Long count = this.getCountBySearchFields(requestData);
		log.info("total count :: {}", count);
		if (count > 0) {
			List<POSAssetRequestEntity> list = this.getRecordsByFilterDTO(requestData);
			if (CollectionUtils.isEmpty(list)) {
				throw new RecordNotFoundException("No Record Found");
			}
			List<POSAssetResponseDTO> dtoList = list.stream().map(posAssetRequestMapper::entityToResponseDTO)
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
	
	public List<POSAssetRequestEntity> getRecordsByFilterDTO(PaginationRequestDTO filterRequestDTO) throws ParseException {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<POSAssetRequestEntity> cq = cb.createQuery(POSAssetRequestEntity.class);
		Root<POSAssetRequestEntity> from = cq.from(POSAssetRequestEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<POSAssetRequestEntity> typedQuery = null;
		addCriteria(cb, list, filterRequestDTO, from);
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

		List<POSAssetRequestEntity> data = typedQuery.getResultList();
		if (CollectionUtils.isEmpty(data)) {
			throw new RecordNotFoundException("No Record Found");
		}
		return data;
	}
	
	private Long getCountBySearchFields(PaginationRequestDTO filterRequestDTO) throws ParseException {
		log.info("getting total count by search fields :: {}", filterRequestDTO.toString());
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<POSAssetRequestEntity> from = cq.from(POSAssetRequestEntity.class);
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
			Root<POSAssetRequestEntity> from) throws ParseException {
		
		try {
			if (Objects.nonNull(filterRequestDTO.getFilters())) {
				log.info("filters ::" + filterRequestDTO.getFilters());

				if (Objects.nonNull(filterRequestDTO.getFilters().get(Constant.FROM_DATE))
						&& !filterRequestDTO.getFilters().get(Constant.TO_DATE).toString().trim().isEmpty()) {

					Date fromDate = new SimpleDateFormat(DATE_FORMAT)
							.parse(filterRequestDTO.getFilters().get(Constant.FROM_DATE).toString() + " " + START_TIME);
					Date toDate = new SimpleDateFormat(DATE_FORMAT)
							.parse(filterRequestDTO.getFilters().get(Constant.TO_DATE).toString() + " " + END_TIME);
					list.add(cb.between(from.get(CREATED_DATE), fromDate, toDate));

				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get(Constant.ASSET_BRAND_ID))
						&& !filterRequestDTO.getFilters().get(Constant.ASSET_BRAND_ID).toString().trim().isEmpty()) {
					Long assetBrandId = Long.valueOf(filterRequestDTO.getFilters().get(Constant.ASSET_BRAND_ID).toString());
					list.add(cb.equal(from.get(Constant.ASSET_BRAND), assetBrandId));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get(Constant.ASSET_TYPE_ID))
						&& !filterRequestDTO.getFilters().get(Constant.ASSET_TYPE_ID).toString().trim().isEmpty()) {
					Long assetTypeId = Long.valueOf(filterRequestDTO.getFilters().get(Constant.ASSET_TYPE_ID).toString());
					list.add(cb.equal(from.get(Constant.ASSET_TYPE), assetTypeId));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get(Constant.DEVICE_HARDWARE_ID))
						&& !filterRequestDTO.getFilters().get(Constant.DEVICE_HARDWARE_ID).toString().trim().isEmpty()) {
					Long deviceHardwareId = Long.valueOf(filterRequestDTO.getFilters().get(Constant.DEVICE_HARDWARE_ID).toString());
					list.add(cb.equal(from.get(Constant.DEVICE_HARDWARE), deviceHardwareId));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get(Constant.STATUS))
						&& !filterRequestDTO.getFilters().get(Constant.STATUS).toString().trim().isEmpty()) {
					String status = String.valueOf(filterRequestDTO.getFilters().get(Constant.STATUS).toString());
					POSAssetRequestStatus statusEnum = POSAssetRequestStatus.valueOf(status);
					list.add(cb.equal(from.get(Constant.STATUS), statusEnum));
				}

			}
		} catch (ParseException e) {
			throw new InvalidParameterException("No Record Found");
		}
	}
	
	@Override
	public GenericResponse getAssetRequestTypes() {
		List<KeyValueResponseDTO> employemntStatusList = new ArrayList<>();
		for (POSAssetRequestStatus status : POSAssetRequestStatus.values()) {
			KeyValueResponseDTO response = new KeyValueResponseDTO();
			response.setKey(status);
			response.setValue(status.getType());
			employemntStatusList.add(response);
		}
		return Library.getSuccessfulResponse(employemntStatusList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	@Override
	@Transactional
	public GenericResponse approve(POSAssetApprovalRequestDTO requestDTO) {
		Optional<POSAssetRequestEntity> entityOptional  = posAssetRequestRepository.findById(requestDTO.getId());
		if(!entityOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		POSAssetRequestEntity entity = entityOptional.get();
		if(POSAssetRequestStatus.APPROVED.equals(entity.getStatus())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ErrorMessages.REQUEST_ALREADY_APPROVED);
		}
		if(!POSAssetRequestStatus.PENDING.equals(entity.getStatus())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ErrorMessages.INVALID_REQUEST_STATUS);
		}
		AuthenticationDTO authenticationDTO = SecurityUtils.findAuthenticationObject();
		if (Objects.isNull(authenticationDTO) || Objects.isNull(authenticationDTO.getUserId())) {
			log.error("authentication details missing");
			return Library.getFailResponseCode(ErrorCode.ACCESS_DENIED.getErrorCode(), ErrorMessages.ACCESS_DENIED);
		}
		Optional<UserEntity> userEntity = userRepository.findById(authenticationDTO.getUserId());
		if (!userEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ErrorMessages.INVALID_USER);
		}
		entity.setApprovedBy(userEntity.get());
		entity.setApprovalDate(LocalDateTime.now());
		entity.setStatus(POSAssetRequestStatus.APPROVED);
		entity.setRemarks(requestDTO.getRemarks());
		if (entity.getAssetDetailsList().size() != requestDTO.getAssetApprovalDetailsList().size()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ErrorMessages.INVALID_REQUEST);
		}
		requestDTO.getAssetApprovalDetailsList().forEach(assetApprovalRequestDTO->{
			entity.getAssetDetailsList().stream().filter(e ->e.getId().equals(assetApprovalRequestDTO.getId())).forEach(element-> {
					element.setApprovedAccessoriesCount(assetApprovalRequestDTO.getApprovedAccessoriesCount());
					element.setApprovedDevicesCount(assetApprovalRequestDTO.getApprovedDevicesCount());
				
			});
		});
		posAssetRequestRepository.save(entity);
		return Library.getSuccessfulResponse(posAssetRequestMapper.entityToResponseDTO(entity), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.APPROVED);
	}
	
	
}
