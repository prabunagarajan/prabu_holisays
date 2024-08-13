package com.oasys.helpdesk.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.oasys.helpdesk.conf.Trackable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@Table(name = "device_hardware_name",uniqueConstraints = {
		@UniqueConstraint(columnNames = {
				"asset_name"
		})
})
@EqualsAndHashCode(callSuper=false)
public class DeviceHardwareEntity extends Trackable  {

	private static final long serialVersionUID = -1876912232063693652L;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "asset_name")
	private String deviceName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "asset_type_id", referencedColumnName = "id", nullable = false)
	private AssetTypeEntity type;

	@Column(name = "status") 
	private boolean status;

//	@JsonManagedReference
//	@OneToMany(mappedBy = "deviceCode",cascade = CascadeType.ALL,fetch = FetchType.LAZY, orphanRemoval = true)
//	private List<DeviceHardwareListEntity> listrequestDTO = new ArrayList<>();	

}
