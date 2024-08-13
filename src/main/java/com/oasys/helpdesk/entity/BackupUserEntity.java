package com.oasys.helpdesk.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "backup_user")
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BackupUserEntity extends Trackable{
	private static final long serialVersionUID = 8456161920720838558L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "role_id")
	private Long roleId;
	
	@Column(name = "designation_code")
	private String designationCode;
	
	@Column(name = "emp_code")
	private String employeeCode;
	
	@Column(name = "backup_user_id")
	private Long backupUserId;
	
	@JsonBackReference
	@OneToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;
}
