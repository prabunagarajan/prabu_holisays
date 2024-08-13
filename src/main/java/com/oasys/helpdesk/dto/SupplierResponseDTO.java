package com.oasys.helpdesk.dto;



import java.io.Serializable;
import java.util.Date;
import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SupplierResponseDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long id;
 
	private String supplier;
	
	private Boolean active;
	
	private String supplierName;
	
	private String mobileNumber;
	
	private String emailId;
	
	private String address;
	
	private String createdBy;
	
	private String modifiedBy;
	
	private String createdDate;
	
	private String modifiedDate;
	
    private Long createdById;
	
	private Long modifiedById;
	
	
}
