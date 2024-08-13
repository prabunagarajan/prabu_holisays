package com.oasys.helpdesk.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.dto.SalutationResponseDTO;
import com.oasys.helpdesk.entity.SalutationEntity;
import com.oasys.helpdesk.utility.CommonDataController;

@Component
public class SalutationMapper {

	@Autowired
	private CommonDataController commonDataController;
	
	public SalutationResponseDTO convertEntityToResponseDTO(SalutationEntity salutation) {

		SalutationResponseDTO salutationResponseDTO = new SalutationResponseDTO();
		salutationResponseDTO.setId(salutation.getId());
		salutationResponseDTO.setCode(salutation.getCode());
		salutationResponseDTO.setSalutationname(salutation.getSalutationname());
		salutationResponseDTO.setStatus(salutation.getStatus());
		String createduser=commonDataController.getUserNameById(salutation.getCreatedBy());
		String modifieduser=commonDataController.getUserNameById(salutation.getModifiedBy());
		salutationResponseDTO.setCreatedBy(createduser);
		if (Objects.nonNull(salutation.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			salutationResponseDTO.setCreatedDate(dateFormat.format(salutation.getCreatedDate()));
		}

		salutationResponseDTO.setModifiedBy(modifieduser);
		if (Objects.nonNull(salutation.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			salutationResponseDTO.setModifiedDate(dateFormat.format(salutation.getModifiedDate()));
		}
		return salutationResponseDTO;
	}
	
}
