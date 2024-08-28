package com.devar.cabs.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.devar.cabs.common.Trackable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "trip_details")
@Audited(withModifiedFlag = true)
@NoArgsConstructor
public class TripDetailsEntity extends Trackable{
	
	  @Column(name = "vehicle_number")
	    private String vehicleNumber;

	    @Column(name = "date")
	    private Date date;

	    @Column(name = "customer_name")
	    private String customerName;
	    
	    @Column(name = "customer_mobile_number")
	    private String customerMobileNumber;

	    @Column(name = "driver_name")
	    private String driverName;

	    @Column(name = "starting_km")
	    private Long startingKM;

	    @Column(name = "closing_km")
	    private Long closingKM;

	    @Column(name = "used_km")
	    private Long usedKM;

	    @Column(name = "starting_time")
	    private String startingTime;

	    @Column(name = "closing_time")
	    private String closingTime;

	    @Column(name = "total_time")
	    private String totalTime;

	    @Column(name = "ac_or_non_ac")
	    private String acOrNonAc;

	    @Column(name = "ac_starting_km")
	    private Long acStartingKM;

	    @Column(name = "ac_closing_km")
	    private Long acClosingKM;
	    
	    @Column(name = "used_ac_km")
	    private Long usedAcKM;

	    @Column(name = "ac_note")
	    private String acNote;

	    @Column(name = "visiting_place")
	    private String visitingPlace;

	    @Column(name = "advance_type")
	    private String advanceType;

	    @Column(name = "advance_amount")
	    private int advanceAmount;

	    @Column(name = "day_rent")
	    private int dayRent;

	    @Column(name = "toll")
	    private int toll;

	    @Column(name = "total_rent")
	    private int totalRent;

	    @Column(name = "diesel")
	    private int diesel;

	    @Column(name = "driver_payment")
	    private int driverPayment;

	    @Column(name = "permit_amount")
	    private int permitAmount;

	    @Column(name = "payment_type")
	    private String paymentType;

	    @Column(name = "received_amount")
	    private int receivedAmount;

	    @Column(name = "pending_amount")
	    private int pendingAmount;

	    @Column(name = "balance_amount")
	    private int balanceAmount;

	    @Column(name = "profit_amount")
	    private int profitAmount;

	    @Column(name = "submitted_by")
	    private String submittedBy;

	    @Column(name = "status")
	    private String status;

}
