package com.oasys.helpdesk.entity;

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


import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@Table(name = "site_observation")
public class SiteObservationEntity extends Trackable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Column(name="observation")
	private String observation;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "isssuetype_id", referencedColumnName = "id")
	private SiteIssueTypeEntity issueType;
	
	@Column(name="is_active")
	private boolean isActive;
	
}
