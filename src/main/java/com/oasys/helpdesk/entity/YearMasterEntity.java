package com.oasys.helpdesk.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.oasys.helpdesk.conf.Trackable;

import lombok.Data;

@Entity
@Data
@Table(name = "year_master")
public class YearMasterEntity extends Trackable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Column(name="year_code")
	private String yearCode;
	
	@Column(name="is_active")
	private boolean isActive;
	
}
