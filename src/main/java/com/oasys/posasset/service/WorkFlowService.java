package com.oasys.posasset.service;

import java.util.Date;
import java.util.Optional;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.oasys.helpdesk.dto.WorkflowDTO;
import com.oasys.helpdesk.entity.ChangeRequestEntity;
import com.oasys.helpdesk.entity.EalWastage;
import com.oasys.helpdesk.repository.ChangeRequestRepository;
import com.oasys.helpdesk.repository.EalWastageRepository;
import com.oasys.helpdesk.security.ServiceHeader;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.posasset.dto.WorkFlowStatusUpdateDTO;
import com.oasys.posasset.entity.DeviceDamageEntity;
import com.oasys.posasset.entity.DeviceReturnEntity;
import com.oasys.posasset.entity.DevicelostEntity;
import com.oasys.posasset.entity.EALRequestAECEntity;
import com.oasys.posasset.entity.EALRequestEntity;
import com.oasys.posasset.repository.DeviceReturnRepository;
import com.oasys.posasset.repository.DevicedamageRepository;
import com.oasys.posasset.repository.DevicelostRepository;
import com.oasys.posasset.repository.EALRequestAECRepository;
import com.oasys.posasset.repository.EALRequestRepository;

import lombok.extern.log4j.Log4j2;

@Service
//@Component
@Log4j2
public class WorkFlowService {

	@Value("${workflow.domain}")
	private String workflow;

	@Value("${domain}")
	private String domain;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	ServiceHeader serviceHeader;

	@Autowired
	HttpServletRequest headerRequest;

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private DevicelostRepository devicelostrepository;

	@Autowired
	private DevicedamageRepository devicedamagerepository;

	@Autowired
	private DeviceReturnRepository deviceReturnRepository;

	@Value("${spring.common.devtoken}")
	private String authtoken;

	@Autowired
	EALRequestRepository ealrequestRepository;

	@Autowired
	EALRequestAECRepository ealRequestAECRepository;

	@Autowired
	ChangeRequestRepository changereqrepo;

	@Autowired
	private EalWastageRepository ealWastageRepository;

//	  private final JdbcTemplate jdbcTemplate;
//
//	    public WorkFlowService(JdbcTemplate jdbcTemplate) {
//	        this.jdbcTemplate = jdbcTemplate;
//	    }

	public Boolean updateChangeRequestWorkFlowDetails(WorkFlowStatusUpdateDTO workflowStatusUpdateDto) {
		log.info(":::::Change request workflow response::::::" + workflowStatusUpdateDto);
		Boolean isSuccess = true;
		String cleanedApplicationNumber = workflowStatusUpdateDto.getApplicationNumber().trim().toString();
		log.info("::::::APPLICATIONNO::::::" + workflowStatusUpdateDto.getApplicationNumber());
		log.info("::::::cleanedApplicationNumber::::::" + cleanedApplicationNumber);
		log.info("::Test14::");
		try {
			Optional<ChangeRequestEntity> deptOptional = changereqrepo.findByChangereqApplnNo(cleanedApplicationNumber);
			if (deptOptional.isPresent()) {
				log.info("::::::Change request Enter::::::" + deptOptional.get().getChangereqApplnNo());
				deptOptional.get().setCurrentlyWorkwith(workflowStatusUpdateDto.getStage());
				deptOptional.get().setModifiedDate(new Date());
				changereqrepo.save(deptOptional.get());
				log.info("Change request workflowStatusUpdateDto" + workflowStatusUpdateDto);
			} else {
				log.info("No ChangeRequestEntity found for application number Testing 14: "
						+ workflowStatusUpdateDto.getApplicationNumber());
//				log.info("SQL QUERY::::Enter::::"); 
//				// Your SQL update query
//		        String updateQuery = "UPDATE change_request SET currently_workwith = ? WHERE changereq_appln_no = ?";
//
//		        // Parameters for the update query
//		        Object[] params = { workflowStatusUpdateDto.getStage(), cleanedApplicationNumber };
//
//		        // Execute the update query
//		        int rowsAffected = jdbcTemplate.update(updateQuery, params);
//
//		        // Optionally, you can check the number of rows affected
//		        System.out.println("Rows affected: " + rowsAffected);
//		        log.info("Rows affected: " + rowsAffected);
//		        log.info("SQLQUERY UPDATE:::" + updateQuery );
//		        log.info("PARAMS:::" + params.toString());
			}

		} catch (Exception exception) {
			log.error("===========error while updaing updateWorkFlowDetails data change request===",
					exception.fillInStackTrace());
			isSuccess = false;
		}
		return isSuccess;
	}

