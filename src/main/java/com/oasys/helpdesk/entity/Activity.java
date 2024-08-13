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
@Table(name = "ACTIVITY", uniqueConstraints = {
    @UniqueConstraint(columnNames = {
        "code"
    })
})
@NoArgsConstructor
@Audited(withModifiedFlag = true)
public class Activity extends Trackable{

	@NotNull
	private String code;

	private String name;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "module_id")
	private AppModule appModule;

	private String icon;
	
	private String routeUrl;

	private Long displayOrder;

	private Boolean active;
	
	@Transient
	private Long appModuleId;
	
}
