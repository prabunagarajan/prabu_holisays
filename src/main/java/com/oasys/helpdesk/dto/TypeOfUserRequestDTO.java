package com.oasys.helpdesk.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class TypeOfUserRequestDTO implements Serializable {

private static final long serialVersionUID = 1L;
	
	private Long id;
	
	@NotBlank(message = "103")
	private String code;
	
	@NotBlank(message = "103")
	private String typeOfUser;
	
	@NotNull(message = "103")
	private Boolean status;
}
