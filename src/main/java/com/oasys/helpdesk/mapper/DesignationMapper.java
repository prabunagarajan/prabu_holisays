package com.oasys.helpdesk.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.entity.DesignationEntity;
import com.oasys.helpdesk.entity.IssueFromEntity;
import com.oasys.helpdesk.response.DesignationResponseDto;
import com.oasys.helpdesk.response.IssueFromResponseDto;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;

@Component
public class DesignationMapper {
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private CommonDataController commonDataController;
	
	public DesignationResponseDto convertEntityToResponseDTO(DesignationEntity entity) {
		DesignationResponseDto responseDto = commonUtil.modalMap(entity, DesignationResponseDto.class);
		String createdByUserName=commonDataController.getUserNameById(entity.getCreatedBy());
		String modifiedByUserName=commonDataController.getUserNameById(entity.getModifiedBy());
		
		responseDto.setCreatedBy(createdByUserName);
		responseDto.setModifiedBy(modifiedByUserName);
		if (Objects.nonNull(entity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			responseDto.setCreatedDate(dateFormat.format(entity.getCreatedDate()));
		}
		if (Objects.nonNull(entity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			responseDto.setModifiedDate(dateFormat.format(entity.getModifiedDate()));
		}
		return responseDto;
	}
}
