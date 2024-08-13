package com.oasys.helpdesk.entity;

import java.time.LocalDateTime;
import java.util.Objects;

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
import com.oasys.helpdesk.utility.EmploymentStatus;
import com.oasys.helpdesk.utility.HelpdeskUserAuditAction;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@Table(name = "helpdesk_user_audit")
@EqualsAndHashCode(callSuper=false)
public class HelpdeskUserAuditEntity extends Trackable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="action_performed_by")
	private UserEntity actionPerformedBy;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="action_performed_on")
	private UserEntity actionPerformedOn;
	
	@Column(name = "action")
	private HelpdeskUserAuditAction action;
	
	@Column(name = "employment_status")
	private EmploymentStatus employmentStatus;
	
    @Column(name="action_performed_date_time")
	private LocalDateTime actionPerformedDateTime;
    
    public String getHelpdeskUserAuditAction() {
		if (Objects.nonNull(action)) {
			return this.action.getType();
		} else {
			return null;
		}
	}
    public String getEmploymentStatus() {
		if (Objects.nonNull(employmentStatus)) {
			return this.employmentStatus.getType();
		} else {
			return null;
		}
	}
    public HelpdeskUserAuditEntity() {}
	public HelpdeskUserAuditEntity(UserEntity actionPerformedBy, UserEntity actionPerformedOn,
			HelpdeskUserAuditAction action, EmploymentStatus employmentStatus, LocalDateTime actionPerformedDateTime
			) {
		this.actionPerformedBy = actionPerformedBy;
		this.actionPerformedOn = actionPerformedOn;
		this.action = action;
		this.employmentStatus = employmentStatus;
		this.actionPerformedDateTime = actionPerformedDateTime;
		
	}  	
    
}
