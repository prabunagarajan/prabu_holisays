package com.oasys.posasset.dto;
import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;

import com.oasys.helpdesk.dto.WorkLocationResponseDTO;
import com.oasys.posasset.constant.ApprovalStatus;
import com.oasys.posasset.constant.VendorStatus;
import com.oasys.posasset.entity.EALRequestMapEntity;

import lombok.Data;

@Data
public class EallDeclarationDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String licenseNo;	
	
	private String entityCode;	

	private String bottlingPlanId;	

	private String codeTypeVaue;	

	private String packagingsizeValue;
	
	private String packagingType;
	
	private Integer plannedCases;
	
	private Integer plannedBottles;

	private Integer addlrequestedCases;
	
	private Integer addlrequestedBottles;

	private Integer printedCases;

	private Integer scannedCases;
	
	private Integer scannedBottles;

	
}
