package com.oasys.helpdesk.response;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.oasys.helpdesk.conf.Trackable;

import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)

public class AllocatedEntity extends Trackable {

   	private String entityCode;
   	
   	private String entityDesc;	
	
	
	private UserResponseDto user;
	
}