// 
// Decompiled by Procyon v0.5.36
// 

package com.oasys.helpdesk.utility;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


@Component
public class HitUrl {
	@Autowired
	RestTemplate restTemplate;

	@Value("${masterdata.domain.url}")
	private String masterDataUrl;

	@Value("${masterdata.domain.api.tank}")
	private String masterDataTank;

	@Value("${masterdata.domain.api.status}")
	private String masterDataStatus;

	@Value("${masterdata.domain.api.supplytype}")
	private String masterDataSupplyType;

	@Value("${masterdata.domain.api.routemaster}")
	private String masterDataRouteMaster;

	@Value("${masterdata.domain.api.verificationtype}")
	private String masterDataVerificationType;

	@Value("${masterdata.domain.api.molassestype}")
	private String masterDataMolassesType;

	@Autowired
	ObjectMapper objectMapper;

	public String getResponse(String url, Map<String, String> mapHeaders) {
		HttpHeaders headers = Library.getHeader(mapHeaders);
//		Map<String, String> params = new HashMap<String, String>();
//        params.put("id", "612");
		UriComponentsBuilder uriBuilder = Library.getUri(url, mapHeaders);
		// adding the query params to the URL
//        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
//                .queryParam("id", "612");

		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		return restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, entity, String.class).getBody();

	}
//	
//	public String getResponse(String url,Map<String, String> mapHeaders) {
//		HttpHeaders headers = Library.getHeader(mapHeaders);
//		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//		HttpEntity<String> entity = new HttpEntity<String>(headers);
//		return restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
//
//	}

	public String getPostResponse(String url, Object object, Map<String, String> mapHeaders) throws Exception {
		String requestJson = Library.getObjectToJson(object);
		HttpHeaders headers = Library.getHeader(mapHeaders);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
		return restTemplate.exchange(url, HttpMethod.POST, entity, String.class).getBody();
	}

	public String getPutResponse(String url, Object object, Map<String, String> mapHeaders) {
		String requestJson = Library.getObjectToJson(object);
		HttpHeaders headers = Library.getHeader(mapHeaders);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
		return restTemplate.exchange(url, HttpMethod.PUT, entity, String.class).getBody();
	}

	public String getDeleteResponse(String url, Object object, Map<String, String> mapHeaders) {
		String requestJson = Library.getObjectToJson(object);
		HttpHeaders headers = Library.getHeader(mapHeaders);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
		return restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class).getBody();
	}

	public String getResponse(String url, Map<String, String> mapHeaders, Map<String, String> mapBody) {
		HttpHeaders headers = Library.getHeader(mapHeaders);
		UriComponentsBuilder uriBuilder = Library.getUri(url, mapBody);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		return restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, entity, String.class).getBody();
	}

}