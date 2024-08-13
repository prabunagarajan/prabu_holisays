package com.oasys.posasset.service;


import java.text.ParseException;

import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.SubsolutionRequestDTO;
import com.oasys.helpdesk.dto.TicketstausRequestDTO;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.posasset.request.SIMProviderRequestDTO;
import com.oasys.posasset.request.SIMRequestDTO;
public interface SIMService {
	
	GenericResponse addsim(SIMRequestDTO requestDto);
	GenericResponse getAll();
	GenericResponse getById(Long id);
	GenericResponse updatesim(SIMRequestDTO requestDto);
	GenericResponse getAllActive();
	GenericResponse getAllByRequestFilter(PaginationRequestDTO requestData, AuthenticationDTO authenticationDTO) throws ParseException;

}
