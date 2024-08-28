package com.devar.cabs.requestDTO;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserRequestDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	private long id;
	private String userId;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String encryptedPassword;
	private String emailVerificationToken;
	private Boolean emailVerificationStatus = false;
}
