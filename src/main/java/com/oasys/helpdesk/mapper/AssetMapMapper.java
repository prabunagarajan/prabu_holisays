package com.oasys.helpdesk.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.dto.AssetListResponseDto;
import com.oasys.helpdesk.dto.AssetMapRequestDto;
import com.oasys.helpdesk.dto.AssetMapResponseDTO;
import com.oasys.helpdesk.dto.EntityDetailsDTO;
import com.oasys.helpdesk.entity.AssetMapEntity;
import com.oasys.helpdesk.entity.EntityDetails;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;

@Component

public class AssetMapMapper {
	@Autowired
	private  CommonDataController commonDataController;
	
	@Autowired
	private CommonUtil commonUtil;

	
	
	public AssetMapResponseDTO  convertEntityToResponseDTO(AssetMapEntity assetEntity) {
		
		//AssetMapRequestDto ResponseDTO = new AssetMapRequestDto();
		AssetMapResponseDTO responseDTO = commonUtil.modalMap(assetEntity, AssetMapResponseDTO.class);
		String createdByUserName = commonDataController.getUserNameById(assetEntity.getCreatedBy());
		String modifiedByUserName = commonDataController.getUserNameById(assetEntity.getModifiedBy());
		  responseDTO.setId(assetEntity.getId());
		if (Objects.nonNull(assetEntity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			responseDTO.setCreatedDate(dateFormat.format(assetEntity.getCreatedDate()));
			responseDTO.setCreatedByName(createdByUserName);
		}
		if (Objects.nonNull(assetEntity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			responseDTO.setModifiedDate(dateFormat.format(assetEntity.getModifiedDate()));
			responseDTO.setModifiedByName(modifiedByUserName);
		}
		if (Objects.nonNull(assetEntity.getAssetType())) {
			responseDTO.setAssetType(assetEntity.getAssetType().getType());
			responseDTO.setAssetTypeId(assetEntity.getAssetType().getId());	
		}
		if (Objects.nonNull(assetEntity.getAssetName())) {
			responseDTO.setAssetName(assetEntity.getAssetName().getDeviceName());
			responseDTO.setAssetNameId((assetEntity.getAssetName().getId()));
		}
		if (Objects.nonNull(assetEntity.getAssetGroup())) {
			responseDTO.setAssetGroup(assetEntity.getAssetGroup().getEntityName());
			responseDTO.setAssetGroupId((assetEntity.getAssetGroup().getId()));
		}
		if (Objects.nonNull(assetEntity.getStatus())) {
			responseDTO.setStatus(assetEntity.getStatus().getName());
			responseDTO.setStatusId((assetEntity.getStatus().getId()));
		}
		
		 	responseDTO.setDateOfInstallation(assetEntity.getDateOfInstallation());
			responseDTO.setUserName(assetEntity.getUserName());
			responseDTO.setDistrict(assetEntity.getDistrict());
			responseDTO.setAssetLocation(assetEntity.getAssetLocation());
			responseDTO.setSerialNo(assetEntity.getSerialNo());
			responseDTO.setUserType(assetEntity.getUserType());
			responseDTO.setUserName(assetEntity.getUserName());
			responseDTO.setLicenseNo(assetEntity.getLicenseNo());
			responseDTO.setDesignation(assetEntity.getDesignation());
		   
		return responseDTO;
		
	}

}
