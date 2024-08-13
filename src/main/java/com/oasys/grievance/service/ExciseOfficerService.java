package com.oasys.grievance.service;

import java.text.ParseException;

import com.oasys.helpdesk.dto.GrievanceRegRequestDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.utility.GenericResponse;

public interface ExciseOfficerService {

	GenericResponse getAllByRequestFilter(PaginationRequestDTO paginationRequestDTO, AuthenticationDTO authenticationDTO) throws ParseException;

	GenericResponse ListByAll(PaginationRequestDTO paginationRequestDTO);

	GenericResponse getById(Long id);

	GenericResponse getCount(GrievanceRegRequestDTO requestDto, AuthenticationDTO authenticationDTO);
	
	
	GenericResponse getCountscreen(GrievanceRegRequestDTO requestDto, AuthenticationDTO authenticationDTO);

}
