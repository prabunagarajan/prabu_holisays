package com.oasys.helpdesk.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oasys.helpdesk.conf.Trackable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })

@Table(name = "grievance_workflow",uniqueConstraints = {
	    @UniqueConstraint(columnNames = {
	            "code"
	        })
})
@EqualsAndHashCode(callSuper=false)
public class GrievanceWorkflowEntity extends Trackable {

	private static final long serialVersionUID = -1876912232063693652L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "code")
	private String code;
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "sla", referencedColumnName = "sla", nullable = false, foreignKey = @ForeignKey(name = "fk_gw_sla"))
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sla", referencedColumnName = "id", nullable = false)
	private GrievanceSlaEntity sla;  		//GrievanceSlaEntity
	
//	@ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "assignto_Group", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_gworkf_assignto_Group")) 
  	private Long assignto_Group;
    
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "assignto_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_gworkf_assignto_id"))
  	private Long assignto_id;
    
    @Column(name = "is_active") 
    private boolean status;

    @Column(name = "type_of_user")
  	private String typeofUser;
}
