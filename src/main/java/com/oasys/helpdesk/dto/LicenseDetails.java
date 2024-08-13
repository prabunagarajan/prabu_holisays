package com.oasys.helpdesk.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LicenseDetails implements Serializable{
	
	private String licenseApplicationNumber;
    
    private Long licenseApplicationId;
	
    private String unitName;
    
    private String licenseNumber;
    
    private String licenseCategory;
    
    private String licenseSubCategory;
    
    private String districtCode;
    
    private String districtDesc;

}
