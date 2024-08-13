package com.oasys.posasset.response;

import java.io.Serializable;
import java.util.Date;
import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class DeviceLostLogResponseDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

    private Long id;
	
	private String designation;
	
	private String action;
	
	private String comments;
	
	private String actionPerformedby;
	
	public String createdDate;
	
	
	
	
}