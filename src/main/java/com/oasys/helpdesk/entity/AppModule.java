package com.oasys.helpdesk.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;


import com.oasys.helpdesk.conf.Trackable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "MODULE", uniqueConstraints = {
    @UniqueConstraint(columnNames = {
        "code"
    })
})
@NoArgsConstructor
@Audited(withModifiedFlag = true)
public class AppModule extends Trackable{

	@NotNull
	private String code;

	private String name;
	
	private String icon;
	
	private Long displayOrder;
	
	private String routeUrl;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	private AppModule parentModule;
	 
	private Boolean active;
	
	@Transient
	private Long parentAppModuleId;

}
