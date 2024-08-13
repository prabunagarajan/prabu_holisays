package com.oasys.helpdesk.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.request.GroupRequestDto;
import com.oasys.helpdesk.request.TicketRequestDto;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.service.ActionTakenService;
import com.oasys.helpdesk.service.ActualProblemService;
import com.oasys.helpdesk.service.GroupService;
import com.oasys.helpdesk.service.PriorityService;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

//This is using spring security 
//based on the scope Domain can access  this controller 
//its access and right give the Domain module ,this call goes Domain module 
//@PreAuthorize("#oauth2.hasScope('Admin') or #oauth2.hasScope('Merchant') ")

@RestController
@Api(value = "HelpDeskData", description = "This controller contain all  operation of HelpDesk Group")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/helpdeskgroup")
public class GroupController extends BaseController {
	@Autowired
	GroupService helpDeskGroupService;

	@RequestMapping(value = "/getAllgroup", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to getAllgroup data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllgroup() {
		GenericResponse objGenericResponse = helpDeskGroupService.getAllgroup();
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/searchgroupByName", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to getgroupByName data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getgroupByName(@RequestParam("groupName") String groupName) {
		GenericResponse objGenericResponse = helpDeskGroupService.getgroupByName(groupName);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	

	@RequestMapping(value = "/getGroupById", method = RequestMethod.GET)
	@ApiOperation(value = "This api is used to get user data based on id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getGroupById(@RequestParam("id") Long id) throws Exception {
		GenericResponse objGenericResponse = helpDeskGroupService.getGroupById(id);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/addgroup", method = RequestMethod.POST)
    @ApiOperation(value = "This api is used to create the group data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> createTicket(@RequestBody GroupRequestDto helpDeskGroupRequestDto) throws RecordNotFoundException, Exception {
		GenericResponse objGenericResponse = helpDeskGroupService.createGroup(helpDeskGroupRequestDto);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
//	@RequestMapping(value = "/getUserByDesignationCode", method = RequestMethod.GET)
//	@ApiOperation(value = "This api is used to get user data based on id", notes = "Returns HTTP 200 if successful get the record")
//	public ResponseEntity<Object> getUserByDesignationCode(@RequestParam("code") String code) throws Exception {
//		GenericResponse objGenericResponse = helpDeskGroupService.getUserByDesignationCode(code);
//		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
//	}
	
	@RequestMapping(value = "/getUserByDesignationCode", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to/getUserByDesignationCode based on code", notes = "Returns HTTP 200 if successful get the record")
	public GenericResponse getUserByDesignationCode(@RequestParam("code") String code,Locale locale)throws RecordNotFoundException {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return helpDeskGroupService.getUserByDesignationCode(authenticationDTO,locale,code);
	}
	

//	@RequestMapping(value = "/getUserById", method = RequestMethod.GET)
//	@ApiOperation(value = "This api is used to get user data based on id", notes = "Returns HTTP 200 if successful get the record")
//	public ResponseEntity<Object> getUserById(@RequestParam("id") Long id) throws Exception {
//		GenericResponse objGenericResponse = helpDeskGroupService.getUserById(id);
//		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
//	}
	@RequestMapping(value = "/getUserById", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to/getUserByDesignationCode based on code", notes = "Returns HTTP 200 if successful get the record")
	public GenericResponse getUserById(@RequestParam("id") Long id,Locale locale)throws RecordNotFoundException {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return helpDeskGroupService.getUserById(authenticationDTO,locale,id);
	}
	
	
	
	
	
}