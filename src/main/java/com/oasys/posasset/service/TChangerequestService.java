package com.oasys.posasset.service;

import javax.validation.Valid;

import com.oasys.helpdesk.dto.ChangereqCountRequestDTO;
import com.oasys.helpdesk.dto.ChangereqRequestDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.posasset.dto.ApprovalDTO;
import com.oasys.posasset.dto.WorkFlowStatusUpdateDTO;

public interface TChangerequestService {
 
	GenericResponse getById(Long id);
	GenericResponse addchnagerequest(ChangereqRequestDTO requestDto);
	GenericResponse getsubPagesearchNewByFilter(@Valid PaginationRequestDTO paginationRequestDTO);
	GenericResponse updateApproval(ApprovalDTO approvalDto);
	GenericResponse update(ChangereqRequestDTO requestDTO);
	GenericResponse changereqStatusUpdate(ApprovalDTO requestDTO);
	Object changereqCount(@Valid ChangereqCountRequestDTO requestDto);
	GenericResponse getCount();
	
	//Boolean updateChangeRequestWorkFlowDetails(WorkFlowStatusUpdateDTO workflowStatusUpdateDto);
	

	
}
