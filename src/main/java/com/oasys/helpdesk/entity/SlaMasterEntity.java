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
import com.oasys.helpdesk.conf.Trackable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@EqualsAndHashCode(callSuper=false)
@Table(name = "sla_master")
public class SlaMasterEntity extends Trackable{
	private static final long serialVersionUID = -1876912232063693652L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "code")
	private String code;

    @Column(name = "is_active") 
	private boolean isActive;
    
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "issue_details_id",referencedColumnName = "id", nullable = false)
    private IssueDetails issueDetails;
    
    @ManyToOne(fetch = FetchType.LAZY)
 	@JoinColumn(name = "priority_id",referencedColumnName = "id", nullable = false)
    private PriorityMaster priorityMaster;
    
    @Column(name = "sla")
    private Long sla;

	public SlaMasterEntity(Long id, String code, boolean isActive, IssueDetails issueDetails,
			PriorityMaster priorityMaster, Long sla) {
		super();
		this.id = id;
		this.code = code;
		this.isActive = isActive;
		this.issueDetails = issueDetails;
		this.priorityMaster = priorityMaster;
		this.sla = sla;
	}
	public SlaMasterEntity() {
		
	}
	
}
