package com.oasys.helpdesk.service;

import org.springframework.transaction.annotation.Transactional;

import com.oasys.helpdesk.dto.EalWastageDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.posasset.constant.ApprovalStatus;

public interface EalWastageService {

	GenericResponse getBySearchFilter(PaginationRequestDTO requestDTO);

	GenericResponse getWastageById(Long id);

	GenericResponse createWastage(EalWastageDTO wastage);

	GenericResponse updateWastage(Long id, EalWastageDTO updatedWastage);

	GenericResponse deleteWastage(Long id);

	GenericResponse updateStatus(Long id, ApprovalStatus status);

	GenericResponse getInprogressList();
	
	GenericResponse getBottlePlanId(String bottelingPlanId);
	

}

