package com.oasys.posasset.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class VendorStatusRequestDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long id;
	private String code;
	private String name;
	private boolean isActive;
}
