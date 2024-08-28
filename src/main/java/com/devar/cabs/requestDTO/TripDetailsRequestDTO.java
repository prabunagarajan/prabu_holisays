package com.devar.cabs.requestDTO;

import java.util.Date;

import org.joda.time.DateTime;
import java.time.LocalDateTime;


import lombok.Data;

@Data
public class TripDetailsRequestDTO {

		private Long id;
	 	private String vehicleNumber;
	    private Date date;
	    private String customerName;
	    private String customerMobileNumber;
	    private String driverName;
	    private Long startingKM;
	    private Long closingKM;
	    private Long usedKM;
	    private String startingTime;
	    private String closingTime;
	    private String totalTime;
	    private String acOrNonAc;
	    private Long acStartingKM;
	    private Long acClosingKM;
	    private Long usedAcKM;
	    private String acNote;
	    private String visitingPlace;
	    private String advanceType;
	    private int advanceAmount;
	    private int dayRent;
	    private int toll;
	    private int totalRent;
	    private int diesel;
	    private int driverPayment;
	    private int permitAmount;
	    private String paymentType;
	    private int receivedAmount;
	    private int pendingAmount;
	    private int balanceAmount;
	    private int profitAmount;
	    private String submittedBy;
	    private String status;
}
