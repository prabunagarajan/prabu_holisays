package com.oasys.posasset.service;

import java.text.ParseException;

import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.posasset.dto.POSAssetApprovalRequestDTO;
import com.oasys.posasset.dto.POSAssetRequestDTO;

public interface POSAssetRequestService {
	GenericResponse save(POSAssetRequestDTO requestDTO);
	GenericResponse getById(Long id);
	GenericResponse getAssetRequestTypes();
	GenericResponse getAllByRequestFilter(PaginationRequestDTO requestData) throws ParseException;
	GenericResponse approve(POSAssetApprovalRequestDTO requestDTO);
}
