package com.oasys.helpdesk.entity;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oasys.helpdesk.conf.Trackable;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity
@Data
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@Table(name = "grievance_register")
@EqualsAndHashCode(callSuper=false)
public class GrievanceregisterEntity extends Trackable {
	
	private static final long serialVersionUID = -6067075145690853782L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name = "grievanceid")
	private String grievanceid;
	
	@Column(name = "appln_date")
	private String appln_date;
	
	@Column(name = "issuefrom")
	private String issuefrom;
	
	@Column(name = "typeofuser")
	private String typeofuser;
	
	@Column(name = "phone_number")
	private String phone_number;
	
	@Column(name = "licence_type")
	private String licencetype;
	
	
	@Column(name = "unit_name")
	private String unitname;
	
	
	@Column(name = "licenece_number")
	private String liceneceNumber;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "department")
	private String department;
	
	@Column(name = "licence_status")
	private String licence_status;
	
	@Column(name = "name_of_complaint")
	private String nameof_complaint;
	
	
	@Column(name = "address_of_complaint")
	private String addressof_complaint;
	
	
	@Column(name = "district")
	private String district;
	
	
	@Column(name = "emailid")
	private String emailid;
	
	@Column(name = "refertic_number")
	private String referticnumber;
	
	
	@Column(name = "grie_category")
	private String grie_category;
	
	
	@Column(name = "uploaddoc")
	private String uploaddoc;
	
	
	@Column(name = "grie_desc")
	private String grie_desc;
	
	@Column(name = "status")
	private Boolean status;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false,foreignKey = @ForeignKey(name = "fk_gr_category_id"))
	private GrievanceCategoryEntity categoryId;
	
	@ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
	@JoinColumn(name = "assignto",referencedColumnName = "id", nullable = false,foreignKey = @ForeignKey(name = "fk_gr_assignto"))
	@NotFound (action=NotFoundAction.IGNORE)
	private GrievanceWorkflowEntity assignto;
	
	@Column(name = "grievance_tc_status")
	private String grievancetcstatus;

	@Column(name = "resolve_grievance")
	private String resolvegrievance;

	@Column(name = "quality_response")
	private String quality_response;

	@Column(name = "grievance_resolved")
	private String grievanceresolved;

	@Column(name = "valuable_input")
	private String valuableinput;
	
	
	@ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
	@JoinColumn(name = "issue_details",referencedColumnName = "id")
	private GrievanceIssueDetails issuedetails;

		
	@Column(name = "resolution_details")
	private String resolutiondetails;

	@Column(name = "resolution_date")
	private String resolutiondate;

	@Column(name = "user_remarks")
	private String userremarks;

	@Column(name = "officer_remarks")
	private String officerremarks;

	@Column(name = "notes")
	private String notes;
	
	@Column(name = "userid")
	private String  userid;
	
	@Column(name = "knowledgebase")
	private String knowledgebase;
	
	@Column(name = "assigngroup")
	private String assigngroup;
	
	
	
	@Column(name = "priority")
	private String priority;
	
	@Column(name = "name_info")
	private String nameinfo;

	@ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
	@JoinColumn(name = "sla",referencedColumnName = "id", nullable = false)
	private GrievanceSlaEntity slaEntity;
	
	@Column(name = "escalated")
	private String escalated;
	
	
	@Column(name = "assignto_id")
	private String assigntoid;
	
	@Column(name = "uuid")
	private String uuid;
	
	@Column(name = "filename")
	private String filename;
	
	 @Column(name = "flag") 
	 private boolean flag;
	 
	 @Column(name = "doc_uuid")
	 private String docuuid;
	 
	 @Column(name = "doc_filename")
	 private String docfilename;
	 
	
	 @Column(name = "faqid")
	 private Integer faqId;
	 
	 
	 @Column(name = "createdby_name")
	 private String createdbyName;
	 
	 @Column(name = "hofficer_id")
	 private Integer hofficerId;
	 
	 @Column(name = "updated_by")
	 private String updatedBy;
	 
	 
	 
	 
}