	public Boolean updateWorkFlowDetails(WorkFlowStatusUpdateDTO workflowStatusUpdateDto) {
		Boolean isSuccess = true;
		try {

			Optional<DevicelostEntity> DeptOptional = devicelostrepository
					.findByApplicationNumber(workflowStatusUpdateDto.getApplicationNumber());
			if (!DeptOptional.isPresent()) {
				log.info("::::INVALID APPLICATION NUMBER:::::");
			}

			DevicelostEntity devicelostEntity = DeptOptional.get();
			devicelostEntity.setCurrentlyWorkwith(workflowStatusUpdateDto.getStage());
			devicelostEntity.setModifiedDate(new Date());
			devicelostrepository.save(devicelostEntity);
			log.info("workflowStatusUpdateDto" + workflowStatusUpdateDto);
		} catch (Exception exception) {
			log.error("===========error while updaing updateWorkFlowDetails data===", exception);
			isSuccess = false;
		}
		return isSuccess;
	}

//	public Boolean updateWorkFlowDetailsdevicedamage(WorkFlowStatusUpdateDTO workflowStatusUpdateDto) {
//		Boolean isSuccess = true;
//		try {
//			log.info("::::BEFORE APPLICATION NUMBER:::::" + workflowStatusUpdateDto.getApplicationNumber());
//
//			Optional<DeviceDamageEntity> DeptOptional = devicedamagerepository
//					.findByDeviceDamageapplnno(workflowStatusUpdateDto.getApplicationNumber());
//			if (!DeptOptional.isPresent()) {
//				log.info("::::INVALID APPLICATION NUMBER:::::" + workflowStatusUpdateDto.getApplicationNumber());
//			}
//
//			log.info("::::AFTER APPLICATION NUMBER:::::" + workflowStatusUpdateDto.getApplicationNumber());
//
//			DeviceDamageEntity devicelostEntity = DeptOptional.get();
//			devicelostEntity.setCurrentlyWorkwith(workflowStatusUpdateDto.getStage());
//			devicelostEntity.setModifiedDate(new Date());
//			devicedamagerepository.save(devicelostEntity);
//			// log.info("workflowStatusUpdateDto" + workflowStatusUpdateDto);
//			log.info("Workflow status updated successfully for application number: "
//					+ workflowStatusUpdateDto.getApplicationNumber());
//		} catch (Exception exception) {
//			log.error("===========error while updaing updateWorkFlowDetails Device damage data===", exception);
//			isSuccess = false;
//		}
//		return isSuccess;
//	}

	// sk
	public Boolean updateWorkFlowDetailsdevicedamage(WorkFlowStatusUpdateDTO workflowStatusUpdateDto) {
		Boolean isSuccess = true;
		try {
			log.info("::::PRINT WORKFLOW STATUS UPDATE DTO:::::" + workflowStatusUpdateDto);

			log.info("::::BEFORE APPLICATION NUMBER:::::" + workflowStatusUpdateDto.getApplicationNumber());
			Optional<DeviceDamageEntity> deptOptional = devicedamagerepository
					.findByDeviceDamageapplnno(workflowStatusUpdateDto.getApplicationNumber());
			if (!deptOptional.isPresent()) {
				log.info("Invalid application number: " + workflowStatusUpdateDto.getApplicationNumber());
				isSuccess = false; // Set isSuccess to false if the application number is invalid
			}

			else {
				System.out.println();
				log.info("::::AFTER APPLICATION NUMBER:::::" + workflowStatusUpdateDto.getApplicationNumber());
				DeviceDamageEntity deviceDamageEntity = deptOptional.get();
				deviceDamageEntity.setCurrentlyWorkwith(workflowStatusUpdateDto.getStage());
				deviceDamageEntity.setModifiedDate(new Date());
				devicedamagerepository.save(deviceDamageEntity);
				log.info("Workflow status updated successfully for application number: "
						+ workflowStatusUpdateDto.getApplicationNumber());
			}
		} catch (Exception exception) {
			log.error("Error while updating WorkFlowDetails for Device damage data", exception);
			isSuccess = false;
		}
		return isSuccess;
	}

