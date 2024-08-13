package com.oasys.helpdesk.dto;

import java.io.Serializable;
import java.util.Date;
import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UsergroupRequestDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long id;
	
	
	private String usergroup_name;
	
	private String role;
	
	private String usergroup_code;
	
	private String created_by;

	public String created_date;

	private String modified_by;

	public String modified_date;

	private String update_by;

	public Date update_date;
	
	private boolean status;
}
