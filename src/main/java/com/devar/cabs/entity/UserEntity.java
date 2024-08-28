package com.devar.cabs.entity;


import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;

import lombok.Data;

@Entity
@Data
@Table(name = "dc_users")
public class UserEntity {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String phoneNumber;
    private String password;

}
