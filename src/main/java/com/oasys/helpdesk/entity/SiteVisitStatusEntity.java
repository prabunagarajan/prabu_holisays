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
@Table(name = "site_visit_status")
public class SiteVisitStatusEntity extends Trackable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Column(name="code")
	private String code;
	
	@Column(name="name")
	private String name;
	
	@Column(name="status")
	private boolean status;
	
	

}
