package com.oasys.cabs.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.oasys.helpdesk.conf.Trackable;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "vehicle_details")
@Audited(withModifiedFlag = true)
@NoArgsConstructor
public class VehicleDetailsEntity extends Trackable {

	   @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "id")
	    private Long id;

	    @Column(name = "vehicle_number", unique = true, nullable = false, length = 20)
	    private String vehicleNumber;

	    @Column(name = "vehicle_name", length = 100)
	    private String vehicleName;

	    @Column(name = "vehicle_color", length = 50)
	    private String vehicleColor;

	    @Column(name = "status")
	    private Boolean status;

	    @Column(name = "remarks", length = 100)
	    private String remarks;

	    @Column(name = "insurance_date")
	    @Temporal(TemporalType.TIMESTAMP)
	    private Date insuranceDate;

	    @Column(name = "tax_date")
	    @Temporal(TemporalType.TIMESTAMP)
	    private Date taxDate;

	    @Column(name = "fc_date")
	    @Temporal(TemporalType.TIMESTAMP)
	    private Date fcDate;
}
