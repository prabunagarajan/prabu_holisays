package com.oasys.helpdesk.service;

import java.text.ParseException;

import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.request.GrievancePriorityRequestDTO;
import com.oasys.helpdesk.utility.GenericResponse;

public interface GrievancePriorityService {

	GenericResponse createPriority(GrievancePriorityRequestDTO priorityRequestDTO);

	GenericResponse updatePriority(GrievancePriorityRequestDTO requestDTO);

	GenericResponse getById(Long id);

	GenericResponse getAll();

	GenericResponse getAllActive();

	GenericResponse getCode();

	GenericResponse searchByPriority(PaginationRequestDTO paginationRequestDTO);

	GenericResponse getByCategory(Long category);
	GenericResponse getAllByRequestFilter(PaginationRequestDTO requestData) throws ParseException;

}
