package com.oasys.posasset.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oasys.helpdesk.conf.Trackablewastage;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@EqualsAndHashCode(callSuper=false)
@Table(name = "eal_declaration")
public class EALDeclarationEntity extends Trackablewastage {
	
	private static final long serialVersionUID = -1876912232063693652L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "license_no")
	private String licenseNo;	
	
	@Column(name = "entity_code")
	private String entityCode;	
	
	
	@Column(name = "bottling_plan_id")
	private String bottlingPlanId;	
	
	
	@Column(name = "code_type_value")
	private String codeTypeVaue;	
	
	@Column(name = "packaging_size_value")
	private String packagingsizeValue;	
	
	@Column(name = "packaging_type")
	private String packagingType;	
	
	@Column(name = "planned_cases")
	private Integer plannedCases;
	
	@Column(name = "planned_bottles")
	private Integer plannedBottles;
	
	@Column(name = "addl_requested_cases")
	private Integer addlrequestedCases;
	
	@Column(name = "addl_requested_bottles")
	private Integer addlrequestedBottles;
	
	@Column(name = "printed_cases")
	private Integer printedCases;
	
	@Column(name = "printed_bottles")
	private Integer printedBottles;
	
	@Column(name = "scanned_cases")
	private Integer scannedCases;
	
	@Column(name = "scanned_bottles")
	private Integer scannedBottles;
	
	
}
