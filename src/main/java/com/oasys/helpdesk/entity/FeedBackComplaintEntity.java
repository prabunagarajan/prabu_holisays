package com.oasys.helpdesk.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oasys.helpdesk.conf.Trackable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@EqualsAndHashCode(callSuper=false)
@Table(name = "feedback_complaint")
public class FeedBackComplaintEntity extends Trackable {
	private static final long serialVersionUID = -1876912232063693652L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "name") 
	private String name;
	
	@Column(name = "email") 
	private String email;
	
	@Column(name = "phone_number") 
	private String phoneNumber;

	@Column(name = "problem_date") 
	private String problemDate;
	
	@Column(name = "retail_type_id") 
	private String retailtypeId;
	
	@Column(name = "retail_type_name") 
	private String retailtypeName;
	
	@Column(name = "retail_shop_name") 
	private String retailshopName;
	

	@Column(name = "retail_shop_code") 
	private String retailshopCode;
	
	@Column(name = "district_id") 
	private String districtId;
	
	@Column(name = "district_name") 
	private String districtName;
	
	@Column(name = "complaint_category_id") 
	private String complaintcategoryId;
	
	@Column(name = "complaintcategory_name") 
	private String complaintcategoryNmae;
	

	@Column(name = "complaint_details") 
	private String complaintDetails;
	

	@Column(name = "resolved_issue") 
	private String resolvedIssue;
	
	@Column(name = "supporting_doc") 
	private String supportingDoc;

	
	
	
	

}
