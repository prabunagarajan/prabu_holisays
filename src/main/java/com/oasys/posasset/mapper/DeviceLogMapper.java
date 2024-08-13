package com.oasys.posasset.mapper;
import java.text.DateFormat;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.posasset.entity.DeviceEntity;
import com.oasys.posasset.entity.DeviceLogEntity;
import com.oasys.posasset.repository.DeviceRepository;
import com.oasys.posasset.request.DeviceRequestDTO;
import com.oasys.posasset.response.DeviceLogResponseDTO;
import com.oasys.posasset.response.DeviceResponseDTO;

@Component
public class DeviceLogMapper {
	
	
	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private CommonDataController commonDataController;

	public DeviceLogResponseDTO convertEntityToResponseDTO(DeviceLogEntity entity) {
		DeviceLogResponseDTO responseDTO = commonUtil.modalMap(entity, DeviceLogResponseDTO.class);
		String createdByUserName = commonDataController.getUserNameById(entity.getCreatedBy());
		
		if (Objects.nonNull(entity.getCreatedDate())) {
			responseDTO.setCreatedBy(createdByUserName);
		}
		responseDTO.setDeviceId(entity.getDeviceId());
		responseDTO.setShopCode(entity.getShopCode());
		responseDTO.setStatus(entity.getStatus());
		responseDTO.setRemarks(entity.getRemarks());
		responseDTO.setCreatedDate(entity.getCreatedDate().toString());
		return responseDTO;

	}

}
