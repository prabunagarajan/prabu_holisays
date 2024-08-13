package com.oasys.posasset.service;

import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.posasset.dto.PosAssetApprovalTypeDTO;

public interface POSAssetApprovalTypeService {
	GenericResponse update(PosAssetApprovalTypeDTO requestDTO);
	GenericResponse getAll();
}
