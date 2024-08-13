package com.oasys.helpdesk.request;

import java.util.ArrayList;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import javax.validation.constraints.NotNull;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateTicketRequestDto {
	
	    private Long id;
		
	    private String ticketNumber;
		
		private Long categoryId;

		private Long subCategoryId;

		
		@NotNull(message = "103")
		private Long priorityId;
		
		private Long ticketStatusId;
		
		private Long issueDetailsId;

		
		@NotNull(message = "103") 
		private Long slaId;
		
		private Long knowledgeBaseId;
		
		private Long assignGroupId;
		
		private Long issueFromId;
		
		private String entityTypeId;
		
		private String  licenseNumber;
		
		private String licenseTypeId;
		
		private String callDisconnect;
		
		private String requiredField;
		
		private Long assignToId;

	    private boolean isActive;

		private String createdBy;

		private Date createdDate;

		private String modifiedBy;

		private Date modifiedDate;
		
		private String remarks;
		
		private String unitName;
		
		private String licenseStatus;
		
		private Long actualProblemId;
		
		private Long actionTakenId;
		
		private Long problemReportedId;
		
		private String problemReported;
		
		private String email;
		
		private String mobile;
		
		private String notes;
		
		private Long solutionId;
		
		private boolean flag;
		
		private String alternativemobileNumber;
		
		private String fromDate;
		
		private String toDate;
		
		private String address;
	    
		private String ticketstatusName;
		
		private String createdbyName;
		
		private String userRemarks;
        
		@NotNull(message = "103")
		private String district;

		private String shopCode;

		private String shopName;

		@NotNull(message = "103")
		private String districtCode;

		private ArrayList<String> districtIdcode;

		private boolean viewStatus;
		
		private String tehsilName;
		
		private String tehsilCode;

		private String uploadApplication;

		private String applicationUuid;
		
		private String roleCode;
		
		private String imageUrl;

		private String imageUuid;
		
		private String unitCode;
		
		private String searchOption;
		
		private String raisedBy;
		
		private String duration;
		
		private String issueTypeSH;
		
}
