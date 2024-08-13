package com.oasys.posasset.dto;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;

import com.oasys.posasset.constant.ApprovalStatus;
import com.oasys.posasset.constant.VendorStatus;

import lombok.Data;

@Data
public class EalPUtoBWFLRequestDTO implements Serializable{
	
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
	
	private String puEntityName;
	
	private String puEntityAddress;
	
	private String puLicenseType;
	
	private String puLicenseNo;	
	
	private List<String> licenseNumberArray;
	
	private String codeType;	
	
	//private List<EALRequestMapEntity> ealmapRequestDTO;
	
	private List<EalRequestMapResponseDTO>  ealmapRequestDTO;
	
	private String unmappedType;	
	
	private Integer totBarcode;
	
	private Integer totQrcode;
	
	private Long createdBy;
	private Long modifiedBy;
	
	private String userName;
	
	private String action;
	
    private String subModuleNameCode;
	
	private String moduleNameCode;
	
    private String event;
	
	private String level;
	
	private String currentlyWorkwith;
	
	private String fromDate;
	
	private String toDate;
	
	private String remarks;
	
    private String district;
    
    
    private Integer totalnumofBarcode;
	
	private Integer totalnumofQrcode ;
	
	private Integer totalnumofRoll ;
	
	private String stockApplnno ;
	
	private String stockDate ;
	 
	private Integer ealrequestId;
	
	private String licenseNumber;
	
    private String codeTypeValue;
    
     private String startDate;
	
	private String endDate;

	private String openstockApplnno;
	
	private Long ealreqId;
	
	private Boolean flag;
	
	private String printingType;
	
	private boolean forceclosureFlag; 
	
	private Integer noofRoll;
	
	
	private String mapType;
	
	private String unitCode;
	private List<String> unitCodeArray;
	
	private String dateOfPackaging;
	private String liquorType;
	private String liquorSubType;
	private String brandName;
	private String requestType;

}
