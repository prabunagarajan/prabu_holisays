package com.oasys.posasset.dto;

import lombok.Data;

@Data
public class POSAssetResponseDetailDTO {

	private Long id;

	private Long accessoriesId;

	private String accessoriesName;

	private Integer numberOfAccessories;

	private Integer numberOfDevices;

	private Integer approvedDevicesCount;

	private Integer approvedAccessoriesCount;
	private String createdBy;

	private String createdDate;

	private String modifiedBy;

	private String modifiedDate;


}
