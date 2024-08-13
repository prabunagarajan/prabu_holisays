package com.oasys.helpdesk.service;

import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.request.GrievanceWorkflowRequestDTO;
import com.oasys.helpdesk.utility.GenericResponse;

public interface GrievanceWorkflowService {

	GenericResponse createWorkflow(GrievanceWorkflowRequestDTO requestDto);

	GenericResponse updateWorkflow(GrievanceWorkflowRequestDTO requestDTO);

	GenericResponse getById(Long id);

	GenericResponse getAll();

	GenericResponse getAllActive();

	GenericResponse getCode();

	GenericResponse searchByGWorkflow(PaginationRequestDTO paginationRequestDTO);

}
