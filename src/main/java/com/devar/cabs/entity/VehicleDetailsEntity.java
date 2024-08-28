package com.devar.cabs.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.devar.cabs.common.Trackable;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "vehicle_details")
//@Audited(withModifiedFlag = true)
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
	    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	    private String insuranceDate;

	    @Column(name = "tax_date")
	    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	    private String taxDate;

	    @Column(name = "fc_date")
	    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
//	    @Temporal(TemporalType.TIMESTAMP)
	    private String fcDate;
	    
	    @Column(name = "polution_date")
	    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
//	    @Temporal(TemporalType.TIMESTAMP)
	    private String polutionDate;
}
