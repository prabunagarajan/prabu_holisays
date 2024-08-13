package com.oasys.helpdesk.service;

import javax.validation.Valid;

import com.oasys.helpdesk.dto.AssetTypeRequestDTO;
import com.oasys.helpdesk.request.IssueFromRequestDto;
import com.oasys.helpdesk.utility.GenericResponse;

public interface IssueFromService {

	GenericResponse getIssueFromList();

	GenericResponse createIssueFrom(IssueFromRequestDto issueRequestDto);
	
	GenericResponse getCode();

	GenericResponse getById(Long id);

	GenericResponse updateAssetType(IssueFromRequestDto requestDTO);

	GenericResponse getAllActive();

}
