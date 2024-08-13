//package com.oasys.helpdesk.entity;
//
//import java.util.Date;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.Table;
//import javax.persistence.UniqueConstraint;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.oasys.helpdesk.conf.Trackable;
//
//import lombok.EqualsAndHashCode;
//import lombok.Getter;
//import lombok.Setter;
//
//@Entity
//@Getter
//@Setter
//@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
//@Table(name = "device_hardware_list",uniqueConstraints = {
//		@UniqueConstraint(columnNames = {
//				"device_id"
//		})
//})
//@EqualsAndHashCode(callSuper=false)
//public class DeviceHardwareListEntity extends Trackable{
//
//
//	private static final long serialVersionUID = -6067075145690853782L;
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long id;
//
//
////	@JsonBackReference
////	@ManyToOne
////	@JoinColumn(name = "device_code", referencedColumnName = "device_code", nullable = false)
////	private DeviceHardwareEntity deviceCode;
//
//	@Column(name = "device_id")
//	private String deviceId;
//
//	@Column(name = "device_serial_no")
//	private String deviceSerialNo;
//
//	@Column(name = "device_hardware_name")		
//	private String deviceHardwareName;
//
//	@Column(name = "warranty")
//	private Integer warranty;
//
//	@Column(name = "registered_date")
//	private Date registeredDate;
//
//	@Column(name = "expired_date")
//	private Date expiredDate;
//
//	@Column(name = "device_status")
//	private String deviceStatus;
//
//	@Column(name = "status") 
//	private boolean status;
//
//
//
//
//}
