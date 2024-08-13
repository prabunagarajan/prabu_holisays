package com.oasys.helpdesk.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.oasys.helpdesk.conf.Zabbixtrackable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "zabbix_server_data")
public class ZabbixServerEntity extends Zabbixtrackable {

	private static final long serialVersionUID = -1876912232063693652L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "service_id")
	private String serviceID;

	@Column(name = "status")
	private String status;

	@Column(name = "server_name")
	private String serverName;

	@Column(name = "sli")
	private String sli;

	@Column(name = "error_budget")
	private String errorBudget;

	@Column(name = "uptime")
	private String upTime;
	
	@Column(name = "down_time")
	private String downTime;
	
	@Column(name = "excluded_downtimes")
	private String excludedownTimes;

	@Column(name = "slo")
	private String slo;
	
	@Column(name = "todaydate")
	private String todaydate;
	
	
	

}
