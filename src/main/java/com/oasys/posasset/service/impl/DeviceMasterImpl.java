package com.oasys.posasset.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.posasset.dto.DeviceMasterDTO;
import com.oasys.posasset.entity.DeviceMastersEntity;
import com.oasys.posasset.repository.DeviceMasterRespository;
import com.oasys.posasset.service.DeviceMasterService;

import io.micrometer.core.instrument.util.StringUtils;

@Service
public class DeviceMasterImpl implements DeviceMasterService {

	@Autowired
	DeviceMasterRespository devicemasterrespository;

	@Override
	public GenericResponse addDeviceMasterDeviceNumber(DeviceMasterDTO deviceMasterDTO) {

		if (deviceMasterDTO.getDeviceNumber().isEmpty()) {

			throw new InvalidDataValidation("Device number is Empty!");
		}

		if (StringUtils.isBlank(deviceMasterDTO.getDeviceNumber())) {
			return Library.getFailResponseCode(201, "Device number is isBlank!");
		}

		List<DeviceMastersEntity> entitiesWithSameDeviceNumber = devicemasterrespository
				.findByDeviceNumber(deviceMasterDTO.getDeviceNumber());

		if (entitiesWithSameDeviceNumber.size() > 1) {

			return Library.getFailResponseCode(201, "Multiple entities found with the same device number!");
		} else if (entitiesWithSameDeviceNumber.size() == 1) {

			return Library.getFailedfulResponse(entitiesWithSameDeviceNumber.get(0),
					ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { "DEVICE NUMBER" }));
		} else {

			DeviceMastersEntity deviceMasterEntity = new DeviceMastersEntity();
			deviceMasterEntity.setId(null);
			deviceMasterEntity.setDeviceNumber(deviceMasterDTO.getDeviceNumber().toUpperCase());
			DeviceMastersEntity savedEntity = devicemasterrespository.save(deviceMasterEntity);
			return Library.getSuccessfulResponse(savedEntity, ErrorCode.CREATED.getErrorCode(),
					ErrorMessages.RECORED_CREATED);
		}
	}

	@Override
	public GenericResponse updateDeviceNumber(DeviceMasterDTO deviceMasterDTO) {

		if (Objects.isNull(deviceMasterDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { "ID" }));
		}

		if (StringUtils.isBlank(deviceMasterDTO.getDeviceNumber())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { "Device Number" }));
		}

		Optional<DeviceMastersEntity> optionalDeviceMastersEntity = devicemasterrespository
				.findById(deviceMasterDTO.getId());

		if (optionalDeviceMastersEntity.isPresent()) {
			DeviceMastersEntity deviceMastersEntity = optionalDeviceMastersEntity.get();

			deviceMastersEntity.setDeviceNumber(deviceMasterDTO.getDeviceNumber().toUpperCase());

			DeviceMastersEntity updatedEntity = devicemasterrespository.save(deviceMastersEntity);
			return Library.getSuccessfulResponse(updatedEntity, ErrorCode.CREATED.getErrorCode(),
					ErrorMessages.RECORED_UPDATED);
		} else {

			throw new InvalidDataValidation("Given is ID Not Found!");
		}
	}

}
