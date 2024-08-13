package com.oasys.posasset.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oasys.helpdesk.conf.Trackable;
import com.oasys.helpdesk.entity.AssetAccessoriesEntity;
import com.oasys.helpdesk.entity.AssetTypeEntity;
import com.oasys.posasset.constant.ActiveStatus;
import com.oasys.posasset.constant.ApprovalStatus;
import com.oasys.posasset.constant.DeviceStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@EqualsAndHashCode(callSuper=false)
@Table(name = "device_return_config")
public class DeviceReturnEntity extends Trackable {
	
	private static final long serialVersionUID = 2209004471409922799L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "application_date")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Kolkata")	
	private Date applicationDate;	
	
	@Column(name = "application_no")
	private String applicationNumber;
	
	@Column(name = "inform_helpdesk")
	private Boolean informHelpdesk;
	
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
	
	@Column(name = "return_device")
	private String returnDevice;
	
	@Column(name = "device")
	private String deviceId;
	
	@Column(name = "district")
	private String district;
	
	@Column(name = "device_name")
	private String deviceName;
	
	@Column(name = "device_status")
	private DeviceStatus deviceStatus;
	
	@Column(name = "status")
	private ApprovalStatus status;
	
	@Column(name = "reason")
	private String reason;
		
	@Column(name = "license_no")
	private String licenseNo;	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "accessories_id", referencedColumnName = "id", nullable = false)
	private AssetTypeEntity accessoriesId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "device_id", referencedColumnName = "id", nullable = false)
	private AssetAccessoriesEntity device;
		
	@Column(name = "upload_document_url")
	private String uploadDocumentUrl;
	
	@Column(name = "upload_document_uuid")
	private String uploadDocumentUuid;
	
	@Column(name = "upload_pod_url")
	private String uploadPodUrl;
	
	@Column(name = "upload_pod_uuid")
	private String uploadPodUuid;
	
	@Column(name = "createdby_name")
	private String createdbyName;
	
	@Column(name = "currently_work_with")  
    private String currentlyWorkwith;
	
	@Column(name = "remarks")  
    private String remarks;
	
	@Column(name = "active")  
    private ActiveStatus active ;
	

}
