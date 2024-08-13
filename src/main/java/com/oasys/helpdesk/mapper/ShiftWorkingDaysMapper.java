package com.oasys.helpdesk.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.dto.ShiftWorkingDaysResponseDTO;
import com.oasys.helpdesk.entity.ShiftWorkingDaysEntity;
import com.oasys.helpdesk.utility.CommonDataController;

@Component
public class ShiftWorkingDaysMapper {

	
	
	@Autowired
	private CommonDataController commonDataController;
	
	public ShiftWorkingDaysResponseDTO convertEntityToResponseDTO(ShiftWorkingDaysEntity shiftwork) {

		ShiftWorkingDaysResponseDTO shiftWorkingDaysResponseDTO = new ShiftWorkingDaysResponseDTO();
		shiftWorkingDaysResponseDTO.setId(shiftwork.getId());
		shiftWorkingDaysResponseDTO.setCode(shiftwork.getCode());
		shiftWorkingDaysResponseDTO.setWorkingdays(shiftwork.getWorkingdays());
		shiftWorkingDaysResponseDTO.setStatus(shiftwork.getStatus());
		String createduser=commonDataController.getUserNameById(shiftwork.getCreatedBy());
		String modifieduser=commonDataController.getUserNameById(shiftwork.getModifiedBy());
		shiftWorkingDaysResponseDTO.setCreatedBy(createduser);
		if (Objects.nonNull(shiftwork.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			shiftWorkingDaysResponseDTO.setCreatedDate(dateFormat.format(shiftwork.getCreatedDate()));
		}

		shiftWorkingDaysResponseDTO.setModifiedBy(modifieduser);
		if (Objects.nonNull(shiftwork.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			shiftWorkingDaysResponseDTO.setModifiedDate(dateFormat.format(shiftwork.getModifiedDate()));
		}
		return shiftWorkingDaysResponseDTO;
	}
}
	

