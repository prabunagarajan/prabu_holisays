package com.oasys.helpdesk.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DistrictMasterResponse {
	private Integer responseCode;
	private String responseMessage;
	private List<DistrictDTO> content;
}
