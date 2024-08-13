package com.oasys.helpdesk.service;

import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.request.GrievanceIssueRequestDto;
import com.oasys.helpdesk.utility.GenericResponse;

public interface GrievanceIssueDetailsService {

	GenericResponse createIssue(GrievanceIssueRequestDto issueRequestDto);

	GenericResponse updateIssue(GrievanceIssueRequestDto issueRequestDto);

	GenericResponse searchCategory(PaginationRequestDTO paginationRequestDTO);

	GenericResponse getAllGIssueDetails();

	GenericResponse getGIssueDetailsById(Long id);

	GenericResponse getGCode();

	GenericResponse getAllActive();

	GenericResponse getByCategory(Long categoryId);
	
	
	
	GenericResponse getByCategoryid(GrievanceIssueRequestDto issueRequestDto);


}
