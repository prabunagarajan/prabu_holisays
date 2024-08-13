package com.oasys.helpdesk.service;

import com.oasys.helpdesk.dto.AssetBrandTypeRequestDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.utility.GenericResponse;

public interface AssetBrandTypeService {
	 GenericResponse add(AssetBrandTypeRequestDTO requestDTO);
	 GenericResponse update(AssetBrandTypeRequestDTO requestDTO);
	 GenericResponse getCode();
	 GenericResponse getById(Long id);
	 GenericResponse getAll();
	 GenericResponse searchByFilter(PaginationRequestDTO paginationDto);
	 GenericResponse getAllActive();
}