	public Boolean updateDeviceReturnWorkFlowDetails(WorkFlowStatusUpdateDTO workflowStatusUpdateDto) {
		Boolean isSuccess = true;
		try {
			Optional<DeviceReturnEntity> deviceReturn = deviceReturnRepository
					.findByApplicationNumber(workflowStatusUpdateDto.getApplicationNumber());
			if (deviceReturn.isPresent()) {
				deviceReturn.get().setCurrentlyWorkwith(workflowStatusUpdateDto.getStage());
				deviceReturn.get().setModifiedDate(new Date());
				deviceReturnRepository.save(deviceReturn.get());
			} else {
				log.info("::::INVALID APPLICATION NUMBER:::::");
			}
		} catch (Exception exception) {
			log.error("===========error while updaing updateWorkFlowDetails data===", exception);
			isSuccess = false;
		}
		return isSuccess;
	}

	public Boolean updateEALWorkFlowDetails(WorkFlowStatusUpdateDTO workflowStatusUpdateDto) {
		Boolean isSuccess = true;
		try {
			Optional<EALRequestEntity> eal = ealrequestRepository
					.findByRequestedapplnNo(workflowStatusUpdateDto.getApplicationNumber());
			if (eal.isPresent()) {
				eal.get().setCurrentlyWorkwith(workflowStatusUpdateDto.getStage());
				eal.get().setModifiedDate(new Date());
				// eal.get().setApprovedBy(workflowStatusUpdateDto.getStage());
				ealrequestRepository.save(eal.get());
				log.info("workflowStatusUpdateDto" + workflowStatusUpdateDto);
			} else {
				log.info("::::INVALID APPLICATION NUMBER:::::");
			}
		} catch (Exception exception) {
			log.error("===========error while updaing updateWorkFlowDetails EAL data===", exception);
			isSuccess = false;
		}
		return isSuccess;
	}

	public Boolean updateEALAECWorkFlowDetails(WorkFlowStatusUpdateDTO workflowStatusUpdateDto) {
		log.info("::::updateEALAECWorkFlowDetails::::"+ workflowStatusUpdateDto);
		Boolean isSuccess = true;
		try {
			log.info("::::workflowrequestDTO::::"+ workflowStatusUpdateDto);
			Optional<EALRequestAECEntity> eal = ealRequestAECRepository
					.findByRequestedapplnNo(workflowStatusUpdateDto.getApplicationNumber().trim().toString());
			if (eal.isPresent()) {
				eal.get().setCurrentlyWorkwith(workflowStatusUpdateDto.getStage());
				eal.get().setModifiedDate(new Date());
				// eal.get().setApprovedBy(workflowStatusUpdateDto.getStage());
				ealRequestAECRepository.save(eal.get());
				log.info("workflowStatusUpdateDto" + workflowStatusUpdateDto);
			} else {
				log.info("::::INVALID APPLICATION NUMBER:::::"+ workflowStatusUpdateDto.getApplicationNumber());
			}
		} catch (Exception exception) {
			log.error("===========error while updaing updateWorkFlowDetails EAL data===", exception);
			isSuccess = false;
		}
		return isSuccess;
	}

