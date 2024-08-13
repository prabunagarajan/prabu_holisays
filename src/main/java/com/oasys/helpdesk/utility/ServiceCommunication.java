package com.oasys.helpdesk.utility;

import java.net.URI;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.response.BaseResponse;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.security.ServiceHeader;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class ServiceCommunication {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ServiceHeader serviceHeader;

	@Autowired
	HttpServletRequest headerRequest;

	@Autowired
	EntityManager entityManager;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	HitUrl hiturl;


	/**
	 * This method will be used to find the user details based on the token received
	 * and will be called from filter
	 * 
	 * @param jwtToken
	 * @return
	 */
	/*
	 * public AuthenticationDTO callGetServiceToFetchUserByUserId(String jwtToken) {
	 * List<ClientHttpRequestInterceptor> interceptors =
	 * serviceHeader.getHeader(jwtToken);
	 * restTemplate.setInterceptors(interceptors); URI uri =
	 * serviceHeader.getServiceEndPointByServiceName(Constant.USER_SERVICE,
	 * Constant.USER_CONTEXT_PATH); String url = uri +
	 * "/authentication/check_token?token=" + jwtToken; AuthenticationDTO
	 * userDetails = restTemplate.getForObject(url, AuthenticationDTO.class);
	 * userDetails.setToken(jwtToken); return userDetails;
	 * 
	 * }
	 */

	/**
	 * This method will be used when any of the service implementation needs to call
	 * the workflow service to initiate the workflow
	 * 
	 * @param token
	 * @param applicationNumber
	 * @param moduleName
	 * @param workFlowName
	 * @param sendBackLevels
	 * @param comments
	 * @param event
	 * @param level
	 * @param updateURL
	 *//*
		 * public void callWorkFlowService(String token, String applicationNumber,
		 * String moduleName, String workFlowName, String sendBackLevels, String
		 * comments, String event, String level, String updateURL) { // this is call
		 * back start URI uri =
		 * serviceHeader.getServiceEndPointByServiceName(Constant.LICENSE_SERVICE_NAME,
		 * Constant.LICENSE_MANAGEMENT_SERVICE_CONTEXT_PATH);
		 * List<ClientHttpRequestInterceptor> interceptors =
		 * serviceHeader.getHeader(token); restTemplate.setInterceptors(interceptors);
		 * // String url = uri + "/CLRegistration/updateCLRegistrationStatus"; String
		 * url = updateURL; WorkflowDTO workflowDto = new WorkflowDTO();
		 * workflowDto.setApplicationNumber(applicationNumber);
		 * workflowDto.setWorkflowName(workFlowName); workflowDto.setCallbackURL(url);//
		 * this is call back URL to update the table with required data after the //
		 * workflow completion workflowDto.setModuleNameCode(moduleName);
		 * workflowDto.setSendBackTo(sendBackLevels); workflowDto.setComments(comments);
		 * workflowDto.setEvent(event); workflowDto.setLevel(level); // workflow service
		 * calling try { uri =
		 * serviceHeader.getServiceEndPointByServiceName(Constant.WORKFLOW_SERVICE_NAME,
		 * Constant.WORKFLOW_SERVICE_CONTEXT_PATH); interceptors =
		 * serviceHeader.getHeader(token); restTemplate.setInterceptors(interceptors);
		 * url = uri + "/api/master/startExecution"; ResponseEntity<BaseResponse> data =
		 * restTemplate.postForEntity(url, workflowDto, BaseResponse.class); } catch
		 * (Exception e) { log.error(e.getMessage()); } }
		 */

	/**
	 * This method will be used to validate the business user
	 * 
	 * @param headers
	 * @param token
	 * @return
	 */
	/*
	 * public AuthenticationDTO executeBusinessUser(String jwtToken) {
	 * List<ClientHttpRequestInterceptor> interceptors =
	 * serviceHeader.getHeader(jwtToken);
	 * restTemplate.setInterceptors(interceptors); URI uri =
	 * serviceHeader.getServiceEndPointByServiceName(Constant.USER_SERVICE,
	 * Constant.USER_CONTEXT_PATH); String url = uri +
	 * "/authentication/check_token?token=" + jwtToken; log.info(url);
	 * AuthenticationDTO userDetails = restTemplate.getForObject(url,
	 * AuthenticationDTO.class); return userDetails;
	 * 
	 * }
	 */

	/**
	 * This method will be used to validate the customer users
	 * 
	 * @param headers
	 * @param token
	 * @return
	 */
	/*
	 * public AuthenticationDTO executeCustomerUser(String jwtToken) {
	 * List<ClientHttpRequestInterceptor> interceptors =
	 * serviceHeader.getHeader(jwtToken);
	 * restTemplate.setInterceptors(interceptors); URI uri =
	 * serviceHeader.getServiceEndPointByServiceName(Constant.LICENSE_SERVICE_NAME,
	 * Constant.LICENSE_MANAGEMENT_SERVICE_CONTEXT_PATH); String url = uri +
	 * "/customer/authenticateCustomerToken?customerToken=" + jwtToken;
	 * AuthenticationDTO userDetails = restTemplate.getForObject(url,
	 * AuthenticationDTO.class); return userDetails;
	 * 
	 * }
	 */

	public void updateIndentAcceptanceStatus(String indentNo, String indentApprovalStatus) {
		// WholesaleIndentRequestDetails indentRequest = new
		// WholesaleIndentRequestDetails();
		String access_token = headerRequest.getHeader("X-Authorization");
		List<ClientHttpRequestInterceptor> interceptors = serviceHeader.getHeader(access_token);
		restTemplate.setInterceptors(interceptors);
		URI uri = serviceHeader.getServiceEndPointByServiceName(Constant.RETAIL_SERVICE_NAME,
				Constant.RETAIL_SERVICE_CONTEXT_PATH);
		String url = uri + "/indentRequest/updateIndentRequestOrderStatus?indentNo=" + indentNo + "&status="
				+ indentApprovalStatus;
		restTemplate.put(url, BaseResponse.class);

	}
	
	/**
	 * This method will be used to fetch indent  request details from retail
	 * 
	 * */
//	public RetailIndentDetails fetchRetailIndentDetails(String indentNo) throws JsonProcessingException {
//		
//		RetailIndentDetails retailDetails = new RetailIndentDetails() ;
//		String access_token = headerRequest.getHeader("X-Authorization");
//		List<ClientHttpRequestInterceptor> interceptors = serviceHeader.getHeader(access_token);
//		restTemplate.setInterceptors(interceptors);
//		URI uri = serviceHeader.getServiceEndPointByServiceName(Constant.RETAIL_SERVICE_NAME,
//				Constant.RETAIL_SERVICE_CONTEXT_PATH);
//		String url = uri + "/indentRequest/getIndentRequestByIndentNo?indentNo=" + indentNo;		
//		GenericResponse generic = restTemplate.getForObject(url, GenericResponse.class);
//	    retailDetails = objectMapper.convertValue(generic.getData(), RetailIndentDetails.class);		
//		return retailDetails;
//	}


}
