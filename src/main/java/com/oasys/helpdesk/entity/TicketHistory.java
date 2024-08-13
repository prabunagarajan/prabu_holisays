package com.oasys.helpdesk.entity;

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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oasys.helpdesk.conf.Trackable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@EqualsAndHashCode(callSuper=false)
@Table(name = "help_desk_ticket_history")
public class TicketHistory extends Trackable {



	private static final long serialVersionUID = -1876912232063693652L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ticket_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_hdth_ticket"))
	private Ticket helpDeskTicket;

	@Column(name = "assigned_by") 
	private Long assignedBy;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assigned_group", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_hdth_assigned_group"))
	private Group group;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "closure_priority", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_hdth_ticket_priority"))
	private Priority closurepriority;
	
	@Column(name = "comments") 
	private String comments;
	
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "update_status", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_hdth_update_status"))
	private Status status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "actual_problem_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_hdth_actual_problem"))
	private ActualProblem actualproblem;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "action_taken_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_action_taken"))
	private ActionTaken actionTaken;
	

    
    @Column(name = "is_active") 
   	private boolean isActive;

}