package com.oasys.helpdesk.response;

import java.sql.Blob;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
//import com.oasys.app.entity.UserAddress;
import com.oasys.helpdesk.conf.Trackable;
import com.oasys.helpdesk.entity.RoleMaster;

import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)

public class UserResponseDto  extends Trackable{
 
	
	private static final long serialVersionUID = 8435364849405792857L;

	private Long id;
    private String password; // not coming from screen
	
	private String niveshMitraRefNumber;
	
	private String salutation;
	
	private String firstName;	
	
	private String middleName;	
	
	private String lastName;
	
    private LocalDate dob;
	
	private String genderCode;
	
	private String genderDesc;
	
	private Long aadharNumber;
	
	private String email;
	
	private String mobileNumber;
	
	private String panNumber;
	
	private String applicantFatherHusbandName;
	
    private Boolean accountNonLocked =  true; //not coming from screen
    
    private Blob photo;
    
    @Transient
    private byte[] photoImg;
    
    //new fields as per the business user screen
   	private String username;
       
   	private Boolean isCustomer = false;//not coming from screen
   	
   	private Boolean status = true;
  
	private Long designationId;
	
    private List<RoleMaster> roles = new ArrayList<>();
   	
    private UserAddress userAddress;
    
	private List<UserDocuments> userDocumentList;
	
	private Designation designation;
	
	private List<AllocatedEntity> allocatedEntityList;
	
	private List<AllocatedDistrict> allocatedDistrictList;
	 
	private List<WorkLocation> allocatedWorkLocationList;
//	private List<LabelDetailsResponseDto> labeldetails ; 
	
	
	
}
