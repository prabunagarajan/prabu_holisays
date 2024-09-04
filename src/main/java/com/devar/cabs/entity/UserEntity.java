package com.devar.cabs.entity;


import javax.persistence.Entity;
import javax.persistence.Table;

import com.devar.cabs.common.Trackable;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import lombok.Data;

@Entity
@Data
@Table(name = "dc_users")
public class UserEntity extends Trackable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String phoneNumber;
   
    private String password;
    private String Roll;

}
