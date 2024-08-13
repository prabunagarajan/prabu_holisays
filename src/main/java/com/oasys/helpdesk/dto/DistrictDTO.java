package com.oasys.helpdesk.dto;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class DistrictDTO  {

	
	private ArrayList<String> districtcode;
	
}
