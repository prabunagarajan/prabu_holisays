package com.oasys.helpdesk.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oasys.helpdesk.conf.Trackable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@JsonIgnoreProperties({ "hibernatelazyinitializer", "handler" })
@Table(name = "login_history")
@EqualsAndHashCode(callSuper=false)
public class LoginHistory extends Trackable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name = "login_ip")
	private String loginIP;

	@Column(name = "user_id")
    private Long userId;
    
	@Column(name = "login_time")
    private LocalDateTime loginTime;
    
	@Column(name = "logout_time")
    private LocalDateTime logoutTime;
	
	public LoginHistory() {}
	
	public LoginHistory(String loginIP, Long userId, LocalDateTime loginTime) {
		super();
		this.loginIP = loginIP;
		this.userId = userId;
		this.loginTime = loginTime;
	}
}
