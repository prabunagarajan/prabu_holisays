package com.oasys.posasset.mapper;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.posasset.dto.DevicelostResponseDTO;
import com.oasys.posasset.entity.DeviceLostLogEntity;
import com.oasys.posasset.response.DeviceLostLogResponseDTO;

@Component
public class DeviceLostLogMapper {
	
	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private CommonDataController commonDataController;
	
	public DeviceLostLogResponseDTO convertEntityToResponseDTO(DeviceLostLogEntity tsEntity) {
		DeviceLostLogResponseDTO devicelostlogResponseDTO = commonUtil.modalMap(tsEntity, DeviceLostLogResponseDTO.class);
		
		if (Objects.nonNull(tsEntity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			devicelostlogResponseDTO.setCreatedDate(dateFormat.format(tsEntity.getCreatedDate()));
		}
		
		if (Objects.nonNull(tsEntity.getAction())) {
			devicelostlogResponseDTO.setAction(tsEntity.getAction());
		}
			
		if (Objects.nonNull(tsEntity.getComments())) {
			devicelostlogResponseDTO.setComments(tsEntity.getComments());
		}
		
		if (Objects.nonNull(tsEntity.getDesignation())) {
			devicelostlogResponseDTO.setDesignation(tsEntity.getDesignation());
		}
		
		if (Objects.nonNull(tsEntity.getActionPerformedby())) {
			devicelostlogResponseDTO.setActionPerformedby(tsEntity.getActionPerformedby());
		}
		
		return devicelostlogResponseDTO;
		
		
	}

	
	
	
}
