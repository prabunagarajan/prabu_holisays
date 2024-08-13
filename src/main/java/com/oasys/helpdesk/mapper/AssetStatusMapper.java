package com.oasys.helpdesk.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.dto.AssetMapResponseDTO;
import com.oasys.helpdesk.dto.AssetStatusResponseDTO;
import com.oasys.helpdesk.entity.AssetMapEntity;
import com.oasys.helpdesk.entity.AssetStatusEntity;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;

@Component
public class AssetStatusMapper {
	
	@Autowired
	private  CommonDataController commonDataController;
	
	@Autowired
	private CommonUtil commonUtil;
	
	
	
public AssetStatusResponseDTO  convertEntityToResponseDTO(AssetStatusEntity assetEntity) {
		
//		//AssetMapRequestDto ResponseDTO = new AssetMapRequestDto();
	AssetStatusResponseDTO responseDTO = commonUtil.modalMap(assetEntity, AssetStatusResponseDTO.class);
		String createdByUserName = commonDataController.getUserNameById(assetEntity.getCreatedBy());
		String modifiedByUserName = commonDataController.getUserNameById(assetEntity.getModifiedBy());
//		  responseDTO.setId(assetEntity.getId());
//		
		responseDTO.setCode(assetEntity.getCode());
		responseDTO.setName(assetEntity.getName());
		    
	return responseDTO;
	   
	
	}

}


