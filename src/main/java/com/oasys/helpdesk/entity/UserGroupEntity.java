package com.oasys.helpdesk.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oasys.helpdesk.conf.Trackable;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity
@Data
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@Table(name = "usergroup_help")
@EqualsAndHashCode(callSuper=false)
public class UserGroupEntity extends Trackable {
	
	private static final long serialVersionUID = -6067075145690853782L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	
	@Column(name = "usergroup_name")
	private String usergroupName;
	
	@Column(name = "role")
	private String role;
	

	@Column(name = "usergroup_code")
	private String usergroupCode;
	
	 @Column(name = "status") 
    private boolean status;
	
}
