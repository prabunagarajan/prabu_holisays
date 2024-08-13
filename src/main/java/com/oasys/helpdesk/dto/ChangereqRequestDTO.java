package com.oasys.helpdesk.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.oasys.helpdesk.enums.ApplnStatus;
import com.oasys.helpdesk.enums.ChangereqStatus;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChangereqRequestDTO {
	private Long id;
	private String licenseType;
	private String entityType;
	private String unitName;
	private String licenseNo;
	private String licenseStatus;
	private String address;
	private String mobileNo;
	private String emailId;
	private String shopCode;
	private String shopName;
	private String district;
	private String changereqApplnNo;
	private ApplnStatus applnStatus;
	private ChangereqStatus changereqStatus;
	private String description;
	private String iescmsUrl;
	private String iescmsUuid;
	private String departmentUrl;
	private String departmentUuid;
	private String currentlyWorkwith;
	private String approvedBy;
	private String userName;
	private Long featureId;
	private String subModuleNameCode;
	private String moduleNameCode;
	private String event;
	private String level;
	private String entityName;
	private String raisedBy;
	private String userMobileNumber;
	private String modifiedBy;
	private String createdBy;
	private String action;
	private String remarks;
	private String actionperformedby;
	private String designation;
	private String districtCode;

}
