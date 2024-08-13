package com.oasys.helpdesk.service;

import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.request.FaqRequestDto;
import com.oasys.helpdesk.utility.GenericResponse;

public interface FaqService {

	GenericResponse getAllfaq();
	GenericResponse getFaqById(Long id);
	GenericResponse createFaq(FaqRequestDto helpDeskFaqRequestDto);
	GenericResponse editFaq(FaqRequestDto helpDeskFaqRequestDto);
	GenericResponse getCode();
	GenericResponse getAllActive();
	GenericResponse searchByFilter(PaginationRequestDTO paginationDto);
	GenericResponse getById(Long subCategoryId, Long categoryId);
}
