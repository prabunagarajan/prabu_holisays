package com.oasys.helpdesk.response;



import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DashBoardResponseDto {
	private int totalticket;
	
	private int openticket;
	
	private int inprogress;
	
    private int closed;
    
    private int rectified;
    
    private int p1Count;
    
    private int p2Count;
 
	
}
