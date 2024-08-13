package com.oasys.posasset.service;

import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.posasset.dto.ApprovalDTO;
import com.oasys.posasset.dto.DevicedamagerequestDTO;

public interface DevicedamageService {	
	
	GenericResponse adddevicedamage(DevicedamagerequestDTO requestDto);
	
	GenericResponse adddevicedamage1(DevicedamagerequestDTO requestDto);
	
	GenericResponse getAll();
	
	GenericResponse getById(Long id);
	
	GenericResponse update(DevicedamagerequestDTO requestDto);
	
	GenericResponse getBydesignationCode(String designationCode);
	
	GenericResponse getByUserId(Long userId);	
	
	GenericResponse getsubPagesearchNewByFilter(PaginationRequestDTO requestData);
	
	GenericResponse updateApproval(ApprovalDTO approvalDto);
	
	
	GenericResponse licnoverify(DevicedamagerequestDTO requestDto);

	GenericResponse getLogByApplicationNo(String applicationNo);
	
	
	
	
}
