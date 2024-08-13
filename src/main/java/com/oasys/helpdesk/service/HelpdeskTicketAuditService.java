package com.oasys.helpdesk.service;

import java.text.ParseException;

import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.utility.GenericResponse;

public interface HelpdeskTicketAuditService {

	GenericResponse getAllByRequestFilter(PaginationRequestDTO requestData) throws ParseException;
	GenericResponse getAllActions();
}
