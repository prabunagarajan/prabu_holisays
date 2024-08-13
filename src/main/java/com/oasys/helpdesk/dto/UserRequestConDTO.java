package com.oasys.helpdesk.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.oasys.helpdesk.utility.EmploymentStatus;

import lombok.Data;

@Data
public class UserRequestConDTO  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	
	
	private String firstName;


	private String middleName;

	
	private String lastName;

	
	private String username;


	private String emailId;

	
	private String phoneNumber;


	private String employeeId;
	

    private String usernameMiddlename;


	private Long assigntoId;


}
