package com.oasys.helpdesk.service;

import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.SubsolutionRequestDTO;
import com.oasys.helpdesk.dto.TicketstausRequestDTO;
import com.oasys.helpdesk.utility.GenericResponse;

public interface SubsolutionService {
	
	GenericResponse addsubsolution(SubsolutionRequestDTO requestDto);
	GenericResponse updatesubsolution(SubsolutionRequestDTO requestDto);
	GenericResponse getById(Long id);
	GenericResponse getAll();
//	GenericResponse searchByFilter(PaginationRequestDTO paginationDto);
    GenericResponse getCode(); 
    GenericResponse getAllActive();

}
