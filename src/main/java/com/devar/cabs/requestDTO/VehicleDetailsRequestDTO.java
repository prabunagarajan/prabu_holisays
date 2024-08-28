package com.devar.cabs.requestDTO;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehicleDetailsRequestDTO {
	
	    private Long id;
	    private String vehicleNumber;
	    private String vehicleName;
	    private String vehicleColor;
	    private Boolean status;
	    private String remarks;
	    private String insuranceDate;
	    private String taxDate;
	    private String fcDate;
	    private String polutionDate;
}
