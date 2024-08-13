package com.oasys.helpdesk.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.dto.SupplierResponseDTO;
import com.oasys.helpdesk.entity.SupplierEntity;
import com.oasys.helpdesk.repository.SlaMasterRepository;
import com.oasys.helpdesk.repository.UserRepository;
import com.oasys.helpdesk.repository.WorkflowRepository;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class SupplierMapper {
	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private CommonDataController commonDataController;
	
	@Autowired
	private SlaMasterRepository slaMasterRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	WorkflowRepository workflowRepository;


	public SupplierResponseDTO convertEntityToResponseDTO(SupplierEntity entity) {
		SupplierResponseDTO responseDTO = commonUtil.modalMap(entity,
				SupplierResponseDTO.class);
		String createdByUserName = commonDataController.getUserNameById(entity.getCreatedBy());
		String modifiedByUserName = commonDataController.getUserNameById(entity.getModifiedBy());
		responseDTO.setMobileNumber(entity.getMobileNumber());
		responseDTO.setAddress(entity.getAddress());
		responseDTO.setEmailId(entity.getEmailId());
		responseDTO.setActive(entity.isActive());
		responseDTO.setCreatedBy(createdByUserName);
		responseDTO.setModifiedBy(modifiedByUserName);
		responseDTO.setSupplier(entity.getSupplier());
		responseDTO.setSupplierName(entity.getSupplierName());
		responseDTO.setCreatedDate(String.valueOf(entity.getCreatedDate()));
		responseDTO.setModifiedDate(String.valueOf(entity.getModifiedDate()));
		responseDTO.setCreatedById(entity.getCreatedBy());
		responseDTO.setModifiedById(entity.getModifiedBy());
		return responseDTO;
	}
}
