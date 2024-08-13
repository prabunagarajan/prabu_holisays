package com.oasys.helpdesk.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.oasys.helpdesk.conf.Trackable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "")
public class SLATimestampTimeConverterEntity extends Trackable {

	private static final long serialVersionUID = -1876912232063693652L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "")
	private long serviceIds;

	@Column(name = "")
	private int sli;

	@Column(name = "")
	private int downTime;

	@Column(name = "")
	private long excludedDowntimes;

	@Column(name = "")
	private long errorBudget;

	@Column(name = "")
	private int upTime;

}
