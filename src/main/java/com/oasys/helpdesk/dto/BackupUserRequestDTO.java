package com.oasys.helpdesk.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class BackupUserRequestDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	@NotNull(message = "103")
	private Long roleId;
	@NotBlank(message = "103")
	private String designationCode;
	@NotNull(message = "103")
	private Long backupUserId;
	
	@NotBlank(message = "103")
	private String employeeCode;
}
