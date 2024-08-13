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
public class UploadDTO implements Serializable {
	
	  private String userName;
		
	  private String password;
	  
	  private String filename;
	  
	  private String file;
	  
	  private String screename;
	  
	  private String modulename;
	  
	  private String applicationNumber;
	  
	  private String source;
	
		
	

}
