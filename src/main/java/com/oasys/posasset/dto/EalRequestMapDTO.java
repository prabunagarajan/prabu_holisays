package com.oasys.posasset.dto;
import java.io.Serializable;
import java.util.Date;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.oasys.helpdesk.entity.AssetAccessoriesEntity;
import com.oasys.helpdesk.entity.AssetTypeEntity;
import com.oasys.posasset.constant.ApprovalStatus;
import com.oasys.posasset.entity.EALRequestEntity;

import lombok.Data;

@Data
public class EalRequestMapDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String packagingSize;
	
	private String cartonSize;	
	
	private String noofBarcode;	
	
	private String noofQrcode;	
	
	private String remarks;	
	
	private Long ealrequestId;
	
	private String unmappedType;
	
	
	
}
