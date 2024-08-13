package com.oasys.helpdesk.dto;

import java.io.Serializable;
import java.util.Date;

import com.oasys.helpdesk.entity.EntityMaster;

import lombok.Data;

@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserMasterResponseDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7157547015793301184L;

	private Long id;

	private String userName;
	
	private String emailId;
	
	private Long phoneNumber;
	
	private RoleMasterResponseDTO roleMasterResponseDto; 
	
	private EntityMaster entityMaster; 
	
   //private DesignationResponseDto designationResponseDto; 
	
	private Boolean status;
	
	private String userType;
	
	private String createdBy;
	
 	private Date createdDate;
	
	private String modifiedBy;
	
	private Date modifiedDate;
}
