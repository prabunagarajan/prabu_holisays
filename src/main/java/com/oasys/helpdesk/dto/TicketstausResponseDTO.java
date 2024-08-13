package com.oasys.helpdesk.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketstausResponseDTO {
	
	
	private Long id;

	private String ticketstatusname;

	private String status;

	private String ticketstatusCode;

	private String created_by;

	public String created_date;
	
	//public Date created_date;

	private String modified_by;

	//public Date modified_date;
	
	public String modified_date;

	private String update_by;

	public Date update_date;
	
	

}
