package com.oasys.helpdesk.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.oasys.helpdesk.conf.Trackable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@Table(name = "user_shift_configuration")
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserShiftConfigurationEntity  extends Trackable {

	private static final long serialVersionUID = 8456161920720838558L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "start_date")
	private LocalDate startDate;
	
	@Column(name = "end_date")
	private LocalDate endDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="shift_config_id")
	private ShiftConfigEntity shifConfig;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="shift_working_days")
	private ShiftWorkingDaysEntity shifWorkingDays;
	
	@JsonBackReference
	@OneToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;
}
