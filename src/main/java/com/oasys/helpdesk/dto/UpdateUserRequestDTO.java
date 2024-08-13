package com.oasys.helpdesk.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.oasys.helpdesk.utility.EmploymentStatus;

import lombok.Data;

@Data
public class UpdateUserRequestDTO  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull(message = "103")
	private Long id;
	
	@NotNull(message = "103")
	private List<WorkLocationRequestDTO> workLocationRequestDTO;

	@NotNull(message = "103")
	private UserShiftConfigurationRequestDTO shiftConfigurationRequestDTO;

	private BackupUserRequestDTO backupUserRequestDTO;
	@NotNull(message = "103")
	private String username;
	
	//@NotNull(message = "103")
	private String address;
	
	//@NotNull(message = "103")
	private String deviceId;
	
	private String emailid;
	
	private String firstName;
	
	private String middleName;
	
	private String lastName;
	
	private String phoneNumber;
	
	private String designationCode;
	
	private Long roleId;
	
	private EmploymentStatus employmentStatus;
	
}
