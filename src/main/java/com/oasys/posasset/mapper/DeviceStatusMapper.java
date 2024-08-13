package com.oasys.posasset.mapper;

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
import com.oasys.posasset.entity.DevicestatusEntity;
import com.oasys.posasset.repository.DeviceRepository;
import com.oasys.posasset.request.DeviceRequestDTO;
import com.oasys.posasset.response.DeviceResponseDTO;
import com.oasys.posasset.response.DevicestatusResponseDTO;

@Component
public class DeviceStatusMapper {

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private CommonDataController commonDataController;

	@Autowired
	DeviceRepository deviceRepository;

	public DevicestatusResponseDTO convertEntityToResponseDTO(DevicestatusEntity entity) {
		DevicestatusResponseDTO responseDTO = commonUtil.modalMap(entity, DevicestatusResponseDTO.class);
		String createdByUserName = commonDataController.getUserNameById(entity.getCreatedBy());
		String modifiedByUserName = commonDataController.getUserNameById(entity.getModifiedBy());

		if (Objects.nonNull(entity.getCreatedDate())) {
			responseDTO.setCreatedBy(createdByUserName);
		}

		if (Objects.nonNull(entity.getModifiedBy())) {
			responseDTO.setModifiedBy(modifiedByUserName);
		}
		
		if (Objects.nonNull(entity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			responseDTO.setCreatedDate(dateFormat.format(entity.getCreatedDate()));
		}
		if (Objects.nonNull(entity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			responseDTO.setModifiedDate(dateFormat.format(entity.getModifiedDate()));
		}

		responseDTO.setCode(entity.getCode());
		responseDTO.setName(entity.getName());
		return responseDTO;

	}


}
