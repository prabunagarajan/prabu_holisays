package com.oasys.posasset.dto;
import java.io.Serializable;
import java.util.Date;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.oasys.helpdesk.entity.AssetAccessoriesEntity;
import com.oasys.helpdesk.entity.AssetTypeEntity;

import lombok.Data;

@Data
public class DevicedamagerequestDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String applicableDate;	
	
	private String deviceDamageapplnno;
	
	private String informHelpdesk;
	
	private String complaintNumber;
	
	private String licenseType;
	
	private String licenseName;
	
	private String shopId;
	
	private String shopName;
	
	private String district;
	
	private String damagedeviceName;
	
	private String deviceName;
	
	private String reason;
	
	private String deviceserialNo;
	
	private Long accessoriesId;

	private Long deviceId;
	
	private String createdbyName;
	
	private String currentlyWorkwith;
	
	private String replyRemarks;
	
	private String uploadPod;
	
	private String podUuid;
	
	private String status;
	
	private String lastsatusDevice;
	
	private String uuid;

	private String licenseNo;

	private String fileurl;
	
    private String designation;
	
	private String action;
	
	private String comments;
	
	private String actionPerformedby;
	
	public String createdDate;
	
	
	///workflow
	
    private String subModuleNameCode;
	
	private String moduleNameCode;
	
	private String event;
	
	private String level;
	
	private String sendBackTo;
	
	private String forwardTo;
	
	private String callbackURL;
	
	
    private String lostDevice;
	
    private String uploadApplication;

    private String applicationUuid;
	
	private String firCopyuuid;
	
	private String uploadFIRCopy;

	private Long createdBy;
	
	
	
	
}
