package com.oasys.posasset.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import com.oasys.posasset.constant.VendorStatus;

import lombok.Data;

@Data
public class BwflDispatchRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String packagingSize;

	private String cartonSize;

	@NotBlank(message = "103")
	private String noofBarcode;

	private String noofQrcode;

	private String remarks;

	private Long ealrequestId;

	private String unmappedType;

	private Integer noofBarcodereceived;

	private Integer noofQrcodereceived;

	private Integer noofRollcodereceived;

	private Integer noofBarcodepending;

	private Integer noofqrpending;

	private Integer totalnumofBarcode;

	private Integer totalnumofQrcode;

	private Integer totalnumofRoll;

	private String tpApplnno;

	private String tpDate;

	private String codeType;

	private String licencenumber;

	private Integer openstock;

	private boolean flag;

	private Integer noofBarcodeBalance;

	private Integer noofQrcodeBalance;

	private String createdDate;

	private String openstockApplnno;

	private String ealrequestApplnno;

	private Integer noofBarcodedamaged;

	private Integer noofQrcodedamaged;

	private Integer Actualbarcode;

	private Integer Actualqrcode;

	private String modifiedDate;

	private String printingType;

	private boolean openStockFlag;

	private Long createdBy;

	private String entityName;

	private String entityAddress;

	private String licenseType;

	private String entityType;

	private String licenseNo;

	@NotBlank(message = "103")
	private Integer noOfRoll;

	private String userRemarks;

	private VendorStatus vendorStatus;

	private String mapType;

	@NotBlank(message = "103")
	private String vehicleAgencyName;

	@NotBlank(message = "103")
	private String vehicleAgencyAddress;

	@NotBlank(message = "103")
	private String vehicleNumber;

	@NotBlank(message = "103")
	private String driverName;

	@NotBlank(message = "103")
	private String routeType;

	@NotBlank(message = "103")
	private String routeDetails;

	@NotBlank(message = "103")
	private String majorRoute;

	@NotBlank(message = "103")
	private String distanceKms;

	private String validUptohrs;

	private String validUpto;

	private String digiLockID;

	private String gpsDeviceID;

	private String tareWeightQtls;

	private String grossWeightQtls;

	private String netWeightQtls;
	

}
