package com.oasys.helpdesk.dto;

import java.io.Serializable;
import java.util.Date;
import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class DepartmentRequestDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	@NotBlank(message = "103")
	private String department;

	@NotBlank(message = "103")
	private String status;

	@NotBlank(message = "103")
	private String departmentCode;

	private String created_by;

	public Date created_date;

	private String modified_by;

	public Date modified_date;

	private String update_by;

	public Date update_date;

}
