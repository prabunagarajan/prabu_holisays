package com.oasys.helpdesk.service;



import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.ShiftConfigRequestDTO;
import com.oasys.helpdesk.utility.GenericResponse;

public interface ShiftConfigService {

	GenericResponse addShiftConfiguration(ShiftConfigRequestDTO requestDto);
	
	GenericResponse getByConfiguration(String config);
	
	GenericResponse updateShiftConfiguration(ShiftConfigRequestDTO requestDTO);
	
	GenericResponse searchByConfigFilter(PaginationRequestDTO paginationDto);
	GenericResponse getById(Long Id);

	GenericResponse getAll();

	GenericResponse getCode();

	GenericResponse getAllActive();




	
	
	
}
