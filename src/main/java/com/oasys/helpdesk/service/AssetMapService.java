package com.oasys.helpdesk.service;

import java.text.ParseException;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.oasys.helpdesk.dto.AssetMapRequestDto;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.utility.GenericResponse;

@Service

public interface AssetMapService {

	GenericResponse addAssetType(AssetMapRequestDto requestDTO);

	GenericResponse getById(Long id);

	GenericResponse getAllActive();

	GenericResponse getAllByRequestFilter(PaginationRequestDTO requestData, AuthenticationDTO authenticationDTO) throws ParseException;

	GenericResponse updateAssetmap(AssetMapRequestDto requestDTO);

	GenericResponse getAll();
	public GenericResponse getCount(String code);

	public GenericResponse getassetSummaryCount(String assetType);

	

	
	
}
