package com.oasys.helpdesk.service;

import com.oasys.helpdesk.dto.DepartmentRequestDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.utility.GenericResponse;

public interface DepartmentService {
	
	GenericResponse adddepartment(DepartmentRequestDTO requestDto);
	GenericResponse updatedepartment(DepartmentRequestDTO requestDto);
	GenericResponse getById(Long id);
	GenericResponse getAll();
	GenericResponse searchByFilter(PaginationRequestDTO paginationDto);
	
	GenericResponse getCode();
	GenericResponse getAllActive();
}
