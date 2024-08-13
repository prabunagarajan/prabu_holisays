package com.oasys.helpdesk.service;
import com.oasys.helpdesk.dto.DistrictRequestDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.TicketstausRequestDTO;
import com.oasys.helpdesk.utility.GenericResponse;

public interface DistrictService {
	
	  GenericResponse getCode(); 
	  
	  GenericResponse adddistrict(DistrictRequestDTO requestDto);
	  
	  GenericResponse updatedistrict(DistrictRequestDTO requestDto);
	  
	  GenericResponse  getAll();
	  
	  GenericResponse getById(Long id);
	  
	  GenericResponse getAllActive();
	   
	  GenericResponse searchByFilter(PaginationRequestDTO paginationDto);
	  
	  
	  
	  

}
