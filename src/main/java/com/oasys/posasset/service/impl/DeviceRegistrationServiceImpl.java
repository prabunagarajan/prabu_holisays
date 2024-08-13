package com.oasys.posasset.service.impl;

import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.CREATED_DATE;
import static com.oasys.helpdesk.constant.Constant.DATE_FORMAT;
import static com.oasys.helpdesk.constant.Constant.END_TIME;
import static com.oasys.helpdesk.constant.Constant.FROM_DATE;
import static com.oasys.helpdesk.constant.Constant.MODIFIED_DATE;
import static com.oasys.helpdesk.constant.Constant.START_TIME;
import static com.oasys.helpdesk.constant.Constant.TO_DATE;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.oasys.helpdesk.conf.exception.DeviceAlreadyExistValidation;
import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.KeyValueResponseDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.PaginationResponseDTO;

import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.DeviceRegistrationStatus;
import com.oasys.helpdesk.utility.DeviceUploadColumnNames;
import com.oasys.helpdesk.utility.FileUploadResponse;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.POIUtils;
import com.oasys.posasset.dto.DeviceRegistrationCountDto;

import com.oasys.posasset.dto.DeviceRegistrationReportResponseDTO;
import com.oasys.posasset.dto.DeviceRegistrationRequestDTO;
import com.oasys.posasset.dto.DeviceRegistrationUpdateRequestDTO;
import com.oasys.posasset.dto.DeviceregDTO;
import com.oasys.posasset.dto.EntitysummaryDTO;
import com.oasys.posasset.dto.MMDBDTO;
import com.oasys.posasset.entity.DeviceEntity;
import com.oasys.posasset.entity.DeviceLogEntity;
import com.oasys.posasset.entity.DeviceRegisterEntity;
import com.oasys.posasset.entity.DevicestatusEntity;
import com.oasys.posasset.mapper.DeviceRegistrationMapper;
import com.oasys.posasset.repository.DeviceRegistrationRepository;
import com.oasys.posasset.repository.DeviceRepository;
import com.oasys.posasset.repository.DeviceStatusRepository;
import com.oasys.posasset.repository.DevicelogRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@ComponentScan
public class DeviceRegistrationServiceImpl implements DeviceRegistrationService {

	@Autowired
	private DeviceRegistrationMapper deviceRegistrationMapper;

	@Autowired
	private DeviceRegistrationRepository deviceRegistrationRepository;

	@Autowired
	private DeviceRepository deviceRepository;

	@Autowired
	private DeviceStatusRepository devicestatusrepository;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	DevicelogRepository devicelogRepository;

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	private DeviceRegistrationCountDto deviceRegistrationCountDto;

	@Value("${spring.common.mdm}")
	private String mdmurl;

	@Override
	@Transactional
	public GenericResponse save(DeviceRegistrationRequestDTO requestDTO) {
		DeviceRegisterEntity entity = deviceRegistrationMapper.requestDTOToEntity(requestDTO);
		deviceRegistrationRepository.save(entity);
		try {
			devicelogadd(requestDTO);
		} catch (Exception e) {
			log.info("......Devicereg log entry Failed.....");
		}

		return Library.getSuccessfulResponse(entity, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_CREATED);
	}

	public void devicelogadd(DeviceRegistrationRequestDTO requestDTO) {
		DeviceLogEntity devicelogEntity = commonUtil.modalMap(requestDTO, DeviceLogEntity.class);
		devicelogEntity.setDeviceId(requestDTO.getDeviceNumber());
		devicelogEntity.setRemarks("Device Registration added Successfully");
		devicelogEntity.setShopCode(requestDTO.getFpsCode());
		devicelogEntity.setStatus("Pending");
		devicelogRepository.save(devicelogEntity);
	}

	public void devicedeassociate(DeviceRegistrationRequestDTO requestDTO, String deviceNumber) {
		DeviceLogEntity devicelogEntity = commonUtil.modalMap(requestDTO, DeviceLogEntity.class);
		devicelogEntity.setDeviceId(deviceNumber);
		devicelogEntity.setRemarks(requestDTO.getRemarks());
		devicelogEntity.setShopCode(requestDTO.getFpsCode());
		devicelogEntity.setStatus(requestDTO.getDeviceStatus().getName());
		devicelogRepository.save(devicelogEntity);
	}

	public void devicelogdeassociate(DeviceRegistrationRequestDTO requestDTO) {
		DeviceLogEntity devicelogEntity = commonUtil.modalMap(requestDTO, DeviceLogEntity.class);
		devicelogEntity.setDeviceId(requestDTO.getDeviceNumber());
		devicelogEntity.setRemarks(requestDTO.getRemarks());
		devicelogEntity.setShopCode(requestDTO.getFpsCode());
		devicelogEntity.setStatus(requestDTO.getDeviceStatus().getName());
		devicelogRepository.save(devicelogEntity);
	}

	@Override
	@Transactional
	public GenericResponse update(DeviceRegistrationUpdateRequestDTO requestDTO) {
		List<DeviceRegisterEntity> list = null;
		list = deviceRegistrationRepository.getByFpsCode(requestDTO.getFpsCode());
//		DeviceRegisterEntity deviceRegistrationEntity=null;
//		if (requestDTO.getShopType().equalsIgnoreCase("Retail")) {
//			Integer shopcount=deviceRepository.getCountByShopcode(requestDTO.getFpsCode());
//			if(shopcount>1) {
//				throw new InvalidDataValidation("Retail One shopcode Having One Device Only.");
//			}
//		}
//		else {
//		 deviceRegistrationEntity = deviceRegistrationMapper.updateRequestDTOToEntity(requestDTO);
//		deviceRegistrationRepository.save(deviceRegistrationEntity);
//		if (DeviceRegistrationStatus.APPROVED.equals(requestDTO.getDeviceStatus())) {
//			Optional<DeviceEntity> deviceEntityOptional = deviceRepository.findByDeviceNumber(requestDTO.getDeviceNumber());
//			DeviceEntity deviceEntity = deviceEntityOptional.get();
//			deviceEntity.setFpsCode(requestDTO.getFpsCode());
//			deviceEntity.setFpsName(requestDTO.getFpsName());
//			deviceEntity.setAssociated(Boolean.TRUE);
//			deviceRepository.save(deviceEntity);
//		}
//		}

		DeviceRegisterEntity deviceRegistrationEntity = null;
		deviceRegistrationEntity = deviceRegistrationMapper.updateRequestDTOToEntity(requestDTO);
		deviceRegistrationRepository.save(deviceRegistrationEntity);
		if (DeviceRegistrationStatus.APPROVED.equals(requestDTO.getDeviceStatus())) {
			Optional<DeviceEntity> deviceEntityOptional = deviceRepository
					.findByDeviceNumber(requestDTO.getDeviceNumber());
			DeviceEntity deviceEntity = deviceEntityOptional.get();
			deviceEntity.setFpsCode(requestDTO.getFpsCode());
			deviceEntity.setFpsName(requestDTO.getFpsName());
			deviceEntity.setAssociated(Boolean.TRUE);
			deviceRepository.save(deviceEntity);
		}
		// MDM Call API
		MMDBDTO DTO = new MMDBDTO();
		try {
			DTO.setDevice_number(requestDTO.getDeviceNumber());
			DTO.setFps_code(requestDTO.getFpsCode());
			DTO.setAssociated(deviceRegistrationEntity.getDeviceStatus().getId());
			StringBuffer uri = new StringBuffer(mdmurl);
			if (uri != null) {
				// call the elms service
				String response = restTemplate.postForObject(uri.toString(), DTO, String.class);
				log.info("=======callMDM catch block============" + response);
			}
		} catch (Exception exception) {
			// general error
			log.error("=======callcallMDM catch block============", exception);
		}

		return Library.getSuccessfulResponse(deviceRegistrationEntity, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_UPDATED);

	}

