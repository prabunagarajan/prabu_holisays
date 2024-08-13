package com.oasys.helpdesk.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oasys.helpdesk.conf.IncomingSmsStatus;
import com.oasys.helpdesk.conf.Trackable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "incoming_sms")
//@Getter
@Setter
@ToString
public class IncomingSMSEntity extends Trackable {
	
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", updatable = false, nullable = false, length = 16)
	private UUID id;
	private Long mid;
	private String rawMessage;
	private Long vn;
	private Long send;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
	@Temporal(TemporalType.TIMESTAMP)
	private Date time;
	private String providerName;
	private IncomingSmsStatus incomingSmsConstant;

	public IncomingSMSEntity() {
		this.incomingSmsConstant = IncomingSmsStatus.PENDING;
	}

}
