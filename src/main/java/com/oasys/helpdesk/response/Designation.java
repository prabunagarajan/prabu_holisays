package com.oasys.helpdesk.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.oasys.helpdesk.conf.Trackable;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)

public class Designation extends Trackable {


	private String code;	
	
	private String name;	
	
	private Boolean active;	
	
}
