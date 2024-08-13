package com.oasys.helpdesk.controller;

import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.request.EmailRequestCreationDto;
import com.oasys.helpdesk.request.EmailRequestDto;
import com.oasys.helpdesk.request.ReplyMailRequest;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.service.EmailRequestService;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//This is using spring security 
//based on the scope Domain can access  this controller 
//its access and right give the Domain module ,this call goes Domain module 
//@PreAuthorize("#oauth2.hasScope('Admin') or #oauth2.hasScope('Merchant') ")

@RestController
@Api(value = "HelpDeskData", description = "This controller contain all  operation of Email Request")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/emailrequest")
public class EmailRequestController extends BaseController {
	@Autowired
	EmailRequestService emailRequestService;


	@RequestMapping(value = "/getAllEmailRequest", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to getAllEmailRequest data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllEmailRequest() {
		GenericResponse objGenericResponse = emailRequestService.getAllEmailRequest();
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	

	@RequestMapping(value = "/getEmailRequestById", method = RequestMethod.GET)
	@ApiOperation(value = "This api is used to get EmailRequest based on id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getEmailRequestById(@RequestParam("id") Long id) {
		GenericResponse objGenericResponse = emailRequestService.getEmailRequestById(id);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/searchEmailRequest", method = RequestMethod.POST)
	@ApiOperation(value = "This api is to searchEmailRequest data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> searchEmailRequest(@RequestBody EmailRequestDto emailRequestDto)  {
		GenericResponse objGenericResponse = emailRequestService.searchEmailRequest(emailRequestDto);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}


	@RequestMapping(value = "/createemail", method = RequestMethod.POST)
	@ApiOperation(value = "This api is to create email data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> createEmailRequest(@RequestBody EmailRequestCreationDto emailRequestDto) {
		GenericResponse objGenericResponse = emailRequestService.createEmailRequest(emailRequestDto);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ApiOperation(value = "This api is used to get by from email id and date range", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getByCategoryAndSubCategoryAndIssueDetailsAndStatus(@RequestBody PaginationRequestDTO paginationRequestDTO) {
		GenericResponse objGenericResponse = emailRequestService.getByEmailAndDate(paginationRequestDTO);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/count")
	@ApiOperation(value = "This api is used to get ticket count for each ticket status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCount(@RequestParam(value="date", required=false) String date,@RequestParam(value="todate", required=false) String todate,@RequestParam(value="issueFrom", required=true) String issueFrom) throws ParseException {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return new ResponseEntity<>(emailRequestService.getCount(date,todate, issueFrom, authenticationDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	@PostMapping(value = "/replyemail")
	@ApiOperation(value = "This api is use to reply on user email ", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> replyEmail(@RequestBody ReplyMailRequest replyMailRequest) {
		GenericResponse objGenericResponse = emailRequestService.replyMail(replyMailRequest);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	

}