package com.oasys.helpdesk.service;

import java.text.ParseException;

import org.springframework.stereotype.Service;


import com.oasys.helpdesk.dto.InboundCallsDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.utility.GenericResponse;

@Service
public interface InboundCallsService {
	
	GenericResponse add(InboundCallsDTO requestDTO);

	GenericResponse getById(Long id);

	GenericResponse updateInboundCalls(InboundCallsDTO requestDTO);
	
	GenericResponse getAllByRequestFilter(PaginationRequestDTO requestData) throws ParseException;
	
	GenericResponse getTotalCallsSummaryCount(InboundCallsDTO requestDTO);
}
