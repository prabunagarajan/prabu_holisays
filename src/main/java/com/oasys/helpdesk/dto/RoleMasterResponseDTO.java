package com.oasys.helpdesk.dto;

import lombok.Data;

@Data
public class RoleMasterResponseDTO {
	private Long id;

	private String roleCode;

	private String roleName;

	private Boolean defaultRole;

	private Boolean status;

	private String createdBy;

	private Long createdById;

	private String createdDate;

	private String modifiedBy;

	private Long modifiedById;

	private String modifiedDate;

	private Boolean is_helpdesk_role;

	private String createdByUserName;

	private String modifiedByUserName;
	
	

}
