package com.oasys.helpdesk.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oasys.helpdesk.conf.Trackablewastage;
import com.oasys.posasset.constant.ApprovalStatus;

import lombok.Data;

@Entity
@Table(name = "eal_wastage")
@Data
public class EalWastage extends Trackablewastage {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "financial_year")
	private String financialYear;

	@Column(name = "application_date")
	private String applicationDate;

	@Column(name = "application_no")
	private String applicationNo;

	@Column(name = "entity_type")
	private String entityType;

	@Column(name = "entity_name")
	private String entityName;

	@Column(name = "license_type")
	private String licenseType;

	@Column(name = "license_number")
	private String licenseNumber;

	@Column(name = "entity_address")
	private String entityAddress;

	@Column(name = "bottling_plan_id")
	private String bottlingPlanId;

	@Column(name = "date_of_packaging")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Kolkata")
	private Date dateOfPackaging;

	@Column(name = "liquor_type")
	private String liquorType;

	@Column(name = "liquor_sub_type")
	private String liquorSubType;

	@Column(name = "liquor_detailed_description", length = 255)
	private String liquorDetailedDescription;

	@Column(name = "brand_name")
	private String brandName;

	@Column(name = "package_size")
	private String packageSize;

	@Column(name = "code_type")
	private String codeType;

	@Column(name = "carton_size")
	private String cartonSize;

	@Column(name = "planned_no_of_cases")
	private Integer plannedNumberOfCases;

	@Column(name = "planned_no_of_bottles")
	private Integer plannedNumberOfBottles;

	@Column(name = "damaged_no_of_barcode")
	private Integer damagedNumberOfBarcode;

	@Column(name = "damaged_no_of_bottle_qr_code")
	private Integer damagedNumberOfBottleQRCode;

	@Column(name = "barCode_qrcode_mono_carton")
	private String barCodeQRCodeMonoCarton;

	@Column(name = "barcode_qrcode")
	private String barcodeQRCode;

	@Column(name = "barcode_printing_type")
	private String barcodePrintingType;

	@Column(name = "status")
	private ApprovalStatus status;

	@Column(name = "currently_work_with")
	private String currentlyWorkwith;

	@Column(name = "district")
	private String district;

	@Column(name = "unmapped_type")
	private String unmappedType;

	@Column(name = "map_type")
	private String mapType;

	@Column(name = "printing_type")
	private String printingType;

	@Column(name = "request_status")
	private Integer requestStatus;

	@Column(name = "used_qr_code")
	private Integer usedQrCode;

	@Column(name = "used_br_code")
	private Integer usedBrCode;

	@Column(name = "balance_qr_code")
	private Integer balanceQrCode;

	@Column(name = "balance_br_code")
	private Integer balanceBrCode;

	@Column(name = "bottled_qr_code")
	private Integer bottledQrCode;

	@Column(name = "bottled_br_code")
	private Integer bottledBrCode;

}