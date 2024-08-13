package com.oasys.helpdesk.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oasys.helpdesk.conf.PaymentTrackable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@EqualsAndHashCode(callSuper=false)
@Table(name = "ticket")
public class CreateTicketEntitypayment extends PaymentTrackable {
	private static final long serialVersionUID = -1876912232063693652L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "ticket_number")
	private String ticketNumber;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
	private Category category;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subcategory_id", referencedColumnName = "id", nullable = false)
	private SubCategory subCategory;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "issue_details_id", referencedColumnName = "id", nullable = false)
	private IssueDetails issueMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "priority_id", referencedColumnName = "id", nullable = false)
	private PriorityMaster priority;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ticket_status_id", referencedColumnName = "id", nullable = false) 
	private TicketStatusEntity ticketStatus;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "issue_from_id", referencedColumnName = "id", nullable = false) 
	private IssueFromEntity issueFrom;
	
	@Column(name ="entity_type_id")
	private String entityTypeId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sla_id", referencedColumnName = "id", nullable = false) 
	private SlaMasterEntity slaMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
  	@JoinColumn(name = "assign_group_id", referencedColumnName = "id")
	 private RoleMaster assignGroup;
	
    @Column(name = "is_active") 
	private boolean isActive;
    
    @Column(name = "remarks") 
    private String remarks;
    
    @ManyToOne(fetch = FetchType.LAZY)
  	@JoinColumn(name = "assign_to", referencedColumnName = "id")
    private UserEntity assignTo;
    
    @Column(name = "licence_number")
	private String  licenceNumber;
	
	@Column(name = "licence_type_id")
	private String  licenceTypeId;
    
    @Column(name = "call_disconnect") 
    private String callDisconnect;
    
    @Column(name = "required_field") 
    private String requiredField;
    
    @ManyToOne(fetch = FetchType.LAZY)
  	@JoinColumn(name = "knowledge_base_id", referencedColumnName = "id")
    private KnowledgeBase knowledgeBase;
    
    @ManyToOne(fetch = FetchType.LAZY)
  	@JoinColumn(name = "problem_reported_id", referencedColumnName = "id")
    private ProblemReported problemReportedMaster;
    
    @ManyToOne(fetch = FetchType.LAZY)
  	@JoinColumn(name = "action_taken_id", referencedColumnName = "id")
    private ActionTaken actionTakenMaster;
    
    @ManyToOne(fetch = FetchType.LAZY)
  	@JoinColumn(name = "actual_problem_id", referencedColumnName = "id")
    private ActualProblem actualProblemMaster;
    
    @ManyToOne(fetch = FetchType.LAZY)
  	@JoinColumn(name = "solution_id", referencedColumnName = "id")
    private KnowledgeSolution knowledgeSolution;
    
    @Column(name = "email") 
    private String email;
    
    @Column(name = "mobile") 
    private String mobile;
    
    @Column(name = "unit_name") 
    private String unitName;
    
    @Column(name = "license_status") 
    private String licenseStatus;
    
    @Column(name = "notes") 
    private String notes;
    
    @Column(name = "flag") 
	private boolean flag;
    
    @Column(name = "alternative_mobile_number") 
    private String alternativemobileNumber;
    
    @Column(name = "address") 
    private String address;
    
    @Column(name = "createdby_name") 
    private String createdbyName;
    
    @Column(name = "user_remarks")  
    private String userRemarks;
    
    @Column(name = "district")  
    private String district;
    
    @Column(name = "shop_code")  
    private String shopCode;
    
    @Column(name = "shop_name ")  
    private String shopName;
    
    
    @Column(name = "district_code ")  
    private String districtCode;
    
    @Column(name = "view_status") 
  	private boolean viewStatus;
    
    @Column(name = "tehsil_name ")  
    private String tehsilName;
    
    @Column(name = "tehsil_code ")  
    private String tehsilCode;
    
    
    @Column(name = "upload_application ")  
    private String uploadApplication;
    
    @Column(name = "application_uuid ")  
    private String applicationUuid;
    
    @Column(name = "image_url")  
    private String imageUrl;
    
    @Column(name = "image_uuid")  
    private String imageUuid; 
    
    @Column(name = "unit_code")  
    private String unitCode; 
    
    @Column(name = "search_option") 
    private String searchOption;
    
    @Column(name = "raised_by") 
    private String raisedBy;
    
    @Column(name = "duration") 
    private String duration;
    
    @Column(name="issue_type")
    private String issueTypeSH;
    
}
