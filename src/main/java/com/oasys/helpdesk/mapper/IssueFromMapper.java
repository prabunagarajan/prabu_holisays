package com.oasys.helpdesk.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.entity.IssueFromEntity;
import com.oasys.helpdesk.response.IssueFromResponseDto;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;

@Component
public class IssueFromMapper {
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private CommonDataController commonDataController;
	
	public IssueFromResponseDto convertEntityToResponseDTO(IssueFromEntity issueFromEntity) {
		IssueFromResponseDto issueFromResponseDto = commonUtil.modalMap(issueFromEntity, IssueFromResponseDto.class);
		String createdByUserName=commonDataController.getUserNameById(issueFromEntity.getCreatedBy());
		String modifiedByUserName=commonDataController.getUserNameById(issueFromEntity.getModifiedBy());
		
		issueFromResponseDto.setCreatedBy(createdByUserName);
		issueFromResponseDto.setModifiedBy(modifiedByUserName);
		if (Objects.nonNull(issueFromEntity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			issueFromResponseDto.setCreatedDate(dateFormat.format(issueFromEntity.getCreatedDate()));
		}
		if (Objects.nonNull(issueFromEntity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			issueFromResponseDto.setModifiedDate(dateFormat.format(issueFromEntity.getModifiedDate()));
		}
		return issueFromResponseDto;
	}
}
