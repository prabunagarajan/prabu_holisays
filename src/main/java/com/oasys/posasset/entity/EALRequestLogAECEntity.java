package com.oasys.posasset.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oasys.helpdesk.conf.Trackabledate;
import com.oasys.helpdesk.conf.Trackablewastage;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@EqualsAndHashCode(callSuper=false)
@Table(name = "eal_request_log_aec")
public class EALRequestLogAECEntity extends Trackablewastage {
	
	private static final long serialVersionUID = -1876912232063693652L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "appln_no")
	private String applnNo;	
	
	@Column(name = "username")
	private String userName;	

	@Column(name = "comments")
	private String comments;	
	
	@Column(name = "action")
	private String action;	

	@Column(name = "remarks")
	private String remarks;	
	

}
