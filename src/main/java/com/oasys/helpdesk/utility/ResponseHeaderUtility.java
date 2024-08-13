package com.oasys.helpdesk.utility;

import org.springframework.http.HttpHeaders;

public class ResponseHeaderUtility {
	static HttpHeaders responseHeaders = null;

	static {
		responseHeaders = new HttpHeaders();
		responseHeaders.set("content-type", "application/json");
	}

	public static HttpHeaders HttpHeadersConfig() {
		return responseHeaders;
	}
	
}
