package com.oasys.helpdesk.dto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssociateresponseDTO implements Serializable {
	

	private Long id;
	
	private Long associatedUserId;
	
	private Long user;
	
	private String associateduserName;
	
	private String userName;
	
	
    public String createdDate;
	
	
	public Long createdBy;
	
	
	public String modifiedDate;

	public Long modifiedBy;
	

	
	

}
