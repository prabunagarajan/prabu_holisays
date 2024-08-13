package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.CODE;

import java.text.ParseException;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.AccessoriesRequestDTO;
import com.oasys.helpdesk.dto.AssetListRequestDto;
import com.oasys.helpdesk.dto.AssetReportRequestDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.entity.ActionTaken;
import com.oasys.helpdesk.entity.AssetTypeEntity;
import com.oasys.helpdesk.repository.AssetListRepository;
import com.oasys.helpdesk.repository.AssetTypeRepository;
import com.oasys.helpdesk.request.ActionTakenRequestDto;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;

@Service
public interface AssetListService {
	
	GenericResponse addAssetlist(AssetListRequestDto requestDTO);
	GenericResponse getById(Long id);
	GenericResponse updateAssetlist(AssetListRequestDto requestDTO);
	GenericResponse getBysubtypeId(Long assetnameid);
	GenericResponse getBybrandandnameId(Long id);
	GenericResponse getAllByRequestFilter(PaginationRequestDTO requestData) throws ParseException;
	GenericResponse getAllActive();
	GenericResponse assetReport(AssetReportRequestDTO requestDTO);

}
