package com.oasys.helpdesk.entity;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oasys.helpdesk.conf.Trackable;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity
@Data
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@Table(name = "ticket_status_help")
@EqualsAndHashCode(callSuper=false)
public class TicketStatusEntity extends Trackable {
	
	
	private static final long serialVersionUID = -6067075145690853782L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	
	@Column(name = "ticketstatusname")
	private String ticketstatusname;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "ticketstatus_code")
	private String ticketstatusCode;
	


}
