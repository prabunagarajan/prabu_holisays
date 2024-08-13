package com.oasys.helpdesk.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oasys.helpdesk.conf.Trackable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@EqualsAndHashCode(callSuper=false)
@Table(name = "sent_email_request")
public class SentEmailRequest extends Trackable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1876912232063693652L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @Column(name = "email_body",columnDefinition = "TEXT") 
    private String emailBody;
    
    @Column(name = "to_emailid_list")
	private String toEmailList;
    
    @Column(name = "email_inbox_id",nullable = false)
    private Long emailInboxId;

	@Column(name = "is_active")
	private boolean isActive;
	
	public SentEmailRequest()
	{
		isActive = true;
	}

	public SentEmailRequest(String emailBody, String toEmailList, Long emailInboxId) {
		super();
		this.emailBody = emailBody;
		this.toEmailList = toEmailList;
		this.emailInboxId = emailInboxId;
		isActive = true;
	}
	
}
