package com.oasys.posasset.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.conf.exception.DeviceAlreadyExistValidation;
import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.DeviceRegistrationStatus;
import com.oasys.posasset.dto.DeviceRegistrationReportResponseDTO;
import com.oasys.posasset.dto.DeviceRegistrationRequestDTO;
import com.oasys.posasset.dto.DeviceRegistrationUpdateRequestDTO;
import com.oasys.posasset.entity.DeviceEntity;
import com.oasys.posasset.entity.DeviceLogEntity;
import com.oasys.posasset.entity.DeviceRegisterEntity;
import com.oasys.posasset.entity.DevicestatusEntity;
import com.oasys.posasset.repository.DeviceRegistrationRepository;
import com.oasys.posasset.repository.DeviceRepository;
import com.oasys.posasset.repository.DeviceStatusRepository;
import com.oasys.posasset.repository.DevicelogRepository;

@Component
public class DeviceRegistrationMapper {

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private DeviceRepository deviceRepository;

	@Autowired
	private DeviceStatusRepository devicestatusrepository;

	@Autowired
	private DeviceRegistrationRepository deviceRegistrationRepository;

	@Autowired
	private CommonDataController commonDataController;

	@Autowired
	DevicelogRepository devicelogRepository;

	public DeviceRegisterEntity requestDTOToEntity(DeviceRegistrationRequestDTO requestDTO) {
		Optional<DeviceEntity> deviceEntity = deviceRepository.findByDeviceNumber(requestDTO.getDeviceNumber());
		if (!deviceEntity.isPresent()) {
			throw new InvalidDataValidation(ResponseMessageConstant.DEVICE_DETAILS_MISSING.getMessage());
		}
		Optional<DeviceRegisterEntity> deviceRegistrationEntity = deviceRegistrationRepository
				.findByDeviceNumber(requestDTO.getDeviceNumber());
		if (deviceRegistrationEntity.isPresent()) {
			throw new DeviceAlreadyExistValidation();
		}
		DeviceRegisterEntity entity = commonUtil.modalMap(requestDTO, DeviceRegisterEntity.class);
		// entity.setDeviceStatus(DeviceRegistrationStatus.PENDING);

		Optional<DevicestatusEntity> devicestsatusEntity = devicestatusrepository.findByCode("PENDING");
		entity.setDeviceStatus(devicestsatusEntity.get());

		return entity;
	}

