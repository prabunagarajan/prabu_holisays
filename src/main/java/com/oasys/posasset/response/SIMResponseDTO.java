package com.oasys.posasset.response;


import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.oasys.helpdesk.entity.Group;
import com.oasys.helpdesk.entity.KnowledgeBase;
import com.oasys.helpdesk.entity.Category;
import com.oasys.helpdesk.entity.Priority;
import com.oasys.helpdesk.entity.SubCategory;
import com.oasys.helpdesk.entity.SlaConfiguration;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SIMResponseDTO {
	
    private Long id;
	
	private String providerName;
	
	private String Imis;
	
	private Integer associated;
	
	private String number;
	
	private String serialnumber;
	
	private Boolean status;
	
	
	
	private String imis;
	
	private String siName;
	
	private String created_by;

	public String created_date;
	
	//public Date created_date;

	private String modified_by;

	//public Date modified_date;
	
	public String modified_date;

	private String simprovideid;

}
