package com.oasys.helpdesk.entity;

import java.io.Serializable;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.oasys.helpdesk.conf.Trackable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@Table(name = "role_master"/*
							 * , uniqueConstraints = {
							 * 
							 * @UniqueConstraint(columnNames = { "roleCode" }) }
							 */)
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleMaster extends Trackable implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -7159367709179977596L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name = "role_code")
	private String roleCode;

	@Column(name = "role_name")
	private String roleName;
	
	@Column(name = "is_default_role")
	private Boolean defaultRole;
	
	@Column(name = "status")
	private Boolean status;
	
	@Column(name = "is_helpdesk_role")
	private Boolean helpdeskRole;
	
	

}
