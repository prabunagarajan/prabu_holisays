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
@Table(name = "department",uniqueConstraints = {
	    @UniqueConstraint(columnNames = {
	            "department"
	        })
})
@EqualsAndHashCode(callSuper=false)
public class DepartmentEntity extends Trackable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6067075145690853782L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "department")
	private String department;
	
	@Column(name = "status")
	private String status;
	
	
	/*
	 * @Column(name = "update_by" ,insertable = false, updatable = false) private
	 * String update_by;
	 * 
	 * 
	 * @CreatedDate
	 * 
	 * @Temporal(TemporalType.TIMESTAMP)
	 * 
	 * @Column(name = "update_date" ,insertable = false, updatable = false) public
	 * Date update_date;
	 */
	
	

	@Column(name = "department_code")
	private String departmentCode;
	
	
	
	

}
