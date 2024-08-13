package com.oasys.posasset.dto;
import java.io.Serializable;

import com.oasys.posasset.constant.ApprovalStatus;

import lombok.Data;

@Data
public class EALUsageDTO implements Serializable {
	
	
	
    private String packagesize;	
	
	private Integer usedbarcode;	
	
	private Integer usedqrcode;	
	
	private Integer wastagebarcode;	
	
	private Integer wastageqrcode;	
	
	private Integer totusewastagebarqr;
	
	

}
