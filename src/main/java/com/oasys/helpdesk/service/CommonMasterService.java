package com.oasys.helpdesk.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.Brandlabeldto;
import com.oasys.helpdesk.dto.CommanMasterGetUnitCodeDTO;
import com.oasys.helpdesk.dto.CommanMasterUnitCodeDTO;
import com.oasys.helpdesk.dto.CommaonMasteLicenceDTO;
import com.oasys.helpdesk.dto.CommaonMasterDistrictDto;
import com.oasys.helpdesk.dto.CommonMasterRequestDTO;
import com.oasys.helpdesk.dto.DownloadDTO;
import com.oasys.helpdesk.dto.DownlofileDTO;
import com.oasys.helpdesk.dto.EntityDTO;
import com.oasys.helpdesk.dto.EntityShopDTO;
import com.oasys.helpdesk.dto.LicenceDTO;
import com.oasys.helpdesk.dto.UMsearchDTO;
import com.oasys.helpdesk.dto.UploadDTO;
import com.oasys.helpdesk.dto.UserDto;
import com.oasys.helpdesk.response.BaseResponse;
import com.oasys.helpdesk.response.LicenseResponseDTO;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.dto.CommaonMasteGetUserNameDTO;

import lombok.extern.log4j.Log4j2;

@Service
@Component
@Configuration
//@EnableCaching
@Log4j2
public class CommonMasterService {

	@Value("${spring.common.entity}")
	private String entityurl;

	@Value("${spring.common.salutation}")
	private String salutationurl;

	@Value("${spring.common.designation}")
	private String designationurl;

	@Value("${spring.common.department}")
	private String departmenturl;

	@Value("${spring.common.licence}")
	private String licenceurl;

	@Value("${spring.common.indent}")
	private String indenturl;

	@Value("${spring.common.district}")
	private String districturl;

	@Value("${spring.common.country}")
	private String countryurl;

	@Value("${spring.common.findlicenec}")
	private String findlicenceurl;

	@Value("${spring.common.devloginurl}")
	private String loginurl;

	@Value("${spring.common.devtoken}")
	private String token;

	@Value("${spring.common.username}")
	private String useranme;

	@Value("${spring.common.password}")
	private String password;

	@Value("${spring.common.upload}")
	private String uploadurl;

	@Value("${spring.common.download}")
	private String downloadurl;

	@Value("${spring.common.brand}")
	private String brandurl;

	@Value("${spring.common.search}")
	private String searchurl;

	@Value("${spring.common.role}")
	private String role;

	@Value("${spring.common.searchapp}")
	private String serachlicenceapp;

	@Value("${spring.common.downloadfileurl}")
	private String downloadfileurl;

	@Value("${spring.common.taluk}")
	private String talukurl;

	@Value("${spring.common.user}")
	private String userurl;

	@Value("${spring.common.role}")
	private String roleurl;

	@Value("${spring.common.usertype}")
	private String usertypeurl;

	@Value("${spring.common.sourcetoken}")
	private String source;

	@Value("${spring.common.entityurl}")
	private String entityURL;

	@Value("${spring.common.entitytype}")
	private String entitytypeurl;

	@Value("${spring.common.unitname}")
	private String unitnameurl;

	@Value("${spring.common.FindUserDetails}")
	private String findUserDetailsURL;

	@Value("${spring.common.FindUserUnitCode}")
	private String findUserUnitCodeURL;

