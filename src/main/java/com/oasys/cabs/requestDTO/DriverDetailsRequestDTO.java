package com.oasys.cabs.requestDTO;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DriverDetailsRequestDTO {
	
	private Long id;
	private String aadharNumber;
    private String county;
    private String district;
    private String doorNumber;
    private String drivingLicenseNumber;
    private Boolean isPermanentDriver;
    private String mobileNumber;
    private String name;
    private String state;
    private Boolean status;
    private String street;
    private String villageOrCity;

}
