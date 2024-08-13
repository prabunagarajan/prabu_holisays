package com.oasys.helpdesk.mapper;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.dto.ChangereqResponseDTO;
import com.oasys.helpdesk.entity.ChangeRequestEntity;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;

@Component
public class ChangereqMapper {
	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private CommonDataController commonDataController;

	public ChangereqResponseDTO convertEntityToResponseDTOReports(ChangeRequestEntity entity) {
		ChangereqResponseDTO responseDTO = commonUtil.modalMap(entity, ChangereqResponseDTO.class);
		String createdByUserName = commonDataController.getUserNameById(Long.parseLong(entity.getCreatedBy()));
		String modifiedByUserName = commonDataController.getUserNameById(Long.parseLong(entity.getModifiedBy()));
		responseDTO.setCreatedBy(entity.getCreatedBy());
		responseDTO.setModifiedBy(entity.getModifiedBy());
		responseDTO.setCreatedByName(createdByUserName);
		responseDTO.setModifiedByName(modifiedByUserName);
		responseDTO.setLicenseType(entity.getLicenseType());
		responseDTO.setEntityType(entity.getEntityType());
		responseDTO.setUnitName(entity.getUnitName());
		responseDTO.setLicenseNo(entity.getLicenseNo());
		responseDTO.setLicenseStatus(entity.getLicenseStatus());
		responseDTO.setAddress(entity.getAddress());
		responseDTO.setMobileNo(entity.getMobileNo());
		responseDTO.setEmailId(entity.getEmailId());
		responseDTO.setShopCode(entity.getShopCode());
		responseDTO.setShopName(entity.getShopName());
		responseDTO.setDistrict(entity.getDistrict());
		responseDTO.setChangereqApplnNo(entity.getChangereqApplnNo());
		responseDTO.setApplnStatus(Long.parseLong(entity.getApplnStatus().getId()));
		responseDTO.setApplnStatusName(entity.getApplnStatus().getName());
		responseDTO.setChangereqStatus(Long.parseLong(entity.getApplnStatus().getId()));
		responseDTO.setChangereqStatusName(entity.getChangereqStatus().getName());
		responseDTO.setDepartmentUrl(entity.getDepartmentUrl());
		responseDTO.setDepartmentUuid(entity.getDepartmentUuid());
		responseDTO.setIescmsUrl(entity.getIescmsUrl());
		responseDTO.setIescmsUuid(entity.getIescmsUuid());
		responseDTO.setDescription(entity.getDescription());
		responseDTO.setCurrentlyWorkwith(entity.getCurrentlyWorkwith());
		responseDTO.setApprovedBy(entity.getApprovedBy());
		responseDTO.setCreatedDate(String.valueOf(entity.getCreatedDate()));
		responseDTO.setModifiedDate(String.valueOf(entity.getModifiedDate()));
		responseDTO.setUserName(entity.getUserName());
		responseDTO.setUserMobileNumber(entity.getUserMobileNumber());
		responseDTO.setRaisedBy(entity.getRaisedBy());
		if (Objects.nonNull(entity.getFeatureId())) {
			responseDTO.setFeatureId(entity.getFeatureId().getId());
			// responseDTO.setFeatureName(entity.getFeatureId().ge);
		}

		return responseDTO;
	}

}
