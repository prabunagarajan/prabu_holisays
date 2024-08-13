package com.oasys.helpdesk.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.oasys.helpdesk.conf.Trackable;

import lombok.Data;

@Data
@Entity
@Table(name = "zabbix_master_server")
public class ZabbixMasterServerEntity extends Trackable implements Serializable {

	private static final long serialVersionUID = -7159367709179977596L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "service_id")
	private Long serviceId;

	@Column(name = "status")
	private String status;

	@Column(name = "server_name")
	private String serverName;

}
