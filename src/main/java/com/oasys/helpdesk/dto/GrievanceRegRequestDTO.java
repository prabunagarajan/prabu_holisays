package com.oasys.helpdesk.dto;



import java.io.Serializable;
import java.util.Date;
import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class GrievanceRegRequestDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

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
	
	private Long  categoryId;
	
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

	private Long assignto;
	
	private String date;
	
	private String todate;
	
	private Date dates;
	
	private String categoryname;
	
	private String priority;
	
	private String nameinfo;
	
	private String sla;
	
	private String escalated;
	
	private String assigntoName;
	
	private String assignToId;
	
	private String PriorityId;
	
	private String uuid;

	private String filename;

	private boolean flag;

	private Long issuedetailsId;
	
	private Long slaId;
	
	private String docuuid;
	
	private String docfileName;
	 
	private Integer faqId;
	
	private String type;
	
	private String fromDate;
	 
	private String endDate;
	
	private String createdbyName;
	
	private Integer hofficerId;
	
	
	private String updatedBy;
	
	private String rolename;
	 
	
	
	
	
}
