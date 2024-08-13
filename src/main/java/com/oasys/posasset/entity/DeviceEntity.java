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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oasys.helpdesk.conf.Trackable;
import com.oasys.helpdesk.entity.AssetAccessoriesEntity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@EqualsAndHashCode(callSuper = false)
@Table(name = "device")
public class DeviceEntity extends Trackable {
	private static final long serialVersionUID = -1876912232063693652L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "active")
	private boolean active;

	@Column(name = "associated")
	private boolean associated;

	@Column(name = "device_number")
	private String deviceNumber;

	@Column(name = "make")
	private String make;

	@Column(name = "model")
	private String model;

	@Column(name = "serial_number")
	private String serialNumber;

	@Column(name = "sim_id")
	private String simId;

	@Column(name = "last_sync_on")
	private Date lastSyncOn;

//	@Column(name = "printer_id")
//	private Long printerId;
//	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "printer_id", referencedColumnName = "id", nullable = false)
	private AssetAccessoriesEntity printerId;

	@Column(name = "mac_id")
	private String macId;

	@Column(name = "fps_code")
	private String fpsCode;

	@Column(name = "fps_name")
	private String fpsName;

	@Column(name = "sim_id2")
	private String simId2;

	@Column(name = "asset_type")
	private String assetType;

	@Column(name = "entity")
	private String entity;

	@Column(name = "asset_type_id")
	private Long assetTypeId;

	@Column(name = "asset_name_id")
	private Long assetNameId;

	@Column(name = "asset_brand_id")
	private Long assetBrandId;

	@Column(name = "asset_subtype_id")
	private Long assetSubtypeId;

	@Column(name = "supplier_name_id")
	private Long supplierNameId;

	@Column(name = "warranty_period")
	private Long warrantyPeriod;

	@Column(name = "rating")
	private Long rating;

}
