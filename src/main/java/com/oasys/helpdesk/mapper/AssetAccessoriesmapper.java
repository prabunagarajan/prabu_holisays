package com.oasys.helpdesk.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.dto.AssetAccessoriesResponseDTO;
import com.oasys.helpdesk.entity.AssetAccessoriesEntity;
import com.oasys.helpdesk.utility.CommonDataController;

@Component
public class AssetAccessoriesmapper {

	@Autowired
	private CommonDataController commonDataController;
	
	
	public AssetAccessoriesResponseDTO convertEntityToResponseDTO(AssetAccessoriesEntity accessoriesEntity) {

		AssetAccessoriesResponseDTO accessoriesResponseDTO = new AssetAccessoriesResponseDTO();
		accessoriesResponseDTO.setId(accessoriesEntity.getId());
		accessoriesResponseDTO.setAccessoriesCode(accessoriesEntity.getAccessoriesCode());
		accessoriesResponseDTO.setAccessoriesName(accessoriesEntity.getAccessoriesName());
		accessoriesResponseDTO.setStatus(accessoriesEntity.getStatus());
		String createduser=commonDataController.getUserNameById(accessoriesEntity.getCreatedBy());
		String modifieduser=commonDataController.getUserNameById(accessoriesEntity.getModifiedBy());
		accessoriesResponseDTO.setCreated_by(createduser);
		if (Objects.nonNull(accessoriesEntity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			accessoriesResponseDTO.setCreated_date(dateFormat.format(accessoriesEntity.getCreatedDate()));
		}

		accessoriesResponseDTO.setModified_by(modifieduser);
		if (Objects.nonNull(accessoriesEntity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			accessoriesResponseDTO.setModified_date(dateFormat.format(accessoriesEntity.getModifiedDate()));
		}
		return accessoriesResponseDTO;
	}
}
