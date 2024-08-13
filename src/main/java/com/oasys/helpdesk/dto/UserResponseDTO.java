package com.oasys.helpdesk.dto;

import java.io.Serializable;
import java.util.List;

import com.oasys.helpdesk.entity.WorkLocationEntity;

import lombok.Data;

@Data
public class UserResponseDTO  implements Serializable{
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
	private String salutationCode;
	private String salutationValue;
	private String address;
	private String employeeId;
	private String employmentStatus;
	private String dateOfJoining;
	private Long roleId;
	private String designationCode;
	private String designationValue;
	private Boolean isDeptAssetApplicable;
	private String deviceId;
	private String createdBy;
	private String createdDate;
	private String modifiedBy;
	private String modifiedDate;
	//private List<WorkLocationResponseDTO> workLocationResponseDTO;
	private List<WorkLocationEntity> workLocationResponseDTO;
	private UserShiftConfigurationResponseDTO shiftConfigurationResponseDTO;
	private BackupUserResponseDTO backupUserResponseDTO;
	private String roleName;
	private String stateCode;
}
