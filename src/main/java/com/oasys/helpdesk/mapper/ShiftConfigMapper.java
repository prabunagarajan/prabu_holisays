package com.oasys.helpdesk.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.dto.ShiftConfigResponseDTO;
import com.oasys.helpdesk.entity.ShiftConfigEntity;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;

@Component
public class ShiftConfigMapper {


	
	@Autowired
	private CommonDataController commonDataController;
	
	public ShiftConfigResponseDTO convertEntityToResponseDTO(ShiftConfigEntity shiftConfig) {

		ShiftConfigResponseDTO shiftConfigResponseDTO = new ShiftConfigResponseDTO();
		shiftConfigResponseDTO.setId(shiftConfig.getId());
		shiftConfigResponseDTO.setCode(shiftConfig.getCode());
		shiftConfigResponseDTO.setConfiguration(shiftConfig.getConfiguration());
		shiftConfigResponseDTO.setStatus(shiftConfig.getStatus());
		String createduser=commonDataController.getUserNameById(shiftConfig.getCreatedBy());
		String modifieduser=commonDataController.getUserNameById(shiftConfig.getModifiedBy());
		shiftConfigResponseDTO.setCreatedBy(createduser);
		if (Objects.nonNull(shiftConfig.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			shiftConfigResponseDTO.setCreatedDate(dateFormat.format(shiftConfig.getCreatedDate()));
		}

		shiftConfigResponseDTO.setModifiedBy(modifieduser);
		if (Objects.nonNull(shiftConfig.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			shiftConfigResponseDTO.setModifiedDate(dateFormat.format(shiftConfig.getModifiedDate()));
		}
		return shiftConfigResponseDTO;
	}
}
