package com.oasys.helpdesk.entity;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oasys.helpdesk.conf.Trackable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@EqualsAndHashCode(callSuper=false)
@Table(name = "accessories_list")
public class AccessoriesListData extends Trackable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1876912232063693652L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "accessories_name_id")
	private String accessoriesNameId;
	
	@Column(name = "accessories_code")
	private String accessoriesCode;
	
	@Column(name = "accessories_name")
	private String accessoriesName;
	
	@Column(name = "accessories_serial_no")
	private String accessoriesSerialNo;
	
    @Column(name = "warranty")
	private Integer warranty;
	
	@Column(name = "registered_date")
	private Date registeredDate;
	
	@Column(name = "expired_date")
	private Date expiredDate;
	
	@Column(name = "accessories_status")
	private String accessoriesStatus;
	
    @Column(name = "status") 
	private boolean status; 

}
