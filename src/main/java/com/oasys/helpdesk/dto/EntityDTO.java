package com.oasys.helpdesk.dto;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;



import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EntityDTO implements Serializable{
	
    private String userName;
	
	private String password;
	
	private String source;
	
	private String entityType;
	
	private String unitName;
	
	private String unitCode;
	
	
}
