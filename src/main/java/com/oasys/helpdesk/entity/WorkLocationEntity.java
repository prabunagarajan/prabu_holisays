package com.oasys.helpdesk.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.oasys.helpdesk.conf.Trackable;
import com.oasys.helpdesk.utility.UpdateReason;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@Table(name = "work_location")
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkLocationEntity  extends Trackable{

	private static final long serialVersionUID = 8456161920720838558L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "department_code")
	private String departmentCode;
	
	@Column(name = "entity_type_code")
	private String entityTypeCode;
	
	@Column(name = "district_code")
	private String districtCode;
	
	@Column(name = "update_reason")
	private UpdateReason updateReason;
	
	@JsonBackReference
	@OneToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;
	
	public String getUpdateReason()
	{
		return this.updateReason.getType();
	}
	

	@Column(name = "state_code")
	private String stateCode;
	
	@Column(name = "district_multi_name")
	private String districtNames;
}
