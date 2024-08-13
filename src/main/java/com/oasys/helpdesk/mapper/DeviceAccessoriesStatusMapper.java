package com.oasys.helpdesk.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.dto.DeviceAccessoriesStatusResponseDTO;
import com.oasys.helpdesk.entity.DeviceAccessoriesStatusEntity;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;

@Component
public class DeviceAccessoriesStatusMapper {

	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private CommonDataController commonDataController;
	
	public DeviceAccessoriesStatusResponseDTO convertEntityToResponseDTO(DeviceAccessoriesStatusEntity entity) {
		DeviceAccessoriesStatusResponseDTO responseDTO = commonUtil.modalMap(entity, DeviceAccessoriesStatusResponseDTO.class);
		String createdByUserName=commonDataController.getUserNameById(entity.getCreatedBy());
		String modifiedByUserName=commonDataController.getUserNameById(entity.getModifiedBy());
		
		responseDTO.setCreatedBy(createdByUserName);
		responseDTO.setModifiedBy(modifiedByUserName);
		if (Objects.nonNull(entity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			responseDTO.setCreatedDate(dateFormat.format(entity.getCreatedDate()));
		}
		if (Objects.nonNull(entity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			responseDTO.setModifiedDate(dateFormat.format(entity.getModifiedDate()));
		}
		return responseDTO;
	}
}
