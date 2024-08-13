package com.oasys.helpdesk.response;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.oasys.helpdesk.conf.Trackable;

import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)

public class WorkLocation extends Trackable {

    private String licenseApplicationNumber;
    
    private Long licenseApplicationId;
	
    private String unitName;
    
    private String licenseNumber;
    
    private String licenseCategory;
    
    private String licenseSubCategory;
    
    private String districtCode;
    
    private String districtDesc;
    

	private UserResponseDto user;
}