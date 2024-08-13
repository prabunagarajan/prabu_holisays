package com.oasys.helpdesk.dto;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class EntityDetailsDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;

	private String entityName;

	private String entityCode;

	private boolean isActive;

	private String createdDate;

	private String modifiedDate;

	private Long createdBy;

	private Long modifiedBy;

	private String createdByName;

	private String modifiedByName;

	// @NotNull(message = "103")
	private String entityOrOthers;

}
