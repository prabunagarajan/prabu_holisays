package com.oasys.helpdesk.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.dto.DeviceHardwareListResponseDTO;
import com.oasys.helpdesk.dto.DevicehardwareNameResponseDTO;
import com.oasys.helpdesk.entity.DeviceHardwareEntity;
import com.oasys.helpdesk.utility.CommonDataController;
//import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.CommonUtil;

@Component
public class DeviceHardwareMapper {


	@Autowired
	private CommonDataController commonDataController;

		@Autowired
		private CommonUtil commonUtil;

	public DevicehardwareNameResponseDTO convertEntityToResponseDTO(DeviceHardwareEntity DeviceEntity) {

		DevicehardwareNameResponseDTO deviceResponseDTO = commonUtil.modalMap(DeviceEntity, DevicehardwareNameResponseDTO.class);
		//DevicehardwareNameResponseDTO deviceResponseDTO = new DevicehardwareNameResponseDTO();
		String createdByUserName=commonDataController.getUserNameById(DeviceEntity.getCreatedBy());
		String modifiedByUserName=commonDataController.getUserNameById(DeviceEntity.getModifiedBy());
		deviceResponseDTO.setId(DeviceEntity.getId());
		
		if (Objects.nonNull(DeviceEntity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			deviceResponseDTO.setCreatedDate(dateFormat.format(DeviceEntity.getCreatedDate()));
			deviceResponseDTO.setCreatedBy(createdByUserName);
		}
		if (Objects.nonNull(DeviceEntity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			deviceResponseDTO.setModifiedDate(dateFormat.format(DeviceEntity.getCreatedDate()));
			deviceResponseDTO.setModifiedBy(modifiedByUserName);
		}
		
		if (Objects.nonNull(DeviceEntity.getType())) {
			deviceResponseDTO.setAssetType(DeviceEntity.getType().getType());
			deviceResponseDTO.setAssetTypeId(DeviceEntity.getType().getId());
		}
		
		deviceResponseDTO.setDeviceName(DeviceEntity.getDeviceName());
		deviceResponseDTO.setStatus(DeviceEntity.isStatus());
		
		
		
		
		return deviceResponseDTO;

	}


}




