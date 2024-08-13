package com.oasys.helpdesk.mapper;



import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.dto.TicketstausResponseDTO;
import com.oasys.helpdesk.entity.ProblemReported;
import com.oasys.helpdesk.entity.TicketStatusEntity;
import com.oasys.helpdesk.response.PRResponseDTO;

import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;

@Component
public class TicketstatusMapper {
	
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private CommonDataController commonDataController;
	
	public TicketstausResponseDTO convertEntityToResponseDTO(TicketStatusEntity tsEntity) {
		TicketstausResponseDTO ticketstatusResponseDTO = commonUtil.modalMap(tsEntity, TicketstausResponseDTO.class);
		String createdByUserName=commonDataController.getUserNameById(tsEntity.getCreatedBy());
		String modifiedByUserName=commonDataController.getUserNameById(tsEntity.getModifiedBy());
		
		ticketstatusResponseDTO.setCreated_by(createdByUserName);
		ticketstatusResponseDTO.setModified_by(modifiedByUserName);
		if (Objects.nonNull(tsEntity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			ticketstatusResponseDTO.setCreated_date(dateFormat.format(tsEntity.getCreatedDate()));
		}
		if (Objects.nonNull(tsEntity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			ticketstatusResponseDTO.setModified_date(dateFormat.format(tsEntity.getModifiedDate()));;
		}
		return ticketstatusResponseDTO;
	}

}
