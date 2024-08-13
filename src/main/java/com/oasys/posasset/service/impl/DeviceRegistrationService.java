package com.oasys.posasset.service.impl;

import java.text.ParseException;

import org.springframework.web.multipart.MultipartFile;

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.response.DashBoardResponseDto;
import com.oasys.helpdesk.utility.FileUploadResponse;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;

import com.oasys.posasset.dto.DeviceRegistrationRequestDTO;
import com.oasys.posasset.dto.DeviceRegistrationUpdateRequestDTO;
import com.oasys.posasset.dto.DeviceregDTO;

public interface DeviceRegistrationService {
	GenericResponse save(DeviceRegistrationRequestDTO requestDTO);

	GenericResponse update(DeviceRegistrationUpdateRequestDTO requestDTO);

	GenericResponse deAssociate(DeviceRegistrationRequestDTO requestDTO);

	GenericResponse getAllByRequestFilter(PaginationRequestDTO requestData) throws ParseException;

	GenericResponse getById(Long id);

	GenericResponse getAllDeviceStatus();

	FileUploadResponse upload(MultipartFile file);

	GenericResponse updateDeviceDetailMapping(String deviceNumber, String fpsCode);

	GenericResponse getReport(PaginationRequestDTO requestData) throws ParseException;

	GenericResponse Manualdevicemapping(DeviceregDTO requestDTO);

	public GenericResponse getDeviceRegistrationCount();

	GenericResponse getAllByPassFilter(PaginationRequestDTO requestData) throws ParseException;

	public GenericResponse getCount();
}
