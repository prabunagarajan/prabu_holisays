package com.oasys.posasset.dto;

import java.time.LocalDateTime;
import java.util.Date;

import com.oasys.posasset.constant.VendorStatus;

import lombok.Data;

@Data
public class EalDispatchedDetailsDTO {

	private Date tpDate;
	private String tpApplnno;
	private String packagingSize;
	private String cartonSize;
	private String noOfBarcode;
	private String noOfQrcode;
	private String ealRequestApplnno;
	private Integer noOfRoll;
	
	private Long createdBy;
	private String createdDate;
	private Integer totalnumofBarcode;
	private Integer totalnumofQrcode;
	private Integer totalnumofRoll;
	private String codeType;
	private String tpApplicationNo;
	private Integer ealrequestId;
	private String unmappedType;
	private String modifiedDate;
	private Long modifiedBy;
	private String openstockApplnno;
	private boolean flag;
	private String licencenumber;
	private String remarks;
	private Integer noofBarcodepending;
	private Integer noofBarcodereceived;
	private Integer noofQrcodereceived;
	private Integer noOfRollReceived;
	private Integer noofqrpending;
	private Integer noofBarcodedamaged;
	private Integer noofQrcodedamaged;
	private String printingType;
	private String entityName;
	private String entityAddress;
	private String licenseType;
	private String entityType;
	private VendorStatus vendorStatus;
	private String mapType;
	private String unitCode;
	private Long ealrequestPutoBwflId;

}
