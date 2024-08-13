package com.oasys.helpdesk.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oasys.posasset.constant.ApprovalStatus;

import lombok.Data;

@Data
public class EalWastageDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;

	private String financialYear;

	private String applicationDate;

	private String applicationNo;

	private String entityType;

	private String entityName;

	private String licenseType;

	private String licenseNumber;

	private String entityAddress;

	private String bottlingPlanId;

	@JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Kolkata")
	private Date dateOfPackaging;

	private String liquorType;

	private String liquorSubType;

	private String liquorDetailedDescription;

	private String brandName;

	private String packageSize;

	private String codeType;

	private String cartonSize;

	private Integer plannedNumberOfCases;

	private Integer plannedNumberOfBottles;

	private Integer damagedNumberOfBarcode;

	private Integer damagedNumberOfBottleQRCode;

	private String barCodeQRCodeMonoCarton;

	private String barcodeQRCode;

	private String barcodePrintingType;

	private String remarks;

	private ApprovalStatus status;

	private String requestedapplnNo;

	private String userName;

	private String action;

	private String subModuleNameCode;

	private String moduleNameCode;

	private String event;

	private String level;

	private String district;

	private String unmappedType;

	private String mapType;

	private String printingType;

	private Integer usedQrCode;

	private Integer usedBrCode;

	private Integer balanceQrCode;

	private Integer balanceBrCode;

	private Integer bottledQrCode;

	private Integer bottledBrCode;
	
	private Long createdBy;
	
	private Long modifiedBy;

}
