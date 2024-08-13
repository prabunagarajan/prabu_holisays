package com.oasys.helpdesk.service;

import java.text.ParseException;

import javax.validation.Valid;

import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.request.SiteActionTakenRequestDto;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.utility.GenericResponse;

public interface SiteActionTakenService {

	GenericResponse createSiteActionTaken(@Valid SiteActionTakenRequestDto siteActionTakenRequestDto);

	Object getById(Long id);

	GenericResponse getAllSiteActionTaken(AuthenticationDTO authenticationDTO);

	GenericResponse getSiteActionTakenByObservationId(Long observationid);

	GenericResponse editSiteActionTaken(SiteActionTakenRequestDto requestDTO);
	
	GenericResponse getAllByPassFilter(PaginationRequestDTO requestData) throws ParseException;

}
