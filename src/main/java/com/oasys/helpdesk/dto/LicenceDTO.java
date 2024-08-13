package com.oasys.helpdesk.dto;

import java.io.Serializable;
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
public class LicenceDTO implements Serializable{
	
	private LicenceSearchDTO searchInput;
	
	private Integer page;
	
	private Integer pageSize;
	
	private String userName;
	
	private String password;

}


