package com.oasys.helpdesk.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class CommanMasterUnitCodeDTO implements Serializable {
	private String userName;
	private String password;
	private String source;
	private String userUnitCode;

}
