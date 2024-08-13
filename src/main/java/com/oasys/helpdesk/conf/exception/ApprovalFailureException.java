package com.oasys.helpdesk.conf.exception;


public class ApprovalFailureException extends RuntimeException{
	
	private static final long serialVersionUID = -3471131974334000133L;

	public ApprovalFailureException() {
		super();
	}

	public ApprovalFailureException(String message, Throwable cause) {
		super(message, cause);

	}

	public ApprovalFailureException(String message) {
		super(message);

	}

	public ApprovalFailureException(Throwable cause) {
		super(cause);

	}

}
