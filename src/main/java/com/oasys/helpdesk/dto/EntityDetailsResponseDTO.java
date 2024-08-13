package com.oasys.helpdesk.dto;

import lombok.Data;

@Data
public class EntityDetailsResponseDTO {
	
	private Long id;
	private String entityName;
	private String entityCode;
	private boolean isActive;
	private String createdDate;
	private String modifiedDate;
	private Long createdBy;
	private Long modifiedBy;
	private String createdByName;
	private String modifiedByName;
//	private String createdBy;
//	private String modifiedBy;
	private String created_by;
    private String modified_by;
    public String created_date;
    public String modified_date;
    private String entityOrOthers;
	
	

}
