package com.oasys.posasset.dto;
import java.io.Serializable;

import com.oasys.posasset.constant.ApprovalStatus;

import lombok.Data;

@Data
public class EALAvailableDTO implements Serializable {
	
	
	
    private String packagesize;	
	
	private Integer totbarcode;	
	
	private Integer totqrcode;	
	

}
