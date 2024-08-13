package com.oasys.posasset.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class POSAssetRequestDTO implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	private Long id;

	@NotNull(message = "103")
	private String applicationDate;

	@NotBlank(message = "103")
	private String applicationNumber;

	@NotBlank(message = "103")
	private String applicantPhoneNumber;

	@NotBlank(message = "103")
	private String applicantEmailId;

	@NotNull(message = "103")
	private Long assetTypeId;

	@NotNull(message = "103")
	private Long assetBrandId;

	@NotNull(message = "103")
	private Long deviceHardwareId;

	@NotNull(message = "103")
	private List<POSAssetRequestDetailDTO> assetDetailsList;

}
