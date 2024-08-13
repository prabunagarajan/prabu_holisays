package com.oasys.cabs.requestDTO;

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
	    private Date insuranceDate;
	    private Date taxDate;
	    private Date fcDate;
}
