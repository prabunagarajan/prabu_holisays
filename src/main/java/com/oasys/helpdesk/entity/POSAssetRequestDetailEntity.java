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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oasys.helpdesk.conf.Trackable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@Table(name = "pos_asset_request_details")
@EqualsAndHashCode(callSuper=false)
public class POSAssetRequestDetailEntity extends Trackable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "accessories_id", referencedColumnName = "id", nullable = false)
	private Accessories accessories;
	
	@Column(name = "no_of_accessories")
	private Integer numberOfAccessories;
	
	@Column(name = "no_of_devices")
	private Integer numberOfDevices;
	
	
	@Column(name="no_of_devices_approved")
	private Integer approvedDevicesCount;
	
	@Column(name="no_of_accessories_approved")
	private Integer approvedAccessoriesCount;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "pos_asset_request_id")
	private POSAssetRequestEntity posAssetRequestEntity;
	
}
