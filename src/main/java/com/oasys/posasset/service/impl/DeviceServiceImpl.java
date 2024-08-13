package com.oasys.posasset.service.impl;

import static com.oasys.helpdesk.constant.Constant.ASC;

import static com.oasys.helpdesk.constant.Constant.CREATED_DATE;
import static com.oasys.helpdesk.constant.Constant.DATE_FORMAT;
import static com.oasys.helpdesk.constant.Constant.DESC;
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
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
import com.oasys.helpdesk.entity.AssetAccessoriesEntity;
import com.oasys.helpdesk.entity.CreateTicketEntity;
import com.oasys.helpdesk.entity.DeviceMasterEntity;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.repository.AssetAccessoriesRepository;
import com.oasys.helpdesk.repository.DevicemasterRepository;
import com.oasys.helpdesk.response.CreateTicketResponseDto;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.posasset.dto.DeviceposDTO;
import com.oasys.posasset.entity.DeviceEntity;
import com.oasys.posasset.entity.DeviceLogEntity;
import com.oasys.posasset.entity.DeviceRegisterEntity;
import com.oasys.posasset.mapper.DeviceMapper;
import com.oasys.posasset.repository.DeviceRegistrationRepository;
import com.oasys.posasset.repository.DeviceRepository;
import com.oasys.posasset.repository.DevicelogRepository;
import com.oasys.posasset.request.DeviceRequestDTO;
import com.oasys.posasset.response.DeviceResponseDTO;
import com.oasys.posasset.service.DeviceService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class DeviceServiceImpl implements DeviceService {
	@Autowired
	DeviceRepository deviceRepository;

	@Autowired
	DevicelogRepository devicelogRepository;

	@Autowired
	DeviceMapper deviceMapper;

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	AssetAccessoriesRepository assetaccessoriesrepository;

	@Autowired
	DeviceRegistrationRepository deviceregrepository;

	@Autowired
	DevicemasterRepository devicemasterrepository;

	@Autowired
	private PaginationMapper paginationMapper;

	@Override
	public GenericResponse getAll() {
		List<DeviceEntity> list = deviceRepository.findAllByOrderByModifiedDateDesc();

		if (CollectionUtils.isEmpty(list)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<DeviceResponseDTO> responseDto = list.stream().map(deviceMapper::convertEntityToResponseDTO)
				.collect(Collectors.toList());
		return Library.getSuccessfulResponse(responseDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public Boolean isDeviceAlreadyAssociated(String deviceNumber) {
		if (StringUtils.isBlank(deviceNumber)) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { deviceNumber }));
		}
		Optional<DeviceEntity> deviceEntity = deviceRepository.findByDeviceNumber(deviceNumber);
		if (!deviceEntity.isPresent()) {
			throw new InvalidDataValidation(ResponseMessageConstant.DEVICE_DETAILS_MISSING.getMessage());
		}
		if (Boolean.TRUE.equals(deviceEntity.get().isAssociated())) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	@Override
	@Transactional
	public GenericResponse addDevice(DeviceRequestDTO deviceReqDTO) {

		Optional<DeviceEntity> device = deviceRepository.findByDeviceNumber(deviceReqDTO.getDeviceNumber());

		if (device.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { "DeviceNumber" }));
		}
		DeviceEntity deviceEntity = commonUtil.modalMap(deviceReqDTO, DeviceEntity.class);
		deviceEntity.setAssociated(Boolean.FALSE);
		deviceEntity.setActive(deviceReqDTO.isActive());
		AssetAccessoriesEntity asset = assetaccessoriesrepository.getById(deviceReqDTO.getPrinterId());
		deviceEntity.setPrinterId(asset);
		deviceEntity.setAssetType(deviceReqDTO.getAssetType());

		deviceEntity.setAssetTypeId(deviceReqDTO.getAssetTypeId());
		deviceEntity.setAssetSubtypeId(deviceReqDTO.getAssetSubtypeId());;
		deviceEntity.setAssetNameId(deviceReqDTO.getAssetNameId());
		deviceEntity.setAssetBrandId(deviceReqDTO.getAssetBrandId());
		deviceEntity.setSupplierNameId(deviceReqDTO.getSupplierNameId());
		deviceEntity.setWarrantyPeriod(deviceReqDTO.getWarrantyPeriod());
		deviceEntity.setRating(deviceReqDTO.getRating());

		deviceRepository.save(deviceEntity);
		try {
			devicelog(deviceReqDTO);
		} catch (Exception e) {
			log.info("......Device log entry Failed.....");
		}
		return Library.getSuccessfulResponse(deviceEntity, ErrorCode.CREATED.getErrorCode(),
				ErrorMessages.RECORED_CREATED);
	}

	public void devicelog(DeviceRequestDTO deviceReqDTO) {
		DeviceLogEntity devicelogEntity = commonUtil.modalMap(deviceReqDTO, DeviceLogEntity.class);
		devicelogEntity.setDeviceId(deviceReqDTO.getDeviceNumber());
		devicelogEntity.setRemarks("Device Added Successfully");
		devicelogEntity.setShopCode(deviceReqDTO.getShopCode());
		// devicelogEntity.setStatus(Boolean.TRUE.toString());
		devicelogEntity.setStatus(String.valueOf(deviceReqDTO.isActive()));
		devicelogRepository.save(devicelogEntity);
	}

	@Override
	public GenericResponse getById(Long id) {
		Optional<DeviceEntity> deviceEntity = deviceRepository.findById(id);
		if (!deviceEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(deviceMapper.convertEntityToResponseDTO(deviceEntity.get()),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse update(DeviceRequestDTO deviceReqDTO) {
		DeviceEntity deviceEntity = null;
		DeviceRegisterEntity deviceregentity = new DeviceRegisterEntity();

		// Optional<DeviceRegisterEntity> deviceregEntity =
		// deviceregrepository.findByDeviceNumber(deviceReqDTO.getPreviousDeviceNumber());
		Optional<DeviceRegisterEntity> deviceregEntity = deviceregrepository
				.getByDeviceNumber(deviceReqDTO.getPreviousDeviceNumber());

		if (!deviceregEntity.isPresent()) {
			// Optional<DeviceEntity> device =
			// deviceRepository.findByDeviceNumberr(deviceReqDTO.getDeviceNumber());
			// if(device.isPresent()) {
			deviceEntity = deviceMapper.updateRequestDTOToEntity(deviceReqDTO);
			deviceRepository.save(deviceEntity);
			// }
			// else {
			// throw new InvalidDataValidation("Device is Inactive Status");
			// }

			try {
				devicelog(deviceReqDTO);
			} catch (Exception e) {
				log.info("......Device log Update Failed.....");
			}
			return Library.getSuccessfulResponse(deviceEntity, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_UPDATED);
		} else {
			throw new InvalidDataValidation("Device is Approved Status");
		}

	}

	@Override
	public GenericResponse getAllByRequestFilter(PaginationRequestDTO requestData, AuthenticationDTO authenticationDTO)
			throws ParseException {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		Long count = this.getCountBySearchFields(requestData, authenticationDTO);
		log.info("total count :: {}", count);
		if (count > 0) {
			List<DeviceEntity> list = this.getRecordsByFilterDTO(requestData, authenticationDTO);
			if (CollectionUtils.isEmpty(list)) {
				throw new RecordNotFoundException("No Record Found");
			}
			List<DeviceResponseDTO> dtoList = list.stream().map(deviceMapper::convertEntityToResponseDTO)
					.collect(Collectors.toList());
			Long dtolistcount = (long) dtoList.size();
			paginationResponseDTO.setContents(dtoList);
			paginationResponseDTO.setNumberOfElements(Objects.nonNull(dtolistcount) ? dtolistcount.intValue() : null);
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
		Root<DeviceEntity> from = cq.from(DeviceEntity.class);
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

	public List<DeviceEntity> getRecordsByFilterDTO(PaginationRequestDTO filterRequestDTO,
			AuthenticationDTO authenticationDTO) throws ParseException {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<DeviceEntity> cq = cb.createQuery(DeviceEntity.class);
		Root<DeviceEntity> from = cq.from(DeviceEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<DeviceEntity> typedQuery = null;
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

		List<DeviceEntity> data = typedQuery.getResultList();
		if (CollectionUtils.isEmpty(data)) {
			throw new RecordNotFoundException("No Record Found");
		}
		return data;
	}

	private void addCriteria(CriteriaBuilder cb, List<Predicate> list, PaginationRequestDTO filterRequestDTO,
			Root<DeviceEntity> from, AuthenticationDTO authenticationDTO) throws ParseException {
		// list.add(cb.equal(from.get(CREATED_BY), authenticationDTO.getUserId()));
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

				if (Objects.nonNull(filterRequestDTO.getFilters().get("deviceNumber"))
						&& !filterRequestDTO.getFilters().get("deviceNumber").toString().trim().isEmpty()) {

					String serialnumber = String.valueOf(filterRequestDTO.getFilters().get("deviceNumber").toString());
					list.add(cb.equal(from.get("deviceNumber"), serialnumber));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("simId"))
						&& !filterRequestDTO.getFilters().get("simId").toString().trim().isEmpty()) {

					String imis = String.valueOf(filterRequestDTO.getFilters().get("simId").toString());
					list.add(cb.equal(from.get("simId"), imis));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("simId2"))
						&& !filterRequestDTO.getFilters().get("simId2").toString().trim().isEmpty()) {

					String imis = String.valueOf(filterRequestDTO.getFilters().get("simId2").toString());
					list.add(cb.equal(from.get("simId2"), imis));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("fpsCode"))
						&& !filterRequestDTO.getFilters().get("fpsCode").toString().trim().isEmpty()) {

					String fpscode = String.valueOf(filterRequestDTO.getFilters().get("fpsCode").toString());
					list.add(cb.equal(from.get("fpsCode"), fpscode));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("status"))
						&& !filterRequestDTO.getFilters().get("status").toString().trim().isEmpty()) {

					Long active = Long.valueOf(filterRequestDTO.getFilters().get("status").toString());
					list.add(cb.equal(from.get("active"), active));
				}

			}
		} catch (ParseException e) {
			throw new InvalidParameterException("No Record Found");
		}
	}

	@Override
	public GenericResponse getShopCodeByDeviceNumber(String deviceNumber) {
		Optional<DeviceEntity> device = deviceRepository.findByDeviceNumber(deviceNumber);
		DeviceposDTO devicelist = new DeviceposDTO();
		if (!device.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { Constant.DEVICE_NUMBER }));
		}
		if (StringUtils.isBlank(device.get().getFpsCode())) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(), ErrorMessages.NO_RECORD_FOUND);
		}
		devicelist.setDeviceNumber(device.get().getDeviceNumber());
		devicelist.setFPSCode(device.get().getFpsCode());
		return Library.getSuccessfulResponse(devicelist, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse geverifyByDeviceNumber(String deviceNumber) {
		Optional<DeviceMasterEntity> device = devicemasterrepository.findByDeviceNumber(deviceNumber);
		DeviceposDTO devicelist = new DeviceposDTO();
		if (!device.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(), "Device Number Not Available");
		}

		Optional<DeviceRegisterEntity> devicereg = null;
		devicereg = deviceregrepository.findByDeviceNumber(deviceNumber);
		if (devicereg.isPresent()) {
			devicelist.setDeviceNumber(device.get().getDeviceNumber());
			devicelist.setDeviceName(devicereg.get().getDeviceName());
		}

		return Library.getSuccessfulResponse(devicelist, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getShopCode(String fpscode) {
		List<DeviceEntity> device = deviceRepository.findByFpsCode(fpscode);
		if (Objects.isNull(device) || device.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(), ErrorMessages.NO_RECORD_FOUND);
		}
		return Library.getSuccessfulResponse(device, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	public GenericResponse getAllByRequestFiltermapunmap(PaginationRequestDTO paginationDto,
			AuthenticationDTO authenticationDTO) throws ParseException {

		Pageable pageable = null;
		Page<DeviceEntity> list = null;
		String devicenumber = null;
		String fpsCode = null;
		Date fromDate = null;
		Date toDate = null;
		String type = null;
		if (StringUtils.isBlank(paginationDto.getSortField())) {
			// paginationDto.setSortField(MODIFIED_DATE);
			paginationDto.setSortField("modified_date");
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
			if (Objects.nonNull(paginationDto.getFilters().get("fromDate"))
					&& !paginationDto.getFilters().get("fromDate").toString().trim().isEmpty())
				try {
					fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.parse(paginationDto.getFilters().get("fromDate") + " " + "00:00:00");
				} catch (ParseException e) {
					log.error("error occurred while parsing date : {}", e.getMessage());
					throw new InvalidDataValidation("Invalid date parameter passed");
				}
		}
		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("toDate"))
					&& !paginationDto.getFilters().get("toDate").toString().trim().isEmpty())

				try {
					toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.parse(paginationDto.getFilters().get("toDate") + " " + "23:59:59");
				} catch (ParseException e) {
					log.error("error occurred while parsing date : {}", e.getMessage());
					throw new InvalidDataValidation("Invalid date parameter passed");
				}
		}

		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("deviceNumber"))
					&& !paginationDto.getFilters().get("deviceNumber").toString().trim().isEmpty()) {
				try {
					devicenumber = String.valueOf(paginationDto.getFilters().get("deviceNumber").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing devicenumber :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}

		}

		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("fpsCode"))
					&& !paginationDto.getFilters().get("fpsCode").toString().trim().isEmpty()) {
				try {
					fpsCode = String.valueOf(paginationDto.getFilters().get("fpsCode").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing fpsCode :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}

		}

		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("type"))
					&& !paginationDto.getFilters().get("type").toString().trim().isEmpty()) {
				try {
					type = String.valueOf(paginationDto.getFilters().get("type").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing fpsCode :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}

		}

		if (Objects.nonNull(devicenumber) && Objects.nonNull(fpsCode) && Objects.isNull(type)) {
			list = deviceRepository.getByDeviceNumberAndFpsCode(devicenumber, fpsCode, pageable);
		}

		if (Objects.nonNull(devicenumber) && Objects.isNull(fpsCode) && Objects.isNull(type)) {
			list = deviceRepository.getByDeviceNumber(devicenumber, pageable);
		}

		if (Objects.isNull(devicenumber) && Objects.nonNull(fpsCode) && Objects.isNull(type)) {
			list = deviceRepository.getByFpsCode(fpsCode, pageable);
		}

		if (Objects.isNull(devicenumber) && Objects.isNull(fpsCode) && Objects.nonNull(type)) {
			if (type.equalsIgnoreCase("Mapped")) {

				list = deviceRepository.getByMapped(pageable);
			}

			if (type.equalsIgnoreCase("UnMapped")) {

				list = deviceRepository.getByUnMapped(pageable);
			}

		}

		if (Objects.isNull(devicenumber) && Objects.nonNull(fpsCode) && Objects.nonNull(type)) {

			if (type.equalsIgnoreCase("More than one device")) {
				Integer shopcount = deviceRepository.getCountByShopcode(fpsCode);
				if (shopcount > 1) {
					list = deviceRepository.getByMorethanonedevice(fpsCode, pageable);
				}
			}

		}

		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}

		Page<DeviceResponseDTO> dtoList = list.map(deviceMapper::convertEntityToResponseDTO);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(dtoList),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getAllByRequestFilterfps(PaginationRequestDTO requestData,
			AuthenticationDTO authenticationDTO) throws ParseException {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		Long count = this.getCountBySearchFieldsfps(requestData, authenticationDTO);
		log.info("total count :: {}", count);
		if (count > 0) {
			List<DeviceEntity> list = this.getRecordsByFilterDTOfps(requestData, authenticationDTO);
			if (CollectionUtils.isEmpty(list)) {
				throw new RecordNotFoundException("No Record Found");
			}

			List<DeviceResponseDTO> dtoList = list.stream().map(deviceMapper::convertEntityToResponseDTO)
					.collect(Collectors.toList());
			Long dtolistcount = (long) dtoList.size();
			paginationResponseDTO.setContents(dtoList);
			paginationResponseDTO.setNumberOfElements(Objects.nonNull(dtolistcount) ? dtolistcount.intValue() : null);
			paginationResponseDTO.setTotalElements(count);
			return Library.getSuccessfulResponse(paginationResponseDTO, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException("No Record Found");
		}
	}

	public List<DeviceEntity> getRecordsByFilterDTOfps(PaginationRequestDTO filterRequestDTO,
			AuthenticationDTO authenticationDTO) throws ParseException {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<DeviceEntity> cq = cb.createQuery(DeviceEntity.class);
		Root<DeviceEntity> from = cq.from(DeviceEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<DeviceEntity> typedQuery = null;
		addCriteriafps(cb, list, filterRequestDTO, from, authenticationDTO);
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

		List<DeviceEntity> data = typedQuery.getResultList();
		if (CollectionUtils.isEmpty(data)) {
			throw new RecordNotFoundException("No Record Found");
		}
		return data;
	}

	private Long getCountBySearchFieldsfps(PaginationRequestDTO filterRequestDTO, AuthenticationDTO authenticationDTO)
			throws ParseException {
		log.info("getting total count by search fields :: {}", filterRequestDTO.toString());
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<DeviceEntity> from = cq.from(DeviceEntity.class);
		cq.select(cb.count(from));
		List<Predicate> list = new ArrayList<>();
		addCriteriafps(cb, list, filterRequestDTO, from, authenticationDTO);
		cq.where(cb.and(list.toArray(new Predicate[list.size()])));
		Long count = entityManager.createQuery(cq).getSingleResult();
		if (count > 0) {
			return count;
		} else {
			throw new RecordNotFoundException("No Record Found");
		}
	}

	private void addCriteriafps(CriteriaBuilder cb, List<Predicate> list, PaginationRequestDTO filterRequestDTO,
			Root<DeviceEntity> from, AuthenticationDTO authenticationDTO) throws ParseException {
		// list.add(cb.equal(from.get(CREATED_BY), authenticationDTO.getUserId()));
		DeviceEntity device = new DeviceEntity();

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

				if (Objects.nonNull(filterRequestDTO.getFilters().get("deviceNumber"))
						&& !filterRequestDTO.getFilters().get("deviceNumber").toString().trim().isEmpty()) {

					String serialnumber = String.valueOf(filterRequestDTO.getFilters().get("deviceNumber").toString());
					list.add(cb.equal(from.get("deviceNumber"), serialnumber));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("simId"))
						&& !filterRequestDTO.getFilters().get("simId").toString().trim().isEmpty()) {

					String imis = String.valueOf(filterRequestDTO.getFilters().get("simId").toString());
					list.add(cb.equal(from.get("simId"), imis));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("simId2"))
						&& !filterRequestDTO.getFilters().get("simId2").toString().trim().isEmpty()) {

					String imis = String.valueOf(filterRequestDTO.getFilters().get("simId2").toString());
					list.add(cb.equal(from.get("simId2"), imis));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("status"))
						&& !filterRequestDTO.getFilters().get("status").toString().trim().isEmpty()) {

					Long active = Long.valueOf(filterRequestDTO.getFilters().get("status").toString());
					list.add(cb.equal(from.get("active"), active));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("fpsCode"))
						&& !filterRequestDTO.getFilters().get("fpsCode").toString().trim().isEmpty()) {

					List<String> fpscode = (List<String>) (filterRequestDTO.getFilters().get("fpsCode"));

					boolean ans = fpscode.isEmpty();
					if (ans == true) {
						System.out.println("The ArrayList is empty");
					} else {
						System.out.println("The ArrayList is not empty");
						Expression<String> mainModuleIds = from.get("fpsCode");

						list.add(mainModuleIds.in(fpscode));
					}

				}

			}
		} catch (ParseException e) {
			throw new InvalidParameterException("No Record Found");
		}
	}

}
