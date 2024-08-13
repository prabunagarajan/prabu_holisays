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
public class UserDto implements Serializable{
	

	
    private String page;
	
	private String pageSize;
	
	private String searchInput;
	
    private String userName;
	
	private String password;
	
	private String roleCode;
	
	private String userTypeCode;
	
	private String source;

}
