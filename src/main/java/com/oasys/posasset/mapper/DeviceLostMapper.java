package com.oasys.posasset.mapper;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.dto.GrievanceRegResponseDTO;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.posasset.constant.ApprovalStatus;
import com.oasys.posasset.dto.DevicelostResponseDTO;
import com.oasys.posasset.entity.DevicelostEntity;

@Component
public class DeviceLostMapper {
	
	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private CommonDataController commonDataController;
	
	public DevicelostResponseDTO convertEntityToResponseDTO(DevicelostEntity tsEntity) {
		//DevicelostResponseDTO devicelostResponseDTO = commonUtil.modalMap(tsEntity, DevicelostResponseDTO.class);
		String createdByUserName=commonDataController.getUserNameById(tsEntity.getCreatedBy());

		DevicelostResponseDTO devicelostResponseDTO =new DevicelostResponseDTO(); 
		//new
		devicelostResponseDTO.setId(tsEntity.getId());
		devicelostResponseDTO.setLicenseNo(tsEntity.getLicenseNo());
		devicelostResponseDTO.setCreatedbyName(tsEntity.getCreatedbyName());
		devicelostResponseDTO.setShopId(tsEntity.getShopId());
		devicelostResponseDTO.setApplicableDate(tsEntity.getApplicableDate());
		devicelostResponseDTO.setApplicationNumber(tsEntity.getApplicationNumber());
		devicelostResponseDTO.setInformHelpdesk(tsEntity.getInformHelpdesk());
		devicelostResponseDTO.setComplaintNumber(tsEntity.getComplaintNumber());
		devicelostResponseDTO.setLicenseType(tsEntity.getLicenseType());
		devicelostResponseDTO.setLicenseName(tsEntity.getLicenseName());
		devicelostResponseDTO.setShopName(tsEntity.getShopName());
		devicelostResponseDTO.setDistrict(tsEntity.getDistrict());
		devicelostResponseDTO.setDeviceName(tsEntity.getDeviceName());
		devicelostResponseDTO.setLastsatusDevice(tsEntity.getLastsatusDevice());
		devicelostResponseDTO.setReason(tsEntity.getReason());
		devicelostResponseDTO.setUploadFIRCopy(tsEntity.getUploadFIRCopy());
		devicelostResponseDTO.setFirCopyuuid(tsEntity.getFirCopyuuid());
		devicelostResponseDTO.setApplicationUuid(tsEntity.getApplicationUuid());
		devicelostResponseDTO.setUploadApplication(tsEntity.getUploadApplication());
		devicelostResponseDTO.setUploadPod(tsEntity.getUploadPod());
		devicelostResponseDTO.setPodUuid(tsEntity.getPodUuid());
		devicelostResponseDTO.setLostDevice(tsEntity.getLostDevice());
		devicelostResponseDTO.setReplyRemarks(tsEntity.getReplyRemarks());
		if (Objects.nonNull(tsEntity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			devicelostResponseDTO.setCreatedDate(dateFormat.format(tsEntity.getCreatedDate()));
		}
		
		if (Objects.nonNull(tsEntity.getAccessoriesId())) {
			devicelostResponseDTO.setAccessoriesId(tsEntity.getAccessoriesId().getId());
			devicelostResponseDTO.setAccesoriesName(tsEntity.getAccessoriesId().getType());
		}
		if (Objects.nonNull(tsEntity.getDeviceId())) {
			devicelostResponseDTO.setDeviceId(tsEntity.getDeviceId().getId());
			devicelostResponseDTO.setDevice(tsEntity.getDeviceId().getAccessoriesName());
		}
		devicelostResponseDTO.setStatus(tsEntity.getStatus());
		devicelostResponseDTO.setDeviceserialNo(tsEntity.getDeviceserialNo());
		return devicelostResponseDTO;
	}


}
