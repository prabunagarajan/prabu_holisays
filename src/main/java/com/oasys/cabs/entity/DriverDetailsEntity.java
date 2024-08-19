/**
 * 
 */
package com.oasys.cabs.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.oasys.helpdesk.conf.Trackable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "driver_details")
//@Audited(withModifiedFlag = true)
@NoArgsConstructor
public class DriverDetailsEntity extends Trackable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5009506116207889680L;

//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long id;

	private String name;

	private String mobileNumber;

	private String drivingLicenseNumber;

	private String aadharNumber;

	private String doorNumber;

	private String street;

	private String villageOrCity;

	private String district;

	private String state;

	private String county;

	private Boolean status = false;

	private Boolean isPermanentDriver = false;

}
