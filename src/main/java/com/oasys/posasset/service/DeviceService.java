package com.oasys.posasset.service;

import java.text.ParseException;

import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.posasset.request.DeviceRequestDTO;

public interface DeviceService {

	GenericResponse getAll();

	GenericResponse getById(Long id);

	Boolean isDeviceAlreadyAssociated(String deviceNumber);

	GenericResponse addDevice(DeviceRequestDTO deviceReqDTO);

	GenericResponse update(DeviceRequestDTO deviceReqDTO);
	
	GenericResponse getAllByRequestFilter(PaginationRequestDTO requestData, AuthenticationDTO authenticationDTO) throws ParseException;

	GenericResponse getShopCodeByDeviceNumber(String deviceNumber);
	
	GenericResponse geverifyByDeviceNumber(String deviceNumber);
	

	GenericResponse getShopCode(String shopCode);
	
	GenericResponse getAllByRequestFiltermapunmap(PaginationRequestDTO requestData, AuthenticationDTO authenticationDTO) throws ParseException;

	GenericResponse getAllByRequestFilterfps(PaginationRequestDTO requestData, AuthenticationDTO authenticationDTO) throws ParseException;

	
	
	
}
