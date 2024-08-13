package com.oasys.posasset.dto;

import java.util.Optional;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@Service
@ComponentScan
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceRegistrationEntityCountDTO {
	
	private String entityCount;
	private String entityName;

}
