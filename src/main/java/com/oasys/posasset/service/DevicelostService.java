package com.oasys.posasset.service;

import com.oasys.helpdesk.dto.GrievanceRegRequestDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.posasset.dto.ApprovalDTO;
import com.oasys.posasset.dto.Devicelostrequestdto;

public interface DevicelostService {	
	
	GenericResponse adddevicelost(Devicelostrequestdto requestDto);
	
	GenericResponse adddevicelost1(Devicelostrequestdto requestDto);
	
	GenericResponse getAll();
	
	GenericResponse getById(Long id);
	
	GenericResponse update(Devicelostrequestdto requestDto);
	
	GenericResponse getBydesignationCode(String designationCode);
	
	GenericResponse getByUserId(Long userId);	
	
	GenericResponse getsubPagesearchNewByFilter(PaginationRequestDTO requestData);
	
	GenericResponse updateApproval(ApprovalDTO approvalDto);
	
	
	GenericResponse licnoverify(Devicelostrequestdto requestDto);
	
	
	
	
}
