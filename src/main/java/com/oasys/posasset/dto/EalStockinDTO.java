package com.oasys.posasset.dto;
import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.oasys.helpdesk.dto.WorkLocationResponseDTO;
import com.oasys.posasset.constant.ApprovalStatus;
import com.oasys.posasset.entity.EALRequestMapEntity;

import lombok.Data;

@Data
public class EalStockinDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Long id;

	private Integer ealrequestId;
	
   
	private String unmappedType;
	
	
	
	private Integer totalnumofBarcode;
	
	
	private Integer totalnumofQrcode ;
	
	
	private Integer totalnumofRoll ;
	
	
	private String stockApplnno ;
	
	
	private String stockDate ;
	 
	
	private String codeType ;
    
}
