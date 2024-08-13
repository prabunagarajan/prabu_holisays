package com.oasys.helpdesk.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.validation.constraints.Email;

import lombok.Data;

@Data
public class FeedBackComplaintResponseDTO implements Serializable {

private static final long serialVersionUID = 1L;
	
	private Long id;
	
	
	private String name;
	
	
	private String email;

	
	private String problemDate;
	

	private String retailtypeId;
	
	
	private String retailtypeName;
	
	
	private String retailshopName;
	
	
	
	private String retailshopCode;
	

	private String districtId;
	

	private String districtName;
	
	
	private String complaintcategoryId;
	
	
	private String complaintcategoryNmae;
	

	
	private String complaintDetails;
	


	private String resolvedIssue;
	

	private String supportingDoc;
	
	private String phoneNumber;
	
	
}
