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
@Table(name = "shopcode_master",uniqueConstraints = {
	    @UniqueConstraint(columnNames = {
	            "shop_code"
	        })
})
@EqualsAndHashCode(callSuper=false)
public class ShopcodeEntity extends Trackable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6067075145690853782L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "division")
	private String division;
	
	@Column(name = "shop_code")
	private String shopCode;
	
	@Column(name = "district_code")
	private Integer districtCode;
	
	@Column(name = "user_id")
	private String userId;
	
	@Column(name = "state_code")
	private String stateCode;
	
    @Column(name = "active") 
    private boolean active;
    
    @Column(name = "district_name")
	private String districtName;
    
//    @Column(name = "employee_id")
//	private String employeeId;
    

}
