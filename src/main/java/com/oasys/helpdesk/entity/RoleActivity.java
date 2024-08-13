package com.oasys.helpdesk.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.oasys.helpdesk.conf.Trackable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "ROLE_ACTIVITY")
@NoArgsConstructor
//@Audited(withModifiedFlag = true)
public class RoleActivity extends Trackable {


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id")
	private RoleMaster roleMaster;
	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "module_id")
	private AppModule appModule;
	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "activity_id")
	private Activity activity;
	
	@Column(name = "is_active")
	private Boolean active;
	
	@Column(name = "landing_screen")
	private Boolean landingScreen;
	
	
	public Long getActivityId() {
		return this.activity!=null?activity.getId():null;
	}

}
