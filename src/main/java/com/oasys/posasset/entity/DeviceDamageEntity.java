package com.oasys.posasset.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oasys.helpdesk.conf.Trackabledate;
import com.oasys.helpdesk.entity.AssetAccessoriesEntity;
import com.oasys.helpdesk.entity.AssetTypeEntity;
import com.oasys.posasset.constant.ApprovalStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@EqualsAndHashCode(callSuper=false)
@Table(name = "device_damage")
public class DeviceDamageEntity extends Trackabledate {
	
	private static final long serialVersionUID = -1876912232063693652L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "applicable_date")
	private String applicableDate;	
	
	@Column(name = "device_damege_appln_no")
	private String deviceDamageapplnno;
	
	@Column(name = "inform_helpdesk")
	private String informHelpdesk;
	
	@Column(name = "license_no")
	private String licenseNo;
	
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
	
	@Column(name = "damege_device_name")
	private String damagedeviceName;
	
	@Column(name = "device_name")
	private String deviceName;
	
	
	@Column(name = "reason")
	private String reason;
	
	@Column(name = "device_serialno")  
    private String deviceserialNo;

	
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
	
	@Column(name = "upload_pod")
	private String uploadPod;

	@Column(name = "pod_uuid")
	private String podUuid;

	@Column(name = "status")
	private ApprovalStatus status;
	
	@Column(name = "upload_application")  
    private String uploadApplication;
	
	@Column(name = "application_uuid")  
    private String applicationUuid;
	
	
}
