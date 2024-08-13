package com.oasys.helpdesk.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class BackupUserResponseDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long roleId;
	private String roleName;
	private String designationCode;
	private String designationValue;
	private Long backupUserId;
	private String employeeCode;
	private String createdBy;
	private String createdDate;
	private String modifiedBy;
	private String modifiedDate;
}
