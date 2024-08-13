package com.oasys.posasset.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oasys.helpdesk.conf.Trackabledate;
import com.oasys.helpdesk.conf.Trackablewastage;
import com.oasys.posasset.constant.VendorStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@EqualsAndHashCode(callSuper = false)
@Table(name = "dispatch_tp_quantity_pu_bwfl")
public class BwflDispatchRequestEntity  extends Trackablewastage {

	private static final long serialVersionUID = -1876912232063693652L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// newly added.

	@Column(name = "packaging_size")
	private String packagingSize;

	@Column(name = "carton_size")
	private String cartonSize;

	@Column(name = "no_of_barcode")
	private String noofBarcode;

	@Column(name = "no_of_qrcode")
	private String noofQrcode;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "no_barcode_received")
	private Integer noofBarcodereceived;

	@Column(name = "no_qrcode_received")
	private Integer noofQrcodereceived;

	@Column(name = "no_roll_received")
	private Integer noofRollcodereceived;

	@Column(name = "no_barcode_pending")
	private Integer noofBarcodepending;

	@Column(name = "no_qrcode_pending")
	private Integer noofqrpending;

	@Column(name = "eal_request_applnno")
	private String ealrequestapplno;

	@Column(name = "printing_type")
	private String printingType;

	@Column(name = "eal_requestid")
	private Long ealrequestId;

	@Column(name = "unmapped_type")
	private String unmappedType;

	@Column(name = "total_numof_barcode")
	private Integer totalnumofBarcode;

	@Column(name = "total_numof_qrcode")
	private Integer totalnumofQrcode;

	@Column(name = "total_numof_roll")
	private Integer totalnumofRoll;

	@Column(name = "tp_applnno")
	private String tpApplnno;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tp_date")
	private Date tpDate;

	@Column(name = "code_type")
	private String codeType;

	@Column(name = "openstock_applnno")
	private String openstockApplnno;

	@Column(name = "flag")
	private boolean flag;

	@Column(name = "license_no")
	private String licenseNo;

	@Column(name = "no_barcode_damaged")
	private Integer noofBarcodedamaged;

	@Column(name = "no_qrcode_damaged")
	private Integer noofQrcodedamaged;

	@Column(name = "entity_name")
	private String entityName;

	@Column(name = "entity_address")
	private String entityAddress;

	@Column(name = "license_type")
	private String licenseType;

	@Column(name = "entity_type")
	private String entityType;

	@Transient
	private String userRemarks;

	@Column(name = "vendor_status")
	private VendorStatus vendorStatus;

	@Column(name = "map_type")
	private String mapType;

	@Column(name = "no_of_roll")
	private Integer noOfRoll;

	@Column(name = "unit_code")
	private String unitCode;

	// checking

	@Column(name = "vehicle_agency_name")
	private String vehicleAgencyName;

	@Column(name = "vehicle_agency_address")
	private String vehicleAgencyAddress;

	@Column(name = "vehicle_number")
	private String vehicleNumber;

	@Column(name = "driver_name")
	private String driverName;

	@Column(name = "route_type")
	private String routeType;

	@Column(name = "route_details")
	private String routeDetails;

	@Column(name = "major_route")
	private String majorRoute;

	@Column(name = "distance_kms")
	private String distanceKms;

	@Column(name = "valid_upto")
	private String validUptohrs;

	@Column(name = "valid_upto_hrs")
	private String validUpto;

	@Column(name = "digi_lock_id")
	private String digiLockID;

	@Column(name = "GPS_device_id")
	private String gpsDeviceID;

	@Column(name = "tare_weight_Qtls")
	private String tareWeightQtls;

	@Column(name = "gross_weight_Qtls")
	private String grossWeightQtls;

	@Column(name = "net_weight_Qtls")
	private String netWeightQtls;
	

}
