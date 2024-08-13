package com.oasys.helpdesk.service;

import com.oasys.helpdesk.dto.DeviceAccessoriesStatusRequestDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.utility.GenericResponse;

public interface DeviceAccessoriesStatusService {
	GenericResponse add(DeviceAccessoriesStatusRequestDTO requestDTO);
	GenericResponse update(DeviceAccessoriesStatusRequestDTO requestDTO);
	GenericResponse getById(Long id);
	GenericResponse getAll();
	GenericResponse searchByFilter(PaginationRequestDTO paginationDto);
	GenericResponse getCode();
	GenericResponse getAllActive();
}
