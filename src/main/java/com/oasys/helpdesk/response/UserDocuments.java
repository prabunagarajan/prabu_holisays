package com.oasys.helpdesk.response;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.oasys.helpdesk.conf.Trackable;

import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)

public class UserDocuments extends Trackable {

	private String documentName;
	
	private String name;
	
	private Boolean status;
	
	private String uuid;
	
	
	private UserResponseDto user;

	
}