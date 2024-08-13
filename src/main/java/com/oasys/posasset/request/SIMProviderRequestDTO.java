package com.oasys.posasset.request;

import java.io.Serializable;
import java.util.Date;
import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SIMProviderRequestDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String providerName;
	
	private boolean status;
	
	private String iproviderName;
	
	
	

}
