package com.oasys.helpdesk.service;





import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.SalutationRequestDTO;
import com.oasys.helpdesk.utility.GenericResponse;

public interface SalutationService {

	GenericResponse addSalutation(SalutationRequestDTO requestDTO);

	GenericResponse updateSalutation(SalutationRequestDTO requestDTO);

	GenericResponse getAll();

	GenericResponse getAllActive();

	GenericResponse getById(Long id);

	GenericResponse getCode();

	GenericResponse searchByFilter( PaginationRequestDTO paginationRequestDTO);

	
}
