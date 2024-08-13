package com.oasys.helpdesk.service;

import com.oasys.helpdesk.dto.AssetBrandRequestDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.utility.GenericResponse;

public interface AssetBrandService {
	GenericResponse addAssetBrand(AssetBrandRequestDTO requestDto);
	GenericResponse updateAssetBrand(AssetBrandRequestDTO requestDto);
	GenericResponse getById(Long id);
	GenericResponse getAll();
	GenericResponse searchByFilter(PaginationRequestDTO paginationDto);
	//GenericResponse getCode();
	GenericResponse getAllActive(Long assetTypeId);
}
