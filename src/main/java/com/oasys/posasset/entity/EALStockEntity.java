package com.oasys.posasset.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oasys.helpdesk.conf.Trackabledate;
import com.oasys.helpdesk.conf.Trackablewastage;
import com.oasys.helpdesk.entity.AssetAccessoriesEntity;
import com.oasys.helpdesk.entity.AssetTypeEntity;
import com.oasys.posasset.constant.ApprovalStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@EqualsAndHashCode(callSuper=false)
@Table(name = "stock_ineal")
public class EALStockEntity extends Trackablewastage {
	
	private static final long serialVersionUID = -1876912232063693652L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//newly added.
	
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
	
	@Column(name = "no_barcode_received")
	private Integer noofBarcodereceived;
	
	@Column(name = "no_qrcode_received")
	private Integer noofQrcodereceived;
	
	@Column(name = "no_roll_received")
	private Integer noofRollcodereceived;
	
	@Column(name = "no_barcode_pending")
	private Integer noofBarcodepending;
	
	@Column(name = "no_qrcode_pending")
	private Integer noofqrpending;
	
	 @Column(name = "eal_request_applnno")
     private String ealrequestapplno;
	
	 @Column(name = "printing_type")  
	 private String printingType;
	 
	
	//

	@Column(name = "eal_requestid")
	private Integer ealrequestId;
	
    @Column(name = "unmapped_type")
	private String unmappedType;
	
	
	@Column(name = "total_numof_barcode")
	private Integer totalnumofBarcode;
	
	@Column(name = "total_numof_qrcode")
	private Integer totalnumofQrcode ;
	
	@Column(name = "total_numof_roll")
	private Integer totalnumofRoll ;
	
	@Column(name = "stock_applnno")
	private String stockApplnno;
	
	@Column(name = "stock_date")
	private String stockDate;
	 
	@Column(name = "code_type")
	private String codeType;
	   
	@Column(name = "openstock_applnno")
	private String openstockApplnno; 
	
	@Column(name = "flag") 
	private boolean flag; 
	
	@Column(name = "license_no")
	private String licenseNo;
	
	@Column(name = "no_barcode_damaged")
	private Integer noofBarcodedamaged;
	
	@Column(name = "no_qrcode_damaged")
	private Integer noofQrcodedamaged;
	
	@Column(name = "entity_name")
	private String entityName;	
	
	@Column(name = "entity_address")
	private String entityAddress;	
	
	@Column(name = "license_type")
	private String licenseType;	
	
	@Column(name = "entity_type")
	private String entityType;	
	
	@Column(name = "map_type")
	private String mapType;
	
	@Column(name = "unit_code")
	private String unitCode;
	
	@Column(name = "no_of_roll")
	private String noofRollCode;	
	
	@Column(name = "tp_applnno")
	private String tpApplnNo;
}
