package com.devar.cabs.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;



@ControllerAdvice
public class InvalidUserValidation extends RuntimeException{
	private static final long serialVersionUID = 3598859628828600953L;

	public InvalidUserValidation() {
		super();
	}

	public InvalidUserValidation(String message, Throwable cause) {
		super(message, cause);

	}

	public InvalidUserValidation(String message) {
		super(message);

	}

	public InvalidUserValidation(Throwable cause) {
		super(cause);

	}

}
