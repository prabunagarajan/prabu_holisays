package com.oasys.helpdesk.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ChangereqCountRequestDTO {

	private List<String> licenseNumber;

	private String fromDate;
	
	private String toDate;
	
	private ArrayList<String> shopCode;
}
