package com.oasys.posasset.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oasys.helpdesk.conf.Trackable;
import com.oasys.helpdesk.conf.Trackabledate;
import com.oasys.helpdesk.entity.AssetAccessoriesEntity;
import com.oasys.helpdesk.entity.AssetTypeEntity;
import com.oasys.helpdesk.entity.IssueDetails;
import com.oasys.helpdesk.entity.SubCategory;
import com.oasys.posasset.constant.ApprovalStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@EqualsAndHashCode(callSuper=false)
@Table(name = "device_lost_main")
public class DevicelostEntity extends Trackabledate {
	
	private static final long serialVersionUID = -1876912232063693652L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	//new 
	@Column(name = "applicable_date")
	private String applicableDate;	
	
	@Column(name = "application_no")
	private String applicationNumber;
	
	@Column(name = "inform_helpdesk")
	private String informHelpdesk;
	
	@Column(name = "complaint_no")
	private String complaintNumber;
	
	@Column(name = "license_type")
	private String licenseType;
	
	@Column(name = "license_name")
	private String licenseName;
	
	@Column(name = "shop_id")
	private String shopId;
	
	@Column(name = "shop_name")
	private String shopName;
	
	@Column(name = "district")
	private String district;
	
	@Column(name = "device_name")
	private String deviceName;
	
	@Column(name = "lastsatus_device")
	private String lastsatusDevice;
	
	@Column(name = "reason")
	private String reason;
	
//	@Column(name = "uuid")
//	private String uuid;
//	
//	@Column(name = "file_url")
//	private String fileurl;
	
	@Column(name = "fir_copy_uuid")
	private String firCopyuuid;
	
	@Column(name = "upload_fir_copy")
	private String uploadFIRCopy;
	
	
	@Column(name = "license_no")
	private String licenseNo;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "accessories_id", referencedColumnName = "id", nullable = false)
	private AssetTypeEntity accessoriesId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "device_id", referencedColumnName = "id", nullable = false)
	private AssetAccessoriesEntity deviceId;
	
	
	@Column(name = "createdby_name")
	private String createdbyName;
	
	@Column(name = "currently_work_with")  
    private String currentlyWorkwith;
	
	@Column(name = "reply_to_remarks")  
    private String replyRemarks;
	
	@Column(name = "lost_device")  
    private String lostDevice;
	
	@Column(name = "upload_application")  
    private String uploadApplication;
	
	@Column(name = "application_uuid")  
    private String applicationUuid;
	
	@Column(name = "upload_pod")  
    private String uploadPod;
	

	@Column(name = "pod_uuid")  
    private String podUuid;
	
	@Column(name = "status")  
    private ApprovalStatus status;
	
	@Column(name = "device_serialno")  
    private String deviceserialNo;
	
	
}
