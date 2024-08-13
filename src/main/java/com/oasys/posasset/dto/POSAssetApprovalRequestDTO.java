package com.oasys.posasset.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class POSAssetApprovalRequestDTO  implements Serializable{
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@NotNull(message = "103")
	private Long id;
	
	private String remarks;
	
	@NotNull(message = "103")
	private List<POSAssetApprovalRequestDetailDTO> assetApprovalDetailsList;
}
