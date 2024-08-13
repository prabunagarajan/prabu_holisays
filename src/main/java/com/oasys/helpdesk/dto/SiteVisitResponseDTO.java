package com.oasys.helpdesk.dto;

import lombok.Data;
import java.sql.Date;
@Data
public class SiteVisitResponseDTO {
	private Long id;
	private Long entityTypeId;
	private Long siteobservationId;
	private Long siteactionTakenId;
	private Long siteVisitstausId;
	private Long siteIssueTypeId;
	private String siteObservation;
	private String siteActionTaken;
	private String siteVisitStatus;
	private String siteIssueType;
	private String entityType;
	private String shopCode;
	private String entityName;
	private String deviceId;
	private String licenseName;
	private String licenseType;
	private String Address;
	private String salespersonName;
	private String pendingReason;
	private String Latitude;
	private String Longitude;
	private String District;
	private boolean isActive;
	private String createdDate;
	private String modifiedDate;
	private Long createdBy;
	private Long modifiedBy;
	private String created_by;
    private String modified_by;
    public String created_date;
    public String modified_date;
    private String Image1;
    private String Image2;
    private String Uuid1;
    private String Uuid2;
    private String contactNo;
    private String ticketNumber;
    private String finalStatus;
    


}
