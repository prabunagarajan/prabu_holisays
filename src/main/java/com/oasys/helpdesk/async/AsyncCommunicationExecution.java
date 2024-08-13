package com.oasys.helpdesk.async;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.oasys.helpdesk.common.EmailDetails;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.dto.SMSDetails;
import com.oasys.helpdesk.dto.WorkflowDTO;
import com.oasys.helpdesk.entity.PasswordReset;
import com.oasys.helpdesk.entity.UserEntity;
import com.oasys.helpdesk.repository.EmailVerificationRepository;
import com.oasys.helpdesk.repository.PasswordResetRepository;
import com.oasys.helpdesk.response.BaseResponse;
import com.oasys.helpdesk.security.ApiResponse;
import com.oasys.helpdesk.security.ServiceHeader;
import com.oasys.helpdesk.utility.CommonDataController;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class AsyncCommunicationExecution {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private ServiceHeader serviceHeader;
	
	@Autowired
	private PasswordResetRepository passwordResetRepository;
	
	@Autowired
	private CommonDataController commonDataController;
	

	@Autowired
	private EmailVerificationRepository emailVerificationRepository;

	
	//@Autowired
	//private MessageSource messageSource;
	
    /**
	 * This method will be used when any of the service implementation needs to call the workflow service to initiate the workflow
	 * @param token
	 * @param applicationNumber
	 * @param moduleName
	 * @param workFlowName
	 * @param sendBackLevels
	 * @param comments
	 * @param event
	 * @param level
	 * @param updateURL
	 */
    @Async("processExecutorHelpdesk")
	public void callWorkFlowService(String token,String applicationNumber,String moduleName,String subModuleNameCode,String sendBackLevels,
				String comments,String event,String level,String updateURL,Locale locale) {
		WorkflowDTO workflowDto = new WorkflowDTO();
		workflowDto.setApplicationNumber(applicationNumber);
		workflowDto.setSubModuleNameCode(subModuleNameCode);
		workflowDto.setCallbackURL(updateURL);// this is call back URL to update the table with required data after the workflow completion
		workflowDto.setModuleNameCode(moduleName);
		workflowDto.setSendBackTo(sendBackLevels);
		workflowDto.setComments(comments);
		workflowDto.setEvent(event);
		workflowDto.setLevel(level);
		// workflow service calling			
		try{
			URI uri = serviceHeader.getServiceEndPointByServiceName(Constant.WORKFLOW_SERVICE_NAME,Constant.WORKFLOW_SERVICE_CONTEXT_PATH);
			if(uri!=null){
				//call the method and set the header in service call
				List<ClientHttpRequestInterceptor> interceptors = serviceHeader.getHeader(token);
				restTemplate.setInterceptors(interceptors);
				String url = uri+"/api/master/startExecution";
				//call the elms service 
				ResponseEntity<BaseResponse> result  =  restTemplate.postForEntity(url, workflowDto, BaseResponse.class);
			}
		}catch(Exception exception){
			//general error
			log.error("=======callWorkFlowService catch block============",exception);
		}
	}
    
    @Async("processExecutorHelpdesk")
	public void sendPasswordResetEMail(String token, Locale locale, String event, String subEvent,
			String link, String userName, UserEntity user) {
    	try{
    		Map<String,Object> inputMap = new LinkedHashMap<>();
    		inputMap.put("link", link); //password reset link
    		inputMap.put("user", userName); //user name
    		EmailDetails emailDetails = new EmailDetails(user.getEmailId(), null,event,subEvent,inputMap,locale.getLanguage(),true);
    		ApiResponse apiResponse = commonDataController.processEmail(null, emailDetails, locale);
    		log.info("email notification response :: ", apiResponse);
    		if(apiResponse.getSuccess()) {
    			PasswordReset passwordRest = new PasswordReset();
    			passwordRest.setToken(token);
    			passwordRest.setUser(user);
    			passwordRest.setExpiryDate(30);
    			passwordResetRepository.save(passwordRest);
    		}
		}catch(Exception exception){
			log.error("=====error while sending mail to users===", exception);
		}
    }
    
   // @Async("processExecutorHelpdesk")
    public ApiResponse sendOTPViaEmail(Locale locale,String event,String subEvent,String otp,String emailId) {
    	ApiResponse apiResponse = null;
    	try{
    		Map<String,Object> inputMap = new LinkedHashMap<>();
    		inputMap.put("otp", otp); 
    		inputMap.put("user", "User");
    		EmailDetails emailDetails = new EmailDetails(emailId, null,event,subEvent,inputMap,locale.getLanguage(),true);
    	    apiResponse = commonDataController.processEmail(null,emailDetails,locale);
    		log.info("email notification response ::{} ", apiResponse);
    		
    	}catch(Exception exception){
			log.error("=====error while sending mail to users==={}", exception);
			return null;
		}
    	return apiResponse;
    }
    
   // @Async("processExecutorHelpdesk")
	public void sendRegisteredUserPasswordViaEmail(String token, Locale locale, String password, String userName,
			UserEntity user) {
		try {
			
			EmailDetails emailDetails = new EmailDetails(user.getEmailId(), null, null, null, null,
					locale.getLanguage(), true);
			emailDetails.setFrom(Constant.EMAIL_FROM);
			emailDetails.setMessage(Constant.USER_REGISTRATION_TEMPLATE_CONTENT);
			emailDetails.setMessage(emailDetails.getMessage().replace(Constant.USER_PARAM, userName)
					.replace(Constant.EMAIL_PARAM, user.getEmailId()).replace(Constant.PASSWORD_PARAM, password));
			emailDetails.setSubject(Constant.USER_REGISTRATION_EMAIL_SUBJECT);
			ApiResponse apiResponse = commonDataController.processEmailViaThemeleaf(null, emailDetails, locale);
			log.info(" sendRegisteredUserPasswordViaEmail response :: ", apiResponse);

		} catch (Exception exception) {
			log.error("=====error while sending mail to users===", exception);
		}
	}
    
      
}
