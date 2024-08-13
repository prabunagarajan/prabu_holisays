package com.oasys.helpdesk.service;

import java.text.ParseException;

import com.oasys.helpdesk.dto.DeviceHardwareAddRequestDTO;
import com.oasys.helpdesk.dto.DeviceHardwareNameRequestDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.utility.GenericResponse;

public interface DeviceHardwareService {

	GenericResponse addDevice(DeviceHardwareNameRequestDTO requestDTO);

	GenericResponse updateDevice(DeviceHardwareNameRequestDTO requestDTO);

	GenericResponse searchByFilter(PaginationRequestDTO paginationRequestDTO);

	GenericResponse getAll();

	GenericResponse getCode();

	GenericResponse getAllActive();

	GenericResponse getByDeviceCode(String deviceCode);

	GenericResponse getAllList();

	GenericResponse getDeviceId(DeviceHardwareAddRequestDTO payload);

	GenericResponse getAllActiveList();
	
	GenericResponse getAllByRequestFilter(PaginationRequestDTO requestData) throws ParseException;
	GenericResponse getAllByAssetTypeAndAssetBrandId(Long assetTypeId, Long assetBrandId);
	
	GenericResponse getById(Long id);


}
