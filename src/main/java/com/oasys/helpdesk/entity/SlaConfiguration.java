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
import com.oasys.helpdesk.conf.Trackable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@EqualsAndHashCode(callSuper=false)
@Table(name = "sla_configuration")
public class SlaConfiguration extends Trackable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1876912232063693652L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "rule_name")
	private String ruleName;
	
	@Column(name = "group_assignee")
	private String groupAssignee;
	
	@Column(name = "priority")
	private String priority;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "threshold_time")
	private int thresholdTime;
	
	@Column(name = "user_assinge")
	private String userAssignee;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "email_template_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_sc_email_template"))
	private SlaEmailTemplate slaEmailmatser;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sms_template_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_sc_sms_template"))
	private SlaSmsTemplate slaSmsmatser;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ticket_category_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_sc_ticket_category"))
	private Category ticketCategory;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sub_category_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_sc_sub_category"))
	private SubCategory ticketSubCategory;
	
	@Column(name = "template_type")
	private String templateType;
	
    @Column(name = "is_active") 
	private boolean isActive;

}
