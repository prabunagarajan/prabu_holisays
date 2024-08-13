package com.oasys.posasset.dto;
import java.io.Serializable;
import java.util.List;

import com.oasys.helpdesk.dto.WorkLocationResponseDTO;
import com.oasys.posasset.constant.ApprovalStatus;
import com.oasys.posasset.entity.EALRequestMapEntity;

import lombok.Data;

@Data
public class DeviceposDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	//private Long id;
	
	
	private String deviceNumber;
	
	private String FPSCode;
	
	private String deviceName;
	
	
	
}