	public String callWorkFlowService(WorkflowDTO workflowStatusUpdateDto) {
		WorkflowDTO workflowDto = new WorkflowDTO();
		HttpHeaders headers = new HttpHeaders();

		/*
		 * String moduleName = EntityTypes.BREWERY.toString(); String workFlowName =
		 * ApplicationConstant.BOTLINE_REPAIR_FLOW_NAME_CODE;
		 */

		workflowDto.setApplicationNumber(workflowStatusUpdateDto.getApplicationNumber());
		workflowDto.setSubModuleNameCode(workflowStatusUpdateDto.getSubModuleNameCode());
		workflowDto.setCallbackURL(domain + "/helpdesk/paymentWorkFlow/updateWorkFlow");// this is call back URL to
																						// update the
		// table with required data after the
		// workflow completion
		workflowDto.setModuleNameCode(workflowStatusUpdateDto.getModuleNameCode());
		workflowDto.setSendBackTo(workflowStatusUpdateDto.getSendBackTo());
		workflowDto.setComments("");
		workflowDto.setEvent(workflowStatusUpdateDto.getEvent());
		workflowDto.setLevel(workflowStatusUpdateDto.getLevel());
		// workflow service calling
		try {
			StringBuffer uri = new StringBuffer(workflow);
			uri.append("/api/master/startExecution");
			Gson gson = new Gson();
			if (uri != null) {

				// call the method and set the header in service call

				headers.setContentType(MediaType.APPLICATION_JSON);
				headers.set("Authorization", "Bearer ".concat(authtoken));

				String accessToken = headerRequest.getHeader("X-Authorization");
				headers.set("X-Authorization", accessToken);

				HttpEntity<?> httpRequestEntity = new HttpEntity<>(workflowDto, headers);

				log.info("=======callWorkFlowService URL============" + uri.toString());

				log.info("=======jjjjjjjjjjj============" + gson.toJson(workflowDto));

				String response = restTemplate.postForObject(uri.toString(), httpRequestEntity, String.class);

				log.info("=======callWorkFlowService Response============" + response);

			}
		} catch (Exception exception) {
			// general error
			log.error("=======callWorkFlowService catch block============", exception);
		}

		return "a";
	}

	public String callDeviceReturnWorkFlowService(WorkflowDTO workflowStatusUpdateDto) {
		WorkflowDTO workflowDto = new WorkflowDTO();
		HttpHeaders headers = new HttpHeaders();
		workflowDto.setApplicationNumber(workflowStatusUpdateDto.getApplicationNumber());
		workflowDto.setSubModuleNameCode(workflowStatusUpdateDto.getSubModuleNameCode());
		workflowDto.setCallbackURL(domain + "/helpdesk/devicereturn/updateWorkFlow");// this is call back URL to update
																						// the
		// table with required data after the
		// workflow completion
		workflowDto.setModuleNameCode(workflowStatusUpdateDto.getModuleNameCode());
		workflowDto.setSendBackTo(workflowStatusUpdateDto.getSendBackTo());
		workflowDto.setComments("");
		workflowDto.setEvent(workflowStatusUpdateDto.getEvent());
		workflowDto.setLevel(workflowStatusUpdateDto.getLevel());
		// workflow service calling
		try {
			StringBuffer uri = new StringBuffer(workflow);
			uri.append("/api/master/startExecution");
			Gson gson = new Gson();
			if (uri != null) {

				// call the method and set the header in service call

				headers.setContentType(MediaType.APPLICATION_JSON);
				headers.set("Authorization", "Bearer ".concat(authtoken));

				String accessToken = headerRequest.getHeader("X-Authorization");
				headers.set("X-Authorization", accessToken);

				HttpEntity<?> httpRequestEntity = new HttpEntity<>(workflowDto, headers);

				log.info("=======callWorkFlowService URL============" + uri.toString());

				log.info("=======jjjjjjjjjjj============" + gson.toJson(workflowDto));

				String response = restTemplate.postForObject(uri.toString(), httpRequestEntity, String.class);

				log.info("=======callWorkFlowService Response============" + response);

			}
		} catch (Exception exception) {
			// general error
			log.error("=======callWorkFlowService catch block============", exception.fillInStackTrace());
		}

		return "a";
	}

