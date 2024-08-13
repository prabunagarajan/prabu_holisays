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
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oasys.helpdesk.conf.Trackable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@EqualsAndHashCode(callSuper=false)
@Table(name = "device_name")
public class DeviceName extends Trackable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1876912232063693652L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "device_code")
	private String deviceCode;
	
	@Column(name = "device_name")
	private String deviceName;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "brand_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_accessories_brand_id"))
	private AssetBrandEntity assetBrandEntity;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "type_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_accessories_type_id"))
	private AssetTypeEntity assetTypeEntity;
	
	@Column(name = "no_of_device")
	private Integer noofdevice;
	
    @Column(name = "status") 
	private boolean status;

}
