package com.oasys.helpdesk.service;

import javax.validation.Valid;

import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.request.WorkflowRequestDto;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.utility.GenericResponse;

public interface WorkflowService {

	GenericResponse getAll(AuthenticationDTO authenticationDTO);

	GenericResponse create(WorkflowRequestDto requestDTO);

	GenericResponse getById(Long id);

	GenericResponse update(WorkflowRequestDto requestDTO);

	GenericResponse getCode();

	GenericResponse searchByFilter(PaginationRequestDTO paginationRequestDTO);

	GenericResponse getAllActive();

	GenericResponse getSla(Long categoryId, Long subcategoryId, Long issueDetailsId);

}
