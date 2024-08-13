package com.oasys.helpdesk.dto;

import java.io.Serializable;
import java.util.Date;



import lombok.Data;


@Data
public class AccessoriesListDataRequestDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;

	
	private String accessoriesNameId;
	

	private String accessoriesCode;
	
	
	private String accessoriesName;
	
	
	private String accessoriesSerialNo;

	
	private Integer warranty;
	
	
	private Date registeredDate;

	private Date expiredDate;

	private String accessoriesStatus;

	private boolean status; 
}
