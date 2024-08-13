package com.oasys.posasset.service;


import java.text.ParseException;

import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.utility.GenericResponse;


public interface DeviceLogService {

	
	GenericResponse getAllByRequestFilter(PaginationRequestDTO requestData, AuthenticationDTO authenticationDTO) throws ParseException;

	GenericResponse getAll();
	
}
