package com.oasys.helpdesk.entity;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oasys.helpdesk.conf.Trackable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@EqualsAndHashCode(callSuper=false)
@Table(name = "supplier_master")
public class SupplierEntity extends Trackable {
	
	private static final long serialVersionUID = -1876912232063693652L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "supplier")
	private String supplier;
	
	@Column(name = "supplier_name")
	private String supplierName;
	
	
	@Column(name = "mobile_number")
	private String mobileNumber;
	
	
	@Column(name = "emailid")
	private String emailId;
	
	@Column(name = "address")
	private String address;
	
	
    @Column(name = "active") 
	private boolean active;

    
}
