package com.oasys.helpdesk.request;

import java.io.Serializable;

import javax.validation.constraints.NotNull;



import lombok.Data;

@Data
public class IncomingSmsDTO implements Serializable {
	
	
private static final long serialVersionUID = 4131730606691926359L;
	
	@NotNull(message = "103")
	private Long mid;
	@NotNull(message = "103")
	private String rawMessage;
	private Long vn;
	@NotNull(message = "103")
	private Long send;
	@NotNull(message = "103")
//	@DateFormatValidation(format = "yyyy-MM-dd HH:mm:ss")
//	private String time;
	@NotNull(message = "103")
	private String providerName;
	

}
