package com.oasys.posasset.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oasys.helpdesk.conf.Trackablecdate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@EqualsAndHashCode(callSuper=false)
@Table(name = "device_lost_log")
public class DeviceLostLogEntity extends Trackablecdate{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1876912232063693652L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "designation")
	private String designation;
	
	@Column(name = "action")
	private String action;
	
	@Column(name = "comments")
	private String comments;
	
	
	@Column(name = "action_performedby")
	private String actionPerformedby;
	
//	@CreatedDate
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "created_date")
//	public Date createdDate;
//	
	
	
	@Column(name = "application_no ")
	private String applicationNumber;
	

}
