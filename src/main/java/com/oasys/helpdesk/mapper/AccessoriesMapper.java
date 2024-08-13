package com.oasys.helpdesk.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.dto.AccessoriesListDataResponseDTO;
import com.oasys.helpdesk.dto.AccessoriesResponseDTO;
import com.oasys.helpdesk.entity.Accessories;
import com.oasys.helpdesk.entity.AccessoriesListData;
import com.oasys.helpdesk.utility.CommonDataController;

@Component
public class AccessoriesMapper {


	@Autowired
	private CommonDataController commonDataController;

	public AccessoriesResponseDTO convertEntityToResponseDTO(Accessories deviceEntity) {

		AccessoriesResponseDTO ResponseDTO = new AccessoriesResponseDTO();

		String createduser=commonDataController.getUserNameById(deviceEntity.getCreatedBy());
		String modifieduser=commonDataController.getUserNameById(deviceEntity.getModifiedBy());
		ResponseDTO.setCreatedBy(createduser);
		if (Objects.nonNull(deviceEntity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			ResponseDTO.setCreatedDate(dateFormat.format(deviceEntity.getCreatedDate()));
		}

		ResponseDTO.setModifiedBy(modifieduser);
		if (Objects.nonNull(deviceEntity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			ResponseDTO.setModifiedDate(dateFormat.format(deviceEntity.getModifiedDate()));
		}
		if (Objects.nonNull((deviceEntity.getType()))) {
			ResponseDTO.setAssetType(deviceEntity.getType().getType());
			ResponseDTO.setAssetTypeId(deviceEntity.getType().getId());
		}
		
		if (Objects.nonNull((deviceEntity.getAssetName()))) {
			ResponseDTO.setAssetName(deviceEntity.getAssetName().getDeviceName()); 
			ResponseDTO.setAssetnameId(deviceEntity.getAssetName().getId());  
		}
		ResponseDTO.setAssetsubType(deviceEntity.getAssetsubType());
		ResponseDTO.setStatus(deviceEntity.isStatus());
		ResponseDTO.setId(deviceEntity.getId());
		return ResponseDTO;

	}

	public AccessoriesListDataResponseDTO convertListEntityToListResponseDTO(AccessoriesListData deviceListEntity) {

		AccessoriesListDataResponseDTO listResponseDTO = new AccessoriesListDataResponseDTO();
		listResponseDTO.setId(deviceListEntity.getId());
		listResponseDTO.setAccessoriesCode(deviceListEntity.getAccessoriesCode());
		listResponseDTO.setAccessoriesName(deviceListEntity.getAccessoriesName());
		listResponseDTO.setAccessoriesNameId(deviceListEntity.getAccessoriesNameId());
		listResponseDTO.setAccessoriesSerialNo(deviceListEntity.getAccessoriesSerialNo());
		listResponseDTO.setAccessoriesStatus(deviceListEntity.getAccessoriesStatus());
		listResponseDTO.setWarranty(deviceListEntity.getWarranty());
		listResponseDTO.setRegisteredDate(deviceListEntity.getRegisteredDate());
		listResponseDTO.setExpiredDate(deviceListEntity.getExpiredDate());
		//		listResponseDTO.setDeviceStatus(deviceListEntity.getDeviceStatus());
		listResponseDTO.setStatus(deviceListEntity.isStatus());
		String createduser=commonDataController.getUserNameById(deviceListEntity.getCreatedBy());
		String modifieduser=commonDataController.getUserNameById(deviceListEntity.getModifiedBy());
		listResponseDTO.setCreatedBy(createduser);
		if (Objects.nonNull(deviceListEntity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			listResponseDTO.setCreatedDate(dateFormat.format(deviceListEntity.getCreatedDate()));
		}

		listResponseDTO.setModifiedBy(modifieduser);
		if (Objects.nonNull(deviceListEntity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			listResponseDTO.setModifiedDate(dateFormat.format(deviceListEntity.getModifiedDate()));
		}
		return listResponseDTO;

	}
}
