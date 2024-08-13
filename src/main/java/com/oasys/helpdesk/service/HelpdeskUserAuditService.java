package com.oasys.helpdesk.service;

import java.text.ParseException;

import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.utility.GenericResponse;

public interface HelpdeskUserAuditService {
	GenericResponse getAllByRequestFilter(PaginationRequestDTO requestData) ;
	GenericResponse getAllActions();
}
