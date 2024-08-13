package com.oasys.helpdesk.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.dto.AssetTypeResponseDTO;
import com.oasys.helpdesk.entity.AssetTypeEntity;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;

@Component
public class AssetTypeMapper {
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private CommonDataController commonDataController;
	
	public AssetTypeResponseDTO convertEntityToResponseDTO(AssetTypeEntity assetTypeEntity) {
		AssetTypeResponseDTO assetTypeResponseDTO = commonUtil.modalMap(assetTypeEntity, AssetTypeResponseDTO.class);
		String createdByUserName=commonDataController.getUserNameById(assetTypeEntity.getCreatedBy());
		String modifiedByUserName=commonDataController.getUserNameById(assetTypeEntity.getModifiedBy());
		
		assetTypeResponseDTO.setCreatedBy(createdByUserName);
		assetTypeResponseDTO.setModifiedBy(modifiedByUserName);
		if (Objects.nonNull(assetTypeEntity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			assetTypeResponseDTO.setCreatedDate(dateFormat.format(assetTypeEntity.getCreatedDate()));
		}
		if (Objects.nonNull(assetTypeEntity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			assetTypeResponseDTO.setModifiedDate(dateFormat.format(assetTypeEntity.getModifiedDate()));
		}
		return assetTypeResponseDTO;
	}
}
