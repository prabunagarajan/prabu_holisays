package com.oasys.helpdesk.dto;

import lombok.Data;

@Data
public class LoginHistoryResponseDTO {
	private Long id;

	private String loginIP;

    private Long userId;
    
    private String loginTime;
    
    private String logoutTime;
    
    private String userName;
    
    private String EmailId;
    
    private String employeeId;
}
