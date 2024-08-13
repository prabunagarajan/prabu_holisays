package com.oasys.posasset.mapper;



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
import com.oasys.posasset.entity.SIMEntity;
import com.oasys.posasset.entity.SIMProviderDetEntity;
import com.oasys.posasset.response.SIMProviderResponseDTO;
import com.oasys.posasset.response.SIMResponseDTO;

@Component
public class SIMMapper {
	
	

	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private CommonDataController commonDataController;
	
	public SIMResponseDTO convertEntityToResponseDTO(SIMEntity tsEntity) {
		SIMResponseDTO simresdto = commonUtil.modalMap(tsEntity, SIMResponseDTO.class);
		String createdByUserName=commonDataController.getUserNameById(tsEntity.getCreatedBy());
		String modifiedByUserName=commonDataController.getUserNameById(tsEntity.getModifiedBy());
		//simresdto.setSimproviderdetId(tsEntity.getSimproviderdetId().getId());
		simresdto.setCreated_by(createdByUserName);
		simresdto.setModified_by(modifiedByUserName);
		if (Objects.nonNull(tsEntity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			simresdto.setCreated_date(dateFormat.format(tsEntity.getCreatedDate()));
		}
		if (Objects.nonNull(tsEntity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			simresdto.setModified_date(dateFormat.format(tsEntity.getModifiedDate()));;
		}
		
		simresdto.setSimprovideid(String.valueOf(tsEntity.getSimproviderdetId().getId()));
		simresdto.setProviderName(tsEntity.getSimprovidername());
		return simresdto;
	}


}