	public String APIcall(CommonMasterRequestDTO commonmasterrequest) {
		
		String response = null;
		
		CommonMasterService cmservice = new CommonMasterService();
		commonmasterrequest.setUserName(useranme);
		commonmasterrequest.setPassword(password);
		commonmasterrequest.setSource(source);
		String thirdPartyResponse = null;
		// CommonMasterRequestDTO commonmasterrequest = new CommonMasterRequestDTO();

		// String access_token = "24d14de9-8237-3ff9-805a-c212281d6e90";

		String access_token = token;

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("Authorization", "Bearer " + access_token);

		HttpEntity<CommonMasterRequestDTO> APIRequest = new HttpEntity<>(commonmasterrequest, headers);

		// String
		// URL="https://devapigateway.upexciseonline.co/user-managment/v1.0.0/authentication/login";
		// loginurl

		thirdPartyResponse = restTemplate.exchange(loginurl, HttpMethod.POST, APIRequest, String.class).getBody();

		Gson gson = new Gson();

		// String gsonDto = gson.toJson(thirdPartyResponse);

		ObjectMapper mapper = new ObjectMapper();

		String bearertoken = null;

		JsonNode actualObj = null;

		

		try {
			actualObj = mapper.readTree(thirdPartyResponse);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		bearertoken = actualObj.get("content").get("auth").get("token").asText();

		System.out.println("bearertoken::::" + bearertoken);

		if (commonmasterrequest.getType().equalsIgnoreCase("ENTITY_TYPE")) {

			response = cmservice.entitytypeAPI(entityurl, bearertoken, access_token);

		}

		if (commonmasterrequest.getType().equalsIgnoreCase("SALUTATION")) {
			response = cmservice.salutationAPI(salutationurl, bearertoken, access_token);
		}

		if (commonmasterrequest.getType().equalsIgnoreCase("DESIGNATION")) {
			response = cmservice.designationAPI(designationurl, bearertoken, access_token);
		}

		if (commonmasterrequest.getType().equalsIgnoreCase("DEPARTMENT")) {
			response = cmservice.departmentAPI(departmenturl, bearertoken, access_token);
		}

		if (commonmasterrequest.getType().equalsIgnoreCase("LICENSE_TYPE")) {
			response = cmservice.licenceAPI(licenceurl, bearertoken, access_token);
		}

		if (commonmasterrequest.getType().equalsIgnoreCase("INDENT")) {
			response = cmservice.indentAPI(indenturl, bearertoken, access_token, commonmasterrequest);
		}

		return response;

	}

	public String entitytypeAPI(String entityurl, String BTToken, String accesstoken) {

		String entitytyperesponse = null;

		CommonMasterRequestDTO commonmasterrequest = new CommonMasterRequestDTO();

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("X-Authorization", BTToken);

		headers.set("Authorization", "Bearer " + accesstoken);

		HttpEntity<CommonMasterRequestDTO> APIRequest = new HttpEntity<>(commonmasterrequest, headers);

		// String
		// URL="https://devapigateway.upexciseonline.co/master/v1.0.0/api/masterValues/findActiveDropDownChildList?dropDownKey=ENTITY_TYPE";
		// entityurl
		entitytyperesponse = restTemplate.exchange(entityurl, HttpMethod.GET, APIRequest, String.class).getBody();

		return entitytyperesponse;

	}

	public String salutationAPI(String salutationurl, String BTToken, String accesstoken) {

		String salutationAPIresponse = null;

		CommonMasterRequestDTO commonmasterrequest = new CommonMasterRequestDTO();

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("X-Authorization", BTToken);

		headers.set("Authorization", "Bearer " + accesstoken);

		HttpEntity<CommonMasterRequestDTO> APIRequest = new HttpEntity<>(commonmasterrequest, headers);

		// String
		// URL="https://devapigateway.upexciseonline.co/master/v1.0.0/api/masterValues/findDropDownMasterOnMaster?dropDownKey=SALUTATION";
		// salutationurl
		salutationAPIresponse = restTemplate.exchange(salutationurl, HttpMethod.GET, APIRequest, String.class)
				.getBody();

		return salutationAPIresponse;

	}

	public String designationAPI(String designationurl, String BTToken, String accesstoken) {

		String designationAPIresponse = null;

		CommonMasterRequestDTO commonmasterrequest = new CommonMasterRequestDTO();

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("X-Authorization", BTToken);

		headers.set("Authorization", "Bearer " + accesstoken);

		HttpEntity<CommonMasterRequestDTO> APIRequest = new HttpEntity<>(commonmasterrequest, headers);

		// String
		// URL="https://devapigateway.upexciseonline.co/master/v1.0.0/api/masterValues/findDropDownMasterOnMaster?dropDownKey=DESIGNATION";
		// designationurl
		designationAPIresponse = restTemplate.exchange(designationurl, HttpMethod.GET, APIRequest, String.class)
				.getBody();

		return designationAPIresponse;

	}

	public String departmentAPI(String departmenturl, String BTToken, String accesstoken) {

		String departmentAPIresponse = null;

		CommonMasterRequestDTO commonmasterrequest = new CommonMasterRequestDTO();

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("X-Authorization", BTToken);

		headers.set("Authorization", "Bearer " + accesstoken);

		HttpEntity<CommonMasterRequestDTO> APIRequest = new HttpEntity<>(commonmasterrequest, headers);

		// String
		// URL="https://devapigateway.upexciseonline.co/master/v1.0.0/api/masterValues/findDropDownMasterOnMaster?dropDownKey=DEPARTMENT";
		// departmenturl
		departmentAPIresponse = restTemplate.exchange(departmenturl, HttpMethod.GET, APIRequest, String.class)
				.getBody();

		return departmentAPIresponse;

	}

	public String licenceAPI(String licenceurl, String BTToken, String accesstoken) {

		String licenceAPIresponse = null;

		CommonMasterRequestDTO commonmasterrequest = new CommonMasterRequestDTO();

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("X-Authorization", BTToken);

		headers.set("Authorization", "Bearer " + accesstoken);

		HttpEntity<CommonMasterRequestDTO> APIRequest = new HttpEntity<>(commonmasterrequest, headers);

		// String
		// URL="http://devapigateway.upexciseonline.co/master/v1.0.0/api/masterValues/findDropDownMaster?dropDownKey=LICENSE_TYPE";
		// licenceurl
		licenceAPIresponse = restTemplate.exchange(licenceurl, HttpMethod.GET, APIRequest, String.class).getBody();

		return licenceAPIresponse;

	}

	public String indentAPI(String indenturl, String BTToken, String accesstoken,
			CommonMasterRequestDTO commonmasterrequest) {

		String indentAPIresponse = null;

		// CommonMasterRequestDTO commonmasterrequest=new CommonMasterRequestDTO();

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("X-Authorization", BTToken);

		headers.set("Authorization", "Bearer " + accesstoken);

		HttpEntity<CommonMasterRequestDTO> APIRequest = new HttpEntity<>(commonmasterrequest, headers);

		// String URL="http://115.124.100.227:8151/scmretailapi/customdata/getdata";
		// indenturl
		indentAPIresponse = restTemplate.exchange(indenturl, HttpMethod.POST, APIRequest, String.class).getBody();

		return indentAPIresponse;

	}

	public String APIcallDistrict(CommaonMasterDistrictDto commonmasterrequest) {
		CommonMasterService cmservice = new CommonMasterService();
		Integer stcode = commonmasterrequest.getStatecode();
		// CommonMasterRequestDTO commonmasterrequest=new CommonMasterRequestDTO();
		String thirdPartyResponse = null;
		// CommonMasterRequestDTO commonmasterrequest = new CommonMasterRequestDTO();

		commonmasterrequest.setUserName(useranme);
		commonmasterrequest.setPassword(password);
		commonmasterrequest.setSource(source);
		// String access_token = "24d14de9-8237-3ff9-805a-c212281d6e90";
		String access_token = token;

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("Authorization", "Bearer " + access_token);

		HttpEntity<CommaonMasterDistrictDto> APIRequest = new HttpEntity<>(commonmasterrequest, headers);

		// String
		// URL="https://devapigateway.upexciseonline.co/user-managment/v1.0.0/authentication/login";
		// loginurl
		thirdPartyResponse = restTemplate.exchange(loginurl, HttpMethod.POST, APIRequest, String.class).getBody();

		Gson gson = new Gson();

		// String gsonDto = gson.toJson(thirdPartyResponse);

		ObjectMapper mapper = new ObjectMapper();

		String bearertoken = null;

		JsonNode actualObj = null;

		String response = null;

		try {
			actualObj = mapper.readTree(thirdPartyResponse);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		bearertoken = actualObj.get("content").get("auth").get("token").asText();

		System.out.println("bearertoken::::" + bearertoken);

		response = cmservice.districtAPI(districturl, bearertoken, access_token, stcode);

		return response;

	}

	public String districtAPI(String districturl, String BTToken, String accesstoken, Integer stcode) {

		String districtAPIresponse = null;

		CommaonMasterDistrictDto commonmasterrequest = new CommaonMasterDistrictDto();

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("X-Authorization", BTToken);

		headers.set("Authorization", "Bearer " + accesstoken);

		HttpEntity<CommaonMasterDistrictDto> APIRequest = new HttpEntity<>(commonmasterrequest, headers);

		// String
		// URL="http://65.0.150.210:3503/masterData/location/findDistrictByStateCode?stateCode=".concat(String.valueOf(stcode));

		String URL = districturl.concat(String.valueOf(stcode));
		districtAPIresponse = restTemplate.exchange(URL, HttpMethod.GET, APIRequest, String.class).getBody();

		return districtAPIresponse;

	}

	public String APIcallTaluk(CommaonMasterDistrictDto commonmasterrequest) {
		CommonMasterService cmservice = new CommonMasterService();
		Integer ditrictcode = commonmasterrequest.getDistrictCode();
		// CommonMasterRequestDTO commonmasterrequest=new CommonMasterRequestDTO();
		String thirdPartyResponse = null;
		// CommonMasterRequestDTO commonmasterrequest = new CommonMasterRequestDTO();

		commonmasterrequest.setUserName(useranme);
		commonmasterrequest.setPassword(password);
		commonmasterrequest.setSource(source);
		// String access_token = "24d14de9-8237-3ff9-805a-c212281d6e90";
		String access_token = token;

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("Authorization", "Bearer " + access_token);

		HttpEntity<CommaonMasterDistrictDto> APIRequest = new HttpEntity<>(commonmasterrequest, headers);

		// String
		// URL="https://devapigateway.upexciseonline.co/user-managment/v1.0.0/authentication/login";
		// loginurl
		thirdPartyResponse = restTemplate.exchange(loginurl, HttpMethod.POST, APIRequest, String.class).getBody();

		Gson gson = new Gson();

		// String gsonDto = gson.toJson(thirdPartyResponse);

		ObjectMapper mapper = new ObjectMapper();

		String bearertoken = null;

		JsonNode actualObj = null;

		String response = null;

		try {
			actualObj = mapper.readTree(thirdPartyResponse);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		bearertoken = actualObj.get("content").get("auth").get("token").asText();

		System.out.println("bearertoken::::" + bearertoken);

		response = cmservice.talukAPI(talukurl, bearertoken, access_token, ditrictcode);

		return response;

	}

	public String talukAPI(String talukurl, String BTToken, String accesstoken, Integer ditrictcode) {

		String talukAPIresponse = null;

		CommaonMasterDistrictDto commonmasterrequest = new CommaonMasterDistrictDto();

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("X-Authorization", BTToken);

		headers.set("Authorization", "Bearer " + accesstoken);

		HttpEntity<CommaonMasterDistrictDto> APIRequest = new HttpEntity<>(commonmasterrequest, headers);

		// String
		// URL="http://65.0.150.210:3503/masterData/location/findDistrictByStateCode?stateCode=".concat(String.valueOf(stcode));

		String URL = talukurl.concat(String.valueOf(ditrictcode));
		talukAPIresponse = restTemplate.exchange(URL, HttpMethod.GET, APIRequest, String.class).getBody();

		return talukAPIresponse;

	}

	public String Usercall(UserDto commonmasterrequest) {
		CommonMasterService cmservice = new CommonMasterService();

		String thirdPartyResponse = null;
		commonmasterrequest.setUserName(useranme);
		commonmasterrequest.setPassword(password);
		commonmasterrequest.setSource(source);
		String access_token = token;

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("Authorization", "Bearer " + access_token);

		HttpEntity<UserDto> APIRequest = new HttpEntity<>(commonmasterrequest, headers);

		thirdPartyResponse = restTemplate.exchange(loginurl, HttpMethod.POST, APIRequest, String.class).getBody();

		Gson gson = new Gson();

		ObjectMapper mapper = new ObjectMapper();

		String bearertoken = null;

		JsonNode actualObj = null;

		String response = null;

		try {
			actualObj = mapper.readTree(thirdPartyResponse);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		bearertoken = actualObj.get("content").get("auth").get("token").asText();

		System.out.println("bearertoken::::" + bearertoken);

		response = cmservice.userAPI(userurl, bearertoken, access_token, commonmasterrequest);

		return response;

	}

	public String userAPI(String userurl, String BTToken, String accesstoken, UserDto commonmasterrequest) {

		String userresponse = null;

		// UserDto commonmasterrequest=new UserDto();

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("X-Authorization", BTToken);

		headers.set("Authorization", "Bearer " + accesstoken);

		HttpEntity<UserDto> APIRequest = new HttpEntity<>(commonmasterrequest, headers);

		String URL = userurl;

		userresponse = restTemplate.exchange(URL, HttpMethod.POST, APIRequest, String.class).getBody();

		return userresponse;

	}

	public String APIcallCountry(CommaonMasterDistrictDto commonmasterrequest) {
		CommonMasterService cmservice = new CommonMasterService();
		String country = commonmasterrequest.getCountrycode();
		// CommonMasterRequestDTO commonmasterrequest=new CommonMasterRequestDTO();
		String thirdPartyResponse = null;
		// CommonMasterRequestDTO commonmasterrequest = new CommonMasterRequestDTO();

		commonmasterrequest.setUserName(useranme);
		commonmasterrequest.setPassword(password);
		commonmasterrequest.setSource(source);
		// String access_token = "24d14de9-8237-3ff9-805a-c212281d6e90";

		String access_token = token;

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("Authorization", "Bearer " + access_token);

		HttpEntity<CommaonMasterDistrictDto> APIRequest = new HttpEntity<>(commonmasterrequest, headers);

		// String
		// URL="https://devapigateway.upexciseonline.co/user-managment/v1.0.0/authentication/login";
		// loginurl
		thirdPartyResponse = restTemplate.exchange(loginurl, HttpMethod.POST, APIRequest, String.class).getBody();

		Gson gson = new Gson();

		// String gsonDto = gson.toJson(thirdPartyResponse);

		ObjectMapper mapper = new ObjectMapper();

		String bearertoken = null;

		JsonNode actualObj = null;

		String response = null;

		try {
			actualObj = mapper.readTree(thirdPartyResponse);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		bearertoken = actualObj.get("content").get("auth").get("token").asText();

		System.out.println("bearertoken::::" + bearertoken);

		response = cmservice.countryAPI(countryurl, bearertoken, access_token, country);

		return response;

	}

	public String countryAPI(String countryurl, String BTToken, String accesstoken, String country) {

		String countryAPIresponse = null;

		CommaonMasterDistrictDto commonmasterrequest = new CommaonMasterDistrictDto();

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("X-Authorization", BTToken);

		headers.set("Authorization", "Bearer " + accesstoken);

		HttpEntity<CommaonMasterDistrictDto> APIRequest = new HttpEntity<>(commonmasterrequest, headers);

		// String
		// URL="http://65.0.150.210:3503/masterData/location/findStateByCountryCode?countryCode=".concat(country);
		String URL = countryurl.concat(country);

		countryAPIresponse = restTemplate.exchange(URL, HttpMethod.GET, APIRequest, String.class).getBody();

		return countryAPIresponse;

	}

	public String APIcallLicence(CommaonMasterDistrictDto commonmasterrequest) {
		CommonMasterService cmservice = new CommonMasterService();
		String licencenumber = commonmasterrequest.getApplicationNumber();

		String passwordEn = commonmasterrequest.getPassword();

		String thirdPartyResponse = null;

		commonmasterrequest.setUserName(useranme);
		commonmasterrequest.setPassword(password);
		commonmasterrequest.setSource(source);

		// String access_token = "24d14de9-8237-3ff9-805a-c212281d6e90";

		String access_token = token;

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("Authorization", "Bearer " + access_token);

		HttpEntity<CommaonMasterDistrictDto> APIRequest = new HttpEntity<>(commonmasterrequest, headers);
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setConnectTimeout(90000); // 90 seconds
		requestFactory.setReadTimeout(90000);   // 90 seconds
		restTemplate.setRequestFactory(requestFactory);
		// String
		// URL="https://devapigateway.upexciseonline.co/user-managment/v1.0.0/authentication/login";
		// loginurl
		thirdPartyResponse = restTemplate.exchange(loginurl, HttpMethod.POST, APIRequest, String.class).getBody();
		log.info("::::::Login response :::::::" + thirdPartyResponse);
		log.info(":::login url::" + loginurl);
		Gson gson = new Gson();

		// String gsonDto = gson.toJson(thirdPartyResponse);

		ObjectMapper mapper = new ObjectMapper();

		String bearertoken = null;

		JsonNode actualObj = null;

		String response = null;

		try {
			actualObj = mapper.readTree(thirdPartyResponse);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		bearertoken = actualObj.get("content").get("auth").get("token").asText();

		System.out.println("bearertoken::::" + bearertoken);

		response = cmservice.LicenceAPPAPI(findlicenceurl, bearertoken, access_token, licencenumber);

		return response;

	}

	public String LicenceAPPAPI(String findlicenceurl, String BTToken, String accesstoken, String licencenumber) {

		String LicenceAPIresponse = null;

		CommaonMasteLicenceDTO commonmasterrequest = new CommaonMasteLicenceDTO();

		commonmasterrequest.setApplicationNumber(licencenumber);

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("X-Authorization", BTToken);

		headers.set("Authorization", "Bearer " + accesstoken);

		HttpEntity<CommaonMasteLicenceDTO> APIRequest = new HttpEntity<>(commonmasterrequest, headers);
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setConnectTimeout(90000); // 90 seconds
		requestFactory.setReadTimeout(90000);   // 90 seconds
		restTemplate.setRequestFactory(requestFactory);
		// String
		// URL="http://devapigateway.upexciseonline.co/licensemanagement/v1.0.0/license/findLicenseApplication?applicationNumber=".concat(licencenumber);
		String URL = findlicenceurl.concat(licencenumber);
		LicenceAPIresponse = restTemplate.exchange(URL, HttpMethod.POST, APIRequest, String.class).getBody();
		log.info(":::::::Licence API Response::::::" + LicenceAPIresponse);
		log.info(":::Licence URL::::" + URL);
		return LicenceAPIresponse;

	}

	public String APIcalluploaddoucment(UploadDTO commonmasterrequest) {
		CommonMasterService cmservice = new CommonMasterService();

		String thirdPartyResponse = null;

		commonmasterrequest.setUserName(useranme);
		commonmasterrequest.setPassword(password);
		commonmasterrequest.setSource(source);
		// String access_token = "24d14de9-8237-3ff9-805a-c212281d6e90";

		String access_token = token;

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("Authorization", "Bearer " + access_token);

		HttpEntity<UploadDTO> APIRequest = new HttpEntity<>(commonmasterrequest, headers);

		// String
		// loginurl="https://devapigateway.upexciseonline.co/user-managment/v1.0.0/authentication/login";
		// loginurl
		thirdPartyResponse = restTemplate.exchange(loginurl, HttpMethod.POST, APIRequest, String.class).getBody();

		Gson gson = new Gson();

		// String gsonDto = gson.toJson(thirdPartyResponse);

		ObjectMapper mapper = new ObjectMapper();

		String bearertoken = null;

		JsonNode actualObj = null;

		String response = null;

		try {
			actualObj = mapper.readTree(thirdPartyResponse);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		bearertoken = actualObj.get("content").get("auth").get("token").asText();

		System.out.println("bearertoken::::" + bearertoken);

		// String
		// uploadurl="http://65.0.150.210:3517/docManagement/api/document/uploaddocumentbyversion";

		response = cmservice.uploadAPI(uploadurl, bearertoken, access_token, commonmasterrequest);

		return response;

	}

	public String APIcalldownloaddoucment(DownloadDTO commonmasterrequest) {
		CommonMasterService cmservice = new CommonMasterService();

		String thirdPartyResponse = null;

		commonmasterrequest.setUserName(useranme);
		commonmasterrequest.setPassword(password);
		commonmasterrequest.setSource(source);
		// commonmasterrequest.setUserName("john@yopmail.com");
		// commonmasterrequest.setPassword("johnajan");

		String access_token = token;

		// String access_token = "24d14de9-8237-3ff9-805a-c212281d6e90";

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("Authorization", "Bearer " + access_token);

		HttpEntity<DownloadDTO> APIRequest = new HttpEntity<>(commonmasterrequest, headers);

		// String
		// loginurl="https://devapigateway.upexciseonline.co/user-managment/v1.0.0/authentication/login";
		// loginurl
		thirdPartyResponse = restTemplate.exchange(loginurl, HttpMethod.POST, APIRequest, String.class).getBody();

		Gson gson = new Gson();

		// String gsonDto = gson.toJson(thirdPartyResponse);

		ObjectMapper mapper = new ObjectMapper();

		String bearertoken = null;

		JsonNode actualObj = null;

		String response = null;

		try {
			actualObj = mapper.readTree(thirdPartyResponse);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		bearertoken = actualObj.get("content").get("auth").get("token").asText();

		System.out.println("bearertoken::::" + bearertoken);

		// String
		// downloadurl="https://devapigateway.upexciseonline.co/docManagement/v1.0.0/api/document/downloadfile?";

		response = cmservice.downloadAPI(downloadurl, bearertoken, access_token, commonmasterrequest);

		return response;

	}

	// public HashMap<String, String> downloadAPI(String downloadurl,String
	// BTToken,String accesstoken,DownloadDTO commonmasterrequest) {
	public String downloadAPI(String downloadurl, String BTToken, String accesstoken, DownloadDTO commonmasterrequest) {

		HashMap<String, String> map = new HashMap<>();
		String uuid = "uuid=" + commonmasterrequest.getUuids();

		// String filename="fileName="+commonmasterrequest.getFilename();

		String UploadAPIresponse = null;

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("X-Authorization", BTToken);

		headers.set("Authorization", "Bearer " + accesstoken);

		HttpEntity<DownloadDTO> APIRequest = new HttpEntity<>(commonmasterrequest, headers);

		// String URL=downloadurl.concat(uuid).concat("&").concat(filename);
		// String URL=downloadurl.concat(uuid).toString();

		String URL = downloadurl;
		log.info("::::image View API URL::::::::::" + URL);
		log.info("::::image View API request::::::::::" + APIRequest);
		try {
			UploadAPIresponse = restTemplate.exchange(URL, HttpMethod.POST, APIRequest, String.class).getBody();
			log.info("::::image View API ::::::::::" + UploadAPIresponse);
		} catch (Exception e) {
			e.printStackTrace();

			return UploadAPIresponse;
		}
		return UploadAPIresponse;

	}

	public String APIcalldownloadfile(DownlofileDTO commonmasterrequest) {
		CommonMasterService cmservice = new CommonMasterService();

		String thirdPartyResponse = null;

		commonmasterrequest.setUserName(useranme);
		commonmasterrequest.setPassword(password);
		commonmasterrequest.setSource(source);
		// commonmasterrequest.setUserName("john@yopmail.com");
		// commonmasterrequest.setPassword("johnajan");

		String access_token = token;

		// String access_token = "24d14de9-8237-3ff9-805a-c212281d6e90";

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("Authorization", "Bearer " + access_token);

		HttpEntity<DownlofileDTO> APIRequest = new HttpEntity<>(commonmasterrequest, headers);

		// String
		// loginurl="https://devapigateway.upexciseonline.co/user-managment/v1.0.0/authentication/login";
		// loginurl
		thirdPartyResponse = restTemplate.exchange(loginurl, HttpMethod.POST, APIRequest, String.class).getBody();

		Gson gson = new Gson();

		// String gsonDto = gson.toJson(thirdPartyResponse);

		ObjectMapper mapper = new ObjectMapper();

		String bearertoken = null;

		JsonNode actualObj = null;

		String response = null;

		try {
			actualObj = mapper.readTree(thirdPartyResponse);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		bearertoken = actualObj.get("content").get("auth").get("token").asText();

		System.out.println("bearertoken::::" + bearertoken);

		// String
		// downloadurl="https://devapigateway.upexciseonline.co/docManagement/v1.0.0/api/document/downloadfile?";

		response = cmservice.downloadfileAPI(downloadfileurl, bearertoken, access_token, commonmasterrequest);

		return response;

	}

	public String downloadfileAPI(String downloadurl, String BTToken, String accesstoken,
			DownlofileDTO commonmasterrequest) {

		HashMap<String, String> map = new HashMap<>();
		String uuid = "uuid=" + commonmasterrequest.getUuid();

		String filename = "fileName=" + commonmasterrequest.getFilename();

		String downloadfileAPIresponse = null;

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("X-Authorization", BTToken);

		headers.set("Authorization", "Bearer " + accesstoken);

		HttpEntity<DownlofileDTO> APIRequest = new HttpEntity<>(commonmasterrequest, headers);

		String URL = downloadurl.concat(uuid).concat("&").concat(filename);
		// String URL=downloadurl.concat(uuid);

		// String URL=downloadurl;
		try {
			downloadfileAPIresponse = restTemplate.exchange(URL, HttpMethod.GET, APIRequest, String.class).getBody();
		} catch (Exception e) {
			e.printStackTrace();
			return downloadfileAPIresponse;
		}
		return downloadfileAPIresponse;

	}

	public String Barandlabel(Brandlabeldto commonmasterrequest) {
		CommonMasterService cmservice = new CommonMasterService();
		DownloadDTO commonmasterrequest1 = new DownloadDTO();
		String thirdPartyResponse = null;

		commonmasterrequest1.setUserName(useranme);
		commonmasterrequest1.setPassword(password);
		commonmasterrequest1.setSource(source);
		String access_token = token;

		// String access_token = "24d14de9-8237-3ff9-805a-c212281d6e90";

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("Authorization", "Bearer " + access_token);

		HttpEntity<DownloadDTO> APIRequest = new HttpEntity<>(commonmasterrequest1, headers);

		// String
		// loginurl="https://devapigateway.upexciseonline.co/user-managment/v1.0.0/authentication/login";
		// loginurl
		thirdPartyResponse = restTemplate.exchange(loginurl, HttpMethod.POST, APIRequest, String.class).getBody();

		Gson gson = new Gson();

		// String gsonDto = gson.toJson(thirdPartyResponse);

		ObjectMapper mapper = new ObjectMapper();

		String bearertoken = null;

		JsonNode actualObj = null;

		String response = null;

		try {
			actualObj = mapper.readTree(thirdPartyResponse);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		bearertoken = actualObj.get("content").get("auth").get("token").asText();

		System.out.println("bearertoken::::" + bearertoken);

		// String
		// downloadurl="https://devapigateway.upexciseonline.co/docManagement/v1.0.0/api/document/downloadfile?";

		response = cmservice.brandAPI(brandurl, bearertoken, access_token, commonmasterrequest);

		return response;

	}

	public String brandAPI(String brandurl, String BTToken, String accesstoken, Brandlabeldto commonmasterrequest) {

		String brandAPIresponse = null;

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("X-Authorization", BTToken);

		headers.set("Authorization", "Bearer " + accesstoken);

		HttpEntity<Brandlabeldto> APIRequest = new HttpEntity<>(commonmasterrequest, headers);

		String URL = brandurl;

		brandAPIresponse = restTemplate.exchange(URL, HttpMethod.POST, APIRequest, String.class).getBody();

		return brandAPIresponse;

	}

	public GenericResponse Usermanagementsearch(UMsearchDTO commonmasterrequest) {
		BaseResponse finalResponse = new BaseResponse();
		CommonMasterService cmservice = new CommonMasterService();
		DownloadDTO commonmasterrequest1 = new DownloadDTO();
		String thirdPartyResponse = null;
		String serachnumber = null;
		commonmasterrequest1.setUserName(useranme);
		commonmasterrequest1.setPassword(password);
		commonmasterrequest1.setSource(source);
		serachnumber = commonmasterrequest.getSearch();
		String access_token = token;

		// String access_token = "24d14de9-8237-3ff9-805a-c212281d6e90";

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("Authorization", "Bearer " + access_token);

		HttpEntity<DownloadDTO> APIRequest = new HttpEntity<>(commonmasterrequest1, headers);

		// String
		// loginurl="https://devapigateway.upexciseonline.co/user-managment/v1.0.0/authentication/login";
		// loginurl
		thirdPartyResponse = restTemplate.exchange(loginurl, HttpMethod.POST, APIRequest, String.class).getBody();

		Gson gson = new Gson();

		// String gsonDto = gson.toJson(thirdPartyResponse);

		ObjectMapper mapper = new ObjectMapper();

		String bearertoken = null;

		JsonNode actualObj = null;

		List<String> response = null;

		try {
			actualObj = mapper.readTree(thirdPartyResponse);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		bearertoken = actualObj.get("content").get("auth").get("token").asText();

		System.out.println("bearertoken::::" + bearertoken);

		// String
		// downloadurl="https://devapigateway.upexciseonline.co/docManagement/v1.0.0/api/document/downloadfile?";

		response = cmservice.UMSearchAPI(searchurl, bearertoken, access_token, serachnumber);

		if (response.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}

		return Library.getSuccessfulResponse(response, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

	public List<String> UMSearchAPI(String searchurl, String BTToken, String accesstoken, String serachnumber) {

		UMsearchDTO commonmasterrequest = new UMsearchDTO();
		commonmasterrequest.setSearch(serachnumber);

		LicenseResponseDTO brandAPIresponse = null;

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("X-Authorization", BTToken);

		headers.set("Authorization", "Bearer " + accesstoken);

		HttpEntity<UMsearchDTO> APIRequest = new HttpEntity<>(commonmasterrequest, headers);

		String URL = searchurl.concat(serachnumber);

		URL = URL.replaceAll("\\s+", "").toString();

		// String
		// URL="http://testapigateway.upexciseonline.co/user-managment/v1.0.0/user/getUserByPhoneOrEmail?search=9942177234";
		List<String> finalList = new ArrayList<String>();

		LicenseResponseDTO licenselist = null;
		try {
			licenselist = restTemplate.exchange(URL.toString(), HttpMethod.GET, APIRequest, LicenseResponseDTO.class)
					.getBody();
		} catch (Exception e) {
			return (List<String>) licenselist;
		}

		if (licenselist.getResponseMessage().equalsIgnoreCase("Record not found.")) {
			throw new InvalidDataValidation("Record Not Found");
		} else {

			licenselist.getContent().forEach(list -> {
				if (StringUtils.isBlank(list.getApplicationNumber())) {
					return;
				}

				finalList.add(list.getApplicationNumber());
			});

			return finalList;
		}

	}

	public String uploadAPI(String uploadurl, String BTToken, String accesstoken, UploadDTO commonmasterrequest) {

		String UploadAPIresponse = null;

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("X-Authorization", BTToken);

		headers.set("Authorization", "Bearer " + accesstoken);

		HttpEntity<UploadDTO> APIRequest = new HttpEntity<>(commonmasterrequest, headers);

		String URL = uploadurl;

		UploadAPIresponse = restTemplate.exchange(URL, HttpMethod.POST, APIRequest, String.class).getBody();

		return UploadAPIresponse;

	}

	public HashMap<String, String> APIlogincall(LicenceDTO commonmasterrequest) {
		CommonMasterService cmservice = new CommonMasterService();
		// commonmasterrequest.setUserName(useranme);
		// commonmasterrequest.setPassword(password);
		String thirdPartyResponse = null;
		// CommonMasterRequestDTO commonmasterrequest = new CommonMasterRequestDTO();

		// String access_token = "24d14de9-8237-3ff9-805a-c212281d6e90";

		String access_token = token;

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("Authorization", "Bearer " + access_token);

		HttpEntity<LicenceDTO> APIRequest = new HttpEntity<>(commonmasterrequest, headers);

		// String
		// URL="https://devapigateway.upexciseonline.co/user-managment/v1.0.0/authentication/login";
		// loginurl

		thirdPartyResponse = restTemplate.exchange(loginurl, HttpMethod.POST, APIRequest, String.class).getBody();

		Gson gson = new Gson();

		// String gsonDto = gson.toJson(thirdPartyResponse);

		ObjectMapper mapper = new ObjectMapper();

		String bearertoken = null;

		String username = null;

		String entitylist = null;

		String allocatedEntityName = null;

		String email = null;

		String designationCode = null;

		String userDistrictList = null;

		String lastLoginTime = null;

		JsonNode actualObj = null;
		JsonNode actualObj1 = null;

		String response = null;

		String roleCode = null;
		String responsemessage = null;

		String isCustomer = null;
		String responsecode = null;
		HashMap<String, String> map = new HashMap<>();

		try {
			// actualObj = mapper.readTree(thirdPartyResponse.replace("token",
			// "").replace("Bearer", "").replace("eyJhbGciOiJIUzUxMiJ9.", ""));
			// actualObj=mapper.readTree(thirdPartyResponse.replace("Bearer
			// eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VySWRcIjo1MDgsXCJ1c2VyTmFtZVwiOlwiZGVvYWdyYV9uZXdcIixcImVudGl0eUxpc3RcIjpbXCIxMjM0NVwiLFwiMjAwNzVcIixcIkJSRVdFUllcIixcIjM0NTY3XCIsXCJESVNUSUxMRVJZXCIsXCJFTkMxMjFcIixcIkVOQzIxMjFcIixcIkVOQzMzM1wiLFwiNDY1NzhcIixcIkVDTzEyXCIsXCJIRUFEX09GRklDRVwiLFwiTElDRU5TRV9NQU5BR0VNRU5UXCIsXCJFTjAwMVwiLFwiNzg5NjU0XCIsXCJSRVRBSUxcIixcIjc3MVwiLFwiNDU2N1wiLFwiMTIzNDU2XCIsXCJTVUdBUl9NSUxMXCIsXCI1NTZcIixcIldBUkVIT1VTRVwiLFwiV0hPTEVTQUxFXCIsXCJXSU5FUllcIixcIjU0MzIxXCJdLFwiYWxsb2NhdGVkRW50aXR5TmFtZVwiOnt9LFwiZW1haWxcIjpcImRlb2FncmFfbmV3QHlvcG1haWwuY29tXCIsXCJkZXNpZ25hdGlvbkNvZGVcIjpcImRlb2FncmFfbmV3XCIsXCJyb2xlQ29kZVwiOm51bGwsXCJ0b2tlblwiOm51bGwsXCJpc0N1c3RvbWVyXCI6ZmFsc2UsXCJ1c2VyRGlzdHJpY3RMaXN0XCI6W1wiMTE4XCIsXCIxMTlcIixcIjEyMVwiLFwiNjQwXCIsXCIxNTRcIixcIjEyMlwiLFwiMTQwXCIsXCIxMjNcIixcIjEyNFwiLFwiMTI1XCIsXCIxMjZcIixcIjEyN1wiLFwiMTI4XCIsXCIxMjlcIixcIjEzMFwiLFwiMTMxXCIsXCIxNzlcIixcIjEzMlwiLFwiMTMzXCIsXCIxMzRcIixcIjEzNVwiLFwiMTM2XCIsXCIxMzdcIixcIjEzOFwiLFwiMTM5XCIsXCIxNDFcIixcIjE0MlwiLFwiMTQzXCIsXCIxNDRcIixcIjE0NVwiLFwiMTQ2XCIsXCIxNDdcIixcIjE0OFwiLFwiMTQ5XCIsXCI2NjFcIixcIjE1MFwiLFwiMTYzXCIsXCIxNTFcIixcIjE1MlwiLFwiMTUzXCIsXCIxNTVcIixcIjE1NlwiLFwiMTU3XCIsXCI2MzNcIixcIjE1OFwiLFwiMTU5XCIsXCIxNjBcIixcIjE2MVwiLFwiMTYyXCIsXCIxNjRcIixcIjE2NVwiLFwiMTY2XCIsXCIxNjdcIixcIjE2OFwiLFwiMTY5XCIsXCIxNzBcIixcIjE3MVwiLFwiMTcyXCIsXCIxNzNcIixcIjE3NFwiLFwiMTIwXCIsXCIxNzVcIixcIjE3NlwiLFwiMTc3XCIsXCI2NTlcIixcIjE3OFwiLFwiMTgwXCIsXCI2NjBcIixcIjE4MVwiLFwiMTgyXCIsXCIxODNcIixcIjE4NFwiLFwiMTg1XCIsXCIxODZcIixcIjE4N1wiXX0iLCJpYXQiOjE2NDkzOTk3OTQsImV4cCI6MTY1MDAwNDU5NH0.qxcQXPmeUVtSXcmhqMWN8RzYV8xLriASLY1k35rDAX-r0QZ36Q6QdqW1sVRCtDdO1pv2KixwXtR-r3pTnoVEbw",
			// ""));
			actualObj = mapper.readTree(thirdPartyResponse);

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		responsecode = actualObj.get("responseCode").asText();
		responsemessage = actualObj.get("responseMessage").asText();
		if (responsecode.equals("200")) {
			String BTOken = actualObj.get("content").get("auth").get("token").asText();
			// bearertoken =
			// actualObj.get("content").get("auth").get("userId").get("userName").get("entityList").get("allocatedEntityName").get("email").get("designationCode").get("designationCode").get("roleCode").get("userDistrictList");
			bearertoken = actualObj.get("content").get("auth").get("userId").toString();
			username = actualObj.get("content").get("auth").get("userName").asText();
			entitylist = actualObj.get("content").get("auth").get("entityList").textValue();
			allocatedEntityName = actualObj.get("content").get("auth").get("allocatedEntityName").asText();
			email = actualObj.get("content").get("auth").get("email").asText();
			designationCode = actualObj.get("content").get("auth").get("designationCode").asText();
			roleCode = actualObj.get("content").get("auth").get("roleCode").asText();
			userDistrictList = actualObj.get("content").get("auth").get("userDistrictList").asText();
			isCustomer = actualObj.get("content").get("auth").get("isCustomer").asText();
			// lastLoginTime=actualObj.get("content").get("auth").get("lastLoginTime").asText();
			// HashMap<String, ArrayList<String>> map = new HashMap<String,
			// ArrayList<String>>();

			try {
				response = cmservice.SearchLicenceAPP(serachlicenceapp, BTOken, access_token, commonmasterrequest);

			} catch (Exception e) {
				e.printStackTrace();
				map.put("Licence", "Bad Request");
			}
			String jsonFormattedString = response.replace("'\\\\'", "");

			map.put("Licence", jsonFormattedString);

			map.put("userid", bearertoken);
			map.put("username", username);
			map.put("email", email);
			// map.put("entitylist", entitylist);
			// map.put("allocatedEntityName", allocatedEntityName);
			map.put("designationCode", designationCode);
			map.put("roleCode", roleCode);
			map.put("isCustomer", isCustomer);
			// map.put("userDistrictList", userDistrictList);
			// map.put("lastLoginTime", lastLoginTime);

			return map;

		}

		else {
			map.put("responseMessage", responsemessage);
			return map;

		}

	}

//	public static void main(String[] args) {
//		UMsearchDTO commonmasterrequest =new UMsearchDTO();
//		
//		//UploadDTO commonmasterrequest=new UploadDTO();
//		//DownloadDTO commonmasterrequest=new DownloadDTO();
//		CommaonMasterDistrictDto request=new CommaonMasterDistrictDto();
//		CommonMasterService obj=new CommonMasterService();
//		commonmasterrequest.setUserName("business");
//		commonmasterrequest.setPassword("123456");
//		commonmasterrequest.setSearch("9942177234");
////		commonmasterrequest.setFilename("Bottling%20plan.png");
////		commonmasterrequest.setUuid("68d90007-c89d-4ac2-8979-bbbf8133a41a");
////		commonmasterrequest.setFile("binary");
////		commonmasterrequest.setModulename("berewery");
////		commonmasterrequest.setScreename("brite reg");
////		commonmasterrequest.setApplicationNumber("BRE_2021-2022_25MAR22_1125");
//		//commonmasterrequest.setType("ENTITY_TYPE");
////		request.setStatecode(9);
////		request.setCountrycode("IN");
////		request.setApplicationNumber("HBRL23600554");
//		obj.Usermanagementsearch(commonmasterrequest);
//	}

	public GenericResponse getByRole(String id) {
		BaseResponse finalResponse = new BaseResponse();
		CommonMasterService cmservice = new CommonMasterService();
		DownloadDTO commonmasterrequest1 = new DownloadDTO();
		String thirdPartyResponse = null;

		commonmasterrequest1.setUserName(useranme);
		commonmasterrequest1.setPassword(password);
		commonmasterrequest1.setSource(source);
		String access_token = token;

		// String access_token = "24d14de9-8237-3ff9-805a-c212281d6e90";

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("Authorization", "Bearer " + access_token);

		HttpEntity<DownloadDTO> APIRequest = new HttpEntity<>(commonmasterrequest1, headers);

		// String
		// loginurl="https://devapigateway.upexciseonline.co/user-managment/v1.0.0/authentication/login";
		// loginurl
		thirdPartyResponse = restTemplate.exchange(loginurl, HttpMethod.POST, APIRequest, String.class).getBody();

		Gson gson = new Gson();

		// String gsonDto = gson.toJson(thirdPartyResponse);

		ObjectMapper mapper = new ObjectMapper();

		String bearertoken = null;

		JsonNode actualObj = null;

		BaseResponse response = null;

		try {
			actualObj = mapper.readTree(thirdPartyResponse);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		bearertoken = actualObj.get("content").get("auth").get("token").asText();

		System.out.println("bearertoken::::" + bearertoken);

		// String
		// downloadurl="https://devapigateway.upexciseonline.co/docManagement/v1.0.0/api/document/downloadfile?";

		response = cmservice.UMRoleAPI(role, bearertoken, access_token, id);

		if (response.getResponseCode() != 200) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(), response.getResponseMessage());
		}

		return Library.getSuccessfulResponse(response.getContent(), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

	private BaseResponse UMRoleAPI(String role2, String BTToken, String accesstoken, String id) {
		UMsearchDTO commonmasterrequest = new UMsearchDTO();
		commonmasterrequest.setSearch(id);

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("X-Authorization", BTToken);

		headers.set("Authorization", "Bearer " + accesstoken);

		HttpEntity<UMsearchDTO> APIRequest = new HttpEntity<>(commonmasterrequest, headers);

		String URL = role2.concat(id);

		// System.out.println(" URL --------> "+URL);

		URL = URL.replaceAll("\\s+", "").toString();

		// String
		// URL="http://devapigateway.upexciseonline.co/user-managment/v1.0.0/user/getUserById?id=2";

		BaseResponse response = restTemplate.exchange(URL.toString(), HttpMethod.GET, APIRequest, BaseResponse.class)
				.getBody();

		return response;
	}

	public String APIsearchcall(LicenceDTO commonmasterrequest) {
		CommonMasterService cmservice = new CommonMasterService();

		String thirdPartyResponse = null;
		String access_token = token;

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("Authorization", "Bearer " + access_token);

		HttpEntity<LicenceDTO> APIRequest = new HttpEntity<>(commonmasterrequest, headers);

		thirdPartyResponse = restTemplate.exchange(loginurl, HttpMethod.POST, APIRequest, String.class).getBody();

		Gson gson = new Gson();

		// String gsonDto = gson.toJson(thirdPartyResponse);

		ObjectMapper mapper = new ObjectMapper();

		String bearertoken = null;

		JsonNode actualObj = null;

		String response = null;

		try {
			actualObj = mapper.readTree(thirdPartyResponse);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		bearertoken = actualObj.get("content").get("auth").get("token").asText();

		System.out.println("bearertoken::::" + bearertoken);

		response = cmservice.SearchLicenceAPP(serachlicenceapp, bearertoken, access_token, commonmasterrequest);

		return response;

	}

	public String SearchLicenceAPP(String serachlicenceapp, String BTToken, String accesstoken,
			LicenceDTO commonmasterrequest) {

		String searchAPIresponse = null;

		try {

			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();

			headers.set("X-Authorization", BTToken);

			headers.set("Authorization", "Bearer " + accesstoken);

			HttpEntity<LicenceDTO> APIRequest = new HttpEntity<>(commonmasterrequest, headers);

			searchAPIresponse = restTemplate.exchange(serachlicenceapp, HttpMethod.POST, APIRequest, String.class)
					.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			return searchAPIresponse;
		}

		return searchAPIresponse;

	}

	public String APIcallgetuserbyrole(UserDto commonmasterrequest) {
		CommonMasterService cmservice = new CommonMasterService();
		String rolecode = commonmasterrequest.getRoleCode();

		String thirdPartyResponse = null;

		commonmasterrequest.setUserName(useranme);
		commonmasterrequest.setPassword(password);
		commonmasterrequest.setSource(source);

		// String access_token = "24d14de9-8237-3ff9-805a-c212281d6e90";

		String access_token = token;

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("Authorization", "Bearer " + access_token);

		HttpEntity<UserDto> APIRequest = new HttpEntity<>(commonmasterrequest, headers);

		// String
		// URL="https://devapigateway.upexciseonline.co/user-managment/v1.0.0/authentication/login";
		// loginurl
		thirdPartyResponse = restTemplate.exchange(loginurl, HttpMethod.POST, APIRequest, String.class).getBody();

		Gson gson = new Gson();

		// String gsonDto = gson.toJson(thirdPartyResponse);

		ObjectMapper mapper = new ObjectMapper();

		String bearertoken = null;

		JsonNode actualObj = null;

		String response = null;

		try {
			actualObj = mapper.readTree(thirdPartyResponse);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		bearertoken = actualObj.get("content").get("auth").get("token").asText();

		System.out.println("bearertoken::::" + bearertoken);

		response = cmservice.RoleAPI(roleurl, bearertoken, access_token, rolecode);

		return response;

	}

	public String RoleAPI(String roleurl, String BTToken, String accesstoken, String rolecode) {

		String RoleAPIresponse = null;

		UserDto commonmasterrequest = new UserDto();

		commonmasterrequest.setRoleCode(rolecode);

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("X-Authorization", BTToken);

		headers.set("Authorization", "Bearer " + accesstoken);

		HttpEntity<UserDto> APIRequest = new HttpEntity<>(commonmasterrequest, headers);

		// String
		// URL="http://devapigateway.upexciseonline.co/licensemanagement/v1.0.0/license/findLicenseApplication?applicationNumber=".concat(licencenumber);
		String URL = roleurl.concat(rolecode);

		RoleAPIresponse = restTemplate.exchange(URL, HttpMethod.GET, APIRequest, String.class).getBody();

		return RoleAPIresponse;

	}

	public String APIcallroletype(UserDto commonmasterrequest) {
		CommonMasterService cmservice = new CommonMasterService();
		String usertypecode = commonmasterrequest.getUserTypeCode();

		String thirdPartyResponse = null;

		commonmasterrequest.setUserName(useranme);
		commonmasterrequest.setPassword(password);
		commonmasterrequest.setSource(source);

		// String access_token = "24d14de9-8237-3ff9-805a-c212281d6e90";

		String access_token = token;

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("Authorization", "Bearer " + access_token);

		HttpEntity<UserDto> APIRequest = new HttpEntity<>(commonmasterrequest, headers);

		// String
		// URL="https://devapigateway.upexciseonline.co/user-managment/v1.0.0/authentication/login";
		// loginurl
		thirdPartyResponse = restTemplate.exchange(loginurl, HttpMethod.POST, APIRequest, String.class).getBody();

		Gson gson = new Gson();

		// String gsonDto = gson.toJson(thirdPartyResponse);

		ObjectMapper mapper = new ObjectMapper();

		String bearertoken = null;

		JsonNode actualObj = null;

		String response = null;

		try {
			actualObj = mapper.readTree(thirdPartyResponse);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		bearertoken = actualObj.get("content").get("auth").get("token").asText();

		System.out.println("bearertoken::::" + bearertoken);

		response = cmservice.RoletypeAPI(usertypeurl, bearertoken, access_token, usertypecode);

		return response;

	}

	public String RoletypeAPI(String usertypeurl, String BTToken, String accesstoken, String usertypecode) {

		String RoletypeAPI = null;

		UserDto commonmasterrequest = new UserDto();

		commonmasterrequest.setUserTypeCode(usertypecode);

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("X-Authorization", BTToken);

		headers.set("Authorization", "Bearer " + accesstoken);

		HttpEntity<UserDto> APIRequest = new HttpEntity<>(commonmasterrequest, headers);

		String URL = usertypeurl.concat(usertypecode);

		RoletypeAPI = restTemplate.exchange(URL, HttpMethod.GET, APIRequest, String.class).getBody();

		return RoletypeAPI;

	}

	public String EntityDetails(UMsearchDTO commonmasterrequest) {
		CommonMasterService cmservice = new CommonMasterService();
		UMsearchDTO commonmasterrequest1 = new UMsearchDTO();
		String thirdPartyResponse = null;
		commonmasterrequest1.setUserName(useranme);
		commonmasterrequest1.setPassword(password);
		commonmasterrequest1.setSource(source);
		String access_token = token;

		// String access_token = "24d14de9-8237-3ff9-805a-c212281d6e90";

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("Authorization", "Bearer " + access_token);

		HttpEntity<UMsearchDTO> APIRequest = new HttpEntity<>(commonmasterrequest1, headers);

		// String
		// loginurl="https://devapigateway.upexciseonline.co/user-managment/v1.0.0/authentication/login";
		// loginurl
		thirdPartyResponse = restTemplate.exchange(loginurl, HttpMethod.POST, APIRequest, String.class).getBody();

		Gson gson = new Gson();

		ObjectMapper mapper = new ObjectMapper();

		String bearertoken = null;

		JsonNode actualObj = null;

		String response = null;

		try {
			actualObj = mapper.readTree(thirdPartyResponse);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		bearertoken = actualObj.get("content").get("auth").get("token").asText();
		response = cmservice.EntityDetails(entityURL, bearertoken, access_token, commonmasterrequest);
		return response;
	}

	public String EntityDetails(String brandurl, String BTToken, String accesstoken, UMsearchDTO commonmasterrequest) {
		EntityShopDTO commonmasterrequest1 = new EntityShopDTO();

		commonmasterrequest1.setShopId(commonmasterrequest.getShopId());

		commonmasterrequest1.setUserId(commonmasterrequest.getUserId());

		String entityAPIresponse = null;

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("X-Authorization", BTToken);

		headers.set("Authorization", "Bearer " + accesstoken);

		HttpEntity<EntityShopDTO> APIRequest = new HttpEntity<>(commonmasterrequest1, headers);

		String shopid = commonmasterrequest.getSearch();

		String URL = null;
		try {
			if (commonmasterrequest1.getShopId() != null && !commonmasterrequest1.getShopId().isEmpty()) {
				URL = brandurl.concat("shopId=" + String.valueOf(commonmasterrequest1.getShopId()));
			} else {
				URL = brandurl.concat("userId=" + String.valueOf(commonmasterrequest1.getUserId()));

			}
			entityAPIresponse = restTemplate.exchange(URL, HttpMethod.GET, APIRequest, String.class).getBody();
			return entityAPIresponse;
		} catch (Exception e) {
			return "{responseMessage:Record not found.}";
		}

	}

	public String APIcallentityType(EntityDTO commonmasterrequest) {
		CommonMasterService cmservice = new CommonMasterService();
		String thirdPartyResponse = null;
		String entitytype = commonmasterrequest.getEntityType();
		commonmasterrequest.setUserName(useranme);
		commonmasterrequest.setPassword(password);
		commonmasterrequest.setSource(source);
		String access_token = token;
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + access_token);
		HttpEntity<EntityDTO> APIRequest = new HttpEntity<>(commonmasterrequest, headers);
		thirdPartyResponse = restTemplate.exchange(loginurl, HttpMethod.POST, APIRequest, String.class).getBody();
		Gson gson = new Gson();
		ObjectMapper mapper = new ObjectMapper();
		String bearertoken = null;
		JsonNode actualObj = null;
		String response = null;
		try {
			actualObj = mapper.readTree(thirdPartyResponse);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bearertoken = actualObj.get("content").get("auth").get("token").asText();
		System.out.println("bearertoken::::" + bearertoken);
		response = cmservice.EntityTypeAPI(entitytypeurl, bearertoken, access_token, entitytype);
		return response;

	}

	public String EntityTypeAPI(String usertypeurl, String BTToken, String accesstoken, String entitytype) {

		String RoletypeAPI = null;

		EntityDTO commonmasterrequest = new EntityDTO();

		commonmasterrequest.setEntityType(entitytype);

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("X-Authorization", BTToken);

		headers.set("Authorization", "Bearer " + accesstoken);

		HttpEntity<EntityDTO> APIRequest = new HttpEntity<>(commonmasterrequest, headers);

		String URL = usertypeurl.concat(entitytype);

		RoletypeAPI = restTemplate.exchange(URL, HttpMethod.GET, APIRequest, String.class).getBody();

		return RoletypeAPI;

	}

	public String APIcallunitname(EntityDTO commonmasterrequest) {
		CommonMasterService cmservice = new CommonMasterService();
		String thirdPartyResponse = null;
		String entitytype = commonmasterrequest.getEntityType();
		String unitName = commonmasterrequest.getUnitName();
		String unitCode = commonmasterrequest.getUnitCode();
		commonmasterrequest.setUserName(useranme);
		commonmasterrequest.setPassword(password);
		commonmasterrequest.setSource(source);
		String access_token = token;
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + access_token);
		HttpEntity<EntityDTO> APIRequest = new HttpEntity<>(commonmasterrequest, headers);
		thirdPartyResponse = restTemplate.exchange(loginurl, HttpMethod.POST, APIRequest, String.class).getBody();
		Gson gson = new Gson();
		ObjectMapper mapper = new ObjectMapper();
		String bearertoken = null;
		JsonNode actualObj = null;
		String response = null;
		try {
			actualObj = mapper.readTree(thirdPartyResponse);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bearertoken = actualObj.get("content").get("auth").get("token").asText();
		System.out.println("bearertoken::::" + bearertoken);
		response = cmservice.UnitNameAPI(unitnameurl, bearertoken, access_token, entitytype, unitName, unitCode);
		return response;

	}

	public String UnitNameAPI(String unitnameurl, String BTToken, String accesstoken, String entitytype,
			String unitName, String unitCode) {
		String unitNameAPI = null;
		EntityDTO commonmasterrequest = new EntityDTO();
		commonmasterrequest.setEntityType(entitytype);
		commonmasterrequest.setUnitCode(unitCode);
		commonmasterrequest.setUnitName(unitName);

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("X-Authorization", BTToken);

		headers.set("Authorization", "Bearer " + accesstoken);

		HttpEntity<EntityDTO> APIRequest = new HttpEntity<>(commonmasterrequest, headers);
		String unitNameURL = unitnameurl;

		if (entitytype != null && unitName != null && unitCode != null) {
			unitNameURL = unitnameurl.concat(entitytype).concat("&unitName=").concat(unitName).concat("&unitCode")
					.concat(unitCode);
		} else if (entitytype != null && unitName != null) {
			unitNameURL = unitnameurl.concat(entitytype).concat("&unitName=").concat(unitName);
		} else if (entitytype != null && unitCode != null) {
			unitNameURL = unitnameurl.concat(entitytype).concat("&unitCode=").concat(unitCode);
		}

		unitNameAPI = restTemplate.exchange(unitNameURL, HttpMethod.GET, APIRequest, String.class).getBody();

		return unitNameAPI;

	}

	public String applicationNumberAPI(CommaonMasterDistrictDto commonmasterrequest) {
		String userdetails = commonmasterrequest.getUserDetails();
		String passwordEn = commonmasterrequest.getPassword();
		CommonMasterService cmservice = new CommonMasterService();
		String thirdPartyResponse = null;
		commonmasterrequest.setUserName(useranme);
		commonmasterrequest.setPassword(password);
		commonmasterrequest.setSource(source);

		String access_token = token;

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("Authorization", "Bearer " + access_token);
		HttpEntity<CommaonMasterDistrictDto> APIRequest = new HttpEntity<>(commonmasterrequest, headers);
		thirdPartyResponse = restTemplate.exchange(loginurl, HttpMethod.POST, APIRequest, String.class).getBody();
		log.info("::::::Login response :::::::" + thirdPartyResponse);
		log.info(":::login url::" + loginurl);
		Gson gson = new Gson();

		ObjectMapper mapper = new ObjectMapper();

		String bearertoken = null;

		JsonNode actualObj = null;

		String response = null;

		try {
			actualObj = mapper.readTree(thirdPartyResponse);
		} catch (JsonProcessingException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		bearertoken = actualObj.get("content").get("auth").get("token").asText();

		System.out.println("bearertoken::::" + bearertoken);

		response = cmservice.getUserNameAPI(findUserDetailsURL, bearertoken, access_token, userdetails);

		return response;

	}

	public String getUserNameAPI(String findUserDetailsURL, String BTToken, String accesstoken, String userdetails) {

		String userNameAPIResponce = null;

		CommaonMasteGetUserNameDTO commonmastergetusernamedto = new CommaonMasteGetUserNameDTO();

		commonmastergetusernamedto.setUsername(userdetails);

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("X-Authorization", BTToken);

		headers.set("Authorization", "Bearer " + accesstoken);

		HttpEntity<CommaonMasteGetUserNameDTO> APIRequest = new HttpEntity<>(commonmastergetusernamedto, headers);
		String URL = findUserDetailsURL;
		userNameAPIResponce = restTemplate.exchange(URL, HttpMethod.POST, APIRequest, String.class).getBody();
		log.info(":::::::GetUserName API Response::::::" + userNameAPIResponce);
		log.info(":::GetUserName URL::::" + URL);
		return userNameAPIResponce;

	}

	public String userUnitCodeAPI(CommanMasterUnitCodeDTO commonmasterrequest) {

		String userUnitCode = commonmasterrequest.getUserUnitCode();
		String password2 = commonmasterrequest.getPassword();
		CommonMasterService cmservice = new CommonMasterService();
		String thirdPartyResponse = null;
		commonmasterrequest.setUserName(useranme);
		commonmasterrequest.setPassword(password);
		commonmasterrequest.setSource(source);
		String access_token = token;

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();

		headers.set("Authorization", "Bearer " + access_token);
		HttpEntity<CommanMasterUnitCodeDTO> APIRequest = new HttpEntity<>(commonmasterrequest, headers);
		thirdPartyResponse = restTemplate.exchange(loginurl, HttpMethod.POST, APIRequest, String.class).getBody();
		log.info("::::::Login response :::::::" + thirdPartyResponse);
		log.info(":::login url::" + loginurl);

		Gson gson = new Gson();

		ObjectMapper mapper = new ObjectMapper();
		String bearertoken = null;

		JsonNode actualObj = null;

		String response = null;

		try {
			actualObj = mapper.readTree(thirdPartyResponse);
		} catch (JsonProcessingException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		bearertoken = actualObj.get("content").get("auth").get("token").asText();

		response = cmservice.getUserUnitCodeAPI(findUserUnitCodeURL, bearertoken, access_token, userUnitCode);
		return response;

	}

	public String getUserUnitCodeAPI(String findUserUnitCodeURL, String BTToken, String accesstoken,
			String userUnitCode) {

		String getUserUnitCodeAPIResponce = null;

		CommanMasterGetUnitCodeDTO commanmastergetunitcode = new CommanMasterGetUnitCodeDTO();

		commanmastergetunitcode.setUnitCode(userUnitCode);

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.set("X-Authorization", BTToken);

		headers.set("Authorization", "Bearer " + accesstoken);
		HttpEntity<CommanMasterGetUnitCodeDTO> APIRequest = new HttpEntity<>(commanmastergetunitcode, headers);
		String URL = findUserUnitCodeURL.concat(userUnitCode);

		getUserUnitCodeAPIResponce = restTemplate.exchange(URL, HttpMethod.GET, APIRequest, String.class).getBody();

		log.info(":::::::GetUnitCode API Response::::::" + getUserUnitCodeAPIResponce);
		log.info(":::GetUnitCode URL::::" + URL);

		return getUserUnitCodeAPIResponce;

	}
	
	
	public String UserloginTokenGet(CommanMasterUnitCodeDTO commonmasterrequest) {

		String userUnitCode = commonmasterrequest.getUserUnitCode();
		String password2 = commonmasterrequest.getPassword();
		CommonMasterService cmservice = new CommonMasterService();
		String thirdPartyResponse = null;
		commonmasterrequest.setUserName(useranme);
		commonmasterrequest.setPassword(password);
		commonmasterrequest.setSource(source);
		String access_token = token;

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();

		headers.set("Authorization", "Bearer " + access_token);
		HttpEntity<CommanMasterUnitCodeDTO> APIRequest = new HttpEntity<>(commonmasterrequest, headers);
		thirdPartyResponse = restTemplate.exchange(loginurl, HttpMethod.POST, APIRequest, String.class).getBody();
		log.info("::::::Login response :::::::" + thirdPartyResponse);
		log.info(":::login url::" + loginurl);

		Gson gson = new Gson();

		ObjectMapper mapper = new ObjectMapper();
		String bearertoken = null;

		JsonNode actualObj = null;

		String response = null;

		try {
			actualObj = mapper.readTree(thirdPartyResponse);
		} catch (JsonProcessingException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		bearertoken = actualObj.get("content").get("auth").get("token").asText();

		response =bearertoken; 
		return response;

	}

}
