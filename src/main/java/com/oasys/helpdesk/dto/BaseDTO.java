package com.oasys.helpdesk.dto;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;


import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	Integer statusCode = 200;

	String message;

	Object responseContent;

	List<?> responseContents;

	List<?> data;

	
}