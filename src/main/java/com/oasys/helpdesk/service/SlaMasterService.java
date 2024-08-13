package com.oasys.helpdesk.service;

import javax.validation.Valid;

import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.request.SlaMasterRequestDto;
import com.oasys.helpdesk.utility.GenericResponse;

public interface SlaMasterService {

	GenericResponse getAll();

	GenericResponse create(SlaMasterRequestDto slaMasterRequestDto);

	GenericResponse getById(Long id);

	GenericResponse update(SlaMasterRequestDto requestDTO);

	GenericResponse getCode();

	GenericResponse searchByFilter(PaginationRequestDTO paginationRequestDTO);

	GenericResponse getAllActive();

	GenericResponse getById(Long subCategoryId, Long categoryId);

	GenericResponse getSla(Long categoryId, Long subcategoryId, Long issueDetailsId);

}
