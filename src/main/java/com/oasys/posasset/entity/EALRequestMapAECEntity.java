package com.oasys.posasset.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oasys.helpdesk.conf.Trackablewastage;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@EqualsAndHashCode(callSuper=false)
@Table(name = "eal_request_map_aec")
public class EALRequestMapAECEntity extends Trackablewastage{
	
	private static final long serialVersionUID = -1876912232063693652L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "packaging_size")
	private String packagingSize;	
	
	@Column(name = "carton_size")
	private String cartonSize;	

	
	@Column(name = "no_of_barcode")
	private String noofBarcode;	
	
	
	@Column(name = "no_of_qrcode")
	private String noofQrcode;	
	
	@Column(name = "remarks")
	private String remarks;	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ealrequest_id", referencedColumnName = "id", nullable = false)
	private EALRequestAECEntity ealrequestId;
	
	
	@Column(name = "unmapped_type")
	private String unmappedType;
	
	@Column(name = "no_barcode_received")
	private Integer noofBarcodereceived;
	
	@Column(name = "no_qrcode_received")
	private Integer noofQrcodereceived;
	
	@Column(name = "no_roll_received ")
	private Integer noofRollcodereceived;
	
	@Column(name = "no_barcode_pending ")
	private Integer noofBarcodepending;
	
	@Column(name = "no_qrcode_pending ")
	private Integer noofqrpending;
	
	@Column(name = "total_numof_barcode ")
	private Integer totalnumofBarcode;
	
	@Column(name = "total_numof_qrcode  ")
	private Integer totalnumofQrcode ;
	
	@Column(name = "total_numof_roll")
	private Integer totalnumofRoll ;
	
	@Column(name = "stock_applnno")
	private String stockApplnno ;
	
	@Column(name = "stock_date")
	private String stockDate ;
	 
	@Column(name = "code_type")
	private String codeType ;
	   
	@Column(name = "flag") 
	private boolean flag; 
	
	@Column(name = "opening_stock") 
	private Integer openstock;
	
	@Column(name = "licenece_number ")
	private String licenseNo;	
	
	@Column(name = "printing_type")  
    private String printingType;
	
	@Column(name = "no_of_roll")
	private Integer noofRoll;
	
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
//	public EALRequestMapAECEntity()
//	{
//		createdDate=new Date();
//		
//	}
	
	
	@Column(name = "map_type")  
    private String mapType;

}
