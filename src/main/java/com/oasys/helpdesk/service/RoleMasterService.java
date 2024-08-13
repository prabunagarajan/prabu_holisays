package com.oasys.helpdesk.service;

import java.text.ParseException;

import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.RoleMasterResponseDTO;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.utility.GenericResponse;

public interface RoleMasterService {
	GenericResponse getAll(Boolean helpDeskRoleRequired, Boolean defaultRoleRequired);

	GenericResponse search(PaginationRequestDTO paginationDto, AuthenticationDTO authenticationDTO) throws ParseException;
	
	GenericResponse get();

	GenericResponse addRoleCode(RoleMasterResponseDTO rolemasterresponsedto);

	GenericResponse getById(Long id);

	GenericResponse updateRoleMaster(RoleMasterResponseDTO rolemasterresponcedto);

}
