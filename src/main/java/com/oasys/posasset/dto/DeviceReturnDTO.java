package com.oasys.posasset.dto;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oasys.helpdesk.entity.AssetAccessoriesEntity;
import com.oasys.helpdesk.entity.AssetTypeEntity;
import com.oasys.posasset.constant.ActiveStatus;
import com.oasys.posasset.constant.ApprovalStatus;
import com.oasys.posasset.constant.DeviceStatus;

import lombok.Data;

@Data
public class DeviceReturnDTO {
	
	
	private Long id;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Kolkata")	
	private Date applicationDate;	
	
	@NotNull @NotEmpty (message = "103")
	private String applicationNumber;
	
	private Boolean informHelpdesk;
	
	private String complaintNumber;
	
	private String licenseType;
	
	private String licenseName;
	
	private String shopId;
	
	private String shopName;
	
	private String returnDevice;
	
	private String deviceId;
	
	private String district;
	
	private String deviceName;
	
	private DeviceStatus deviceStatus;
	
	private ApprovalStatus status;
	
	private String reason;
		
	private String licenseNo;	
	
	private Long accessoriesId;
	
	private Long device;
		
	private String uploadDocumentUrl;
	
	private String uploadDocumentUuid;
	
	private String uploadPodUrl;
	
	private String uploadPodUuid;
	
	private String createdbyName;
	 
    private String currentlyWorkwith;
	
    private String remarks;
    
    private Date createdDate;
	
	private Long createdBy;
	
	private Date modifiedDate;
	
	private Long modifiedBy;	
	
    private String subModuleNameCode;
	
	private String moduleNameCode;
	
	private String event;
	
	private String level;
	
	private ActiveStatus active;
	
	private String accessoriestype;
	
	private String deviceaccessoriesName;
	
	

}
