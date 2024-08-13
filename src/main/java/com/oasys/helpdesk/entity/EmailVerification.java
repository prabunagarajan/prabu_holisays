package com.oasys.helpdesk.entity;

import java.time.LocalDateTime;

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
@Table(name = "email_verification")
public class EmailVerification extends Trackable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "otp")
	private String otp;
	
	@Column(name = "email_id")
	private String emailId;
	
	@Column(name = "otp_count")
	private Integer otpCount;
	
	@Column(name = "expiry_date_time")
	private LocalDateTime otpExpiryDateTime;
	
	@Column(name = "is_verified")
	private Boolean verified;
	
	public boolean isExpired() {
		LocalDateTime ldt = LocalDateTime.now();
		return ldt.isAfter(this.otpExpiryDateTime);
	}

	public void setExpiryDateTime(long minutes) {
		this.otpExpiryDateTime = LocalDateTime.now().plusMinutes(minutes);
	}

}
