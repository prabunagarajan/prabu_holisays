package com.oasys.helpdesk.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.dto.VersionUpgradeDTO;
import com.oasys.helpdesk.entity.CreateTicketEntity;
import com.oasys.helpdesk.entity.VersionUpgradeEntity;
import com.oasys.helpdesk.mapper.VersionUpgradeMapper;
import com.oasys.helpdesk.repository.VersionUpgradeRepository;
import com.oasys.helpdesk.response.CreateTicketResponseDto;
import com.oasys.helpdesk.response.VersionUpgradeResponseDto;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;

@Service
public class VersionUpgradeServiceImpl implements VersionUpgradeService{
	
	@Autowired
	VersionUpgradeRepository versionUpgradeRepository;
	
	@Autowired
	VersionUpgradeMapper versionUpgradeMapper;

	@Override
	public GenericResponse latestVersion() {
     VersionUpgradeEntity versionUpgradeEntity = versionUpgradeRepository.findTop1OrderByCreatedDateDesc();
		
		if(versionUpgradeEntity == null)
		{
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(), ErrorMessages.NO_RECORD_FOUND);
			
		}
	
			VersionUpgradeResponseDto responseDto = versionUpgradeMapper.convertEntityToResponseDTO(versionUpgradeEntity);
			return Library.getSuccessfulResponse(responseDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),ErrorMessages.RECORED_FOUND);

	}

	@Override
	public GenericResponse addNewVersion(VersionUpgradeDTO versionUpgradeDTO) {	
			versionUpgradeDTO.setId(null);
			VersionUpgradeEntity entity = versionUpgradeMapper.convertRequestDTOToEntity(versionUpgradeDTO,null);
			versionUpgradeRepository.save(entity);
			
			VersionUpgradeResponseDto responseDto = versionUpgradeMapper.convertEntityToResponseDTO(entity);

			return Library.getSuccessfulResponse(responseDto, ErrorCode.CREATED.getErrorCode(),ErrorMessages.RECORED_CREATED);

	}

	@Override
	public GenericResponse updateVersion(Long id, VersionUpgradeDTO versionUpgradeDTO) {
		VersionUpgradeEntity versionUpgradeEntity = null;
		versionUpgradeEntity = versionUpgradeRepository.getOne(id);
		if (versionUpgradeEntity != null) {

			versionUpgradeEntity = versionUpgradeMapper.convertRequestDTOToEntity(versionUpgradeDTO,null);
			versionUpgradeRepository.save(versionUpgradeEntity);
			
			VersionUpgradeResponseDto responseDto = versionUpgradeMapper.convertEntityToResponseDTO(versionUpgradeEntity);

			return Library.getSuccessfulResponse(responseDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),ErrorMessages.RECORED_UPDATED);

		} else {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(), ErrorMessages.NO_RECORD_FOUND);
		}
	}

	@Override
	public GenericResponse getAllVersion() {
		List<VersionUpgradeEntity> versionUpgradeEntity = versionUpgradeRepository.findAll();
		
		if(CollectionUtils.isEmpty(versionUpgradeEntity))
		{
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(), ErrorMessages.NO_RECORD_FOUND);
			
		}
	
			List<VersionUpgradeResponseDto> responseDto = versionUpgradeEntity.stream()
					.map(versionUpgradeMapper::convertEntityToResponseDTO).collect(Collectors.toList());
			return Library.getSuccessfulResponse(responseDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),ErrorMessages.RECORED_FOUND);

	}

}
