package com.devar.cabs.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class DeviceAlreadyExistValidation  extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4789677120773169117L;


	public DeviceAlreadyExistValidation() {
		super();
	}

	public DeviceAlreadyExistValidation(String message, Throwable cause) {
		super(message, cause);

	}

	public DeviceAlreadyExistValidation(String message) {
		super(message);

	}

	public DeviceAlreadyExistValidation(Throwable cause) {
		super(cause);

	}
	
}
