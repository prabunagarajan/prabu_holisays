package com.oasys.posasset.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class placeholderDTO implements Serializable {
	
	
    
    private String licenseNumber;
	
	private String fromDate;
	
	private String toDate;
	
    private String codeTypeValue;
    
    private Long createdBy;
    
    private String  bottlingPlanId;

    
		
}
