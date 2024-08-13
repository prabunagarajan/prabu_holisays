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
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@EqualsAndHashCode(callSuper=false)
@Table(name = "accessories_name")
public class Accessories extends Trackable {

	/** 
	 * 
	 */
	private static final long serialVersionUID = -1876912232063693652L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "asset_sub_type")
	private String assetsubType;

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "asset_type_id", referencedColumnName = "id", nullable = false)
	private AssetTypeEntity type;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "asset_name_id", referencedColumnName = "id", nullable = false)
	private DeviceHardwareEntity assetName;
	
	
    @Column(name = "status") 
	private boolean status;

 
}
