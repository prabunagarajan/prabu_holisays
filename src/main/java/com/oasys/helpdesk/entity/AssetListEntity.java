package com.oasys.helpdesk.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oasys.helpdesk.conf.Trackable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@EqualsAndHashCode(callSuper=false)
@Table(name = "asset_list")
public class AssetListEntity extends Trackable{
	
	private static final long serialVersionUID = -1876912232063693652L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "serial_no")
	private String serialNo;
	
	@Column(name = "rating")
	private String rating;
	
	@Column(name="date_of_purchase")
	private String dateOfPurchase;
	
	@Column(name="warranty_period")
	private String warrantyPeriod;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="supplier_name",referencedColumnName="id",nullable=false)
	private SupplierEntity supplierName;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="asset_type",referencedColumnName="id",nullable=false)
	private AssetTypeEntity type;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="asset_name",referencedColumnName="id",nullable=false)
	private DeviceHardwareEntity deviceName;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="asset_brand",referencedColumnName="id",nullable=false)
	private AssetBrandEntity brand;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="asset_sub_type",referencedColumnName="id",nullable=false)
	private Accessories assetsubTypeNmae;
	
	@Column(name = "is_active") 
	private boolean isActive;
	
     

}
