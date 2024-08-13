package com.oasys.helpdesk.service;

import java.text.ParseException;

import com.oasys.helpdesk.dto.GrievanceEscalationWorkflowRequestDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.utility.GenericResponse;

public interface GrievanceEscalationWorkflowService {
	GenericResponse getAll();
	GenericResponse save(GrievanceEscalationWorkflowRequestDTO requestDTO);
	GenericResponse getById(Long id);
	GenericResponse update(GrievanceEscalationWorkflowRequestDTO requestDTO);
	GenericResponse getCode();
	GenericResponse getAllActive() ;
	GenericResponse getAllByRequestFilter(PaginationRequestDTO requestData) throws ParseException;
}
