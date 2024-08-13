package com.oasys.helpdesk.dto;

import lombok.Data;

@Data
public class ZabbixMasterServerDTO {

	private Long serviceId;
	private String status;
	private String serverName;
	private String createdBy;
	private String createdDate;
	private String modifiedDate;
	private String modifiedBy;
}
