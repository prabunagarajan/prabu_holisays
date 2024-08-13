package com.oasys.helpdesk.entity;
import java.util.Date;

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
@Table(name = "device_list")
public class DeviceList extends Trackable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1876912232063693652L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "device_name_id")
	private String deviceNameID;
	
	@Column(name = "device_serial_no")
	private String deviceSerialNo;
	
	@Column(name = "device_hardware_name")
	private String deviceHardwareName;
	
	
	@Column(name = "warranty")
	private Integer warranty;
	
	@Column(name = "registered_date")
	private Date registeredDate;
	
	@Column(name = "days_remaining")
	private String daysRemaining;
	
	@Column(name = "device_status")
	private String deviceStatus;
	
    @Column(name = "status") 
	private boolean status;

}
