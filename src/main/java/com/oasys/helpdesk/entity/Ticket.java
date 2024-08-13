package com.oasys.helpdesk.entity;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oasys.helpdesk.conf.Trackable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@EqualsAndHashCode(callSuper=false)
@Table(name = "help_desk_ticket")
public class Ticket extends Trackable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1876912232063693652L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "ticket_number")
	private String ticketNumber;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_hdt_category"))
	private Category categoryId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subcategory_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_hdt_subcategory"))
	private SubCategory subcategoryId;
	
	@Column(name = "entity_id") 
	private Long entityId;
	
	@Column(name = "entity_type_id") 
	private Long entityTypeId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "issue_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_hdt_issue"))
	private IssueDetails issueMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "knowledgebase_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_hdt_knowledgebase"))
	private KnowledgeBase knowledgeBaseId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "group_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_hdt_group"))
	private Group groupId;

    @Column(name = "group_member_id")
	private Long groupMemberid;
    
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sla_configuration_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_hdt_sla_configuration"))
	private SlaConfiguration slaMaster;
    
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "priority_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_hdt_priority"))
	private Priority priorityId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "closure_priority_id", referencedColumnName = "id", nullable = true, foreignKey = @ForeignKey(name = "fk_hdtc_priority"))
	private Priority closurepriorityId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ticket_status_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_hdt_ticket_status"))
	private Status status;
	
	@Column(name = "licence_number")
	private String  licenceNumber;
	
	@Column(name = "inr_number")
	private String  inrNumber;
	
	@Column(name = "address1")
	private String  address1;
	
	@Column(name = "address2")
	private String  address2;
	
	@Column(name = "state")
	private String  state;
	
	@Column(name = "mobile_number")
	private String  mobileNumber;
	
	@Column(name = "email_id")
	private String  emailId;
	
	@Column(name = "query_issue")
	private String  issueName;
	
	@Column(name = "query_knowledge")
	private String  queryKnowledge;
	
	@Column(name = "caller_number")
	private String  callerNumber;
	
	@Column(name = "description")
	private String  description;
	
	@Column(name = "notes")
	private String  notes;
	
	
    @Column(name = "is_active") 
	private boolean isActive;
    
    @CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date")
	public Date createdDate;
    
   
      
   

}
