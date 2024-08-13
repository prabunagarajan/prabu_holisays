package com.oasys.helpdesk.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GrievanceTicketStatusResponseDTO {

	private String ticketstatusname;

	private String status;

	private String ticketstatusCode;

	private String created_by;

	public String created_date;

	private String modified_by;

	public String modified_date;

	private String update_by;

	public Date update_date;

}
