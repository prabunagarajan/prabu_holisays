package com.oasys.posasset.dto;

import java.io.Serializable;

import com.oasys.posasset.constant.ApprovalStatus;

import lombok.Data;

@Data
public class EalPUtoBWFLResponseDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long id;
	
    private String applicationDate;	
	
	private String requestedapplnNo;	
	
	private ApprovalStatus status;
	
	private String entityName;	
	
	private String entityAddress;	
	
	private String licenseType;	
	
	private String entityType;	
	
	private String licenseNo;	
	
	private String codeType;

	private String createdDate;

	private String modifiedDate;
	
	private EalRequestMapResponseDTO  ealmapResponseDTO;
	
//private String packagingSize;
//	
//	private String cartonSize;	
//	
//	private String noofBarcode;	
//	
//	private String noofQrcode;	
//	
	private String remarks;	
//	
//	private Long ealrequestId;
	
    private String district;
    private String dateOfPackaging;
	private String liquorType;
	private String liquorSubType;
	private String brandName;
	private String requestType;
}
