package com.oasys.helpdesk.conf.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;



@ControllerAdvice
public class InvalidDataValidation extends RuntimeException{
	private static final long serialVersionUID = 3598859628828600953L;

	public InvalidDataValidation() {
		super();
	}

	public InvalidDataValidation(String message, Throwable cause) {
		super(message, cause);

	}

	public InvalidDataValidation(String message) {
		super(message);

	}

	public InvalidDataValidation(Throwable cause) {
		super(cause);

	}

}
