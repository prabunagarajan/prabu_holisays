package com.oasys.helpdesk.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oasys.helpdesk.conf.Trackable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@EqualsAndHashCode(callSuper=false)
@Table(name = "survey_form")
public class SurveyFormListEntity extends Trackable{/**
 * 
 */
	private static final long serialVersionUID = -1876912232063693652L;

	@Column(name = "form_id")
	private String formId;

	@Column(name = "question_id")
	private Long questionId;

	@Column(name = "rating")
	private String rating;


	@Column(name = "user_name")
	private String userName;

	@Column(name = "email")
	private String email;

	@Column(name = "licence_id")
	private String licenceId;

	@Column(name = "ticket_no")
	private String ticketNo;

	@Column(name = "question_name")
	private String questionName;


}
