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
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oasys.helpdesk.conf.Trackable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@Table(name = "workflow",uniqueConstraints = {
	    @UniqueConstraint(columnNames = {
	            "code"
	        })
})
@EqualsAndHashCode(callSuper=false)
public class WorkflowEntity extends Trackable {

	private static final long serialVersionUID = -1876912232063693652L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sla")
	private SlaMasterEntity slaDetails;
	
	@Column(name = "code")
	private String code;
	
    @Column(name = "is_active") 
	private boolean isActive;
    
    @ManyToOne(fetch = FetchType.LAZY)
  	@JoinColumn(name = "user_group_id", referencedColumnName = "id") 
  	private UserEntity AssignTo;
    
    @Column(name = "priority")
	private String priority;
    
}
