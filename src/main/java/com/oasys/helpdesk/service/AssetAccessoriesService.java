package com.oasys.helpdesk.service;

import javax.validation.Valid;

import com.oasys.helpdesk.dto.AssetAccessoriesRequestDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.utility.GenericResponse;

public interface AssetAccessoriesService {

	GenericResponse adddAssetAccessories(@Valid AssetAccessoriesRequestDTO requestDTO);

	GenericResponse updateAssetAccessories(@Valid AssetAccessoriesRequestDTO requestDTO);

	GenericResponse searchByFilter(@Valid PaginationRequestDTO paginationRequestDTO);

	GenericResponse getCode();

	GenericResponse getAllActive();

	GenericResponse getAll();

	GenericResponse getById(Long id);


}
