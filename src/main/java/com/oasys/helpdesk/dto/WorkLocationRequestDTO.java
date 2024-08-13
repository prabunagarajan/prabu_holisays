package com.oasys.helpdesk.dto;

import java.io.Serializable;
import java.util.ArrayList;

import javax.validation.constraints.NotBlank;

import com.oasys.helpdesk.utility.UpdateReason;

import lombok.Data;

@Data
public class WorkLocationRequestDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	@NotBlank(message = "103")
	private String departmentCode;
	
	@NotBlank(message = "103")
	private String entityTypeCode;
	
	@NotBlank(message = "103")
	private String districtCode;
	
	private UpdateReason updateReason;
	
	@NotBlank(message = "103")
	private String stateCode;
	
//   private String[] districtCode;
//	
	private String districtNames;
}
