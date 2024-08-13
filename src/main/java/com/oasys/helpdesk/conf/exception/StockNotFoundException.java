package com.oasys.helpdesk.conf.exception;


public class StockNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 7949165638616877310L;

	public StockNotFoundException() {
		super();
	}

	public StockNotFoundException(String message, Throwable cause) {
		super(message, cause);

	}

	public StockNotFoundException(String message) {
		super(message);

	}

	public StockNotFoundException(Throwable cause) {
		super(cause);

	}

}
