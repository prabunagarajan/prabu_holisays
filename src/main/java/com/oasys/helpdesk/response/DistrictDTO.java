package com.oasys.helpdesk.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DistrictDTO {
	private String code;
	private String name;
	private Boolean isActive;
}
