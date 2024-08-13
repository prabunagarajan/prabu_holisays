package com.oasys.helpdesk.service;

import com.oasys.helpdesk.dto.GrievanceKnowledgeBaseRequestDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.utility.GenericResponse;

public interface GrievanceKnowledgeBaseService {

	GenericResponse createKnowledge(GrievanceKnowledgeBaseRequestDTO knowledgeRequestDto);

	GenericResponse updateKnowledge(GrievanceKnowledgeBaseRequestDTO knowledgeRequestDto);

	GenericResponse getByCategoryAndIssueDetailsAndStatus(PaginationRequestDTO paginationRequestDTO);

	GenericResponse getAllKnowledgeBase();

	GenericResponse getUniqueCode();

	GenericResponse getKnowledgeBaseById(Long id);

	GenericResponse getKnowledgeByCatAndIssueDId(Long categoryId, Long issueDetailsId);

	GenericResponse updateResolvedCount(Long id);

	GenericResponse getSolutionByIssueDetailsId(Long id);

	GenericResponse getGrievanceKnowledgeBaseCountByStatus();

	
}
