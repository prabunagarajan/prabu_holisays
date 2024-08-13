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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oasys.helpdesk.conf.Trackable;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@EqualsAndHashCode(callSuper=false)
@Table(name = "site_action_taken")
@Data
public class SiteActionTakenEntity extends Trackable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "action_taken")
	private String siteActionTaken;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "observation_id", referencedColumnName = "id")
	private SiteObservationEntity observation;

    @Column(name = "is_active") 
	private boolean isActive;

}
