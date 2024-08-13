package com.oasys.helpdesk.service;

import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.request.CategoryRequestDto;
import com.oasys.helpdesk.utility.GenericResponse;

public interface CategoryService {
	GenericResponse createCategory(CategoryRequestDto helpDeskCategoryRequestDto);
	GenericResponse getAllCategory();
	GenericResponse getCategoryById(Long id);
	GenericResponse searchCategory(PaginationRequestDTO paginationDto);
	GenericResponse editCategory(CategoryRequestDto requestDTO) ;
	GenericResponse getCode();
	GenericResponse getAllActive();
}
