package com.oasys.helpdesk.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.PermissionConstant;
import com.oasys.helpdesk.request.CategoryRequestDto;
import com.oasys.helpdesk.request.PriorityRequestDto;
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
@Api(value = "HelpDeskData", description = "This controller contain all  operation of Priority")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/ticketpriority")
public class PriorityController {
	@Autowired
	PriorityService helpDeskPriorityService;

	@RequestMapping(value = "/getAllPriority", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to getAllPriority data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllPriority() {
		GenericResponse objGenericResponse = helpDeskPriorityService.getAllPriority();
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/getPriorityById", method = RequestMethod.GET)
	@ApiOperation(value = "This api is used to get user data based on id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getPriorityById(@RequestParam("id") Long id) throws Exception {
		GenericResponse objGenericResponse = helpDeskPriorityService.getPriorityById(id);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PreAuthorize(PermissionConstant.HELP_DESK_PRIORITY)
	@RequestMapping(value = "/addPriority", method = RequestMethod.POST)
    @ApiOperation(value = "This api is used to create the Priority data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> addPriority(@Valid @RequestBody PriorityRequestDto priorityRequestDto) throws RecordNotFoundException, Exception {
		GenericResponse objGenericResponse = helpDeskPriorityService.createPriority(priorityRequestDto);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PreAuthorize(PermissionConstant.HELP_DESK_PRIORITY)
	@RequestMapping(value = "/editCategory", method = RequestMethod.POST)
    @ApiOperation(value = "This api is used to edit the Priority data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> editPriority(@Valid @RequestBody PriorityRequestDto priorityRequestDto) throws RecordNotFoundException, Exception {
		GenericResponse objGenericResponse = helpDeskPriorityService.editPriority(priorityRequestDto);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	 

	
}