package com.oasys.helpdesk.dto;

import lombok.Data;

@Data
public class TicketCountResponseDTO {
	private String status;
	private Integer count;
    private Double percentage;
	
	private Integer totcount;
	
	private String month;
	
	private Integer newEmailCount;
	
	private Integer smsCount;
	
}
