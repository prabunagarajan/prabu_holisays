package com.oasys.helpdesk.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppFeatureRequestDto implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -4253448072353584928L;

	private Long id;

	private String code;
	
	private String name;
	
	private Long displayOrder;
	
	private String appModuleCode;
	
	private Boolean active;
	
	private Long createdBy;
	
	private Long modifiedBy;
	
}
