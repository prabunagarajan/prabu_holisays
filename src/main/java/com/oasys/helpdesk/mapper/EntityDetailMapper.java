package com.oasys.helpdesk.mapper;

import java.text.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.dto.AccessoriesResponseDTO;
import com.oasys.helpdesk.dto.EntityDetailsDTO;
import com.oasys.helpdesk.dto.EntityDetailsResponseDTO;
import com.oasys.helpdesk.dto.SiteVisitResponseDTO;
import com.oasys.helpdesk.entity.EntityDetails;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;

@Component
public class EntityDetailMapper {
	@Autowired
	private  CommonDataController commonDataController;
	
	@Autowired
	private CommonUtil commonUtil;

	public EntityDetailsResponseDTO  entityToResponseDTO(EntityDetails entityDetails) {

		EntityDetailsResponseDTO EntityDetailsDTO = commonUtil.modalMap(entityDetails, EntityDetailsResponseDTO.class);
		String createdByUserName=commonDataController.getUserNameById(entityDetails.getCreatedBy());
		String modifiedByUserName=commonDataController.getUserNameById(entityDetails.getModifiedBy());
		
		EntityDetailsDTO.setCreated_by(createdByUserName);
		EntityDetailsDTO.setModified_by(modifiedByUserName);
		if (Objects.nonNull(entityDetails.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			EntityDetailsDTO.setCreated_date(dateFormat.format(entityDetails.getCreatedDate()));
		}
		if (Objects.nonNull(entityDetails.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			EntityDetailsDTO.setModified_date(dateFormat.format(entityDetails.getModifiedDate()));;
		}
		return EntityDetailsDTO;
	}
}
