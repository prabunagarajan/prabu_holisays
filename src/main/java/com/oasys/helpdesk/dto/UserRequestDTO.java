package com.oasys.helpdesk.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.oasys.helpdesk.utility.EmploymentStatus;

import lombok.Data;

@Data
public class UserRequestDTO  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private Long id;
	
	@NotBlank(message = "103")
	private String firstName;

	@NotBlank(message = "103")
	private String middleName;

	@NotBlank(message = "103")
	private String lastName;

	@NotBlank(message = "103")
	private String username;

	@NotBlank(message = "103")
	private String emailId;

	@NotBlank(message = "103")
	private String phoneNumber;

	@NotBlank(message = "103")
	private String salutationCode;

	private String address;

	@NotBlank(message = "103")
	private String employeeId;

	@NotNull(message = "103")
	private EmploymentStatus employmentStatus;

	private String dateOfJoining;

	@NotNull(message = "103")
	private Long roleId;

	@NotBlank(message = "103")
	private String designationCode;

	private Boolean isDeptAssetApplicable;

	private String deviceId;

	private Boolean status;

//	@NotNull(message = "103")
//	private List<WorkLocationRequestDTO> workLocationRequestDTO;
//	
	@NotNull(message = "103")
	private List<WorkLocationRequestDTO> workLocationRequestDTO;

	@NotNull(message = "103")
	private UserShiftConfigurationRequestDTO shiftConfigurationRequestDTO;

	private BackupUserRequestDTO backupUserRequestDTO;
	
	private ArrayList<String> districtCode;
	
	private ArrayList<String> districtNames;
	
	
}
