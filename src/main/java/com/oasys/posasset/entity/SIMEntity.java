package com.oasys.posasset.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oasys.helpdesk.conf.Trackable;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity
@Data
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@Table(name = "sim")
@EqualsAndHashCode(callSuper=false)
public class SIMEntity extends Trackable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "sim_providername")
	private String simprovidername;
	
	@Column(name = "siname")
	private String siname;
	
	
	@Column(name = "imis")
	private String imis;
	
	@Column(name = "associated")
	private Integer associated;
	
	
	@Column(name = "number")
	private String number;
	
	@Column(name = "serial_number")
	private String serialnumber;
	
	@Column(name = "status")
	private Boolean status;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sim_provider_details_id", referencedColumnName = "id", nullable = false)
	private SIMProviderDetEntity simproviderdetId;
	
	

}
