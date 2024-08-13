package com.oasys.helpdesk.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)

public class DowntimeDTO {

	private String hrs;
	
	private Integer Hours;
	
}
