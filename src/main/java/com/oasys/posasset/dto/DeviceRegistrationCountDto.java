package com.oasys.posasset.dto;



import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@Service
@ComponentScan
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceRegistrationCountDto {
	
	
	private int totalDevice;
	
	private int mappedDevice;
	
	private int notMappedDevice;
	
	private int deviceRejected;
	
    private int deviceLost;
    
    private int deviceReplace;
    
    private int deviceReturn;
    
   

}
