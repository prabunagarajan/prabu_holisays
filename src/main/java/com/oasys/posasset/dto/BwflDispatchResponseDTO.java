package com.oasys.posasset.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class BwflDispatchResponseDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String packagingSize;

	private String cartonSize;

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

	private String stockApplnno;

	private String stockDate;

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

	private String puEntityName;

	private String puEntityAddress;

	private String puLicenseType;

	private String puLicenseNo;

	private Integer noofRoll;

	private Integer noofRollBalance;

	private String mapType;

	private String unitCode;

	private Integer dispatchnoofBarcodereceived;

	private Integer dispatchnoofQrcodereceived;

	private Integer dispatchnoofRollcodereceived;

	private String noofRollCode;

	private String vendorStatus;

	private String vehicleAgencyName;

	private String vehicleAgencyAddress;

	private String vehicleNumber;

	private String driverName;

	private String routeType;

	private String routeDetails;

	private String majorRoute;

	private String distanceKms;

	private String validUptohrs;

	private String validUpto;

	private String digiLockID;

	private String GPSDeviceID;

	private String tareWeightQtls;

	private String grossWeightQtls;

	private String netWeightQtls;

	private String tpApplnno;

}
