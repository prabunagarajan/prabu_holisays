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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oasys.helpdesk.conf.Trackable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@EqualsAndHashCode(callSuper=false)
@Table(name = "email_request")
public class EmailRequest extends Trackable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1876912232063693652L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "bcc_emailid_list")
	private String bccEmailList;

	@Column(name = "application_no")
	private String applicationNo;
	
	@Column(name = "cc_emailid_list")
	private String ccEmailList;

    @Column(name = "email_body") 
    private String emailBody;
    
    @Column(name = "email_type")
	private String emailType;
    
    @Column(name = "from_emailid")
  	private String fromEmailId;
    
    @Column(name = "is_valid_email")
	private boolean isValidEmail;
    
    @Column(name = "priority")
  	private String priority;
    
    @Column(name = "subject")
  	private String subject;
    
    @Column(name = "to_emailid_list")
	private String toEmailidList;

	@Column(name = "is_active")
	private boolean isActive;

}
