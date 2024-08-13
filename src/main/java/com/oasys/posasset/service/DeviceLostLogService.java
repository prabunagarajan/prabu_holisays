package com.oasys.posasset.service;


import java.text.ParseException;

import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.utility.GenericResponse;


public interface DeviceLostLogService {
	GenericResponse getAll();
	
	GenericResponse getById(Long id);
	
	GenericResponse getByApplicationNo(String designationCode);
	
	
	
	
	
	
}
