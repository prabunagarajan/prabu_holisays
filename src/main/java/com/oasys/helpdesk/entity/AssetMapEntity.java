package com.oasys.helpdesk.entity;

import java.sql.Date;
import java.time.LocalDate;

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

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@EqualsAndHashCode(callSuper = false)
@Table(name = "asset_map")
public class AssetMapEntity extends Trackable {

	// private static final long serialVersionUID = -1876912232063693652L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "asset_group", referencedColumnName = "id", nullable = false)
	private EntityDetails assetGroup;

	@Column(name = "district")
	private String District;

	@Column(name = "district_id")
	private Long districtId;

	@Column(name = "state")
	private String State;

	@Column(name = "state_id")
	private Long stateId;

	@Column(name = "asset_location")
	private String assetLocation;

	@Column(name = "user_name")
	private String userName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "asset_type", referencedColumnName = "id", nullable = false)
	private AssetTypeEntity assetType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "asset_name", referencedColumnName = "id", nullable = false)
	private DeviceHardwareEntity assetName;
	// private AssetBrandEntity assetName;

	@Column(name = "serial_no")
	private String serialNo;

	@Column(name = "date_of_installation")
	private String dateOfInstallation;

	@Column(name = "is_active")
	private boolean isActive;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status", referencedColumnName = "id", nullable = false)
	private AssetStatusEntity status;

	@Column(name = "user_type")
	private String userType;

	@Column(name = "unit_name")
	private String unitName;

	@Column(name = "licenseno")
	private String licenseNo;

	@Column(name = "designation")
	private String Designation;

	@Column(name = "reason")
	private String reason;

	@Column(name = "unit_code")
	private String unitcode;

}
