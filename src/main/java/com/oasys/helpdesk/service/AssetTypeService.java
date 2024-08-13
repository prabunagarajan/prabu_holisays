package com.oasys.helpdesk.service;

import com.oasys.helpdesk.dto.AssetTypeRequestDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.utility.GenericResponse;

public interface AssetTypeService {
	GenericResponse addAssetType(AssetTypeRequestDTO requestDto);
	GenericResponse updateAssetType(AssetTypeRequestDTO requestDto);
	GenericResponse getById(Long id);
	GenericResponse getAll();
	GenericResponse getAllActive(Long assetBrandId);
	GenericResponse getAllActiveQ();
	
	GenericResponse searchByFilter(PaginationRequestDTO paginationDto);
	GenericResponse getCode();
}
