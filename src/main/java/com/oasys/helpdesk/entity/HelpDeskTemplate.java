package com.oasys.helpdesk.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Data
@Entity
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@Table(name = "helpdesk_template")
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HelpDeskTemplate implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name = "date")
	private Date date;

	@Column(name = "template_id")
	private int templateId;

	@Column(name = "template_name")
	private String templateName;

	@Column(name = "template_type")
	private String templateType;

	@Column(name = "status")
	private boolean status;
	
	@Column(name = "mobile_number")
	private String mobileNumber;

	@Column(name = "message")
	private String message;

	@Column(name = "from_email")
	private String fromEmail;

	@Column(name = "sms_status")
	private boolean smsStatus;

	@Column(name = "license_no")
	private String licenseNumber;

	@Column(name = "to_email")
	private String toEmail;

	@Column(name = "subject")
	private String subject;

	@Column(name = "description")
	private String description;

	@Column(name = "email_status")
	private boolean emailStatus;

	@Column(name = "license_no_email")
	private String licenseNumberForEmail;
}