	public DeviceRegisterEntity updateRequestDTOToEntity(DeviceRegistrationUpdateRequestDTO requestDTO) {
		Optional<DeviceEntity> deviceEntity = deviceRepository.findByDeviceNumber(requestDTO.getDeviceNumber());
		if (!deviceEntity.isPresent()) {
			throw new InvalidDataValidation(ResponseMessageConstant.DEVICE_DETAILS_MISSING.getMessage());
		}
		if (Boolean.TRUE.equals(deviceEntity.get().isAssociated())) {
			throw new InvalidDataValidation(ResponseMessageConstant.DEVICE_ALREADY_ASSOCIATED_TO_ANOTHER_SHOP
					.getMessage(new Object[] { deviceEntity.get().getFpsCode() }));
		}
		Optional<DeviceRegisterEntity> deviceRegistrationEntity = deviceRegistrationRepository
				.findByDeviceNumber(requestDTO.getDeviceNumber());
		if (!deviceRegistrationEntity.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { Constant.DEVICE_NUMBER }));

		}
		DeviceRegisterEntity entity = deviceRegistrationEntity.get();
		if (!DeviceRegistrationStatus.PENDING.getType().equals(entity.getDeviceStatus().getName())) {
			throw new InvalidDataValidation(ResponseMessageConstant.DEVICE_STATUS_SHOULD_BE_PENDING.getMessage());
		}
		if (DeviceRegistrationStatus.APPROVED.equals(requestDTO.getDeviceStatus())) {
			if (StringUtils.isBlank(requestDTO.getFpsCode())) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { Constant.FPS_CODE }));

			}
			// entity.setDeviceStatus(DeviceRegistrationStatus.APPROVED);
			Optional<DevicestatusEntity> devicestsatusEntity = devicestatusrepository.findByCode("APPROVED");
			entity.setDeviceStatus(devicestsatusEntity.get());
			entity.setFpsCode(requestDTO.getFpsCode());
			entity.setFpsName(requestDTO.getFpsName());
			try {
				requestDTO.setStatus("Approved");
				devicelog(requestDTO);
			} catch (Exception e) {
				// log.info("......Devicereg log entry Failed.....");
			}

		} else if (DeviceRegistrationStatus.REJECTED.equals(requestDTO.getDeviceStatus())) {
			entity.setFpsCode(null);
			entity.setFpsName(null);
			// entity.setDeviceStatus(DeviceRegistrationStatus.REJECTED);
			Optional<DevicestatusEntity> devicestsatusEntity = devicestatusrepository.findByCode("REJECTED");
			entity.setDeviceStatus(devicestsatusEntity.get());
			try {
				requestDTO.setStatus("Rejected");
				devicelog(requestDTO);
			} catch (Exception e) {
				// log.info("......Devicereg log entry Failed.....");
			}
		} else {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { Constant.DEVICE_STATUS }));
		}
		entity.setVillage(requestDTO.getVillage());
		entity.setTalukCode(requestDTO.getTalukCode());
		entity.setDistrictCode(requestDTO.getDistrictCode());
		entity.setRemarks(requestDTO.getRemarks());
		entity.setEntity(requestDTO.getEntityType());
		entity.setDistrictName(requestDTO.getDistrictName());
		entity.setTalukName(requestDTO.getTalukName());
		return entity;
	}

	public void devicelog(DeviceRegistrationUpdateRequestDTO requestDTO) {
		DeviceLogEntity devicelogEntity = commonUtil.modalMap(requestDTO, DeviceLogEntity.class);
		devicelogEntity.setDeviceId(requestDTO.getDeviceNumber());
		devicelogEntity.setRemarks(requestDTO.getRemarks());
		devicelogEntity.setShopCode(requestDTO.getFpsCode());
		devicelogEntity.setStatus(requestDTO.getStatus());
		devicelogRepository.save(devicelogEntity);
	}

	public DeviceRegistrationReportResponseDTO entityToReportDTO(DeviceRegisterEntity entity) {
		DeviceRegistrationReportResponseDTO response = commonUtil.modalMap(entity,
				DeviceRegistrationReportResponseDTO.class);
		response.setImeiNumber1(entity.getImeiNumber());
		if (Objects.nonNull(entity.getCreatedBy())) {
			response.setCreatedBy(commonDataController.getUserNameByUserId(entity.getCreatedBy()));
		}
		if (Objects.nonNull(entity.getModifiedBy())) {
			response.setModifiedBy(commonDataController.getUserNameByUserId(entity.getModifiedBy()));
		}
		if (Objects.nonNull(entity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			response.setCreatedDate(dateFormat.format(entity.getCreatedDate()));
		}
		if (Objects.nonNull(entity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			response.setModifiedDate(dateFormat.format(entity.getModifiedDate()));
		}
		Optional<DeviceEntity> deviceEntity = deviceRepository.findByDeviceNumber(entity.getDeviceNumber());
		if (deviceEntity.isPresent()) {
			response.setModel(deviceEntity.get().getModel());
		}
		if (Objects.nonNull(entity.getDeviceStatus())) {

			response.setDeviceStatus(entity.getDeviceStatus().getName());
		}
		if (Objects.nonNull(entity.getDeviceStatus())) {

			response.setDeveicestatusId(entity.getDeviceStatus().getId());
		}

		return response;
	}

	public DeviceRegistrationReportResponseDTO entityToReportDTOrecentupdate(DeviceRegisterEntity entity) {
		DeviceRegistrationReportResponseDTO response = commonUtil.modalMap(entity,
				DeviceRegistrationReportResponseDTO.class);

//		if(Objects.nonNull(entity.getCreatedBy())) {
//		response.setCreatedBy(commonDataController.getUserNameByUserId(entity.getCreatedBy()));
//		}
//		if(Objects.nonNull(entity.getModifiedBy())) {
//		response.setModifiedBy(commonDataController.getUserNameByUserId(entity.getModifiedBy()));
//		}
		if (Objects.nonNull(entity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			response.setCreatedDate(dateFormat.format(entity.getCreatedDate()));
		}
		if (Objects.nonNull(entity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			response.setModifiedDate(dateFormat.format(entity.getModifiedDate()));
		}

		if (Objects.nonNull(entity.getDeviceStatus())) {

			response.setDeviceStatus(entity.getDeviceStatus().getName());
			response.setDeveicestatusId(entity.getDeviceStatus().getId());
		}

		response.setAndroidId(entity.getAndroidId());
		response.setAndroidVersion(entity.getAndroidVersion());
		response.setBoard(entity.getBoard());
		response.setBootLoader(entity.getBootLoader());
		response.setBrand(entity.getBrand());
		response.setCpuSpeed(entity.getCpuSpeed());
		response.setDevice(entity.getDevice());
		response.setDeviceName(entity.getDeviceName());
		response.setDeviceNumber(entity.getDeviceNumber());
		response.setDisplay(entity.getDisplay());
		response.setFingerPrint(entity.getFingerPrint());
		response.setFirstInstallTime(entity.getFirstInstallTime());
		response.setHardware(entity.getHardware());
		response.setHost(entity.getHost());
		response.setImeiNumber1(entity.getImeiNumber());
		response.setLastUpdateTime(entity.getLastUpdateTime());
		response.setManufacturer(entity.getManufacturer());
		response.setMemory(entity.getMemory());
		response.setProduct(entity.getProduct());
		response.setRadio(entity.getRadio());
		response.setScreenResolution(entity.getScreenResolution());
		response.setSdkVersion(entity.getSdkVersion());
		response.setSerial(entity.getSerial());
		response.setSerialNumber(entity.getSerialNumber());
		response.setTags(entity.getTags());
		response.setType(entity.getType());
		response.setVersionCode(entity.getVersionCode());
		response.setVersionName(entity.getVersionName());
		response.setWifiMac(entity.getWifiMac());
		response.setAppType(entity.getAppType());
		response.setFpsCode(entity.getFpsCode());
		response.setFpsName(entity.getFpsName());
		response.setVillage(entity.getVillage());
		response.setTalukCode(entity.getTalukCode());
		response.setDistrictCode(entity.getDistrictCode());
		response.setRemarks(entity.getRemarks());
		response.setImeiNumber2(entity.getImeiNumber2());
		response.setEntity(entity.getEntity());
		response.setDistrictName(entity.getDistrictName());
		response.setTalukName(entity.getTalukName());
		return response;

	}

}
