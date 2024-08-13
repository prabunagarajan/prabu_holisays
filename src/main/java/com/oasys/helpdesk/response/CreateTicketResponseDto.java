package com.oasys.helpdesk.response;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.oasys.helpdesk.dto.EntityMasterDTO;
import com.oasys.helpdesk.dto.EntityTypeMasterDTO;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateTicketResponseDto {
	private Long id;

	private String ticketNumber;

	private String callDisconnect;

	private String requiredField;

	private String categoryName;

	private String subCategoryName;

	private Long categoryId;

	private Long subCategoryId;

	private String issueFrom;

	private Long issueFromId;

	private String issueDetails;

	private Long issueDetailsId;

	private String priorityName;

	private Long priorityId;

	private String licenceNumber;

	private Long sla;

	private Long slaId;

	private boolean isActive;

	private String createdBy;

	private String createdDate;

	private String modifiedBy;

	private String modifiedDate;

	private String remarks;

	private String ticketStatus;
	private Long ticketStatusId;

	private String assignGroup;

	private Long assignGroupId;

	private String assignGroupName;

	private String knowledgeBase;

	private Long knowledgeBaseId;

	private String licenseTypeId;

	private String entityTypeId;

	private String entityTypeName;

	private Long assignToId;

	private String assignToName;

	private String unitName;

	private String licenseStatus;

	private Long actualProblemId;

	private String actualProplemName;

	private Long actionTakenId;

	private String actionTakenName;

	private String problemReportedName;

	private Long problemReportedId;

	private String email;

	private String mobile;

	private String notes;

	private String solutionId;

	private Long solutionCategoryId;

	private String solutionCategoryName;

	private Long solutionSubcategoryId;

	private String solutionSubcategoryName;

	private String solutionNotes;

	private String solutionIssueDetails;

	private String durationInHours;

	private boolean flag;

	private Long knowledgeBaseKBID;

	private String alternativemobileNumber;

	private String address;

	// private String createdbyName;

	private String district;

	private String shopCode;

	private String shopName;

	private Long slaDuration;

	private String districtCode;

	private boolean viewStatus;

	private String tehsilName;

	private String tehsilCode;

	private String uploadApplication;

	private String applicationUuid;

	private String imageUrl;

	private String imageUuid;

	private String firstName;

	private String fieldmobileNo;

	private Boolean issueType;

	private String issuetypeName;

	private String unitCode;

	private String userRemarks;

	private String searchOption;

	private String raisedBy;

	private String duration;

	private String SLABreachHrs;

	private String issueTypesHS;

}
