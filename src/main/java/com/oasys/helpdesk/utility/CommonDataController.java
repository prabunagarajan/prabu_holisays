package com.oasys.helpdesk.utility;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.oasys.helpdesk.common.EmailDetails;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.CommonMasterRequestDTO;
import com.oasys.helpdesk.dto.EntityMasterDTO;
import com.oasys.helpdesk.dto.SMSDetails;
import com.oasys.helpdesk.entity.UserEntity;
import com.oasys.helpdesk.repository.UserRepository;
import com.oasys.helpdesk.response.DistrictDTO;
import com.oasys.helpdesk.response.DistrictMasterResponse;
import com.oasys.helpdesk.response.DistrictResponseDto;
import com.oasys.helpdesk.response.EntityResponseDto;
import com.oasys.helpdesk.response.EntityType;
import com.oasys.helpdesk.response.TalukResponseDto;
import com.oasys.helpdesk.response.UserDetails;
import com.oasys.helpdesk.response.UserMasterResponseDto;
import com.oasys.helpdesk.response.UserResponseDto;
import com.oasys.helpdesk.response.VillageResponseDto;
import com.oasys.helpdesk.security.ApiResponse;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.security.ServiceHeader;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class CommonDataController {

	@Value("${masterdata.domain.url}")
	private String masterManagementHost;

	@Value("${masterdata.domain.api.grade}")
	private String masterDataGrade;

	@Value("${masterdata.domain.api.material}")
	private String masterDataMaterial;

	@Value("${masterdata.domain.api.supplytype}")
	private String masterDataSupplyType;

	@Value("${masterdata.domain.api.routemaster}")
	private String masterDataRouteMaster;

	@Value("${masterdata.domain.api.verificationtype}")
	private String masterDataVerificationType;

	@Value("${masterdata.domain.api.molassestype}")
	private String masterDataMolassesType;

	@Value("${masterdata.domain.url}")
	private String masterDataUrl;

	//@Value("${usermanagement.service.url}")
	private String userManagementUrl;
	
	

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	HitUrl hiturl;

	@Autowired
	HttpServletRequest headerRequest;

	@Autowired
	ObjectMapper objectMapper;
	
	
	@Autowired
	ServiceHeader serviceHeader;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private MessageSource messageSource;
	
	@Value("${spring.common.username}")
	private String useranme;
	
	@Value("${spring.common.password}")
	private String password;

	
	//@Autowired
	//UtilityService utilityService;

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

	

	

	
	

	

	

	

	

	

	

	

	
	
	

	
	
	

	


	

	
	
//	private void getAuthenticationDTO() {
//		if (SecurityContextHolder.getContext().getAuthentication() == null) {
//			log.info("getLoginUserEntityId() - SecurityContextHolder.getContext().getAuthentication()  is null");
//			throw new RecordNotFoundException("Login User Not Found");
//		}
//		Object authObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		if (authObject == null) {
//			log.info("getCurrentUserId() - authObject  is null");
//			throw new RecordNotFoundException("Login User Not Found");
//		} else {
//			log.info("getCurrentUser()-UserMaster - found");
//			authenticationDTO = (AuthenticationDTO) authObject;
//		}
//	}


	
//	public EntityMasterDTO getEntityById(Long id) {
//		EntityMasterDTO entityMasterDTO = new EntityMasterDTO();
//		String access_token = null;
////		HttpHeaders headers = new HttpHeaders();
//		Map<String, String> headers = new HashMap<String, String>();
//		try {
//			if (id != null && id > 0) {
//				StringBuffer uri = new StringBuffer(userManagementUrl);
//				uri.append("entity/getEntityById?id=" + id);
//				access_token = headerRequest.getHeader("X-Authorization");
//				headers.put("X-Authorization", access_token);
//				String data = hiturl.getResponse(uri.toString(), headers);
//				ObjectNode node = objectMapper.readValue(data, ObjectNode.class);
//				entityMasterDTO = objectMapper.treeToValue(node.get("data"), EntityMasterDTO.class);
//			} else {
//				throw new RecordNotFoundException();
//			}
//		} catch (Exception e) {
//			return null;
//		}
//		return entityMasterDTO;
//	}
	
	public DistrictResponseDto getDistrictById(Long id) {
		DistrictResponseDto result = new DistrictResponseDto();
		try {
			if (id != null) {
				StringBuffer uri = new StringBuffer(masterManagementHost);
				uri.append("/district/getDistrictById");
				Map<String, String> mapBody = new HashMap<String, String>();
				Map<String, String> mapHeaders = new HashMap<String, String>();
				mapBody.put("id", id.toString());
				String data = hiturl.getResponse(uri.toString(), mapHeaders, mapBody);
				ObjectNode node = objectMapper.readValue(data, ObjectNode.class);
				result = (DistrictResponseDto) objectMapper.treeToValue(node.get("data"),
						DistrictResponseDto.class);
			} else {
				throw new RecordNotFoundException();
			}
		} catch (Exception e) {
			return null;
		}
		return result;
	}
	
	public TalukResponseDto getTalukById(Long id) {
		TalukResponseDto result = new TalukResponseDto();
		try {
			if (id != null) {
				StringBuffer uri = new StringBuffer(masterManagementHost);
				uri.append("/talukMaster/getTalukById");
				Map<String, String> mapBody = new HashMap<String, String>();
				Map<String, String> mapHeaders = new HashMap<String, String>();
				mapBody.put("id", id.toString());
				String data = hiturl.getResponse(uri.toString(), mapHeaders, mapBody);
				ObjectNode node = objectMapper.readValue(data, ObjectNode.class);
				result = (TalukResponseDto) objectMapper.treeToValue(node.get("data"),
						TalukResponseDto.class);
			} else {
				throw new RecordNotFoundException();
			}
		} catch (Exception e) {
			return null;
		}
		return result;
	}
	
	public VillageResponseDto getVillageById(Long id) {
		VillageResponseDto result = new VillageResponseDto();
		try {
			if (id != null) {
				StringBuffer uri = new StringBuffer(masterManagementHost);
				uri.append("/villageMaster/getVillageById");
				Map<String, String> mapBody = new HashMap<String, String>();
				Map<String, String> mapHeaders = new HashMap<String, String>();
				mapBody.put("id", id.toString());
				String data = hiturl.getResponse(uri.toString(), mapHeaders, mapBody);
				ObjectNode node = objectMapper.readValue(data, ObjectNode.class);
				result = (VillageResponseDto) objectMapper.treeToValue(node.get("data"),
						VillageResponseDto.class);
			} else {
				throw new RecordNotFoundException();
			}
		} catch (Exception e) {
			return null;
		}
		return result;
	}
	
	
	public EntityResponseDto getEntityNameById(Long id) {
		EntityResponseDto result = new EntityResponseDto();
		try {
			if (id != null) {
				StringBuffer uri = new StringBuffer(userManagementUrl);
				uri.append("/entity/getEntityById");
				Map<String, String> mapBody = new HashMap<String, String>();
				Map<String, String> mapHeaders = new HashMap<String, String>();
				mapBody.put("id", id.toString());
				String data = hiturl.getResponse(uri.toString(), mapHeaders, mapBody);
				ObjectNode node = objectMapper.readValue(data, ObjectNode.class);
				result = (EntityResponseDto) objectMapper.treeToValue(node.get("data"),
						EntityResponseDto.class);
			} else {
				throw new RecordNotFoundException();
			}
		} catch (Exception e) {
			return null;
		}
		return result;
	}
	
	public EntityMasterDTO getEntityById(Integer id) {
		EntityMasterDTO entityMasterDTO = new EntityMasterDTO();
		String access_token = null;
//		HttpHeaders headers = new HttpHeaders();
		Map<String, String> headers = new HashMap<String, String>();
		try {
			if (id != null && id > 0) {
				StringBuffer uri = new StringBuffer(userManagementUrl);
				uri.append("entity/getEntityById?id=" + id);
				access_token = headerRequest.getHeader("X-Authorization");
				headers.put("X-Authorization", access_token);
				String data = hiturl.getResponse(uri.toString(), headers);
				ObjectNode node = objectMapper.readValue(data, ObjectNode.class);
				entityMasterDTO = objectMapper.treeToValue(node.get("data"), EntityMasterDTO.class);
			} else {
				throw new RecordNotFoundException();
			}
		} catch (Exception e) {
			return null;
		}
		return entityMasterDTO;
	}
	
//	public EntityTypeMasterDTO getEntityTypeById(Integer entityTypeId) {
//		EntityTypeMasterDTO entityTypeResponseDto = new EntityTypeMasterDTO();
//		String access_token = null;
////		HttpHeaders headers = new HttpHeaders();
//		Map<String, String> headers = new HashMap<String, String>();
//		try {
//			if (entityTypeId != null && entityTypeId > 0) {
//				StringBuffer uri = new StringBuffer(userManagementUrl);
//				uri.append("entityType/getEntityTypeById?entityTypeId=" + entityTypeId);
//				access_token = headerRequest.getHeader("X-Authorization");
//				headers.put("X-Authorization", access_token);
//				String data = hiturl.getResponse(uri.toString(), headers);
//				ObjectNode node = objectMapper.readValue(data, ObjectNode.class);
//				entityTypeResponseDto = objectMapper.treeToValue(node.get("data"), EntityTypeMasterDTO.class);
//			} else {
//				throw new RecordNotFoundException();
//			}
//		} catch (Exception e) {
//			return null;
//		}
//		return entityTypeResponseDto;
//	}
	
	public UserMasterResponseDto searchUser(String search) {
		UserMasterResponseDto entityTypeResponseDto = new UserMasterResponseDto();
		String access_token = null;
//		HttpHeaders headers = new HttpHeaders();
		Map<String, String> headers = new HashMap<String, String>();
		try {
			if (!search.isEmpty()) {
				StringBuffer uri = new StringBuffer(userManagementUrl);
				uri.append("usermanager/searchUser?search=" + search);
				access_token = headerRequest.getHeader("X-Authorization");
				headers.put("X-Authorization", access_token);
				String data = hiturl.getResponse(uri.toString(), headers);
				ObjectNode node = objectMapper.readValue(data, ObjectNode.class);
				entityTypeResponseDto = objectMapper.treeToValue(node.get("data"), UserMasterResponseDto.class);
			} else {
				throw new RecordNotFoundException();
			}
		} catch (Exception e) {
			return null;
		}
		return entityTypeResponseDto;
	}
	
//	public EntityMasterDTO getUserByDesignationCode(String code) {
//		EntityMasterDTO entityMasterDTO = new EntityMasterDTO();
//		String access_token = null;
////		HttpHeaders headers = new HttpHeaders();
//		Map<String, String> headers = new HashMap<String, String>();
//		try {
//			if (id != null && id > 0) {
//				StringBuffer uri = new StringBuffer(userManagementUrl);
//				uri.append("usermanager/getUserByDesignationCode?code=" + code);
//				access_token = headerRequest.getHeader("X-Authorization");
//				headers.put("X-Authorization", access_token);
//				String data = hiturl.getResponse(uri.toString(), headers);
//				ObjectNode node = objectMapper.readValue(data, ObjectNode.class);
//				entityMasterDTO = objectMapper.treeToValue(node.get("data"), EntityMasterDTO.class);
//			} else {
//				throw new RecordNotFoundException();
//			}
//		} catch (Exception e) {
//			return null;
//		}
//		return entityMasterDTO;
//	}
	
	// resultList = (List<ProductTypeResponseDto>)
	// objectMapper.treeToValue(node.get("data"),ProductTypeResponseDto.class);
	
//	public List<UserMasterResponseDto> getUserByDesignationCode(String code) {
//		List<UserMasterResponseDto> resultList = new ArrayList<UserMasterResponseDto>();
//		try {
//
//			StringBuffer uri = new StringBuffer(userManagementUrl);
//			uri.append("usermanager/getUserByDesignationCode?code=" + code);
//			Map<String, String> mapBody = new HashMap<String, String>();
//			Map<String, String> mapHeaders = new HashMap<String, String>();
//			String data = hiturl.getResponse(uri.toString(), mapHeaders, mapBody);
//			ObjectNode node = objectMapper.readValue(data, ObjectNode.class);
//			node.get("data").forEach(obj -> {
//				try {
//					resultList.add(objectMapper.treeToValue(obj, UserMasterResponseDto.class));
//				} catch (JsonProcessingException e) {
//					log.error("Exception occured in getUserByDesignationCode - >" + e);
//				}
//			});
//			
//
//		} catch (Exception e) {
//
//		}
//		return resultList;
//	}

	
//	public List<UserMasterResponseDto> getUserByDesignationCode(String code) {
//		List<UserMasterResponseDto> resultList = new ArrayList<UserMasterResponseDto>();
//		String access_token = null;
//		HttpHeaders headers = new HttpHeaders();
//		Map<String, String> mapBody = new HashMap<String, String>();
//		Map<String, String> mapHeaders = new HashMap<String, String>();
//		try {
//		
//			log.info("access_token----------------" + access_token);
//			URI uri = serviceHeader.getServiceEndPointByServiceName(Constant.USER_SERVICE,
//					Constant.USER_CONTEXT_PATH);
//
//			
//			String url = uri + "/usermanager/getUserByDesignationCode?code="+code;
//			log.info("urlll"+ url);
//			String data = hiturl.getResponse(url, mapHeaders, mapBody);
//			ObjectNode node = objectMapper.readValue(data, ObjectNode.class);
//			node.get("data").forEach(obj -> {
//				try {
//					resultList.add(objectMapper.treeToValue(obj, UserMasterResponseDto.class));
//				} catch (JsonProcessingException e) {
//					log.error("Exception occured in getUserByDesignationCode - >" + e);
//				}
//			});
//		
//
//		} catch (Exception e) {
//
//		}
//		return resultList;
//	}
	
//	public UserMasterResponseDto getUserById(Long id) {
//		UserMasterResponseDto userMasterResponseDto = new UserMasterResponseDto();
//		String access_token = null;
//		Map<String, String> headers = new HashMap<String, String>();
//		try {
//			if (id !=null) {
//				StringBuffer uri = new StringBuffer(userManagementUrl);
//				uri.append("usermanager/getUserById?id=" + id);
//				access_token = headerRequest.getHeader("X-Authorization");
//				headers.put("X-Authorization", access_token);
//				String data = hiturl.getResponse(uri.toString(), headers);
//				ObjectNode node = objectMapper.readValue(data, ObjectNode.class);
//				userMasterResponseDto = objectMapper.treeToValue(node.get("data"), UserMasterResponseDto.class);
//			} else {
//				throw new RecordNotFoundException();
//			}
//		} catch (Exception e) {
//			return null;
//		}
//		return userMasterResponseDto;
//	}
	
//	public UserMasterResponseDto getUserById(Long id) {
//		UserMasterResponseDto userMasterResponseDto = new UserMasterResponseDto();
//		String access_token = null;
//		HttpHeaders headers = new HttpHeaders();
//		Map<String, String> mapBody = new HashMap<String, String>();
//		Map<String, String> mapHeaders = new HashMap<String, String>();
//		try {
//			if (id != null && id > 0) {
//				access_token = headerRequest.getHeader("X-Authorization");
//				headers.set("X-Authorization", access_token);
//				log.info("access_token----------------" + access_token);
//				URI uri = serviceHeader.getServiceEndPointByServiceName(Constant.USER_SERVICE,
//						Constant.USER_CONTEXT_PATH);
//
//				List<ClientHttpRequestInterceptor> interceptors = serviceHeader.getHeader(access_token);
//				restTemplate.setInterceptors(interceptors);
//				String url = uri + "/usermanager/getUserById";
//				mapBody.put("id", id.toString());
//				String data = hiturl.getResponse(url, mapHeaders, mapBody);
//				ObjectNode node = objectMapper.readValue(data, ObjectNode.class);
//				userMasterResponseDto = (UserMasterResponseDto) objectMapper.treeToValue(node.get("data"), UserMasterResponseDto.class);
//			} else {
//				throw new RecordNotFoundException();
//			}
//		} catch (Exception e) {
//
//		}
//		return userMasterResponseDto;
//	}
	
//	public AuthenticationDTO callGetServiceToFetchUserByUserId(String jwtToken) {
//		List<ClientHttpRequestInterceptor> interceptors = serviceHeader.getHeader(jwtToken);
//		restTemplate.setInterceptors(interceptors);
//		URI uri = serviceHeader.getServiceEndPointByServiceName(Constant.USER_MANAGEMENT_SERVICE_NAME,
//				Constant.USER_MANAGEMENT_SERVICE_CONTEXT_PATH);
//		String url = uri + "/authentication/check_token?token=" + jwtToken;
//		AuthenticationDTO userDetails = restTemplate.getForObject(url, AuthenticationDTO.class);
//		return userDetails;
//
//	}
	public AuthenticationDTO executeCustomerUser(String jwtToken){
		List<ClientHttpRequestInterceptor> interceptors = serviceHeader.getHeader(jwtToken);
		restTemplate.setInterceptors(interceptors);
		URI uri = serviceHeader.getServiceEndPointByServiceName(Constant.LICENSE_MANAGEMENT_SERVICE,Constant.LICENSE_MANAGEMENT_CONTEXT_PATH);
		String url = uri+"/customer/authenticateCustomerToken?customerToken="+jwtToken;
		AuthenticationDTO userDetails = restTemplate.getForObject(url,AuthenticationDTO.class);
		return userDetails;
		
	}


	
	public AuthenticationDTO executeBusinessUser(String jwtToken) {
		List<ClientHttpRequestInterceptor> interceptors = serviceHeader.getHeader(jwtToken);
		restTemplate.setInterceptors(interceptors);
		URI uri = serviceHeader.getServiceEndPointByServiceName(Constant.USER_SERVICE, Constant.USER_CONTEXT_PATH);
		String url = uri + "/authentication/check_token?token=" + jwtToken;
		log.info("jwtToken=======" + jwtToken);
		log.info("User service URL=======" + url);
		AuthenticationDTO userDetails = restTemplate.getForObject(url, AuthenticationDTO.class);
		return userDetails;
	}
	 
//	public String getUserNameById(Long userID) {
//		String username = null;
//		String access_token = null;
//		HttpHeaders headers = new HttpHeaders();
//		Map<String, String> mapBody = new HashMap<String, String>();
//		Map<String, String> mapHeaders = new HashMap<String, String>();
//		try {
//			if (userID != null && userID > 0) {
//				access_token = headerRequest.getHeader("X-Authorization");
//				headers.set("X-Authorization", access_token);
//				log.info("access_token----------------" + access_token);
//				URI uri = serviceHeader.getServiceEndPointByServiceName(Constant.USER_SERVICE,
//						Constant.USER_CONTEXT_PATH);
//
//				List<ClientHttpRequestInterceptor> interceptors = serviceHeader.getHeader(access_token);
//				restTemplate.setInterceptors(interceptors);
//				String url = uri + "/usermanager/getUserNameById";
//				log.info("----url-------",url);
//				mapBody.put("userId", userID.toString());
//				String data = hiturl.getResponse(url, mapHeaders, mapBody);
//				ObjectNode node = objectMapper.readValue(data, ObjectNode.class);
//				username = objectMapper.treeToValue(node.get("data"), String.class);
//			} else {
//				throw new RecordNotFoundException();
//			}
//		} catch (Exception e) {
//
//		}
//		return username;
//	}
	
//	public String getUserNameById(Long userID) {
//		
//	    UserDetails userDetails = new UserDetails() ;
//	
//		String access_token = headerRequest.getHeader("X-Authorization");
//		List<ClientHttpRequestInterceptor> interceptors = serviceHeader.getHeader(access_token);
//		restTemplate.setInterceptors(interceptors);
//		URI uri = serviceHeader.getServiceEndPointByServiceName(Constant.USER_SERVICE,
//				Constant.USER_CONTEXT_PATH);
//		String url = uri + "/userManagement/user/findUserDetailByUserId?userId=" + userID;		
//		GenericResponse generic = restTemplate.getForObject(url, GenericResponse.class);
//		userDetails = objectMapper.convertValue(generic.getData(), UserDetails.class);		
//		return userDetails.getName();
//	}
	
	
	
	
	
	
//	public List<UserResponseDto> getUserByDesignationCode1(AuthenticationDTO authenticationDTO,Locale locale,String code) {
//		List<UserResponseDto> resultList = new ArrayList<UserResponseDto>();
//		String access_token = null;
//		HttpHeaders headers = new HttpHeaders();
//		Map<String, String> mapBody = new HashMap<String, String>();
//		Map<String, String> mapHeaders = new HashMap<String, String>();
//		try {
//		
//			access_token = headerRequest.getHeader("X-Authorization");
//			headers.set("X-Authorization", access_token);
//			log.info("access_token----------------" + access_token);
//			URI uri = serviceHeader.getServiceEndPointByServiceName(Constant.USER_SERVICE,
//					Constant.USER_CONTEXT_PATH);
//			List<ClientHttpRequestInterceptor> interceptors = serviceHeader.getHeader(access_token);
//			restTemplate.setInterceptors(interceptors);
//			String url = uri + "/user/getUserByDesignationCode?code="+code;
//			log.info("urlll"+ url);
//			String data = hiturl.getResponse(url, mapHeaders, mapBody);
//			ObjectNode node = objectMapper.readValue(data, ObjectNode.class);
//			node.get("data").forEach(obj -> {
//				try {
//					resultList.add(objectMapper.treeToValue(obj, UserResponseDto.class));
//				} catch (JsonProcessingException e) {
//					log.error("Exception occured in getUserByDesignationCode - >" + e);
//				}
//			});
//		
//
//		} catch (Exception e) {
//
//		}
//		return resultList;
//	}
	
	@SuppressWarnings("unchecked")
	public List<UserResponseDto> getUserByDesignationCode(AuthenticationDTO authenticationDTO,Locale locale,String code) {
		List<UserResponseDto> result = new ArrayList<UserResponseDto>();
		String access_token = null;
		access_token = headerRequest.getHeader("X-Authorization"); //
		URI uri = serviceHeader.getServiceEndPointByServiceName(Constant.USER_SERVICE,
				Constant.USER_CONTEXT_PATH);
		List<ClientHttpRequestInterceptor> interceptors = serviceHeader.getHeader(access_token);
		restTemplate.setInterceptors(interceptors);
		String url = uri + "/user/getUserByDesignationCode?code="+code;
		log.info("access_token----------------" + access_token);
		System.out.println("=================url===========" + url);
		String data = restTemplate.getForEntity(url, String.class).getBody();
		ObjectNode node;
		try {
			node = objectMapper.readValue(data, ObjectNode.class);
			result = objectMapper.treeToValue(node.get("content"), List.class);
			log.info("result-----------------------" + result);
		} catch (JsonMappingException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return result;

	}
	
	public UserResponseDto getUserById(Long id) {
		UserResponseDto userMasterResponseDto = new UserResponseDto();
		String access_token = null;
		HttpHeaders headers = new HttpHeaders();
//		Map<String, String> mapBody = new HashMap<String, String>();
//		Map<String, String> mapHeaders = new HashMap<String, String>();
		try {
			if (id != null && id > 0) {
				access_token = headerRequest.getHeader("X-Authorization");
				headers.set("X-Authorization", access_token);
				log.info("access_token----------------" + access_token);
				URI uri = serviceHeader.getServiceEndPointByServiceName(Constant.USER_SERVICE,
						Constant.USER_CONTEXT_PATH);

				List<ClientHttpRequestInterceptor> interceptors = serviceHeader.getHeader(access_token);
				restTemplate.setInterceptors(interceptors);
				String url = uri + "/user/getUserById?id="+id;
				log.info("access_token----------------" + access_token);
				System.out.println("=================url===========" + url);
				String data = restTemplate.getForEntity(url, String.class).getBody();
				ObjectNode node = objectMapper.readValue(data, ObjectNode.class);
				userMasterResponseDto = (UserResponseDto) objectMapper.treeToValue(node.get("content"), UserResponseDto.class);
			} else {
				throw new RecordNotFoundException();
			}
		} catch (Exception e) {

		}
		return userMasterResponseDto;
	}
	
	public EntityType getEntityTypeById(Long id) {
		EntityType entityTypeResponseDto = new EntityType();
		String access_token = null;
		HttpHeaders headers = new HttpHeaders();
		try {
			if (id != null && id > 0) {
				access_token = headerRequest.getHeader("X-Authorization");
				headers.set("X-Authorization", access_token);
				log.info("access_token----------------" + access_token);
				URI uri = serviceHeader.getServiceEndPointByServiceName(Constant.USER_SERVICE,
						Constant.USER_CONTEXT_PATH);

				List<ClientHttpRequestInterceptor> interceptors = serviceHeader.getHeader(access_token);
				restTemplate.setInterceptors(interceptors);
				String url = uri + "/entityType/getEntityTypeById?id="+id;
				log.info("access_token----------------" + access_token);
				System.out.println("=================url===========" + url);
				String data = restTemplate.getForEntity(url, String.class).getBody();
				ObjectNode node = objectMapper.readValue(data, ObjectNode.class);
				entityTypeResponseDto = (EntityType) objectMapper.treeToValue(node.get("content"), EntityType.class);
			} else {
				throw new RecordNotFoundException();
			}
		} catch (Exception e) {

		}
		return entityTypeResponseDto;
	}
	public String getUserNameByUserId(Long id) {
		if (Objects.isNull(id)) {
			return null;
		}
		Optional<UserEntity> userEntity = userRepository.findById(id);
		if(userEntity.isPresent()) {
			return userEntity.get().getFirstName();
		}
		return null;
	}
	
	public String getUserNameById(Long userID) {
		Optional<UserEntity> userEntity=null;
		try {
		 userEntity = userRepository.findById(userID);
		}
		catch(Exception e) {
		return null;	
		}
		
		if(userEntity.isPresent()) {
			String username = userEntity.get().getUsername();
//			if(StringUtils.isNotBlank(userEntity.get().getMiddleName())){
//				username = username.concat(Constant.BLANK_STRING).concat(userEntity.get().getMiddleName());
//			}
//			username = username.concat(Constant.BLANK_STRING).concat(userEntity.get().getLastName());
			return username;
		}
		return null;
	}

	
	public String getMiddleNameById(Long userID) {
		Optional<UserEntity> userEntity=null;
		try {
		 userEntity = userRepository.findById(userID);
		}
		catch(Exception e) {
		return null;	
		}
		
		if(userEntity.isPresent()) {
			String username = userEntity.get().getMiddleName();
//			if(StringUtils.isNotBlank(userEntity.get().getMiddleName())){
//				username = username.concat(Constant.BLANK_STRING).concat(userEntity.get().getMiddleName());
//			}
//			username = username.concat(Constant.BLANK_STRING).concat(userEntity.get().getLastName());
			return username;
		}
		return null;
	}
	
	
	public UserDetails searchByLicenceNumber(String  applicationNumber) {
		UserDetails userDetails = new UserDetails();
		String access_token = null;
		HttpHeaders headers = new HttpHeaders();
		try {
			if (!applicationNumber.isEmpty()) {
				access_token = headerRequest.getHeader("X-Authorization");
				headers.set("X-Authorization", access_token);
				log.info("access_token----------------" + access_token);
				URI uri = serviceHeader.getServiceEndPointByServiceName(Constant.LICENSE_SERVICE_NAME,
						Constant.LICENSE_MANAGEMENT_SERVICE_CONTEXT_PATH);

				List<ClientHttpRequestInterceptor> interceptors = serviceHeader.getHeader(access_token);
				restTemplate.setInterceptors(interceptors);
				String url = uri + "/license/findUserDetailByApplicationNumber?applicationNumber="+applicationNumber;
				log.info("access_token----------------" + access_token);
				System.out.println("=================url===========" + url);
				String data = restTemplate.getForEntity(url, String.class).getBody();
				ObjectNode node = objectMapper.readValue(data, ObjectNode.class);
				userDetails = (UserDetails) objectMapper.treeToValue(node, UserDetails.class);
			} else {
				throw new RecordNotFoundException();
			}
		} catch (Exception e) {

		}
		return userDetails;
	}
	
	public String getMasterDropDownValueByKey(String parentKey, String childKey) {
		ResponseEntity<String> response = null;
		URI uri = serviceHeader.getServiceEndPointByServiceName(Constant.MASTER_MANAGEMENT_SERVICE_NAME,
				Constant.MASTER_MANAGEMENT_SERVICE_CONTEXT_PATH);
		String url = uri + "/api/masterValues/findDropDownValueByKey?dropDownKey=" + parentKey;
		if (StringUtils.isNotBlank(childKey)) {
			url = url + "&childKey=" + childKey;
			log.info("getMasterDropDownValueByKey:::::::::" + url);
		}
		try {
			
			response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
		} catch (Exception e) {
			log.error("error occurred while calling : {} API , exception : {}", url, e.getMessage());
			return null;
		}
		log.info("api url : {} API , response : {}", url, response);

		if (Objects.nonNull(response) && Objects.nonNull(response.getBody())) { 
			return response.getBody();
		} else {
			return null;
		}

	}
	
	public List<DistrictDTO> callDistrictMasterAPI(String code) {
		List<DistrictDTO> apiResponse = null;
		ResponseEntity<DistrictMasterResponse> response = null;
		URI uri = serviceHeader.getServiceEndPointByServiceName(Constant.MASTER_MANAGEMENT_SERVICE_NAME,
				Constant.MASTER_MANAGEMENT_SERVICE_CONTEXT_PATH);
		String url = uri + "/location/findDistrictByCode?districtCode=" + code;

		try {
			response = restTemplate.exchange(url, HttpMethod.GET, null, DistrictMasterResponse.class);
		} catch (Exception e) {
			log.error("error occurred while calling : {} API , exception : {}", url, e.getMessage());
			return null;
		}
		log.info("api url : {} API , response : {}", url, response);

		if (Objects.nonNull(response) && Objects.nonNull(response.getBody())
				&& Objects.nonNull(response.getBody().getResponseCode())
				&& Objects.nonNull(response.getBody().getContent())
				&& response.getBody().getResponseCode() == ErrorCode.SUCCESS_RESPONSE.getErrorCode()) { // result
			apiResponse = response.getBody().getContent();
		} else {
			return null;
		}
		return apiResponse;

	}
	
	
	public String getEntityById(String entityCode) {
		String name = null;
		String access_token = null;
//		HttpHeaders headers = new HttpHeaders();
//		Map<String, String> mapBody = new HashMap<String, String>();
//		Map<String, String> mapHeaders = new HashMap<String, String>();
		HttpHeaders headers = new HttpHeaders();
		try {
			if (entityCode != null) {
				access_token = headerRequest.getHeader("X-Authorization");
				headers.set("X-Authorization", access_token);
				log.info("access_token----------------" + access_token);
				URI uri = serviceHeader.getServiceEndPointByServiceName(Constant.MASTER_SERVICE,
						Constant.MASTER_CONTEXT_PATH);

				List<ClientHttpRequestInterceptor> interceptors = serviceHeader.getHeader(access_token);
				restTemplate.setInterceptors(interceptors);
				String url = uri + "/findActiveDropDownChildList?dropDownKey="+entityCode;
				log.info("----url-------",url);
				//mapBody.put("userId", userID.toString());
//				String data = hiturl.getResponse(url, mapHeaders, mapBody);
				System.out.println("=================url===========" + url);
				String data = restTemplate.getForEntity(url, String.class).getBody();
				ObjectNode node = objectMapper.readValue(data, ObjectNode.class);
				name = objectMapper.treeToValue(node.get("paramValue"), String.class);
			} else {
				throw new RecordNotFoundException();
			}
		} catch (Exception e) {

	}
		return name;
	}
	
	public ApiResponse processEmail(String token,EmailDetails emailDetails,Locale locale){
		ApiResponse apiResponse = null;
		Map<String,Object> finalMap = new LinkedHashMap<String,Object>();
		try{
			URI uri = serviceHeader.getServiceEndPointByServiceName(Constant.NOTIFICATION_SERVICE, Constant.NOTIFICATION_CONTEXT_PATH);
			if(uri!=null){ 
				//call the method and set the header in service call
				List<ClientHttpRequestInterceptor> interceptors = serviceHeader.getHeader(token);
				restTemplate.setInterceptors(interceptors);
				String url = null;
				if(token==null) {
					url = "/api/notificationNoToken/sendEmail";
				}else {
					url = "/api/notification/sendEmail";
				}
				ResponseEntity<ApiResponse> result  =  restTemplate.postForEntity(uri + url,emailDetails,ApiResponse.class);
				if(Objects.nonNull(result) && result.getStatusCodeValue()==ErrorCode.SUCCESS_RESPONSE.getCode()){ //result from elms service
					ApiResponse emailResponse = result.getBody();
					if(emailResponse.getSuccess()){ //success case
						finalMap = emailResponse.getFinalMap();
						apiResponse = new ApiResponse(true, ErrorCode.SUCCESS_RESPONSE_FROM_CALLING_SERVICE.getCode().longValue(),ResponseMessageConstant.SUCCESS_RESPONSE_FROM_CALLING_SERVICE.getMessage(new Object[] { Constant.NOTIFICATION_SERVICE }), finalMap);
					}else{
						//success case with no data and error case from service 
						apiResponse = new ApiResponse(false, ErrorCode.FALSE_RESPONSE_FROM_CALLING_SERVICE.getErrorCode().longValue(), ResponseMessageConstant.FALSE_RESPONSE_FROM_CALLING_SERVICE.getMessage(new Object[] { Constant.NOTIFICATION_SERVICE }), null);
					}
				}else{
					//service error
					apiResponse = new ApiResponse(false, ErrorCode.ERROR_RESPONSE_FROM_CALLING_SERVICE.getErrorCode().longValue(), ResponseMessageConstant.ERROR_RESPONSE_FROM_CALLING_SERVICE.getMessage(new Object[] { Constant.NOTIFICATION_SERVICE }), null);
				}
			}else{
				//connection error
				apiResponse = new ApiResponse(false, ErrorCode.CONNECTION_ERROR.getErrorCode().longValue(), ResponseMessageConstant.CONNECTION_ERROR.getMessage(new Object[] { Constant.NOTIFICATION_SERVICE }), null);
			}
		}catch(Exception exception){
			//general error
			log.error("=======processEmail catch block====processEmail======== {} ",exception);
			apiResponse = new ApiResponse(false, ErrorCode.ERROR_RESPONSE_FROM_CALLING_SERVICE.getErrorCode().longValue(), ResponseMessageConstant.ERROR_RESPONSE_FROM_CALLING_SERVICE.getMessage(new Object[] { Constant.NOTIFICATION_SERVICE }), null);
		}
		return apiResponse;
	}
	
	public ApiResponse processEmailViaThemeleaf(String token,EmailDetails emailDetails,Locale locale){
		ApiResponse apiResponse = null;
		Map<String,Object> finalMap = new LinkedHashMap<String,Object>();
		try{
			URI uri = serviceHeader.getServiceEndPointByServiceName(Constant.NOTIFICATION_SERVICE, Constant.NOTIFICATION_CONTEXT_PATH);
			if(uri!=null){ 
				//call the method and set the header in service call
				List<ClientHttpRequestInterceptor> interceptors = serviceHeader.getHeader(token);
				restTemplate.setInterceptors(interceptors);
				String url = null;
				if(token==null) {
					url = "/api/notificationNoToken/sendEmailViaThymleaf";
				}else {
					url = "/api/notificationNoToken/sendEmailViaThymleaf";
				}
				ResponseEntity<ApiResponse> result  =  restTemplate.postForEntity(uri + url,emailDetails,ApiResponse.class);
				if(Objects.nonNull(result) && result.getStatusCodeValue()==ErrorCode.SUCCESS_RESPONSE.getCode()){ //result from elms service
					ApiResponse emailResponse = result.getBody();
					if(emailResponse.getSuccess()){ //success case
						finalMap = emailResponse.getFinalMap();
						apiResponse = new ApiResponse(true, ErrorCode.SUCCESS_RESPONSE_FROM_CALLING_SERVICE.getCode().longValue(),ResponseMessageConstant.SUCCESS_RESPONSE_FROM_CALLING_SERVICE.getMessage(new Object[] { Constant.NOTIFICATION_SERVICE }), finalMap);
					}else{
						//success case with no data and error case from service 
						apiResponse = new ApiResponse(false, ErrorCode.FALSE_RESPONSE_FROM_CALLING_SERVICE.getErrorCode().longValue(), ResponseMessageConstant.FALSE_RESPONSE_FROM_CALLING_SERVICE.getMessage(new Object[] { Constant.NOTIFICATION_SERVICE }), null);
					}
				}else{
					//service error
					apiResponse = new ApiResponse(false, ErrorCode.ERROR_RESPONSE_FROM_CALLING_SERVICE.getErrorCode().longValue(), ResponseMessageConstant.ERROR_RESPONSE_FROM_CALLING_SERVICE.getMessage(new Object[] { Constant.NOTIFICATION_SERVICE }), null);
				}
			}else{
				//connection error
				apiResponse = new ApiResponse(false, ErrorCode.CONNECTION_ERROR.getErrorCode().longValue(), ResponseMessageConstant.CONNECTION_ERROR.getMessage(new Object[] { Constant.NOTIFICATION_SERVICE }), null);
			}
		}catch(Exception exception){
			//general error
			log.error("=======processEmail catch block====processEmail======== {} ",exception);
			apiResponse = new ApiResponse(false, ErrorCode.ERROR_RESPONSE_FROM_CALLING_SERVICE.getErrorCode().longValue(), ResponseMessageConstant.ERROR_RESPONSE_FROM_CALLING_SERVICE.getMessage(new Object[] { Constant.NOTIFICATION_SERVICE }), null);
		}
		return apiResponse;
	}
	
	/**
	 * This method will be used to send an SMS from the notification service
	 * @param token
	 * @param smsDetails
	 * @param locale
	 * @return
	 */
	public ApiResponse processSMS(SMSDetails smsDetails){
		ApiResponse apiResponse = null;
		Map<String,Object> finalMap = new LinkedHashMap<String,Object>();
		String token = this.getUserManagementToken();
		try{
			URI uri = serviceHeader.getServiceEndPointByServiceName(Constant.NOTIFICATION_SERVICE,Constant.NOTIFICATION_CONTEXT_PATH);
			if(uri!=null){ 
				//call the method and set the header in service call
				List<ClientHttpRequestInterceptor> interceptors = serviceHeader.getHeader(token);
				restTemplate.setInterceptors(interceptors);
				String url = null;
				if(token==null) {
					url = "/api/notification/sendSMSViaThymleaf";
				}else {
					url = "/api/notification/sendSMSViaThymleaf";
				}
				//call the elms service 
				log.info("=======processSMS====request ::{}",smsDetails);
				ResponseEntity<ApiResponse> result  =  restTemplate.postForEntity(uri + url,smsDetails,ApiResponse.class);
				log.info("processSMS() response :: {}",result);
				if(result!=null && result.getStatusCodeValue()==ErrorCode.SUCCESS_RESPONSE.getCode()){ //result from elms service
						apiResponse = new ApiResponse(true, ErrorCode.SUCCESS_RESPONSE_FROM_CALLING_SERVICE.getCode().longValue(),ResponseMessageConstant.SUCCESS_RESPONSE_FROM_CALLING_SERVICE.getMessage(new Object[] { Constant.NOTIFICATION_SERVICE }), null);
					
				}else{
					//service error
					apiResponse = new ApiResponse(false, ErrorCode.ERROR_RESPONSE_FROM_CALLING_SERVICE.getErrorCode().longValue(), ResponseMessageConstant.ERROR_RESPONSE_FROM_CALLING_SERVICE.getMessage(new Object[] { Constant.NOTIFICATION_SERVICE }), null);
				}
			}else{
				//connection error
				apiResponse = new ApiResponse(false, ErrorCode.CONNECTION_ERROR.getErrorCode().longValue(), ResponseMessageConstant.CONNECTION_ERROR.getMessage(new Object[] { Constant.NOTIFICATION_SERVICE }), null);
			}
		}catch(Exception exception){
			//general error
			log.error("=======processSMS catch block====processSMS========",exception);
			apiResponse = new ApiResponse(false, ErrorCode.ERROR_RESPONSE_FROM_CALLING_SERVICE.getErrorCode().longValue(), ResponseMessageConstant.ERROR_RESPONSE_FROM_CALLING_SERVICE.getMessage(new Object[] { Constant.NOTIFICATION_SERVICE }), null);
		}
		return apiResponse;
	}
	
	public String getUserManagementToken() {
		ResponseEntity<String> response = null;
		URI uri = serviceHeader.getServiceEndPointByServiceName(Constant.USER_SERVICE, Constant.USER_CONTEXT_PATH);
		String url = uri + "/authentication/login";
		CommonMasterRequestDTO request = new CommonMasterRequestDTO();
		request.setUserName(useranme);
		request.setPassword(password);
		request.setSource("external");
		try {
			HttpEntity<CommonMasterRequestDTO> requestDTO = new HttpEntity<>(request, null);

			response = restTemplate.exchange(url, HttpMethod.POST, requestDTO, String.class);
		} catch (Exception e) {
			log.error("error occurred while calling : {} API , exception : {}", url, e.getMessage());
			return null;
		}
		log.info("api url : {} API , response : {}", url, response);

		if (Objects.nonNull(response) && Objects.nonNull(response.getBody())) {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode actualObj = null;
			try {
				actualObj = mapper.readTree(response.getBody());
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				log.error("error occurred while parsing response : {} API , exception : {}", url, e.getMessage());
			}
			return actualObj.get("content").get("auth").get("token").asText();
		} else {
			return null;
		}

	}

	public String getFirstNameById(Long userID) {
		Optional<UserEntity> userEntity=null;
		try {
		 userEntity = userRepository.findById(userID);
		}
		catch(Exception e) {
		return null;	
		}
		
		if(userEntity.isPresent()) {
			String firstname = userEntity.get().getFirstName();
//			if(StringUtils.isNotBlank(userEntity.get().getMiddleName())){
//				username = username.concat(Constant.BLANK_STRING).concat(userEntity.get().getMiddleName());
//			}
//			username = username.concat(Constant.BLANK_STRING).concat(userEntity.get().getLastName());
			return firstname;
		}
		return null;
	}

	public String getmobileNoById(Long userID) {
		Optional<UserEntity> userEntity=null;
		try {
		 userEntity = userRepository.findById(userID);
		}
		catch(Exception e) {
		return null;	
		}
		
		if(userEntity.isPresent()) {
			String mobileno = userEntity.get().getPhoneNumber();
//			if(StringUtils.isNotBlank(userEntity.get().getMiddleName())){
//				username = username.concat(Constant.BLANK_STRING).concat(userEntity.get().getMiddleName());
//			}
//			username = username.concat(Constant.BLANK_STRING).concat(userEntity.get().getLastName());
			return mobileno;
		}
		return null;
	}


}
