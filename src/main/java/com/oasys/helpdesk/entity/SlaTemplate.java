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
@Table(name = "sla_template")
public class SlaTemplate extends Trackable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1876912232063693652L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "template_type")
	private String templateType;

	@Column(name = "template_name")
	private String templateName;
	
	@Column(name = "subject")
	private String subject;
	
	@Column(name = "from_address")
	private String fromAddress;
	
	@Column(name = "cc_address")
	private String ccAddress;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "mobile_number")
	private String mobileNumber;
	
	@Column(name = "message")
	private String message;
	
    @Column(name = "is_active") 
	private boolean isActive;

}
