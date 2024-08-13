package com.oasys.helpdesk.service;

import javax.validation.Valid;

import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.request.DesignationRequestDto;
import com.oasys.helpdesk.request.IssueFromRequestDto;
import com.oasys.helpdesk.utility.GenericResponse;

public interface DesignationService {

	GenericResponse getAll();

	GenericResponse create(DesignationRequestDto issueRequestDto);

	GenericResponse getCode();

	GenericResponse getById(Long id);

	GenericResponse update(DesignationRequestDto requestDTO);

	GenericResponse getAllActive();

	Object searchByFilter(PaginationRequestDTO paginationRequestDTO);


}
