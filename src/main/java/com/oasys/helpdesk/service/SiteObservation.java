package com.oasys.helpdesk.service;

import java.text.ParseException;

import org.springframework.stereotype.Service;

import com.oasys.helpdesk.dto.EntityDetailsDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.SiteObservationDTO;
import com.oasys.helpdesk.dto.SiteObservationRequestDto;
import com.oasys.helpdesk.utility.GenericResponse;

@Service
public interface SiteObservation {
	
	GenericResponse getById(Long id);
	GenericResponse add(SiteObservationRequestDto requestDTO);
	GenericResponse getAll();
	GenericResponse updateSite(SiteObservationDTO requestDTO);
	GenericResponse getAllActive();
	GenericResponse getAllByRequestFilter(PaginationRequestDTO requestData) throws ParseException;
	GenericResponse getSiteObservationByIssueTypeId(Long issuetypeid);


	
}
