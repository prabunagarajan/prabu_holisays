package com.oasys.helpdesk.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class GrievanceTicketStatusDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;

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
