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
public class Devicelostrequestdto implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Long id;
	


	private String applicableDate;	

	private String applicationNumber;
	
	private String informHelpdesk;

	private String complaintNumber;
	
	private String licenseType;
	
	private String licenseName;

	private String shopId;

	private String shopName;
	
	private String district;
	
	private String deviceName;
	
	private String lastsatusDevice;
	
	private String reason;
	
	private String uuid;

	private Long accessoriesId;

	private Long deviceId;

	private String licenseNo;
	
	private String createdbyName;

	private String fileurl;
	
   private String designation;
	
	private String action;
	
	private String comments;
	
	private String actionPerformedby;
	
	public String createdDate;
	
	
	private String currentlyWorkwith;
	
	///workflow
	
    private String subModuleNameCode;
	
	private String moduleNameCode;
	
	private String event;
	
	private String level;
	
	private String sendBackTo;
	
	private String forwardTo;
	
	private String callbackURL;
	
	
    private String replyRemarks;
	
    private String lostDevice;
	
    private String uploadApplication;

    private String applicationUuid;
	
    private String uploadPod;
	
    private String podUuid;
   
	private String firCopyuuid;
	
	private String uploadFIRCopy;
	
	private String status;
	
	private String deviceserialNo;
	
	private Long createdBy;
	
	
	
	
}
