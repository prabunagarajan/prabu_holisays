package com.oasys.helpdesk.entity;

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
import com.oasys.helpdesk.dto.AssetMapRequestDto;

import javax.persistence.UniqueConstraint;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@Table(name = "asset_status",uniqueConstraints = {
	    @UniqueConstraint(columnNames = {
	            "code"
	        }),
	    @UniqueConstraint(columnNames = {
	            "name"
	        })
})
@EqualsAndHashCode(callSuper=false)
public class AssetStatusEntity extends Trackable{
	
	private static final long serialVersionUID = -6067075145690853782L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name = "code")
	private String code;
	
	@Column(name = "name")
	private String name;

	

	
	

}
