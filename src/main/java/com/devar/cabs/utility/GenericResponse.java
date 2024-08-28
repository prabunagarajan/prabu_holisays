package com.devar.cabs.utility;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	/** e@Datarror code. 0 if success else unique error code value */


	@Override
	public String toString() {
		return "GenericResponse [status=" + status + ", errorDescription=" + errorDescription + ", errorCode="
				+ errorCode + ", userDisplayMesg=" + userDisplayMesg + ", Data=" + Data + "]";
	}
	private String status;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getErrorDescription() {
		return errorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getUserDisplayMesg() {
		return userDisplayMesg;
	}
	public void setUserDisplayMesg(String userDisplayMesg) {
		this.userDisplayMesg = userDisplayMesg;
	}
	public Object getData() {
		return Data;
	}
	public void setData(Object data) {
		Data = data;
	}
	private String errorDescription;
	private int errorCode;
	private String userDisplayMesg;
	private Object Data;
	private List<Long> count;
	
	//{"statusInfo":{"status":"S","errorDescription":"Success","errorCode":100

}
