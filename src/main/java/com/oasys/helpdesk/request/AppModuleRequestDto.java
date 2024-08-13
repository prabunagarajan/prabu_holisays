package com.oasys.helpdesk.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppModuleRequestDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 55826917100853041L;

	private Long id;

	private String code;
	
	private String name;
	
	private String icon;
	
	private Long displayOrder;
	
	private Long parentAppModuleId;
	
	private Boolean active;
//	
//	private Long createdBy;
//	
//	private Long modifiedBy;
	
}
