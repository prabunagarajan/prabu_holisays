package com.oasys.helpdesk.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserMasterRequestDto implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1918106382614020753L;

	private Long id;

	private String userName;
	
	private String emailId;
	
	private Long phoneNumber;
	
	private String password;
	
	private Boolean status;
	
	private String userType;
	
	private String roleCode;
	
	private Long entityId;
	
	private Long designationId;
	
}
