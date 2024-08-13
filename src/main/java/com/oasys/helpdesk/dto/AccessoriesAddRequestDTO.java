package com.oasys.helpdesk.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class AccessoriesAddRequestDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private int noOfDevices;
	
	private String accessoriesName;

}
