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
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@EqualsAndHashCode(callSuper=false)
@Table(name = "site_visit")
public class SiteVisitEntity extends Trackable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="entity_type_id",referencedColumnName="id",nullable=false)
	private EntityDetails entityType;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="site_obesrvation_id",referencedColumnName="id",nullable=false)
	private SiteObservationEntity siteObservation;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="siteaction_taken_id",referencedColumnName="id",nullable=false)
	private SiteActionTakenEntity siteActionTaken;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="site_visitstaus_id",referencedColumnName="id",nullable=false)
	private SiteVisitStatusEntity siteVisitStatus;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="site_issuetype_id",referencedColumnName="id",nullable=false)
	private SiteIssueTypeEntity siteIssueType;
	
	@Column(name="shop_code")
	private String shopCode;
	
	@Column(name="entity_name")
	private String entityName;
	
	
	@Column(name="license_name")
	private String licenseName;
	
	@Column(name="license_type")
	private String licenseType;
	
	@Column(name="address")
	private String Address;
	
	@Column(name="salesperson_name")
	private String salespersonName;
	
	@Column(name="contact_no")
	private String contactNo;
	
//	@Column(name="observation_site")
//	private String observationSite;
//	
//	@Column(name="action_taken")
//	private String actionTaken;
//	
//	@Column(name="final_status")
//	private String finalStatus;
	
	@Column(name="pending_reason")
	private String pendingReason;
	
	@Column(name="latitude")
	private String Latitude;
	
	@Column(name="longitude")
	private String Longitude;
	
	@Column(name="district")
	private String District;
	
	@Column(name="is_active")
	private boolean isActive;
	
	@Column(name="image1")
	private String Image1;
	
	@Column(name="image2")
	private String Image2;
	
	@Column(name="uuid1")
	private String Uuid1;
	
	@Column(name="uuid2")
	private String Uuid2;
	
	@Column(name="ticket_number")
	private String ticketNumber;
	
	@Column(name="final_status")
	private String finalStatus;
	
}
