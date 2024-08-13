package com.oasys.helpdesk.service;



import java.text.ParseException;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.SiteIssueTypeDTO;

import com.oasys.helpdesk.utility.GenericResponse;

@Service

public interface SiteIssueTypeService {

	
	GenericResponse add(SiteIssueTypeDTO requestDTO);

	GenericResponse getById(Long id);

	GenericResponse getAll();

	GenericResponse updateSite(SiteIssueTypeDTO requestDTO);

	GenericResponse getAllByRequestFilter(PaginationRequestDTO requestData) throws ParseException;

	GenericResponse getAllActive();

	GenericResponse getAlllist();
	

}
