package com.oasys.helpdesk.response;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.oasys.helpdesk.entity.EntityMaster;
import com.oasys.helpdesk.response.DistrictResponseDto;
import com.oasys.helpdesk.response.TalukResponseDto;
import com.oasys.helpdesk.response.VillageResponseDto;

import lombok.Data;

@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserMasterResponseDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7157547015793301184L;

	private Long id;

	private String userName;
	
	private String emailId;
	
	private String phoneNumber;
	
	
	private EntityMaster entityMaster; 
	
	
	private Boolean status;
	
	private String userType;
	
	private String createdBy;
	
 	private Date createdDate;
	
	private String modifiedBy;
	
	private Date modifiedDate;
	
	private String stateName;
		
	private String districtName;
		
	private String talukName;
		
	private String villageName;
	
	private List<EntityResponseDto> workLocationList;
	
    private StateResponseDto stateResponseDto;
	
	private DistrictResponseDto districtResponseDto;
	
	private TalukResponseDto talukResponseDto;
	
	private VillageResponseDto villageResponseDto;
	
	
}
