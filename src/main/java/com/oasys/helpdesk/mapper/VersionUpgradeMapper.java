package com.oasys.helpdesk.mapper;

import java.util.Date;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.dto.VersionUpgradeDTO;
import com.oasys.helpdesk.entity.CreateTicketEntity;
import com.oasys.helpdesk.entity.VersionUpgradeEntity;
import com.oasys.helpdesk.response.CreateTicketResponseDto;
import com.oasys.helpdesk.response.VersionUpgradeResponseDto;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;

@Component
public class VersionUpgradeMapper {
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private CommonDataController commonDataController;

	public VersionUpgradeEntity convertRequestDTOToEntity(VersionUpgradeDTO versionUpgradeDTO, VersionUpgradeEntity entity) {
		if(Objects.isNull(entity)) {
			entity = commonUtil.modalMap(versionUpgradeDTO, VersionUpgradeEntity.class);
		}
		
		entity.setLocation(versionUpgradeDTO.getLocation());
		entity.setVersion(versionUpgradeDTO.getVersion());
		entity.setVersionName(versionUpgradeDTO.getVersionName());
		entity.setApplicationType(versionUpgradeDTO.getApplicationType());
		entity.setChannel(versionUpgradeDTO.getChannel());
		entity.setLocation(versionUpgradeDTO.getLocation());
		entity.setReleaseDate(new Date());
		
		return entity;
	}

	public VersionUpgradeResponseDto convertEntityToResponseDTO(VersionUpgradeEntity entity) {
		VersionUpgradeResponseDto responseDTO = commonUtil.modalMap(entity,
				VersionUpgradeResponseDto.class);
		String createdByUserName = commonDataController.getUserNameById(entity.getCreatedBy());
		//String modifiedByUserName = commonDataController.getUserNameById(entity.getModifiedBy());
		String modifiedByUserName = null;
		if(entity.getModifiedBy() != null) {
		 modifiedByUserName = commonDataController.getUserNameById(entity.getModifiedBy());
		}
		responseDTO.setCreatedBy(createdByUserName);
		responseDTO.setModifiedBy(modifiedByUserName);
		responseDTO.setApplicationType(entity.getApplicationType());
		responseDTO.setChannel(entity.getChannel());
		responseDTO.setId(entity.getId());
		responseDTO.setLocation(entity.getLocation());
		responseDTO.setReleaseDate(entity.getReleaseDate());
		responseDTO.setVersion(entity.getVersion());
		responseDTO.setVersionName(entity.getVersionName());
		
		return responseDTO;
	}

}
