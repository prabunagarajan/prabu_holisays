package com.oasys.helpdesk.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oasys.helpdesk.conf.Trackable;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity
@Data
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@Table(name = "type_of_user",uniqueConstraints = {
	    @UniqueConstraint(columnNames = {
	            "user_code"
	        })
})
@EqualsAndHashCode(callSuper=false)
public class TypeOfUserEntity extends Trackable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6067075145690853782L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_code")
	private String code;
	
	@Column(name = "type_of_user")
	private String typeOfUser;
	
	@Column(name = "status")
	private Boolean status;
	

	
	
}
