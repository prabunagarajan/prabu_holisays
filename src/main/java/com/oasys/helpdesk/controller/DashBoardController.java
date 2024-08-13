package com.oasys.helpdesk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.request.ActionTakenRequestDto;
import com.oasys.helpdesk.request.ActualProblemRequestDto;
import com.oasys.helpdesk.service.DashBoardService;
import com.oasys.helpdesk.service.ActionTakenService;
import com.oasys.helpdesk.service.ActualProblemService;
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
@Api(value = "HelpDeskData", description = "This controller contain all  operation of DashBoard")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/dashboard")
public class DashBoardController {
	@Autowired
	DashBoardService dashBoardService;

	@RequestMapping(value = "/getDashboardCount", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to getDashboardCount data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getDashboardCount() {
		GenericResponse objGenericResponse = dashBoardService.getDashboardCount();
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getFieldAgentDashboardCount", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to getFieldAgentDashboardCount data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getFieldAgentDashboardCount(@RequestParam("id") Long id) {
		GenericResponse objGenericResponse = dashBoardService.getFieldAgentDashboardCount(id);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
}