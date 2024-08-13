package com.oasys.helpdesk.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.oasys.helpdesk.conf.Trackable;

import java.time.LocalDateTime;




import lombok.Data;

@Entity
@Data
@Table(name="entity_details")
public class EntityDetails extends Trackable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Column(name="entity_name")
	private String entityName;
	
	@Column(name="entity_code")
	private String entityCode;
	
	@Column(name="is_active")
	private boolean isActive;
	
	@Column(name="entity_or_others")
	private String entityOrOthers;
	


}