	public String callEALWorkFlowService(WorkflowDTO workflowStatusUpdateDto) {
		WorkflowDTO workflowDto = new WorkflowDTO();
		HttpHeaders headers = new HttpHeaders();
		workflowDto.setApplicationNumber(workflowStatusUpdateDto.getApplicationNumber());
		workflowDto.setSubModuleNameCode(workflowStatusUpdateDto.getSubModuleNameCode());
		workflowDto.setCallbackURL(domain + "/helpdesk/ealrequest/updateWorkFlow");// this is call back URL to update
																					// the
																					// table with required data after
																					// the
		// workflow completion
		workflowDto.setModuleNameCode(workflowStatusUpdateDto.getModuleNameCode());
		workflowDto.setSendBackTo(workflowStatusUpdateDto.getSendBackTo());
		workflowDto.setComments("");
		workflowDto.setEvent(workflowStatusUpdateDto.getEvent());
		workflowDto.setLevel(workflowStatusUpdateDto.getLevel());
		// workflow service calling
//		try {

//			StringBuffer uri = new StringBuffer(workflow);
//			uri.append("/api/master/startExecution");

//			if (uri != null) {
//				
//				headers.setContentType(MediaType.APPLICATION_JSON);
//				headers.set("Authorization", "Bearer ".concat(authtoken));
//
//				String access_token = headerRequest.getHeader("X-Authorization");
//
//				headers.set("X-Authorization", access_token);
//
//				// call the method and set the header in service call
//
//				List<ClientHttpRequestInterceptor> interceptors = serviceHeader.getHeader(headers.get);
//				restTemplate.setInterceptors(interceptors);
//
//				log.info("=======callWorkFlowService catch block============" + uri.toString());
//
//				Gson gson = new Gson();
//
//				log.info("=======jjjjjjjjjjj============" + gson.toJson(workflowDto));
//
//				// call the elms service
//				String response = restTemplate.postForObject(uri.toString(), workflowDto, String.class);
//
//				log.info("=======callWorkFlowService catch block============" + response);
//
//			}

		try {
			StringBuffer uri = new StringBuffer(workflow);
			uri.append("/api/master/startExecution");
			Gson gson = new Gson();
			if (uri != null) {

				// call the method and set the header in service call

				headers.setContentType(MediaType.APPLICATION_JSON);
				headers.set("Authorization", "Bearer ".concat(authtoken));

				String accessToken = headerRequest.getHeader("X-Authorization");
				headers.set("X-Authorization", accessToken);

				HttpEntity<?> httpRequestEntity = new HttpEntity<>(workflowDto, headers);

				log.info("=======callWorkFlowService URL============" + uri.toString());

				log.info("=======jjjjjjjjjjj============" + gson.toJson(workflowDto));

				String response = restTemplate.postForObject(uri.toString(), httpRequestEntity, String.class);

				log.info("=======callWorkFlowService Response============" + response);

			}
		} catch (Exception exception) {
			// general error
			log.error("=======callWorkFlowService catch block============", exception);
		}

		return "a";
	}

	public String callEALAECWorkFlowService(WorkflowDTO workflowStatusUpdateDto) {
		WorkflowDTO workflowDto = new WorkflowDTO();
		HttpHeaders headers = new HttpHeaders();
		workflowDto.setApplicationNumber(workflowStatusUpdateDto.getApplicationNumber());
		workflowDto.setSubModuleNameCode(workflowStatusUpdateDto.getSubModuleNameCode());
		workflowDto.setCallbackURL(domain + "/helpdesk/ealrequest/aec/updateWorkFlow");// this is call back URL to
																						// update
																						// the
																						// table with required data
																						// after
																						// the
		// workflow completion
		log.info("--------------------" + domain + "/helpdesk/ealrequest/aec/updateWorkFlow");
		workflowDto.setModuleNameCode(workflowStatusUpdateDto.getModuleNameCode());
		workflowDto.setSendBackTo(workflowStatusUpdateDto.getSendBackTo());
		workflowDto.setComments("");
		workflowDto.setEvent(workflowStatusUpdateDto.getEvent());
		workflowDto.setLevel(workflowStatusUpdateDto.getLevel());
		// workflow service calling
//		try {

//			StringBuffer uri = new StringBuffer(workflow);
//			uri.append("/api/master/startExecution");

//			if (uri != null) {
//				
//				headers.setContentType(MediaType.APPLICATION_JSON);
//				headers.set("Authorization", "Bearer ".concat(authtoken));
//
//				String access_token = headerRequest.getHeader("X-Authorization");
//
//				headers.set("X-Authorization", access_token);
//
//				// call the method and set the header in service call
//
//				List<ClientHttpRequestInterceptor> interceptors = serviceHeader.getHeader(headers.get);
//				restTemplate.setInterceptors(interceptors);
//
//				log.info("=======callWorkFlowService catch block============" + uri.toString());
//
//				Gson gson = new Gson();
//
//				log.info("=======jjjjjjjjjjj============" + gson.toJson(workflowDto));
//
//				// call the elms service
//				String response = restTemplate.postForObject(uri.toString(), workflowDto, String.class);
//
//				log.info("=======callWorkFlowService catch block============" + response);
//
//			}

		try {
			StringBuffer uri = new StringBuffer(workflow);
			uri.append("/api/master/startExecution");
			Gson gson = new Gson();
			if (uri != null) {

				// call the method and set the header in service call

				headers.setContentType(MediaType.APPLICATION_JSON);
				headers.set("Authorization", "Bearer ".concat(authtoken));

				String accessToken = headerRequest.getHeader("X-Authorization");
				headers.set("X-Authorization", accessToken);

				HttpEntity<?> httpRequestEntity = new HttpEntity<>(workflowDto, headers);

				log.info("=======callWorkFlowService URL============" + uri.toString());

				log.info("=======jjjjjjjjjjj============" + gson.toJson(workflowDto));

				String response = restTemplate.postForObject(uri.toString(), httpRequestEntity, String.class);

				log.info("=======callWorkFlowService Response============" + response);

			}
		} catch (Exception exception) {
			// general error
			log.error("=======callWorkFlowService catch block============", exception);
		}

		return "a";
	}

