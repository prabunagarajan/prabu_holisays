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
@Table(name = "site_issuetype")

public class SiteIssueTypeEntity extends Trackable   {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Column(name="issuetype")
	private String issuetype;
	
	@Column(name="is_active")
	private boolean isActive;
	
	

}
