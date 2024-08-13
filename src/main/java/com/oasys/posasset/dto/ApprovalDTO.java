package com.oasys.posasset.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.oasys.helpdesk.entity.UserEntity;
import com.oasys.helpdesk.enums.ApplnStatus;
import com.oasys.helpdesk.enums.ChangereqStatus;
import com.oasys.posasset.constant.ApprovalStatus;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApprovalDTO {

	private Long userId;

	private String userName;

	private String remarks;

	private Long id;

	private ApprovalStatus status;

	private ApplnStatus applnStatus;

	private String subModuleNameCode;

	private String moduleNameCode;

	private String event;

	private String action;

	private String level;

	private String applnNo;

	private String entityName;

	private Long assignTo;

	private ChangereqStatus changereqStatus;

	public String entityType;
	
	private String actionperformedby;
	
	private String designation;	
	
	private String changereqApplnNo;
	private String description;

}
