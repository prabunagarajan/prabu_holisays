package com.oasys.helpdesk.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.oasys.helpdesk.conf.Trackable;
import com.oasys.helpdesk.utility.POSAssetRequestStatus;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@Table(name = "pos_asset_request")
@EqualsAndHashCode(callSuper=false)
public class POSAssetRequestEntity extends Trackable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "application_date")
	private LocalDate applicationDate;
	
	@Column(name = "application_number")
	private String applicationNumber;
	
	@Column(name = "applicant_phone_number")
	private String applicantPhoneNumber;
	
	@Column(name = "applicant_email_id")
	private String applicantEmailId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "asset_type_id", referencedColumnName = "id", nullable = false)
	private AssetTypeEntity assetType;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "asset_brand_id", referencedColumnName = "id", nullable = false)
	private AssetBrandEntity assetBrand;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "device_hardware_id", referencedColumnName = "id", nullable = false)
	private DeviceHardwareEntity deviceHardware;
	
	@Column(name = "remarks")
	private String remarks;
	
	@Column(name = "shop_code")
	private String shopCode;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "posAssetRequestEntity",cascade = CascadeType.ALL,fetch = FetchType.LAZY, orphanRemoval = true)
	private List<POSAssetRequestDetailEntity> assetDetailsList = new ArrayList<>();	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approved_by", referencedColumnName = "id")
	private UserEntity approvedBy;
	
	@Column(name="approval_date")
	private LocalDateTime approvalDate;
	

	@Column(name="status")
	private POSAssetRequestStatus status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assigned_by", referencedColumnName = "id")
	private UserEntity assignBy;
	
	@Column(name="requested_date")
	private LocalDateTime requestDateTime;

	public String getPOSAssetRequestStatus() {
		return this.status.getType();
	}
	

}
