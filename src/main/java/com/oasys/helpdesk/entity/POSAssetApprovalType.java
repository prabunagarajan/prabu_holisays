package com.oasys.helpdesk.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oasys.helpdesk.conf.Trackable;
import com.oasys.helpdesk.utility.ApprovalType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@Table(name = "pos_asset_approval_type")
@EqualsAndHashCode(callSuper=false)
public class POSAssetApprovalType extends Trackable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "approval_type")
	private ApprovalType approvalType;
	
	@Column(name = "code")
	private String code;
	
	@Column(name = "status")
	private Boolean status;
	
	public String getApprovalType() {
		return this.approvalType.getType();
	}
	
}
