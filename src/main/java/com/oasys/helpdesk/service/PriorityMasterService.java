package com.oasys.helpdesk.service;

import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.request.PriorityMasterRequestDto;
import com.oasys.helpdesk.utility.GenericResponse;

public interface PriorityMasterService {

	GenericResponse getById(Long id);

	GenericResponse updateAssetType(PriorityMasterRequestDto requestDTO);

	GenericResponse getCode();

	GenericResponse getAll();

	GenericResponse create(PriorityMasterRequestDto requestDTO);

	GenericResponse searchByFilter(PaginationRequestDTO paginationRequestDTO);

	GenericResponse getAllActive();

	GenericResponse getById(Long subCategoryId, Long categoryId);

}
