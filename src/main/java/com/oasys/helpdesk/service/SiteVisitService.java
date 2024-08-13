package com.oasys.helpdesk.service;

import java.text.ParseException;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.SiteVisitDTO;
import com.oasys.helpdesk.request.CreateTicketRequestDto;
import com.oasys.helpdesk.utility.GenericResponse;

@Service

public interface SiteVisitService {
	
	
	GenericResponse add(SiteVisitDTO requestDTO);
	GenericResponse getById(Long id);
	GenericResponse updateSite(SiteVisitDTO requestDTO);

	GenericResponse getAll();

  GenericResponse getAllByRequestFilter(PaginationRequestDTO requestData) throws ParseException;
	
  GenericResponse getsitevisit(SiteVisitDTO requestDto);
}
