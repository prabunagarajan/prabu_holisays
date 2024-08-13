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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.oasys.helpdesk.conf.Trackable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@Table(name = "grievance_sla_master")
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GrievanceSlaEntity extends Trackable{
	
	private static final long serialVersionUID = -1876912232063693652L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "code")
	private String code;
	
	@Column(name = "sla")
    private Long sla;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "priority", referencedColumnName = "Id", nullable = false)
    private GrievancePriorityEntity priority;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "issue_details_id",referencedColumnName = "Id", nullable = false)  
//	@Column(name = "issue_details_id")
	private GrievanceIssueDetails gIssueDetails;
	
	
	@Column(name = "is_active")
	private Boolean status;		
	
	 @Column(name = "type_of_user")
	 private String typeofUser;
	 
	 
	
}
