package com.oasys.helpdesk.entity;

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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oasys.helpdesk.conf.Trackable;
import com.oasys.helpdesk.entity.AssetAccessoriesEntity;
import com.oasys.helpdesk.entity.AssetTypeEntity;
import com.oasys.posasset.constant.ActiveStatus;
import com.oasys.posasset.constant.ApprovalStatus;
import com.oasys.posasset.constant.DeviceStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@EqualsAndHashCode(callSuper=false)
@Table(name = "workflow_notification")
public class workflowNotificationEntity extends Trackable {
	
	private static final long serialVersionUID = 2209004471409922799L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "workflow_name")
	private String workflowName;
		
	@Column(name = "description")
	private String description;
	
	@Column(name = "push")
	private Boolean push;
	
	@Column(name = "email")
	private Boolean email;
	
	@Column(name = "sms")
	private Boolean sms;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "level_id", referencedColumnName = "id", nullable = false)
	private LevelMaster levelId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
	private RoleMaster roleId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
	private Category categoryId;
	
	@Column(name = "active")  
    private Boolean active;
	
	@Column(name = "level_name")
	private String levelName;

}
