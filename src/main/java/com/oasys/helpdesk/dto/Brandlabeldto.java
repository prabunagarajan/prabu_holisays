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
public class Brandlabeldto implements Serializable {
	
	private filterdto filters;
	
	private String fromDate;
	
	private String search;
	
	private String sortField;
	
	private String sortOrder;
	
	private String toDate;
	
	private Integer pageNo;
	
	private Integer paginationSize;
	
//	private String userName;
//		
//	private String password;
	private String source;
	


}
