package com.oasys.helpdesk.service;

import com.oasys.helpdesk.dto.AssetStatusRequestDTO;

import com.oasys.helpdesk.utility.GenericResponse;

public interface AssetStatusService {
	
	GenericResponse add(AssetStatusRequestDTO requestDTO);
	GenericResponse update(AssetStatusRequestDTO requestDTO);
	GenericResponse getAll();


}
