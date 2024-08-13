package com.oasys.posasset.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oasys.helpdesk.conf.TrackableCreatedDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@EqualsAndHashCode(callSuper=false)
@Table(name = "device_damage_log")
public class DeviceDamageLogEntity extends TrackableCreatedDate {
	
	private static final long serialVersionUID = 5079438398734923506L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "application_number")
	private String applicationNo;
	
	@Column(name = "action")
	private String action;
	
	@Column(name = "comments")
	private String comments;
	
	@Column(name = "user_name")
	private String userName;
	
	@Column(name = "designation")
	private String designation;
	
	@Column(name = "level")
	private String level;
	
	@Column(name = "sub_fees")
	private String sub_fees;
	
    
}
