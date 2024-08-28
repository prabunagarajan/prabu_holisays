package com.devar.cabs.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;



@ControllerAdvice
public class InvalidEmployeeIdValidation extends RuntimeException{
	private static final long serialVersionUID = 3598859628828600953L;

	public InvalidEmployeeIdValidation() {
		super();
	}

	public InvalidEmployeeIdValidation(String message, Throwable cause) {
		super(message, cause);

	}

	public InvalidEmployeeIdValidation(String message) {
		super(message);

	}

	public InvalidEmployeeIdValidation(Throwable cause) {
		super(cause);

	}

}
