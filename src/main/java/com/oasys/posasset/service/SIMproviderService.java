package com.oasys.posasset.service;


import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.SubsolutionRequestDTO;
import com.oasys.helpdesk.dto.TicketstausRequestDTO;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.posasset.request.SIMProviderRequestDTO;

public interface SIMproviderService {

	GenericResponse addsimprovider(SIMProviderRequestDTO requestDto);

	GenericResponse getAllActive();

	GenericResponse getAll();

	GenericResponse getById(Long id);
}
