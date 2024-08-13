package com.oasys.helpdesk.security;


import java.io.Serializable;
import java.util.Map;

public class ApiResponse implements Serializable {
	private Boolean success;
	private String message;
	private Long id;
	private String errorCode;
	private String errorTitle;
	private Long responseCode;
	private String responseMsg;
	private Map<String,Object> finalMap;
	
	public ApiResponse(Boolean success, String message) {
		this.success = success;
		this.message = message;
	}
	
	public ApiResponse(Boolean success, String errorCode,String errorTitle,String message) {
		this.success = success;
		this.errorCode = errorCode;
		this.errorTitle = errorTitle;
		this.message = message;
	}
	
	public ApiResponse(Boolean success, String message,String errorCode,String errorTitle,Map<String,Object> finalMap) {
		this.success = success;
		this.message = message;
		this.errorCode = errorCode;
		this.finalMap = finalMap;
		this.errorTitle = errorTitle;
	}
	
	public ApiResponse(Long id, String message) {
		super();
		this.id = id;
		this.message = message;
	}
	public ApiResponse(Boolean success, Long responseCode, String responseMsg, Map<String, Object> finalMap) {
		super();
		this.success = success;
		this.responseCode = responseCode;
		this.responseMsg = responseMsg;
		this.finalMap = finalMap;
	}
	
	public ApiResponse() {
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @return the finalMap
	 */
	public Map<String, Object> getFinalMap() {
		return finalMap;
	}

	/**
	 * @param finalMap the finalMap to set
	 */
	public void setFinalMap(Map<String, Object> finalMap) {
		this.finalMap = finalMap;
	}

	/**
	 * @return the errorTitle
	 */
	public String getErrorTitle() {
		return errorTitle;
	}

	/**
	 * @param errorTitle the errorTitle to set
	 */
	public void setErrorTitle(String errorTitle) {
		this.errorTitle = errorTitle;
	}
	/**
	 * @return the responseCode
	 */
	public Long getResponseCode() {
		return responseCode;
	}

	/**
	 * @param responseCode the responseCode to set
	 */
	public void setResponseCode(Long responseCode) {
		this.responseCode = responseCode;
	}

	/**
	 * @return the responseMsg
	 */
	public String getResponseMsg() {
		return responseMsg;
	}

	/**
	 * @param responseMsg the responseMsg to set
	 */
	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}
}
