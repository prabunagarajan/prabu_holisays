package com.oasys.helpdesk.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.dto.AssetBrandResponseDTO;
import com.oasys.helpdesk.entity.AssetBrandEntity;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;

@Component
public class AssetBrandMapper {
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private CommonDataController commonDataController;
	
	public AssetBrandResponseDTO convertEntityToResponseDTO(AssetBrandEntity assetBrandEntity) {
		AssetBrandResponseDTO assetBrandResponseDTO = commonUtil.modalMap(assetBrandEntity, AssetBrandResponseDTO.class);
		String createdByUserName=commonDataController.getUserNameById(assetBrandEntity.getCreatedBy());
		String modifiedByUserName=commonDataController.getUserNameById(assetBrandEntity.getModifiedBy());
		
		assetBrandResponseDTO.setCreatedBy(createdByUserName);
		assetBrandResponseDTO.setModifiedBy(modifiedByUserName);
		if (Objects.nonNull(assetBrandEntity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			assetBrandResponseDTO.setCreatedDate(dateFormat.format(assetBrandEntity.getCreatedDate()));
		}
		if (Objects.nonNull(assetBrandEntity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			assetBrandResponseDTO.setModifiedDate(dateFormat.format(assetBrandEntity.getModifiedDate()));
		}
	
		if (Objects.nonNull((assetBrandEntity.getType()))) {
			assetBrandResponseDTO.setAssetType(assetBrandEntity.getType().getType());
			assetBrandResponseDTO.setAssetTypeId(assetBrandEntity.getType().getId());
		}
		assetBrandResponseDTO.setBrand(assetBrandEntity.getBrand());
		return assetBrandResponseDTO;
	}
	
	
	public AssetBrandResponseDTO convertEntityToResponseDTOAssetaccessories(AssetBrandEntity assetBrandEntity) {
		AssetBrandResponseDTO assetBrandResponseDTO = commonUtil.modalMap(assetBrandEntity, AssetBrandResponseDTO.class);
		String createdByUserName=commonDataController.getUserNameById(assetBrandEntity.getCreatedBy());
		String modifiedByUserName=commonDataController.getUserNameById(assetBrandEntity.getModifiedBy());
		
		assetBrandResponseDTO.setCreatedBy(createdByUserName);
		assetBrandResponseDTO.setModifiedBy(modifiedByUserName);
		if (Objects.nonNull(assetBrandEntity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			assetBrandResponseDTO.setCreatedDate(dateFormat.format(assetBrandEntity.getCreatedDate()));
		}
		if (Objects.nonNull(assetBrandEntity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			assetBrandResponseDTO.setModifiedDate(dateFormat.format(assetBrandEntity.getModifiedDate()));
		}
	
		if (Objects.nonNull((assetBrandEntity.getType()))) {
			assetBrandResponseDTO.setAssetType(assetBrandEntity.getType().getType());
			assetBrandResponseDTO.setAssetTypeId(assetBrandEntity.getType().getId());
		}
		assetBrandResponseDTO.setBrand(assetBrandEntity.getBrand());
		return assetBrandResponseDTO;
	}
	
}
