package com.oasys.helpdesk.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.oasys.helpdesk.utility.EmploymentStatus;

import lombok.Data;

@Data
public class UserFieldDTO  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
   private Long roleId;
   
   private String fromDate;

	
	
	
}
