package com.oasys.posasset.entity;



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
@Table(name = "sim_provider_details")
@EqualsAndHashCode(callSuper=false)

public class SIMProviderDetEntity extends Trackable{
	
	private static final long serialVersionUID = -6067075145690853782L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	
	@Column(name = "provider_name")
	private String providername;
	
	@Column(name = "status") 
	private boolean status;
	
	
	@Column(name = "l_provider_name")
	private String iprovidername;
	
	
	
	
	

}
