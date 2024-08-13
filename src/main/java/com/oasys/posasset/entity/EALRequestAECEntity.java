package com.oasys.posasset.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oasys.helpdesk.conf.Trackablewastage;
import com.oasys.posasset.constant.ApprovalStatus;
import com.oasys.posasset.constant.VendorStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@EqualsAndHashCode(callSuper=false)
@Table(name = "eal_request_aec")
public class EALRequestAECEntity extends Trackablewastage{
	
	private static final long serialVersionUID = -1876912232063693652L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "application_date")
	private String applicationDate;	
	
	@Column(name = "requested_appln_no")
	private String requestedapplnNo;	
	
	@Column(name = "status")
	private ApprovalStatus status;
	
	
	@Column(name = "entity_name")
	private String entityName;	
	
	
	@Column(name = "entity_address")
	private String entityAddress;	
	
	@Column(name = "license_type")
	private String licenseType;	
	
	@Column(name = "entity_type")
	private String entityType;	
	
	@Column(name = "license_no")
	private String licenseNo;	
	
	@Column(name = "code_type")
	private String codeType;
	
	@Column(name = "tot_barcode")
	private Integer totBarcode;
	
	@Column(name = "tot_qrcode")
	private Integer totQrcode;
	
	@Column(name = "currently_work_with")  
    private String currentlyWorkwith;
	
	
	@Column(name = "district")  
    private String district;
	
	@Column(name = "printing_type")  
    private String printingType;
	
	@Column(name = "approved_by")  
    private String approvedBy;
	
	@Column(name = "forceclosure_flag") 
	private boolean forceclosureFlag; 
	
//	
//	@CreatedBy
//	@Column(name = "created_by")
//	public Long createdBy;
//	
//	@CreatedDate
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "created_date")
//	public Date createdDate;
//	
//	
//	@LastModifiedDate
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "modified_date")
//	public Date modifiedDate;
//	
//	@LastModifiedBy
//	@Column(name = "modified_by")
//	public Long modifiedBy;
//	
//	public EALRequestAECEntity()
//	{
//		createdDate=new Date();
//		
//	}
	
	@Transient
    private String packagingSize;
	
	@Transient
	private String cartonSize;	
	
	@Transient
	private String noofBarcode;	
	
	@Transient
	private String noofQrcode;	
	
//	@Transient
//	private String remarks;	
//	
	@Transient
	private Long ealrequestId;
	
	@Transient
	private String unmappedType;
	
	@Transient
    private Integer noofBarcodereceived;
	
	@Transient
	private Integer noofQrcodereceived;
	
	@Transient
	private Integer noofRollcodereceived;
	
	@Transient
	private Integer noofBarcodepending;
	
	@Transient
	private Integer noofqrpending;
	
	@Transient
	private Integer totalnumofBarcode;
	
	@Transient
	private Integer totalnumofQrcode ;
	
	@Transient
	private Integer totalnumofRoll ;
	
	@Transient
	private String stockApplnno ;
	
	@Transient
	private String stockDate ;
	
	@Transient
	private String mapType ;
	
	
	@Transient
	private Integer openingStock;
	
	
	@Transient
	private Integer noofRoll;
	
//	
//	@Column(name = "vendor_status")
//	private VendorStatus vendorStatus;
	
	
	@Column(name = "remarks")
	private String remarks;
	
	@Column(name = "unit_code")
	private String unitCode;
	
	@Column(name = "date_of_packaging")
	private String dateOfPackaging;
	
	@Column(name = "liquor_type")
	private String liquorType;
	
	@Column(name = "liquor_sub_type")
	private String liquorSubType;
	
	@Column(name = "brand_name")
	private String brandName;
	
	@Column(name = "request_type")
	private String requestType;
	
	
}
