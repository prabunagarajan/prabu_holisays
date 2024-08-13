package com.oasys.posasset.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.posasset.dto.VendorStatusResponseDTO;
import com.oasys.posasset.entity.VendorStatusMasterEntity;

@Component
public class VendorStatusMapper {

	@Autowired
	private  CommonDataController commonDataController;
	
	@Autowired
	private CommonUtil commonUtil;
	
	
	
public VendorStatusResponseDTO  convertEntityToResponseDTO(VendorStatusMasterEntity vendorStatus) {
		

	VendorStatusResponseDTO responseDTO = commonUtil.modalMap(vendorStatus, VendorStatusResponseDTO.class);
//		String createdByUserName = commonDataController.getUserNameById(vendorStatus.getCreatedBy());
//		String modifiedByUserName = commonDataController.getUserNameById(vendorStatus.getModifiedBy());	
		responseDTO.setCode(vendorStatus.getCode());
		responseDTO.setName(vendorStatus.getName());
		responseDTO.setIsActive(vendorStatus.isActive());
		    
	return responseDTO;
	   
	
	}
}
