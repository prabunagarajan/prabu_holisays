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
import com.oasys.helpdesk.utility.HelpDeskTicketAction;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@Table(name = "helpdesk_ticket_audit")
@EqualsAndHashCode(callSuper=false)
public class HelpdeskTicketAuditEntity extends Trackable{
	
	
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
	
	@Column(name = "action")
	private HelpDeskTicketAction action;
	
	private String ticketNumber;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ticket_status_id", referencedColumnName = "id", nullable = false) 
	private TicketStatusEntity ticketStatus;
	
    @Column(name="action_performed_date_time")
	private LocalDateTime actionPerformedDateTime;
    
    @Column(name="comments")
 	private String comments;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="assign_to")
	private UserEntity assignTo;
	
    public String getHelpDeskTicketAction() {
		if (Objects.nonNull(action)) {
			return this.action.getType();
		} else {
			return null;
		}
	}
    public HelpdeskTicketAuditEntity() {
    	
    }

	public HelpdeskTicketAuditEntity(UserEntity actionPerformedBy, HelpDeskTicketAction action, String ticketNumber,
			TicketStatusEntity ticketStatus, LocalDateTime actionPerformedDateTime, String comments, UserEntity assignTo) {
		this.actionPerformedBy = actionPerformedBy;
		this.action = action;
		this.ticketNumber = ticketNumber;
		this.ticketStatus = ticketStatus;
		this.comments = comments;
		this.assignTo = assignTo;
	}
    
}
