package com.oasys.helpdesk.service;

import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.request.GrievanceSlaRequestDTO;
import com.oasys.helpdesk.utility.GenericResponse;

public interface GrievanceSlaService {

	GenericResponse createSla(GrievanceSlaRequestDTO requestDto);

	GenericResponse updateSla(GrievanceSlaRequestDTO requestDTO);

	GenericResponse getById(Long id);

	GenericResponse getAll();

	GenericResponse getAllActive();

	GenericResponse getCode();

	GenericResponse searchBySla(PaginationRequestDTO paginationRequestDTO);

	GenericResponse getById(Long categoryId, Long issueDetailsId);

}
