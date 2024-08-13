package com.oasys.helpdesk.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oasys.helpdesk.conf.Trackable;
import com.oasys.helpdesk.enums.TemplateType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@EqualsAndHashCode(callSuper=false)
@Table(name = "template")
public class TemplateEntity extends Trackable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1876912232063693652L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "code")
	private String code;
	
	@Column(name = "template_id")
	private String templateId;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "type")
	private TemplateType type;
	
	@Column(name = "content")
	private String content;
	
	@Column(name = "status")
	private Boolean status;
	
}