	public String callWorkFlowChangerequestService(WorkflowDTO workflowStatusUpdateDto) {

		Optional<ChangeRequestEntity> deptOptional = changereqrepo
				.findByChangereqApplnNoIgnoreCase(workflowStatusUpdateDto.getApplicationNumber());
		if (deptOptional.isPresent()) {
			String applnno = deptOptional.get().getChangereqApplnNo();

			WorkflowDTO workflowDto = new WorkflowDTO();
			HttpHeaders headers = new HttpHeaders();

			workflowDto.setApplicationNumber(applnno);
			workflowDto.setSubModuleNameCode(workflowStatusUpdateDto.getSubModuleNameCode());
			workflowDto.setCallbackURL(domain + "/helpdesk/changerequest/updateWorkFlow");// this is call back URL to
																							// update the
			// table with required data after the
			// workflow completion
			workflowDto.setModuleNameCode(workflowStatusUpdateDto.getModuleNameCode());
			workflowDto.setSendBackTo(workflowStatusUpdateDto.getSendBackTo());
			workflowDto.setComments("");
			workflowDto.setEvent(workflowStatusUpdateDto.getEvent());
			workflowDto.setLevel(workflowStatusUpdateDto.getLevel());
			// workflow service calling
			try {
				StringBuffer uri = new StringBuffer(workflow);
				uri.append("/api/master/startExecution");
				Gson gson = new Gson();
				if (uri != null) {

					// call the method and set the header in service call

					headers.setContentType(MediaType.APPLICATION_JSON);
					headers.set("Authorization", "Bearer ".concat(authtoken));

					String accessToken = headerRequest.getHeader("X-Authorization");
					headers.set("X-Authorization", accessToken);

					HttpEntity<?> httpRequestEntity = new HttpEntity<>(workflowDto, headers);

					log.info("=======callWorkFlowService URL============" + uri.toString());

					log.info("=======jjjjjjjjjjj============" + gson.toJson(workflowDto));

					String response = restTemplate.postForObject(uri.toString(), httpRequestEntity, String.class);

					log.info("=======callWorkFlowService Response============" + response);

				}
			} catch (Exception exception) {
				// general error
				log.error("=======callWorkFlowService catch block============", exception);
			}
		}

		else {
			log.info(":::Application number not available::");
		}

		return "a";
	}

//	public String callEALWastageWorkFlowService(WorkflowDTO workflowStatusUpdateDto) {
//		WorkflowDTO workflowDto = new WorkflowDTO();
//		HttpHeaders headers = new HttpHeaders();
//		workflowDto.setApplicationNumber(workflowStatusUpdateDto.getApplicationNumber());
//		workflowDto.setSubModuleNameCode(workflowStatusUpdateDto.getSubModuleNameCode());
//		workflowDto.setCallbackURL(domain + "/helpdesk/eal/wastage/updateWastageWorkFlow");// this is call back URL to
//																							// update
//		// the
//		// table with required data after
//		// the
//		// workflow completion
//		workflowDto.setModuleNameCode(workflowStatusUpdateDto.getModuleNameCode());
//		workflowDto.setSendBackTo(workflowStatusUpdateDto.getSendBackTo());
//		workflowDto.setComments("");
//		workflowDto.setEvent(workflowStatusUpdateDto.getEvent());
//		workflowDto.setLevel(workflowStatusUpdateDto.getLevel());
//
//		try {
//			StringBuffer uri = new StringBuffer(workflow);
//			uri.append("/api/master/startExecution");
//			Gson gson = new Gson();
//			if (uri != null) {
//
//				// call the method and set the header in service call
//
//				headers.setContentType(MediaType.APPLICATION_JSON);
//				headers.set("Authorization", "Bearer ".concat(authtoken));
//
//				String accessToken = headerRequest.getHeader("X-Authorization");
//				headers.set("X-Authorization", accessToken);
//
//				HttpEntity<?> httpRequestEntity = new HttpEntity<>(workflowDto, headers);
//
//				log.info("=======callWorkFlowService URL============" + uri.toString());
//
//				log.info("=======jjjjjjjjjjj============" + gson.toJson(workflowDto));
//
//				String response = restTemplate.postForObject(uri.toString(), httpRequestEntity, String.class);
//
//				log.info("=======callWorkFlowService Response============" + response);
//
//			}
//		} catch (Exception exception) {
//			// general error
//			log.error("=======callWorkFlowService catch block============", exception);
//		}
//
//		return "a";
//	}
	
