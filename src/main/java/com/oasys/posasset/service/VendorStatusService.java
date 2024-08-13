package com.oasys.posasset.service;

import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.posasset.dto.VendorStatusRequestDTO;

public interface VendorStatusService {

	GenericResponse add(VendorStatusRequestDTO requestDTO);
	GenericResponse update(VendorStatusRequestDTO requestDTO);
	GenericResponse getAll();
}
