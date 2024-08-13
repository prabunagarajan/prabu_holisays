package com.oasys.helpdesk.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.dto.GrievanceTicketStatusResponseDTO;
import com.oasys.helpdesk.dto.TicketstausResponseDTO;
import com.oasys.helpdesk.entity.GrievanceTicketStatusEntity;
import com.oasys.helpdesk.entity.TicketStatusEntity;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;

@Component
public class GrievanceTicketStatusMapper {

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private CommonDataController commonDataController;

	public GrievanceTicketStatusResponseDTO convertEntityToResponseDTO(GrievanceTicketStatusEntity gtsEntity) {
		GrievanceTicketStatusResponseDTO grievanceticketstatusResponseDTO = commonUtil.modalMap(gtsEntity,
				GrievanceTicketStatusResponseDTO.class);
		String createdByUserName = commonDataController.getUserNameById(gtsEntity.getCreatedBy());
		String modifiedByUserName = commonDataController.getUserNameById(gtsEntity.getModifiedBy());

		grievanceticketstatusResponseDTO.setCreated_by(createdByUserName);
		grievanceticketstatusResponseDTO.setModified_by(modifiedByUserName);
		if (Objects.nonNull(gtsEntity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			grievanceticketstatusResponseDTO.setCreated_date(dateFormat.format(gtsEntity.getCreatedDate()));
		}
		if (Objects.nonNull(gtsEntity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			grievanceticketstatusResponseDTO.setModified_date(dateFormat.format(gtsEntity.getModifiedDate()));

		}
		return grievanceticketstatusResponseDTO;
	}
}
