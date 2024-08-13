package com.oasys.helpdesk.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.oasys.helpdesk.conf.Trackable;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Data
@Entity
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@Table(name = "entity_master_type")
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EntityMasterType extends Trackable implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name = "entity_name")
	private String entityName;

	@Column(name = "status")
	private boolean status;
	
	@Column(name = "is_applicable")
	private String isApplicable;

	@Column(name = "entity_code")
	private String entityCode;

}
