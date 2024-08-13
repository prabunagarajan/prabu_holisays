package com.oasys.helpdesk.entity;


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
@Table(name = "issue_details")
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IssueDetails extends Trackable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6067075145690853782L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "issue_name")
	private String issueName;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subcategory_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_hdt_subcategory"))
	private SubCategory subcategoryId;
	
	@Column(name = "is_active")
	private boolean isActive;
	
	@Column(name = "issuecode")
	private String issuecode;
	
	@Column(name = "is_issuetype")
	private boolean isIssuetype;
	
}
