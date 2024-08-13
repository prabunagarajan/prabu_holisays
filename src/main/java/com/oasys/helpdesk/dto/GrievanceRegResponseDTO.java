package com.oasys.helpdesk.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class GrievanceRegResponseDTO {

	
	private Long id;

	private String grievanceId;

	private String applnDate;

	private String issueFrom;

	private String typeofUser;

	private String phoneNumber;

	private String licenceType;

	private String unitName;

	private String liceneceNumber;

	private String userName;

	private String department;

	private String licenceStatus;

	private String nameofComplaint;

	private String addressofComplaint;

	private String district;

	private String emailId;

	private String referticNumber;

	private String grieCategory;

	private String uploadDoc;

	private String grieDesc;

	private Boolean status;

	private String created_by;

	public String created_date;

	private String modified_by;

	public String modified_date;
	
	
	
    private String grievancetcStatus;
	
	private String resolvegrievance;
	
	private String qualityresponse;
	
	private String grievanceresolved;
	
	private String valuableinput;
	
    private String issuedetails;
	
	private String resolutiondetails;
	
	private String resolutiondate;
	
	private String userremarks;
	
	private String officerremarks;
	
	private String notes;
	
    private String userid;
	
	private String knowledgebase;
	
	private String assigngroup;
	
	private String assigngroupId;
	
	private String date;
	
	private String categoryname;
	
	private String priority;
	
	private String nameinfo;
	
	private String sla;
	
	private String escalated;
	
	private String assignto;
	
	
	private String assigntoName;
	
	private String  categoryId;
	
	private String assignToId;
	
	private String PriorityId;
	
	private String uuid;

	private String filename;

	private boolean flag;
	
	private Long issuedetailsId;
	
	private Long slaId;
	
	private String docfileName;
	
	private String docuuid;
	
	private Integer faqId;
	
	private String createdbyName;
	 
	private Integer hofficerId;
	
	private String updatedBy; 
	
	
	 

}
