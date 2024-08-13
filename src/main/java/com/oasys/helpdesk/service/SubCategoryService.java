package com.oasys.helpdesk.service;

import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.request.SubCategoryRequestDto;
import com.oasys.helpdesk.utility.GenericResponse;

public interface SubCategoryService {
	GenericResponse getAllSubCategory();
	GenericResponse getSubCategoryById(Long id) ;
	GenericResponse getSubCategoryByCategoryId(Long categoryid);
	GenericResponse createSubCategory(SubCategoryRequestDto helpDeskSubCategoryRequestDto);
	GenericResponse editSubCategory(SubCategoryRequestDto requestDTO) ;
	GenericResponse searchSubCategory(PaginationRequestDTO paginationDto);
	GenericResponse getCode();
	GenericResponse getAllActive();
}
