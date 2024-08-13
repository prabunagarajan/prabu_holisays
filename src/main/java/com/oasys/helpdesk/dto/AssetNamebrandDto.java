package com.oasys.helpdesk.dto;

import java.sql.Date;
import java.util.List;

import lombok.Data;

@Data
public class AssetNamebrandDto {
	
	private List<DeviceHardwareNameRequestDTO> AssetName;
	
	private List<AssetBrandRequestDTO>AssetBrand;
}
