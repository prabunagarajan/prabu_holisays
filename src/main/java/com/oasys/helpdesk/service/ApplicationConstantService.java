package com.oasys.helpdesk.service;

import com.oasys.helpdesk.dto.ApplicationConstantRequestDTO;
import com.oasys.helpdesk.utility.GenericResponse;

public interface ApplicationConstantService {
	GenericResponse update(ApplicationConstantRequestDTO requestDTO);
}
