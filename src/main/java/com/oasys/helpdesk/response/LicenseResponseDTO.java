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
public class LicenseResponseDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7968118080840443051L;

	private Integer responseCode;
	
	private String responseMessage;
	
	private Object contentList;
	
	private List<LicenseDTO> content;
	
	private Map<String,Object> additionalInfo;
	
	public LicenseResponseDTO (Integer responseCode,String responseMessage) {
		super();
		this.responseCode = responseCode;
		this.responseMessage = responseMessage;
	}
	
	public LicenseResponseDTO (Integer responseCode,String responseMessage,Object contentList) {
		super();
		this.responseCode = responseCode;
		this.responseMessage = responseMessage;
		this.contentList = contentList;
	}
	
	public LicenseResponseDTO (Integer responseCode,String responseMessage,List<LicenseDTO> content) {
		super();
		this.responseCode = responseCode;
		this.responseMessage = responseMessage;
		this.content = content;
	}
}
