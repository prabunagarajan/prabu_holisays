package com.oasys.helpdesk.entity;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oasys.helpdesk.conf.Trackable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@Table(name = "district_help",uniqueConstraints = {
	    @UniqueConstraint(columnNames = {
	            "districtname"
	        })
})
@EqualsAndHashCode(callSuper=false)
public class DistrictEntity extends Trackable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6067075145690853782L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "countryname")
	private String countryname;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "districtname")
	private String districtname;
	
	@Column(name = "districtcode")
	private String districtcode;
	
	@Column(name = "districtshortname")
	private String districtshortname;
	
	
	@Column(name = "zone")
	private String zone;
	
	
	@Column(name = "code")
	private String code;
	
	
	@Column(name = "state")
	private String state;
	
	

}
