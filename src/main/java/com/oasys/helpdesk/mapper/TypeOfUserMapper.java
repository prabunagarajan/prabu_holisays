package com.oasys.helpdesk.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.dto.TypeOfUserResponseDTO;
import com.oasys.helpdesk.entity.TypeOfUserEntity;
import com.oasys.helpdesk.utility.CommonDataController;

@Component
public class TypeOfUserMapper {

	@Autowired
	private CommonDataController commonDataController;
	
	public TypeOfUserResponseDTO convertEntityTOResponseDTO(TypeOfUserEntity type) {
		
		TypeOfUserResponseDTO typeOfUserResponseDTO = new TypeOfUserResponseDTO();
		typeOfUserResponseDTO.setId(type.getId());
		typeOfUserResponseDTO.setCode(type.getCode());
		typeOfUserResponseDTO.setTypeOfUser(type.getTypeOfUser());
		typeOfUserResponseDTO.setStatus(type.getStatus());
		String createduser=commonDataController.getUserNameById(type.getCreatedBy());
		String modifieduser=commonDataController.getUserNameById(type.getModifiedBy());
		typeOfUserResponseDTO.setCreatedBy(createduser);
		if (Objects.nonNull(type.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			typeOfUserResponseDTO.setCreatedDate(dateFormat.format(type.getCreatedDate()));
		}

		typeOfUserResponseDTO.setModifiedBy(modifieduser);
		if (Objects.nonNull(type.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			typeOfUserResponseDTO.setModifiedDate(dateFormat.format(type.getModifiedDate()));
		}
		
		return typeOfUserResponseDTO;
	}
}
