package com.oasys.helpdesk.controller;

import javax.validation.Valid;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;

import com.oasys.helpdesk.dto.SLADashboardDTO;
import com.oasys.helpdesk.request.CreateTicketDashboardDTO;
import com.oasys.helpdesk.service.SlaUpexClientService;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "HelpDeskData", description = "This controller contain all  operation of Action Taken")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/sladashboard")
public class SlaDashboardController extends BaseController {

	@Autowired
	SlaUpexClientService slaupexclientservice;

	@PostMapping("/getupexciseproductionclient")
	@ApiOperation(value = "This api is used to add SLADashboard", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> add(@Valid @RequestBody SLADashboardDTO requestDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(slaupexclientservice.UpexClientServerData(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/getupexciseapiprimary")
	@ApiOperation(value = "This api is used to getupexciseapiprimary SLADashboard", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> primaryData(@Valid @RequestBody SLADashboardDTO requestDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(slaupexclientservice.upexciseApiPrimaryServerData(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/getupexciseapisecondary")
	@ApiOperation(value = "This api is used to getupexciseapiprimary SLADashboard", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> secondaryData(@Valid @RequestBody SLADashboardDTO requestDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(slaupexclientservice.upexciseApiSecondaryServerData(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);

	}

	@PostMapping("/getupexciseuiserver")
	@ApiOperation(value = "This api is used to getupexciseapiprimary SLADashboard", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> uiserveData(@Valid @RequestBody SLADashboardDTO requestDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(slaupexclientservice.upexciseUIServerData(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);

	}

	@PostMapping("/getupexcisehaproxyserver")
	@ApiOperation(value = "This api is used to getupexciseapiprimary SLADashboard", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> haproxyserverData(@Valid @RequestBody SLADashboardDTO requestDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(slaupexclientservice.upexciseHaproxyServerData(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);

	}

	@PostMapping("/getupexcisemasterdatabase")
	@ApiOperation(value = "This api is used to getupexciseapiprimary SLADashboard", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> masterserverData(@Valid @RequestBody SLADashboardDTO requestDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(slaupexclientservice.upexciseMasterServerData(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);

	}

	@PostMapping("/getupexciseslavedatabase")
	@ApiOperation(value = "This api is used to getupexciseapiprimary SLADashboard", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> slavedatabaseData(@Valid @RequestBody SLADashboardDTO requestDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(slaupexclientservice.upexciseSlaveDatabaseData(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);

	}

	@PostMapping("/getarchivedatabase")
	@ApiOperation(value = "This api is used to add archivedtabase", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> ArchiveupexDatabase(@Valid @RequestBody SLADashboardDTO requestDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(slaupexclientservice.upexcisearchivedtabaseData(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/getcms")
	@ApiOperation(value = "This api is used to add cms", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> cmsDatabase(@Valid @RequestBody SLADashboardDTO requestDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(slaupexclientservice.upexcmstabaseData(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/getjenkins")
	@ApiOperation(value = "This api is used to add jenkins", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> upexjenkins(@Valid @RequestBody SLADashboardDTO requestDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(slaupexclientservice.upexjenkinsData(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/getmdmserver")
	@ApiOperation(value = "This api is used to add mdmserver", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> upexmdmserver(@Valid @RequestBody SLADashboardDTO requestDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(slaupexclientservice.upexmdmserverData(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/getqrcode")
	@ApiOperation(value = "This api is used to add qrcode", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> upexqrcode(@Valid @RequestBody SLADashboardDTO requestDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(slaupexclientservice.upexqrerverData(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);

	}

	@PostMapping("/getwso2")
	@ApiOperation(value = "This api is used to add wso2", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> upexwso2(@Valid @RequestBody SLADashboardDTO requestDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(slaupexclientservice.wso2serverData(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/getmis")
	@ApiOperation(value = "This api is used to add mis", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> upexmis(@Valid @RequestBody SLADashboardDTO requestDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(slaupexclientservice.misData(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@PostMapping("/getupexciseallserver")
	@ApiOperation(value = "This api is used to add SLADashboard", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> addallserver(@Valid @RequestBody SLADashboardDTO requestDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(slaupexclientservice.AllServerData(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/upexserverdetails")
	@ApiOperation(value = "This api is to get Count", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> upexServerDetails(@Valid @RequestBody SLADashboardDTO requestDto) {
		GenericResponse objGenericResponse = slaupexclientservice.upexServerDetails(requestDto);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

}