package com.oasys.helpdesk.response;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserLoginResponseDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4630728974156437691L;
	
	private Long userId;
	
	private Long entityId;
	
	private String userName;
	
	private String accessToken;
	
	private Date loginDateTime;
	
	private String loginRemoteIp;
	
	private Date lastLoginDateTime;
	
	private String entityType;
	
	
}
