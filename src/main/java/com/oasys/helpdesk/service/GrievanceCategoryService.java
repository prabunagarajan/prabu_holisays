package com.oasys.helpdesk.service;

import com.oasys.helpdesk.dto.GrievanceCategoryRequestDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.utility.GenericResponse;

public interface GrievanceCategoryService {

	GenericResponse createCategory(GrievanceCategoryRequestDTO categoryRequestDto);

	GenericResponse editCategory(GrievanceCategoryRequestDTO categoryRequestDto);

	GenericResponse getAllCategory();

	GenericResponse getCategoryById(Long id);

	GenericResponse searchCategory(PaginationRequestDTO paginationRequestDTO);

	GenericResponse getCode();

	GenericResponse getAllActive();
	
	GenericResponse getAllActivetypeofuser(GrievanceCategoryRequestDTO categoryRequestDto);


}
