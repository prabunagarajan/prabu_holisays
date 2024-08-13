package com.oasys.helpdesk.dto;

import java.io.Serializable;

import javax.persistence.Column;

import lombok.Data;

@Data
public class SiteVisitDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	//private String shopCodeId;
	//private String status;
	
	private Long entityTypeId;
	private Long siteobservationId;
	private Long siteactionTakenId;
	private Long siteVisitstausId;
	private Long siteIssueTypeId;
	private String entityType;
	private String siteObservation;
	private String siteActionTaken;
	private String siteVisitStatus;
	private String siteIssueType;
	private String shopCode;
	private String entityName;
	private String deviceId;
	private String licenseName;
	private String licenseType;
	private String Address;
	private String salespersonName;
	private String contactNo;
	private String pendingReason;
	private String Latitude;
	private String Longitude;
	private String District;
	private boolean isActive;
	private String createdDate;
	private String modifiedDate;
	private Long createdBy;
	private Long modifiedBy;
	private String createdByName;
	private String modifiedByName;
	private String created_by;
    private String modified_by;
    public String created_date;
    public String modified_date;
    //public String update_date;
    private String Image1;
    private String Image2;
    private String Uuid1;
    private String Uuid2;
    private String fromDate;
    private String toDate;
    private Long userId;
    //private String finalstatus;
    private String ticketNumber;
    private String finalStatus;

}
