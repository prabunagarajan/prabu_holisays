package com.oasys.helpdesk.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class holderdto implements Serializable {
	
	
    private String fromEntityCode;
	
	private String fromDate;
	
	private String toDate;
	
   private String indentNumber;
		
   private String toEntityCode;
		
   private String status;
   
   private String indentRequestId;
	  
	

   
   
}
