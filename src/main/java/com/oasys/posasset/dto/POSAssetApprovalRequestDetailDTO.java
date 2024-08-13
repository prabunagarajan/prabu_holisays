package com.oasys.posasset.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class POSAssetApprovalRequestDetailDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull(message = "103")
	private Long id;
	
	private Integer approvedAccessoriesCount;
	
	@NotNull(message = "103")
	private Integer approvedDevicesCount;

	
	
}
