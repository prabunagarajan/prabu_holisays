package com.oasys.helpdesk.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.oasys.helpdesk.conf.Trackable;

import lombok.Data;

@Entity
@Data
@Table(name = "inbound_calls")
public class InboundCallsEntity extends Trackable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Column(name="start_time")
	private String startTime;

	@Column(name="end_time")
	private String endTime;
	
	@Column(name="total_calls_received")
	private Long totalCallsReceived;
	
	@Column(name="total_calls_attended")
	private Long totalCallsAttended;
	
	@Column(name="total_calls_abondoned")
	private Long totalCallsAbondoned;
	
	@Column(name="calls_attended_percentage")
	private float callsAttendedPercentage;
}
