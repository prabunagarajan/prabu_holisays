package com.oasys.helpdesk.entity;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.oasys.helpdesk.conf.Trackable;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity
@Data
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@Table(name = "subsolution_help")
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubsolutionEntity extends Trackable {
	
	
	private static final long serialVersionUID = -6067075145690853782L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "issuedetails")
	private String issuedetails;
	
	@Column(name = "subsolution")
	private String subsolution;
	
	@Column(name = "status")
	private boolean status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subcategory_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_hdt_subcategory"))
	private SubCategory subcategoryId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_hdt_category"))
	private Category categoryId;
	
	@Column(name = "priority")
	private String priority;
	
	@Column(name = "sla")
	private int sla;
	
	@Column(name = "knowledge_resolution")
	private String knowledge_resolution;
	
	
	@Column(name = "remarks")
	private String remarks;
	
	
	@Column(name = "subcode")
	private String subcode;
	
	
	

}
