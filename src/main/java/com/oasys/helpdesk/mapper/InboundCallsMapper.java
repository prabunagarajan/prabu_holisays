package com.oasys.helpdesk.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.dto.AssetMapResponseDTO;
import com.oasys.helpdesk.dto.InboundCallsResponseDTO;
import com.oasys.helpdesk.entity.AssetMapEntity;
import com.oasys.helpdesk.entity.InboundCallsEntity;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;

@Component
public class InboundCallsMapper {
	
	@Autowired
	private  CommonDataController commonDataController;
	
	@Autowired
	private CommonUtil commonUtil;

	
	
	public InboundCallsResponseDTO  convertEntityToResponseDTO( InboundCallsEntity assetEntity) {
		
		
		InboundCallsResponseDTO responseDTO = commonUtil.modalMap(assetEntity, InboundCallsResponseDTO.class);
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
		
		
		
		 	
			responseDTO.setStartTime(assetEntity.getStartTime());
			responseDTO.setEndTime(assetEntity.getEndTime());
			responseDTO.setTotalCallsReceived(assetEntity.getTotalCallsReceived());
			responseDTO.setTotalCallsAbondoned(assetEntity.getTotalCallsAbondoned());
			responseDTO.setTotalCallsAttended(assetEntity.getTotalCallsAttended());
			responseDTO.setCallsAttendedPercentage(assetEntity.getCallsAttendedPercentage());
			
		return responseDTO;
		
	}


}
