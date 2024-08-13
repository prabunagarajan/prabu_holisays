package com.oasys.helpdesk.response;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class BaseResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7968118080840443051L;

	private Integer responseCode;
	
	private String responseMessage;
	
	private Object content;
	
	private List<Object> contentList;
	
	private Map<String,Object> additionalInfo;
	
	public BaseResponse (Integer responseCode,String responseMessage) {
		super();
		this.responseCode = responseCode;
		this.responseMessage = responseMessage;
	}
	
	public BaseResponse (Integer responseCode,String responseMessage,Object content) {
		super();
		this.responseCode = responseCode;
		this.responseMessage = responseMessage;
		this.content = content;
	}
	
	public BaseResponse (Integer responseCode,String responseMessage,List<Object> contentList) {
		super();
		this.responseCode = responseCode;
		this.responseMessage = responseMessage;
		this.contentList = contentList;
	}
}