	public String callEALWastageWorkFlowService(WorkflowDTO workflowStatusUpdateDto) {
		WorkflowDTO workflowDto = new WorkflowDTO();
		HttpHeaders headers = new HttpHeaders();
		workflowDto.setApplicationNumber(workflowStatusUpdateDto.getApplicationNumber());
		workflowDto.setSubModuleNameCode(workflowStatusUpdateDto.getSubModuleNameCode());
		workflowDto.setCallbackURL(domain + "/helpdesk/eal/wastage/updateWastageWorkFlow");
		// workflow completion
		log.info("--------Domain URL------------" + domain + "/helpdesk/eal/wastage/updateWastageWorkFlow");
		workflowDto.setModuleNameCode(workflowStatusUpdateDto.getModuleNameCode());
		workflowDto.setSendBackTo(workflowStatusUpdateDto.getSendBackTo());
		workflowDto.setComments("");
		workflowDto.setEvent(workflowStatusUpdateDto.getEvent());
		workflowDto.setLevel(workflowStatusUpdateDto.getLevel());
		
		try {
			StringBuffer uri = new StringBuffer(workflow);
			uri.append("/api/master/startExecution");
			Gson gson = new Gson();
			if (uri != null) {

				// call the method and set the header in service call

				headers.setContentType(MediaType.APPLICATION_JSON);
				headers.set("Authorization", "Bearer ".concat(authtoken));

				String accessToken = headerRequest.getHeader("X-Authorization");
				headers.set("X-Authorization", accessToken);

				HttpEntity<?> httpRequestEntity = new HttpEntity<>(workflowDto, headers);

				log.info("=======callWorkFlowService URL============" + uri.toString());

				log.info("=======jjjjjjjjjjj============" + gson.toJson(workflowDto));

				String response = restTemplate.postForObject(uri.toString(), httpRequestEntity, String.class);

				log.info("=======callWorkFlowService Response============" + response);

			}
		} catch (Exception exception) {
			// general error
			log.error("=======callWorkFlowService catch block============", exception);
		}

		return "a";
	}

	
	
	public Boolean updateEALWastageWorkFlowDetails(WorkFlowStatusUpdateDTO workflowStatusUpdateDto) {		
		log.info("::::workflowrequestDTO::::"+ workflowStatusUpdateDto);
		Boolean isSuccess = true;
		String cleanedApplicationNumber = workflowStatusUpdateDto.getApplicationNumber().trim().toString();
		try {
			Optional<EalWastage> eal = ealWastageRepository
					.getByApplicationNo(cleanedApplicationNumber);
			log.info("::Object:::" + eal);
			if (eal.isPresent()) {
				eal.get().setCurrentlyWorkwith(workflowStatusUpdateDto.getStage());
				eal.get().setModifiedDate(new Date());
				ealWastageRepository.save(eal.get());
				log.info("workflowStatusUpdateDto" + workflowStatusUpdateDto);
			} else {
				log.info("::::INVALID APPLICATION NUMBER:::::"+ workflowStatusUpdateDto.getApplicationNumber());
			}
		} catch (Exception exception) {
			log.error("===========error while updaing updateWorkFlowDetails EAL data===", exception);
			isSuccess = false;
		}
		return isSuccess;
	
	}

}
