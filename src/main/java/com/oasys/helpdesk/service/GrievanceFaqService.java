package com.oasys.helpdesk.service;

import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.request.GrievanceFaqRequestDTO;
import com.oasys.helpdesk.utility.GenericResponse;

public interface GrievanceFaqService {

	GenericResponse createFaq(GrievanceFaqRequestDTO gfaqRequestDto);

	GenericResponse updateTypeOfUser(GrievanceFaqRequestDTO requestDTO);

	GenericResponse getById(Long id);

	GenericResponse getAll();

	GenericResponse getCode();

	GenericResponse getAllActive();

	GenericResponse searchByTypeOfUser(PaginationRequestDTO paginationRequestDTO);
	GenericResponse getByIssueDetailsId(Long issueDetailsId) ;

}
