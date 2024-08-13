package com.oasys.helpdesk.service;

import com.oasys.helpdesk.dto.AccessoriesAddRequestDTO;
import com.oasys.helpdesk.dto.AccessoriesRequestDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.utility.GenericResponse;

public interface AccessoriesNameService {
	GenericResponse addAccessoriesName(AccessoriesRequestDTO requestDto);

	GenericResponse updateDevice(AccessoriesRequestDTO requestDTO);

	GenericResponse searchByFilter(PaginationRequestDTO paginationRequestDTO);

	GenericResponse getAll();

	GenericResponse getCode();

	GenericResponse getDeviceId(AccessoriesAddRequestDTO payload);

	GenericResponse getAllListActive();

	GenericResponse getAllActive();

	GenericResponse getAllList();

	GenericResponse getByaccessCode(String accessoriesCode);
	
	GenericResponse getById(Long id);


} 