	@Override
	@Transactional
	public GenericResponse deAssociate(DeviceRegistrationRequestDTO requestDTO) {
		if (StringUtils.isBlank(requestDTO.getDeviceNumber())) {
			throw new InvalidDataValidation(ResponseMessageConstant.MANDTORY_REQUEST_PARM
					.getMessage(new Object[] { requestDTO.getDeviceNumber() }));
		}
		Optional<DeviceRegisterEntity> deviceRegistrationEntity = deviceRegistrationRepository
				.findByDeviceNumber(requestDTO.getDeviceNumber());
		if (!deviceRegistrationEntity.isPresent()) {
			throw new InvalidDataValidation(ResponseMessageConstant.DEVICE_DETAILS_MISSING.getMessage());
		}
		Optional<DeviceEntity> deviceEntity = deviceRepository.findByDeviceNumber(requestDTO.getDeviceNumber());
		if (!deviceEntity.isPresent()) {
			throw new InvalidDataValidation(ResponseMessageConstant.DEVICE_DETAILS_MISSING.getMessage());
		}

		DeviceRegisterEntity deviceRegistration = deviceRegistrationEntity.get();
		// deviceRegistration.setDeviceStatus(DeviceRegistrationStatus.PENDING);
		Optional<DevicestatusEntity> devicestsatusEntity = devicestatusrepository
				.findByCode(requestDTO.getStatuscode());
		deviceRegistration.setDeviceStatus(devicestsatusEntity.get());
		deviceRegistration.setFpsCode(null);
		deviceRegistration.setFpsName(null);
		deviceRegistration.setVillage(null);
		deviceRegistration.setTalukCode(requestDTO.getTalukCode());
		deviceRegistration.setDistrictCode(requestDTO.getDistrictCode());
		deviceRegistration.setTalukName(requestDTO.getTalukName());
		deviceRegistration.setDistrictName(requestDTO.getDistrictName());
		deviceRegistration.setRemarks(null);
		deviceRegistration.setEntity(requestDTO.getEntity());
		deviceRegistrationRepository.save(deviceRegistration);

		DeviceEntity device = deviceEntity.get();
		device.setAssociated(Boolean.FALSE);
		device.setFpsCode(null);
		device.setFpsName(null);
		device.setEntity(requestDTO.getEntity());
		deviceRepository.save(device);
		DeviceRegistrationRequestDTO requestDTO1 = new DeviceRegistrationRequestDTO();
		requestDTO1.setDeviceNumber(requestDTO.getDeviceNumber());
		requestDTO1.setRemarks(requestDTO.getRemarks());
		requestDTO1.setFpsCode(null);
		requestDTO1.setDeviceStatus(devicestsatusEntity.get());

		try {
			devicedeassociate(requestDTO1, requestDTO.getDeviceNumber());
		} catch (Exception e) {
			log.info("......De Associate log entry Failed.....");
		}

		// MDM Call API
		MMDBDTO DTO = new MMDBDTO();
		try {
			DTO.setDevice_number(requestDTO.getDeviceNumber());
			DTO.setFps_code(null);
			DTO.setAssociated(devicestsatusEntity.get().getId());
			StringBuffer uri = new StringBuffer(mdmurl);
			if (uri != null) {
				// call the elms service
				String response = restTemplate.postForObject(uri.toString(), DTO, String.class);
				log.info("=======callMDM catch block============" + response);
			}
		} catch (Exception exception) {
			// general error
			log.error("=======callcallMDM catch block============", exception);
		}

		return Library.getSuccessfulResponse(deviceRegistrationEntity, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.DEVICE_DEASSOCIATED_SUCCESSFULLY);

	}

