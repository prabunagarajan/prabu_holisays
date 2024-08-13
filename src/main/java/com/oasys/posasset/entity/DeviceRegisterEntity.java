package com.oasys.posasset.entity;

import java.util.Objects;

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
import com.oasys.helpdesk.utility.DeviceRegistrationStatus;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@EqualsAndHashCode(callSuper = false)
@Table(name = "device_registration_details")
//@NoArgsConstructor
//@Audited(withModifiedFlag = true)
public class DeviceRegisterEntity extends Trackable {

	private static final long serialVersionUID = -1876912232063693652L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "android_id")
	private String androidId;

	@Column(name = "android_version")
	private String androidVersion;

	@Column(name = "bluetooth_mac")
	private String BluetoothMac;

	@Column(name = "board")
	private String board;

	@Column(name = "boot_loader")
	private String bootLoader;

	@Column(name = "brand")
	private String brand;

	@Column(name = "cpu_speed")
	private String cpuSpeed;

	@Column(name = "device")
	private String device;

	@Column(name = "device_name")
	private String deviceName;

	@Column(name = "device_number")
	private String deviceNumber;

	@Column(name = "display")
	private String display;

	@Column(name = "fingerprint")
	private String fingerPrint;

	@Column(name = "first_install_time")
	private String firstInstallTime;

	@Column(name = "hardware")
	private String hardware;

	@Column(name = "host")
	private String host;

	@Column(name = "imei_no")
	private String imeiNumber;

	@Column(name = "last_update_time")
	private String lastUpdateTime;

	@Column(name = "manufacturer")
	private String manufacturer;

	@Column(name = "memory")
	private String memory;

	@Column(name = "product")
	private String product;

	@Column(name = "radio")
	private String radio;

	@Column(name = "screen_resolution")
	private String screenResolution;

	@Column(name = "sdk_version")
	private String sdkVersion;

	@Column(name = "serial")
	private String serial;

	@Column(name = "serial_number")
	private String serialNumber;

	@Column(name = "tags")
	private String tags;

	@Column(name = "type")
	private String type;

	@Column(name = "version_code")
	private String versionCode;

	@Column(name = "version_name")
	private String versionName;

	@Column(name = "wifi_mac")
	private String wifiMac;

	@Column(name = "app_type")
	private String appType;

//	@Column(name = "status")
//	private DeviceRegistrationStatus deviceStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status", referencedColumnName = "id", nullable = false)
	private DevicestatusEntity deviceStatus;

	@Column(name = "fps_code")
	private String fpsCode;

	@Column(name = "fps_name")
	private String fpsName;

//	public String getDeviceStatus() {
//		if (Objects.nonNull(this.deviceStatus)) {
//			return this.deviceStatus.getType();
//		} else {
//			return null;
//		}
//	}

	@Column(name = "village")
	private String village;

	@Column(name = "taluk_code")
	private String talukCode;

	@Column(name = "district_code")
	private String districtCode;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "imei_no2")
	private String imeiNumber2;

	@Column(name = "entity")
	private String entity;

	@Column(name = "district_name")
	private String districtName;

	@Column(name = "taluk_name")
	private String talukName;

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
