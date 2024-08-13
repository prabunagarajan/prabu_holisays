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
@Table(name = "call_disconnected_tickets")
public class CallDisconnectedTickets extends Trackable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1876912232063693652L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "ticket_number")
	private String ticketNumber;
	
	@Column(name = "caller_number")
	private String  callerNumber;
	
	@Column(name = "description")
	private String  description;
	
    @Column(name = "is_active") 
	private boolean isActive;
    
    @CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date")
	public Date createdDate;
    
   
      
   

}
