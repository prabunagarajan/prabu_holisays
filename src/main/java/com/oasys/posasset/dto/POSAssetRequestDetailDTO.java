package com.oasys.posasset.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class POSAssetRequestDetailDTO implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long accessoriesId;
	private Integer numberOfAccessories;
	private Integer numberOfDevices;
}
