package com.oasys.helpdesk.service;



import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.TicketstausRequestDTO;
import com.oasys.helpdesk.utility.GenericResponse;
public interface TicketStatusService {
	
	
	GenericResponse adddticket(TicketstausRequestDTO requestDto);
	GenericResponse updateticket(TicketstausRequestDTO requestDto);
	GenericResponse getById(Long id);
	GenericResponse getAll();
	GenericResponse searchByFilter(PaginationRequestDTO paginationDto);
    GenericResponse getCode(); 
    
    GenericResponse getAllActive();
    
    
}
