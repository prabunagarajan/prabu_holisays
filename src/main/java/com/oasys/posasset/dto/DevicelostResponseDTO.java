package com.oasys.posasset.dto;
import java.io.Serializable;

import com.oasys.posasset.constant.ApprovalStatus;

import lombok.Data;

@Data
public class DevicelostResponseDTO implements Serializable {
	
	
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
	
	
	private String createdDate;

	private String modifiedDate;
	
	private String accesoriesName;
	
	private String device;
	
   private String replyRemarks;
	
    private String lostDevice;
	
    private String uploadApplication;

    private String applicationUuid;
	
    private String uploadPod;
	
    private String podUuid;
   
	private String firCopyuuid;
	
	private String uploadFIRCopy;
	
	private ApprovalStatus status;
	
	
	private String deviceserialNo;
	

}