	public GenericResponse getAllByRequestFilter(PaginationRequestDTO requestData) throws ParseException {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		Long count = this.getCountBySearchFields(requestData);
		log.info("total count :: {}", count);
		if (count > 0) {
			List<DeviceRegisterEntity> list = this.getRecordsByFilterDTO(requestData);
			if (CollectionUtils.isEmpty(list)) {
				throw new RecordNotFoundException("No Record Found");
			}
			Long listcount = (long) list.size();

			paginationResponseDTO.setContents(list);
			paginationResponseDTO.setNumberOfElements(Objects.nonNull(listcount) ? listcount.intValue() : null);
			paginationResponseDTO.setTotalElements(count);
			return Library.getSuccessfulResponse(paginationResponseDTO, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException("No Record Found");
		}
	}

	public List<DeviceRegisterEntity> getRecordsByFilterDTO(PaginationRequestDTO filterRequestDTO)
			throws ParseException {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<DeviceRegisterEntity> cq = cb.createQuery(DeviceRegisterEntity.class);
		Root<DeviceRegisterEntity> from = cq.from(DeviceRegisterEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<DeviceRegisterEntity> typedQuery = null;
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
		if (Objects.nonNull(filterRequestDTO.getPaginationSize())
				&& Objects.nonNull(filterRequestDTO.getPaginationSize())) {
			typedQuery = entityManager.createQuery(cq)
					.setFirstResult((filterRequestDTO.getPageNo() * filterRequestDTO.getPaginationSize()))
					.setMaxResults(filterRequestDTO.getPaginationSize());
		} else {
			typedQuery = entityManager.createQuery(cq);
		}

		List<DeviceRegisterEntity> data = typedQuery.getResultList();
		if (CollectionUtils.isEmpty(data)) {
			throw new RecordNotFoundException("No Record Found");
		}
		return data;
	}

	private Long getCountBySearchFields(PaginationRequestDTO filterRequestDTO) throws ParseException {
		log.info("getting total count by search fields :: {}", filterRequestDTO.toString());
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<DeviceRegisterEntity> from = cq.from(DeviceRegisterEntity.class);
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
			Root<DeviceRegisterEntity> from) throws ParseException {
		DevicestatusEntity div = new DevicestatusEntity();
		try {
			if (Objects.nonNull(filterRequestDTO.getFilters())) {
				log.info("filters ::" + filterRequestDTO.getFilters());

				if (Objects.nonNull(filterRequestDTO.getFilters().get(FROM_DATE))
						&& !filterRequestDTO.getFilters().get(TO_DATE).toString().trim().isEmpty()) {

					Date fromDate = new SimpleDateFormat(DATE_FORMAT)
							.parse(filterRequestDTO.getFilters().get(FROM_DATE).toString() + " " + START_TIME);
					Date toDate = new SimpleDateFormat(DATE_FORMAT)
							.parse(filterRequestDTO.getFilters().get(TO_DATE).toString() + " " + END_TIME);
					list.add(cb.between(from.get(CREATED_DATE), fromDate, toDate));

				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get(Constant.DEVICE_NAME))
						&& !filterRequestDTO.getFilters().get(Constant.DEVICE_NAME).toString().trim().isEmpty()) {

					String deviceName = String
							.valueOf(filterRequestDTO.getFilters().get(Constant.DEVICE_NAME).toString());
					list.add(cb.equal(from.get(Constant.DEVICE_NAME), deviceName));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get(Constant.DISTRICT_CODE))
						&& !filterRequestDTO.getFilters().get(Constant.DISTRICT_CODE).toString().trim().isEmpty()) {

					String districtCode = String
							.valueOf(filterRequestDTO.getFilters().get(Constant.DISTRICT_CODE).toString());
					list.add(cb.equal(from.get(Constant.DISTRICT_CODE), districtCode));
				}
				if (Objects.nonNull(filterRequestDTO.getFilters().get(Constant.TALUK_CODE))
						&& !filterRequestDTO.getFilters().get(Constant.TALUK_CODE).toString().trim().isEmpty()) {

					String talukCode = String
							.valueOf(filterRequestDTO.getFilters().get(Constant.TALUK_CODE).toString());
					list.add(cb.equal(from.get(Constant.TALUK_CODE), talukCode));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get(Constant.DEVICE_NUMBER))
						&& !filterRequestDTO.getFilters().get(Constant.DEVICE_NUMBER).toString().trim().isEmpty()) {

					String deviceNumber = String
							.valueOf(filterRequestDTO.getFilters().get(Constant.DEVICE_NUMBER).toString());
					list.add(cb.equal(from.get(Constant.DEVICE_NUMBER), deviceNumber));
				}
				if (Objects.nonNull(filterRequestDTO.getFilters().get(Constant.DEVICE_NUMBER))
						&& !filterRequestDTO.getFilters().get(Constant.DEVICE_NUMBER).toString().trim().isEmpty()) {

					String deviceNumber = String
							.valueOf(filterRequestDTO.getFilters().get(Constant.DEVICE_NUMBER).toString());
					list.add(cb.equal(from.get(Constant.DEVICE_NUMBER), deviceNumber));
				}
				if (Objects.nonNull(filterRequestDTO.getFilters().get(Constant.DEVICE_STATUS))
						&& !filterRequestDTO.getFilters().get(Constant.DEVICE_STATUS).toString().trim().isEmpty()) {

					String deviceStatus = String
							.valueOf(filterRequestDTO.getFilters().get(Constant.DEVICE_STATUS).toString());
					// list.add(cb.equal(from.get(Constant.DEVICE_STATUS),
					// DeviceRegistrationStatus.valueOf(deviceStatus)));
					Optional<DevicestatusEntity> devicestsatusEntity = devicestatusrepository.findByCode(deviceStatus);
					DeviceRegisterEntity deviceRegistrationEntity = new DeviceRegisterEntity();
					deviceRegistrationEntity.setDeviceStatus(devicestsatusEntity.get());
					System.out.println("STATUS:::" + deviceRegistrationEntity.getDeviceStatus());
					list.add(cb.equal(from.get(Constant.DEVICE_STATUS), deviceRegistrationEntity.getDeviceStatus()));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get(Constant.IMEI_NUMBER))
						&& !filterRequestDTO.getFilters().get(Constant.IMEI_NUMBER).toString().trim().isEmpty()) {
					String imeiNumber = String
							.valueOf(filterRequestDTO.getFilters().get(Constant.IMEI_NUMBER).toString());
					list.add(cb.like(cb.upper(from.get(Constant.IMEI_NUMBER)), "%" + imeiNumber + "%"));
				}
//				if (Objects.nonNull(filterRequestDTO.getFilters().get(Constant.FPS_CODE))
//						&& !filterRequestDTO.getFilters().get(Constant.FPS_CODE).toString().trim().isEmpty()) {
//
//					String fpsCode = String.valueOf(filterRequestDTO.getFilters().get(Constant.FPS_CODE).toString());
//					list.add(cb.equal(from.get(Constant.FPS_CODE), fpsCode));
//				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("fpsCode"))
						&& !filterRequestDTO.getFilters().get("fpsCode").toString().trim().isEmpty()) {

					List<String> fpsCodes = (List<String>) (filterRequestDTO.getFilters().get("fpsCode"));

					boolean empty = fpsCodes.isEmpty();
					if (empty == true) {
						System.out.println("The ArrayList is empty!");
					} else {

						System.out.println("The ArrayList is not empty");
					}
					Expression<String> Mainmodule = from.get("fpsCode");
					list.add(Mainmodule.in(fpsCodes));
				}

			}
		} catch (ParseException e) {
			throw new InvalidParameterException("Invalid filter value passed!");
		}
	}

	public GenericResponse getById(Long id) {
		Optional<DeviceRegisterEntity> entity = deviceRegistrationRepository.findById(id);

		if (!entity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}

//		return Library.getSuccessfulResponse(entity,
//				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);

		return Library.getSuccessfulResponse(deviceRegistrationMapper.entityToReportDTOrecentupdate(entity.get()),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}

	public GenericResponse getAllDeviceStatus() {
		List<KeyValueResponseDTO> employemntStatusList = new ArrayList<>();
		for (DeviceRegistrationStatus status : DeviceRegistrationStatus.values()) {
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
	public FileUploadResponse upload(MultipartFile file) {
		Map<Integer, String> unprocessedData = new HashMap<>();
		Workbook workbook = null;
		try {
			workbook = WorkbookFactory.create(file.getInputStream());
			Sheet worksheet = workbook.getSheetAt(0);
			for (int index = 0; index < worksheet.getPhysicalNumberOfRows(); index++) {
				if (index > 0) {
					this.saveDataToDeviceEntities(workbook, worksheet, index, unprocessedData);
				}
			}

		} catch (ParseException e) {
			log.error("error occurred while parsing the date : {}", e);
			return Library.getFileUploadFailResponse(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
					ErrorMessages.INVALID_DATE_FORMAT);
		} catch (Exception e) {
			log.error("error occurred while saving the data : {}", e);
			return Library.getFileUploadFailResponse(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
					ErrorMessages.FAILED_TO_UPLOAD);
		}
		return Library.getFileUploadResponse(unprocessedData, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.FILE_UPLOADED_SUCCESSFULLY);
	}

	public void saveDataToDeviceEntities(Workbook workbook, Sheet worksheet, int index,
			Map<Integer, String> unprocessedData)
			throws InvalidFormatException, EncryptedDocumentException, IOException, ParseException {

		DeviceRegistrationRequestDTO requestDTO = new DeviceRegistrationRequestDTO();

		String deviceNumber = POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.DEVICE_NUMBER,
				index);
		String fpsCode = POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.FPS_CODE, index);
		String fpsName = POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.FPS_NAME, index);

		if (StringUtils.isBlank(deviceNumber)) {
			unprocessedData.put(index, DeviceUploadColumnNames.DEVICE_NUMBER + " is missing.");
			log.info("device number is missing : {} ", deviceNumber);
			return;
		}
		Optional<DeviceRegisterEntity> deviceRegistrationEntityOptional = deviceRegistrationRepository
				.findByDeviceNumber(deviceNumber);
		if (deviceRegistrationEntityOptional.isPresent()) {
			Optional<DeviceEntity> deviceOptional = deviceRepository.findByDeviceNumber(deviceNumber);
			if (StringUtils.isNotBlank(fpsCode)
					&& StringUtils.isNotBlank(deviceRegistrationEntityOptional.get().getFpsCode())
					&& !fpsCode.equals(deviceRegistrationEntityOptional.get().getFpsCode())
					&& (deviceOptional.isPresent())) {
				DeviceRegisterEntity entity = deviceRegistrationEntityOptional.get();
				entity.setFpsCode(fpsCode);
				entity.setFpsName(fpsName);
				deviceRegistrationRepository.save(entity);

				DeviceEntity updateEntity = deviceOptional.get();
				// updateEntity.setAssociated(Boolean.TRUE);
				updateEntity.setFpsCode(fpsCode);
				updateEntity.setFpsName(fpsName);
				deviceRepository.save(updateEntity);
				requestDTO.setFpsCode(fpsCode);
				Bulkdeviceadd(requestDTO, deviceNumber);

			} else {
				unprocessedData.put(index, DeviceUploadColumnNames.DEVICE_NUMBER + Constant.SPACE_DASH_SPACE
						+ deviceNumber + " already exist.");
				log.info("device already registered : {} ", deviceNumber);
				return;
			}
		} else {

			Optional<DeviceEntity> deviceOptional = deviceRepository.findByDeviceNumber(deviceNumber);
			String macId = POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.MAC_ID, index);
			if (StringUtils.isNotBlank(macId)) {
				Optional<DeviceEntity> deviceEntityOptional = deviceRepository.findByMacIdIgnoreCase(macId);
				if (deviceEntityOptional.isPresent()) {
					unprocessedData.put(index,
							DeviceUploadColumnNames.MAC_ID + Constant.SPACE_DASH_SPACE + macId + " already exist.");
					log.info("mac id already exist : {}", macId);
					return;
				}
			}

			DeviceRegisterEntity deviceRegistrationEntity = new DeviceRegisterEntity();

			if (!deviceOptional.isPresent()) {

				String simId = POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.SIM_ID, index);
				String printerId = POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.PRINTER_ID,
						index);
				String simId2 = POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.SIM_ID2,
						index);
				DeviceEntity deviceEntity = new DeviceEntity();
				deviceEntity.setMacId(macId);

				deviceEntity.setDeviceNumber(deviceNumber);
				deviceEntity.setActive(Boolean.TRUE);
				deviceEntity.setMake(
						POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.MAKE, index));
				deviceEntity.setModel(
						POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.MODEL, index));
				deviceEntity.setSerialNumber(POIUtils.getDataByColumnName(workbook, worksheet,
						DeviceUploadColumnNames.SERIAL_NUMBER, index));
				// deviceEntity.setPrinterId(Objects.nonNull(printerId) ?
				// Long.valueOf(printerId) : null);
				deviceEntity.setSimId(simId);
				Date date = CommonUtil.convertStringToDate(
						POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.LAST_SYNC_ON, index));
				if (Objects.nonNull(date)) {
					deviceEntity.setLastSyncOn(date);
				}
				deviceEntity.setSimId2(simId2);
				if (StringUtils.isBlank(fpsCode)) {
					deviceEntity.setAssociated(Boolean.FALSE);
					// deviceRegistrationEntity.setDeviceStatus(DeviceRegistrationStatus.PENDING);
					Optional<DevicestatusEntity> devicestsatusEntity = devicestatusrepository.findByCode("PENDING");
					deviceRegistrationEntity.setDeviceStatus(devicestsatusEntity.get());
					requestDTO.setStatus("Pending");
				} else {
					deviceEntity.setFpsCode(fpsCode);
					deviceEntity.setFpsName(fpsName);
					deviceRegistrationEntity.setFpsCode(fpsCode);
					deviceRegistrationEntity.setFpsName(fpsName);
					deviceEntity.setAssociated(Boolean.TRUE);
					// deviceRegistrationEntity.setDeviceStatus(DeviceRegistrationStatus.APPROVED);
					Optional<DevicestatusEntity> devicestsatusEntity = devicestatusrepository.findByCode("APPROVED");
					deviceRegistrationEntity.setDeviceStatus(devicestsatusEntity.get());
					requestDTO.setFpsCode(fpsCode);
					requestDTO.setStatus("Approved");
				}
				deviceRepository.save(deviceEntity);
				Bulkdeviceadd(requestDTO, deviceNumber);
			} else {
				/*
				 * if (Objects.nonNull(deviceOptional.get().getFpsCode()) &&
				 * Boolean.TRUE.equals(deviceOptional.get().isAssociated())) {
				 * unprocessedData.put(index, DeviceUploadColumnNames.DEVICE_NUMBER +
				 * Constant.SPACE_DASH_SPACE + deviceNumber + "already mapped with fps code" +
				 * Constant.SPACE_DASH_SPACE + deviceOptional.get().getFpsCode());
				 * log.info("device number : {} already mapped with fps code : {}",
				 * deviceNumber, deviceOptional.get().getFpsCode()); return; }
				 */
				if (StringUtils.isNotBlank(fpsCode)) {
					DeviceEntity updateEntity = deviceOptional.get();
					updateEntity.setAssociated(Boolean.TRUE);
					updateEntity.setFpsCode(fpsCode);
					updateEntity.setFpsName(fpsName);
					deviceRepository.save(updateEntity);
					requestDTO.setFpsCode(fpsCode);
					Bulkdeviceadd(requestDTO, deviceNumber);
				}

			}

			deviceRegistrationEntity.setDeviceNumber(deviceNumber);
			deviceRegistrationEntity.setAndroidId(
					POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.ANDROID_ID, index));
			deviceRegistrationEntity.setAndroidVersion(
					POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.ANDROID_VERSION, index));
			deviceRegistrationEntity.setBluetoothMac(
					POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.BLUETOOTH_MAC, index));
			deviceRegistrationEntity
					.setBoard(POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.BOARD, index));
			deviceRegistrationEntity.setBootLoader(
					POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.BOOTLOADER, index));

			deviceRegistrationEntity
					.setBrand(POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.BRAND, index));
			deviceRegistrationEntity.setCpuSpeed(
					POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.CPU_SPEED, index));
			deviceRegistrationEntity.setDevice(
					POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.DEVICE, index));
			deviceRegistrationEntity.setDeviceName(
					POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.DEVICE_NAME, index));
			deviceRegistrationEntity.setDisplay(
					POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.DISPLAY, index));

			deviceRegistrationEntity.setFingerPrint(
					POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.FINGER_PRINT, index));
			deviceRegistrationEntity.setFirstInstallTime(POIUtils.getDataByColumnName(workbook, worksheet,
					DeviceUploadColumnNames.FIRST_INSTALL_TIME, index));
			deviceRegistrationEntity.setHardware(
					POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.HARDWARE, index));
			deviceRegistrationEntity
					.setHost(POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.HOST, index));
			deviceRegistrationEntity.setImeiNumber(
					POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.IMEI_NUMBER, index));
			deviceRegistrationEntity.setImeiNumber2(
					POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.IMEI_NUMBER2, index));

			deviceRegistrationEntity.setLastUpdateTime(
					POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.LAST_UPDATE_TIME, index));
			deviceRegistrationEntity.setManufacturer(
					POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.MANUFACTURER, index));
			deviceRegistrationEntity.setMemory(
					POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.MEMORY, index));
			deviceRegistrationEntity.setProduct(
					POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.PRODUCT, index));
			deviceRegistrationEntity
					.setRadio(POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.RADIO, index));
			deviceRegistrationEntity.setScreenResolution(POIUtils.getDataByColumnName(workbook, worksheet,
					DeviceUploadColumnNames.SCREEN_RESOLUTION, index));
			deviceRegistrationEntity.setSdkVersion(
					POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.SDK_VERSION, index));
			deviceRegistrationEntity.setSerial(
					POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.SERIAL, index));
			deviceRegistrationEntity.setSerialNumber(
					POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.SERIAL_NUMBER, index));
			deviceRegistrationEntity
					.setTags(POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.TAGS, index));
			deviceRegistrationEntity
					.setType(POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.TYPE, index));

			deviceRegistrationEntity.setVersionCode(
					POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.VERSION_CODE, index));

			deviceRegistrationEntity.setVersionName(
					POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.VERSION_NAME, index));
			deviceRegistrationEntity.setWifiMac(
					POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.WIFI_MAC, index));
			deviceRegistrationEntity.setAppType(
					POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.APP_TYPE, index));
			deviceRegistrationEntity.setVillage(
					POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.VILLAGE, index));
			deviceRegistrationEntity.setTalukCode(
					POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.TALUK_CODE, index));
			deviceRegistrationEntity.setDistrictCode(
					POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.DISTRICT_CODE, index));
			deviceRegistrationEntity.setRemarks(
					POIUtils.getDataByColumnName(workbook, worksheet, DeviceUploadColumnNames.REMARKS, index));

			deviceRegistrationRepository.save(deviceRegistrationEntity);

			try {
				Bulkdeviceregistration(requestDTO, deviceNumber);
			} catch (Exception e) {
				log.info("Excel upload...");
			}

		}
	}

	public void Bulkdeviceregistration(DeviceRegistrationRequestDTO requestDTO, String deviceNumber) {
		DeviceLogEntity devicelogEntity = commonUtil.modalMap(requestDTO, DeviceLogEntity.class);
		devicelogEntity.setDeviceId(deviceNumber);
		devicelogEntity.setRemarks("Bulk  Device Registration Successfully");
		devicelogEntity.setShopCode(requestDTO.getFpsCode());
		devicelogEntity.setStatus(requestDTO.getStatus());
		devicelogRepository.save(devicelogEntity);
	}

	public void Bulkdeviceadd(DeviceRegistrationRequestDTO requestDTO, String deviceNumber) {
		DeviceLogEntity devicelogEntity = commonUtil.modalMap(requestDTO, DeviceLogEntity.class);
		devicelogEntity.setDeviceId(deviceNumber);
		devicelogEntity.setRemarks("Bulk Device Added Successfully");
		devicelogEntity.setShopCode(requestDTO.getFpsCode());
		devicelogEntity.setStatus(requestDTO.getStatus());
		devicelogRepository.save(devicelogEntity);
	}

	public void Manualdeviceadd(DeviceregDTO requestDTO, String deviceNumber) {
		DeviceLogEntity devicelogEntity = commonUtil.modalMap(requestDTO, DeviceLogEntity.class);
		devicelogEntity.setDeviceId(deviceNumber);
		devicelogEntity.setRemarks("Manual Device Added Successfully");
		devicelogEntity.setShopCode(requestDTO.getFpsCode());
		devicelogEntity.setStatus(requestDTO.getStatus());
		devicelogRepository.save(devicelogEntity);
	}

	public GenericResponse Manualdevicemapping(DeviceregDTO requestDTO) {
		MMDBDTO DTO = new MMDBDTO();
		String deviceNumber = requestDTO.getDeviceNumber();
		String fpsCode = requestDTO.getFpsCode();
		String fpsName = requestDTO.getFpsName();
		String assetype = requestDTO.getAssetType();
		String entityName = requestDTO.getEntity();

		Long assetTypeId = requestDTO.getAssetTypeId();
		Long assetNameId = requestDTO.getAssetNameId();
		Long assetSubtypeId = requestDTO.getAssetSubtypeId();
		Long assetBrandId = requestDTO.getAssetBrandId();
		Long supplierNameId = requestDTO.getSupplierNameId();
		Long warrantyPeriod = requestDTO.getWarrantyPeriod();
		Long rating = requestDTO.getRating();

		DeviceRegisterEntity deviceRegistrationEntity = commonUtil.modalMap(requestDTO, DeviceRegisterEntity.class);

		if (StringUtils.isBlank(deviceNumber)) {
			throw new InvalidDataValidation("DEVICE_NUMBER is missing.");
			// log.info("device number is missing : {} ", deviceNumber);
		}

		Optional<DeviceRegisterEntity> deviceRegistrationEntityOptional = deviceRegistrationRepository
				.findByDeviceNumber(deviceNumber);
		if (deviceRegistrationEntityOptional.isPresent()) {

			String StatusCode = deviceRegistrationEntityOptional.get().getDeviceStatus().getCode();
			if (StatusCode != null) {

				if (StatusCode.equalsIgnoreCase("DEVICELOST")) {
					throw new InvalidDataValidation("Device is in Lost Status .Please change to Pending Status");
				} else if (StatusCode.equalsIgnoreCase("DEVICERETURN")) {
					throw new InvalidDataValidation("Device is in Return Status .Please change to Pending Status");
				}

				else if (StatusCode.equalsIgnoreCase("DEVICEREPLACE")) {
					throw new InvalidDataValidation("Device is  in Replace Status .Please change to Pending Status");
				}
			}

			Optional<DeviceEntity> deviceOptional = deviceRepository.findByDeviceNumber(deviceNumber);
			if (StringUtils.isNotBlank(fpsCode)
					// &&
					// StringUtils.isNotBlank(deviceRegistrationEntityOptional.get().getFpsCode())
					// && !fpsCode.equals(deviceRegistrationEntityOptional.get().getFpsCode())
					&& (deviceOptional.isPresent())) {
				DeviceRegisterEntity entity = deviceRegistrationEntityOptional.get();
				entity.setFpsCode(fpsCode);
				entity.setFpsName(fpsName);

				Optional<DevicestatusEntity> devicestsatusEntity = devicestatusrepository.findByCode("APPROVED");
				deviceRegistrationEntity.setDeviceStatus(devicestsatusEntity.get());
				deviceRegistrationEntity.setEntity(entityName);
				entity.setDeviceStatus(devicestsatusEntity.get());
				entity.setEntity(entityName);
				entity.setDevice(assetype);
				// skcode change
				entity.setAssetTypeId(assetTypeId);
				entity.setAssetNameId(assetNameId);
				entity.setAssetBrandId(assetBrandId);
				entity.setAssetSubtypeId(assetSubtypeId);
				entity.setSupplierNameId(supplierNameId);
				entity.setWarrantyPeriod(warrantyPeriod);
				entity.setRating(rating);

				deviceRegistrationRepository.save(entity);
				DeviceEntity updateEntity = deviceOptional.get();
				// updateEntity.setAssociated(Boolean.TRUE);
				updateEntity.setFpsCode(fpsCode);
				updateEntity.setFpsName(fpsName);
				updateEntity.setAssetType(assetype);
				updateEntity.setEntity(entityName);
				// skcode change
				updateEntity.setAssetTypeId(assetTypeId);
				updateEntity.setAssetNameId(assetNameId);
				updateEntity.setAssetBrandId(assetBrandId);
				updateEntity.setAssetSubtypeId(assetSubtypeId);
				updateEntity.setSupplierNameId(supplierNameId);
				updateEntity.setWarrantyPeriod(warrantyPeriod);
				updateEntity.setRating(rating);
				deviceRepository.save(updateEntity);

				requestDTO.setFpsCode(fpsCode);
				DeviceLogEntity devicelogEntity = new DeviceLogEntity();
				devicelogEntity.setDeviceId(deviceNumber);
				devicelogEntity.setRemarks("Manual device Mapping");
				devicelogEntity.setShopCode(fpsCode);
				devicelogEntity.setStatus(devicestsatusEntity.get().getName());
				devicelogRepository.save(devicelogEntity);

				try {
					DTO.setDevice_number(deviceNumber);
					DTO.setFps_code(fpsCode);
					DTO.setAssociated(entity.getDeviceStatus().getId());
					StringBuffer uri = new StringBuffer(mdmurl);
					if (uri != null) {

						// call the elms service
						String response = restTemplate.postForObject(uri.toString(), DTO, String.class);

						log.info("=======callMDM catch block============" + response);

					}
				} catch (Exception exception) {
					// general error
					log.error("=======callcallMDM catch block============", exception);
				}

			} else {
				throw new InvalidDataValidation("DEVICE_NUMBER already exist." + deviceNumber);
				// log.info("device already registered : {} ", deviceNumber);

			}
		}

		else {

			Optional<DeviceEntity> deviceOptional = deviceRepository.findByDeviceNumber(deviceNumber);
			String macId = requestDTO.getMACID();
			if (StringUtils.isNotBlank(macId)) {
				Optional<DeviceEntity> deviceEntityOptional = deviceRepository.findByMacIdIgnoreCase(macId);
				if (deviceEntityOptional.isPresent()) {
					throw new InvalidDataValidation("mac id already exist." + macId);
					// log.info("mac id already exist : {}", macId);
				}
			}

			// DeviceRegisterEntity deviceRegistrationEntity = new DeviceRegisterEntity();

			Optional<DevicestatusEntity> devicestsatusEntity = null;
			if (!deviceOptional.isPresent()) {

				String simId = requestDTO.getSimID();
				String printerId = requestDTO.getPrinterID();
				String simId2 = requestDTO.getSIMID2();
				DeviceEntity deviceEntity = new DeviceEntity();
				deviceEntity.setMacId(macId);
				deviceEntity.setAssetType(assetype);
				deviceEntity.setDeviceNumber(deviceNumber);
				deviceEntity.setActive(Boolean.TRUE);
				deviceEntity.setMake(requestDTO.getMake());
				deviceEntity.setModel(requestDTO.getModel());
				deviceEntity.setSerialNumber(requestDTO.getSerialNumber());
				// deviceEntity.setPrinterId(Objects.nonNull(printerId) ?
				// Long.valueOf(printerId) : null);
				deviceEntity.setSimId(simId);
				deviceEntity.setEntity(entityName);

				// skcodechange
				deviceEntity.setAssetTypeId(assetTypeId);
				deviceEntity.setAssetNameId(assetNameId);
				deviceEntity.setAssetBrandId(assetBrandId);
				deviceEntity.setAssetSubtypeId(assetSubtypeId);
				deviceEntity.setSupplierNameId(supplierNameId);
				deviceEntity.setWarrantyPeriod(warrantyPeriod);
				deviceEntity.setRating(rating);

				deviceEntity.setSimId2(simId2);
				if (StringUtils.isBlank(fpsCode)) {
					deviceEntity.setAssociated(Boolean.FALSE);
					// deviceRegistrationEntity.setDeviceStatus(DeviceRegistrationStatus.PENDING);
					devicestsatusEntity = devicestatusrepository.findByCode("PENDING");
					deviceRegistrationEntity.setDeviceStatus(devicestsatusEntity.get());
					requestDTO.setStatus("Pending");
				} else {
					deviceEntity.setFpsCode(fpsCode);
					deviceEntity.setFpsName(fpsName);
					deviceRegistrationEntity.setFpsCode(fpsCode);
					deviceRegistrationEntity.setFpsName(fpsName);
					deviceRegistrationEntity.setEntity(entityName);
					deviceRegistrationEntity.setDevice(assetype);

					// skcodechange
					deviceRegistrationEntity.setAssetTypeId(assetTypeId);
					deviceRegistrationEntity.setAssetNameId(assetNameId);
					deviceRegistrationEntity.setAssetBrandId(assetBrandId);
					deviceRegistrationEntity.setAssetSubtypeId(assetSubtypeId);
					deviceRegistrationEntity.setSupplierNameId(supplierNameId);
					deviceRegistrationEntity.setWarrantyPeriod(warrantyPeriod);
					deviceRegistrationEntity.setRating(rating);

					deviceEntity.setAssociated(Boolean.TRUE);
					// deviceRegistrationEntity.setDeviceStatus(DeviceRegistrationStatus.APPROVED);
					// Optional<DevicestatusEntity> devicestsatusEntity =
					// devicestatusrepository.findByCode("APPROVED");
					devicestsatusEntity = devicestatusrepository.findByCode("APPROVED");
					deviceRegistrationEntity.setDeviceStatus(devicestsatusEntity.get());
					requestDTO.setFpsCode(fpsCode);
					requestDTO.setStatus("Approved");
				}
				deviceRepository.save(deviceEntity);
				Manualdeviceadd(requestDTO, deviceNumber);

				// MDM Call API

				try {
					DTO.setDevice_number(deviceNumber);
					DTO.setFps_code(fpsCode);
					DTO.setAssociated(devicestsatusEntity.get().getId());
					StringBuffer uri = new StringBuffer(mdmurl);
					if (uri != null) {

						// call the elms service
						String response = restTemplate.postForObject(uri.toString(), DTO, String.class);

						log.info("=======callMDM catch block============" + response);

					}
				} catch (Exception exception) {
					// general error
					log.error("=======callcallMDM catch block============", exception);
				}

			} else {
				if (StringUtils.isNotBlank(fpsCode)) {
					DeviceEntity updateEntity = deviceOptional.get();
					updateEntity.setAssociated(Boolean.TRUE);
					updateEntity.setFpsCode(fpsCode);
					updateEntity.setFpsName(fpsName);
					updateEntity.setAssetType(assetype);
					updateEntity.setEntity(entityName);

					// skcodechange
					updateEntity.setAssetTypeId(assetTypeId);
					updateEntity.setAssetNameId(assetNameId);
					updateEntity.setAssetBrandId(assetBrandId);
					updateEntity.setAssetSubtypeId(assetSubtypeId);
					updateEntity.setSupplierNameId(supplierNameId);
					updateEntity.setWarrantyPeriod(warrantyPeriod);
					updateEntity.setRating(rating);

					deviceRepository.save(updateEntity);
					requestDTO.setFpsCode(fpsCode);

					DeviceLogEntity devicelogEntity = new DeviceLogEntity();
					devicelogEntity.setDeviceId(deviceNumber);
					devicelogEntity.setRemarks("Manual device Mapping");
					devicelogEntity.setShopCode(fpsCode);
					 devicelogEntity.setStatus("Approved");
					devicelogRepository.save(devicelogEntity);
					devicestsatusEntity = devicestatusrepository.findByCode("APPROVED");
					deviceRegistrationEntity.setDeviceStatus(devicestsatusEntity.get());

				}

			}

			deviceRegistrationRepository.save(deviceRegistrationEntity);

			// MDM Call API
			try {
				DTO.setDevice_number(deviceNumber);
				DTO.setFps_code(fpsCode);
				DTO.setAssociated(devicestsatusEntity.get().getId());
				StringBuffer uri = new StringBuffer(mdmurl);
				if (uri != null) {

					// call the elms service
					String response = restTemplate.postForObject(uri.toString(), DTO, String.class);

					log.info("=======callMDM catch block============" + response);

				}
			} catch (Exception exception) {
				// general error
				log.error("=======callcallMDM catch block============", exception);
			}

		}
		return Library.getSuccessfulResponse(deviceRegistrationEntity, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_CREATED);
	}

	public GenericResponse updateDeviceDetailMapping(String deviceNumber, String fpsCode) {
		Optional<DeviceRegisterEntity> deviceRegistrationEntityOptional = deviceRegistrationRepository
				.findByDeviceNumber(deviceNumber);
		if (deviceRegistrationEntityOptional.isPresent()) {
			log.info("device already registered : {} ", deviceNumber);
			throw new DeviceAlreadyExistValidation();
		}
		Optional<DeviceEntity> deviceOptional = deviceRepository.findByDeviceNumber(deviceNumber);

		DeviceRegisterEntity deviceRegistrationEntity = new DeviceRegisterEntity();
		deviceRegistrationEntity.setDeviceNumber(deviceNumber);
		if (!deviceOptional.isPresent()) {
			DeviceEntity deviceEntity = new DeviceEntity();
			deviceEntity.setDeviceNumber(deviceNumber);
			deviceEntity.setFpsCode(fpsCode);
			deviceRegistrationEntity.setFpsCode(fpsCode);
			deviceEntity.setAssociated(Boolean.TRUE);
			// deviceRegistrationEntity.setDeviceStatus(DeviceRegistrationStatus.APPROVED);
			Optional<DevicestatusEntity> devicestsatusEntity = devicestatusrepository.findByCode("APPROVED");
			deviceRegistrationEntity.setDeviceStatus(devicestsatusEntity.get());
			deviceEntity.setActive(Boolean.TRUE);
			deviceRepository.save(deviceEntity);
			DeviceLogEntity devicelogEntity = new DeviceLogEntity();
			devicelogEntity.setDeviceId(deviceNumber);
			devicelogEntity.setRemarks("device Mapping");
			devicelogEntity.setShopCode(fpsCode);
			// devicelogEntity.setStatus(requestDTO.getStatus());
			devicelogRepository.save(devicelogEntity);
		} else {
			if (Objects.nonNull(deviceOptional.get().getFpsCode())
					&& Boolean.TRUE.equals(deviceOptional.get().isAssociated())) {
				log.info("device number : {} already mapped with fps code : {}", deviceNumber,
						deviceOptional.get().getFpsCode());
				throw new InvalidDataValidation(ResponseMessageConstant.DEVICE_ALREADY_ASSOCIATED_TO_ANOTHER_SHOP
						.getMessage(new Object[] { deviceOptional.get().getFpsCode() }));
			}
			if (StringUtils.isNotBlank(fpsCode)) {
				DeviceEntity updateEntity = deviceOptional.get();
				updateEntity.setAssociated(Boolean.TRUE);
				updateEntity.setFpsCode(fpsCode);
				deviceRepository.save(updateEntity);

				DeviceLogEntity devicelogEntity = new DeviceLogEntity();
				devicelogEntity.setDeviceId(deviceNumber);
				devicelogEntity.setRemarks("device Mapping");
				devicelogEntity.setShopCode(fpsCode);
				// devicelogEntity.setStatus(requestDTO.getStatus());
				devicelogRepository.save(devicelogEntity);

			}
		}
		deviceRegistrationRepository.save(deviceRegistrationEntity);
		return Library.getSuccessfulResponse(deviceRegistrationEntity, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_UPDATED);
	}

	@Override
	public GenericResponse getReport(PaginationRequestDTO requestData) throws ParseException {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		Long count = this.getCountBySearchFields(requestData);
		log.info("total count :: {}", count);
		if (count > 0) {
			List<DeviceRegisterEntity> list = this.getRecordsByFilterDTO(requestData);
			if (CollectionUtils.isEmpty(list)) {
				throw new RecordNotFoundException("No Record Found");
			}
			List<DeviceRegistrationReportResponseDTO> responseList = list.stream()
					.map(deviceRegistrationMapper::entityToReportDTO).collect(Collectors.toList());

			paginationResponseDTO.setContents(responseList);
			paginationResponseDTO.setNumberOfElements(Objects.nonNull(count) ? count.intValue() : null);
			paginationResponseDTO.setTotalElements(count);
			return Library.getSuccessfulResponse(paginationResponseDTO, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException("No Record Found");
		}
	}

	public GenericResponse getDeviceRegistrationCount() {
		try {
			DeviceRegistrationCountDto deviceRegistrationCountDto = new DeviceRegistrationCountDto();

			deviceRegistrationCountDto.setTotalDevice(deviceRegistrationRepository.getTotalDeviceCount());
			deviceRegistrationCountDto.setMappedDevice(deviceRegistrationRepository.getMappedDeviceCount());
			deviceRegistrationCountDto.setDeviceRejected(deviceRegistrationRepository.getDeviceRejectedCount());
			deviceRegistrationCountDto.setNotMappedDevice(deviceRegistrationRepository.getNotMappedDeviceCount());
			deviceRegistrationCountDto.setDeviceLost(deviceRegistrationRepository.getDeviceLostCount());
			deviceRegistrationCountDto.setDeviceReplace(deviceRegistrationRepository.getDeviceReplaceCount());
			deviceRegistrationCountDto.setDeviceReturn(deviceRegistrationRepository.getDeviceReturnCount());

			if (deviceRegistrationCountDto.getTotalDevice() == 0) {
				throw new RecordNotFoundException("No record found");
			}

			return Library.getSuccessfulResponse(deviceRegistrationCountDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		} catch (RecordNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"An error occurred while retrieving device registration count.", e);
		}
	}

	public GenericResponse getAllByPassFilter(PaginationRequestDTO requestData) throws ParseException {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		Long count = this.getCountBySearchFields(requestData);
		log.info("total count :: {}", count);
		if (count > 0) {
			List<DeviceRegisterEntity> list = this.getRecordsByFilterDTO(requestData);
			if (CollectionUtils.isEmpty(list)) {
				throw new RecordNotFoundException("No Record Found");
			}
			Long listcount = (long) list.size();

			paginationResponseDTO.setContents(list);
			paginationResponseDTO.setNumberOfElements(Objects.nonNull(listcount) ? listcount.intValue() : null);
			paginationResponseDTO.setTotalElements(count);
			return Library.getSuccessfulResponse(paginationResponseDTO, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException("No Record Found");
		}
	}

	public GenericResponse getCount() {
		try {
			List<EntitysummaryDTO> entitysummary = deviceRegistrationRepository.getCount();
			if (entitysummary.isEmpty()) {
				throw new RecordNotFoundException("No record found");
			}

			else {
				return Library.getSuccessfulResponse(entitysummary, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
						ErrorMessages.RECORED_FOUND);
			}

		} catch (RecordNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"An error occurred while retrieving entity wise summary count.", e);
		}
	}

}
